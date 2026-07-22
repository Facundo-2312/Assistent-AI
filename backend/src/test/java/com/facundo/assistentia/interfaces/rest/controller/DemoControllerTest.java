package com.facundo.assistentia.interfaces.rest.controller;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DemoControllerTest {

    @Test
    void shouldExposeQuickStartPayload() {
        DemoController controller = new DemoController();
        var response = controller.quickStart();

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).containsEntry("message", "Plataforma lista para pruebas");
    }
}
