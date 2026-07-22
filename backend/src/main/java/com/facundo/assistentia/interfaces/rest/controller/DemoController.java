package com.facundo.assistentia.interfaces.rest.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/demo")
public class DemoController {

    @GetMapping("/quick-start")
    public ResponseEntity<Map<String, Object>> quickStart() {
        return ResponseEntity.ok(Map.of(
                "message", "Plataforma lista para pruebas",
                "nextSteps", List.of(
                        "Registrar un equipo",
                        "Crear un usuario",
                        "Explorar el CRM",
                        "Preparar reuniones y tareas"
                )
        ));
    }
}
