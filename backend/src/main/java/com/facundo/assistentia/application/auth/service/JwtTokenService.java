package com.facundo.assistentia.application.auth.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.facundo.assistentia.shared.config.SecurityProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JwtTokenService {

    private static final TypeReference<Map<String, Object>> MAP_TYPE = new TypeReference<>() { };
    private static final Base64.Encoder BASE64_URL_ENCODER = Base64.getUrlEncoder().withoutPadding();
    private static final Base64.Decoder BASE64_URL_DECODER = Base64.getUrlDecoder();

    private final ObjectMapper objectMapper;
    private final SecurityProperties securityProperties;

    public String createAccessToken(DesktopSession session, Instant issuedAt, Instant expiresAt) {
        Map<String, Object> header = Map.of("alg", "HS256", "typ", "JWT");
        Map<String, Object> claims = new LinkedHashMap<>();
        claims.put("iss", securityProperties.jwtIssuer());
        claims.put("iat", issuedAt.getEpochSecond());
        claims.put("exp", expiresAt.getEpochSecond());
        claims.put("sub", session.userId().toString());
        claims.put("username", session.username());
        claims.put("roles", List.of(session.role().name()));
        if (session.teamId() != null) {
            claims.put("teamId", session.teamId().toString());
        }

        String signingInput = encode(header) + "." + encode(claims);
        return signingInput + "." + BASE64_URL_ENCODER.encodeToString(sign(signingInput));
    }

    public AuthenticatedPrincipal parseAndValidate(String token) {
        if (token == null || token.isBlank()) {
            throw new IllegalArgumentException("El token es obligatorio.");
        }

        String[] parts = token.split("\\.", -1);
        if (parts.length != 3 || parts[0].isBlank() || parts[1].isBlank() || parts[2].isBlank()) {
            throw new IllegalArgumentException("El token no tiene un formato valido.");
        }

        String signingInput = parts[0] + "." + parts[1];
        byte[] suppliedSignature;
        try {
            suppliedSignature = BASE64_URL_DECODER.decode(parts[2]);
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException("La firma del token no es valida.");
        }

        if (!MessageDigest.isEqual(sign(signingInput), suppliedSignature)) {
            throw new IllegalArgumentException("La firma del token no es valida.");
        }

        Map<String, Object> header = decode(parts[0]);
        if (!"HS256".equals(header.get("alg")) || !"JWT".equals(header.get("typ"))) {
            throw new IllegalArgumentException("El algoritmo del token no esta permitido.");
        }

        Map<String, Object> claims = decode(parts[1]);
        if (!securityProperties.jwtIssuer().equals(claims.get("iss"))) {
            throw new IllegalArgumentException("El emisor del token no es valido.");
        }

        long expiresAt = numericClaim(claims, "exp");
        if (expiresAt <= Instant.now().getEpochSecond()) {
            throw new IllegalArgumentException("El token vencio.");
        }

        String subject = stringClaim(claims, "sub");
        String username = stringClaim(claims, "username");
        List<String> roles = stringListClaim(claims, "roles");
        try {
            return new AuthenticatedPrincipal(UUID.fromString(subject), username, roles);
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException("El usuario del token no es valido.");
        }
    }

    private String encode(Map<String, Object> value) {
        try {
            return BASE64_URL_ENCODER.encodeToString(objectMapper.writeValueAsBytes(value));
        } catch (JsonProcessingException exception) {
            throw new IllegalStateException("No se pudo crear el token.", exception);
        }
    }

    private Map<String, Object> decode(String encodedValue) {
        try {
            return objectMapper.readValue(BASE64_URL_DECODER.decode(encodedValue), MAP_TYPE);
        } catch (IllegalArgumentException | IOException exception) {
            throw new IllegalArgumentException("El contenido del token no es valido.");
        }
    }

    private byte[] sign(String signingInput) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(
                    securityProperties.jwtSecret().getBytes(StandardCharsets.UTF_8),
                    "HmacSHA256"
            ));
            return mac.doFinal(signingInput.getBytes(StandardCharsets.US_ASCII));
        } catch (GeneralSecurityException exception) {
            throw new IllegalStateException("No se pudo firmar el token.", exception);
        }
    }

    private long numericClaim(Map<String, Object> claims, String name) {
        Object value = claims.get(name);
        if (value instanceof Number number) {
            return number.longValue();
        }
        throw new IllegalArgumentException("El token no contiene " + name + ".");
    }

    private String stringClaim(Map<String, Object> claims, String name) {
        Object value = claims.get(name);
        if (value instanceof String stringValue && !stringValue.isBlank()) {
            return stringValue;
        }
        throw new IllegalArgumentException("El token no contiene " + name + ".");
    }

    private List<String> stringListClaim(Map<String, Object> claims, String name) {
        Object value = claims.get(name);
        if (value instanceof List<?> values && !values.isEmpty() && values.stream().allMatch(String.class::isInstance)) {
            return values.stream().map(String.class::cast).toList();
        }
        throw new IllegalArgumentException("El token no contiene roles validos.");
    }
}
