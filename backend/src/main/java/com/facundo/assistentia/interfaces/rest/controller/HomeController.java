package com.facundo.assistentia.interfaces.rest.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

	@GetMapping(value = "/", produces = MediaType.TEXT_HTML_VALUE)
	public ResponseEntity<String> home() {
		String html = """
			<!DOCTYPE html>
			<html lang=\"es\">
			<head>
			    <meta charset=\"UTF-8\" />
			    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\" />
			    <title>AssistentIA Backend</title>
			    <style>
			        body { font-family: Segoe UI, Arial, sans-serif; background: #0f172a; color: #e2e8f0; margin: 0; padding: 48px 24px; }
			        .card { max-width: 760px; margin: 0 auto; background: #111827; border: 1px solid #1f2937; border-radius: 16px; padding: 32px; box-shadow: 0 20px 45px rgba(0,0,0,.35); }
			        h1 { margin-top: 0; font-size: 32px; }
			        p { color: #cbd5e1; line-height: 1.6; }
			        ul { padding-left: 20px; }
			        li { margin: 10px 0; }
			        a { color: #38bdf8; text-decoration: none; }
			        a:hover { text-decoration: underline; }
			        .badge { display: inline-block; padding: 6px 10px; border-radius: 999px; background: #0ea5e9; color: #082f49; font-weight: 700; font-size: 12px; }
			    </style>
			</head>
			<body>
			    <div class=\"card\">
			        <span class=\"badge\">Backend operativo</span>
			        <h1>AssistentIA</h1>
			        <p>La API arrancó correctamente. Esta pantalla existe para que al ejecutar la aplicación veas algo útil de inmediato en el navegador.</p>
			        <ul>
			            <li><a href=\"/api/v1/health\">Health check</a></li>
			            <li><a href=\"/api/v1/system/info\">Información del sistema</a></li>
			            <li><a href=\"/api/v1/demo/quick-start\">Demo rápida</a></li>
			            <li><a href=\"/h2-console\">Consola H2</a></li>
			        </ul>
			        <p>Puerto local por defecto: 8081.</p>
			    </div>
			</body>
			</html>
			""";

		return ResponseEntity.ok(html);
	}
}