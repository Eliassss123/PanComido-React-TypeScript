package com.pancomido.pancomido.swagger;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.junit.jupiter.api.Test;

import com.pancomido.pancomido.swagger.ConfigSwagger;

import static org.assertj.core.api.Assertions.assertThat;

public class ConfigSwaggerTest {

    @Test
    void testDeliveryOpenAPI() {
        ConfigSwagger config = new ConfigSwagger();

        OpenAPI api = config.deliveryOpenAPI();

        assertThat(api).isNotNull();
        Info info = api.getInfo();
        assertThat(info).isNotNull();
        assertThat(info.getTitle()).isEqualTo("API de Pedidos - pancomido");
        assertThat(info.getDescription()).contains("gestionar pedidos");
        assertThat(info.getVersion()).isEqualTo("1.0");
        assertThat(info.getContact()).isNotNull();
        assertThat(info.getContact().getEmail()).isEqualTo("soporte@pancomido.com");
    }
}
