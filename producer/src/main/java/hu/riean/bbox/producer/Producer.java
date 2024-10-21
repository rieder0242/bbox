/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package hu.riean.bbox.producer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.embedded.EmbeddedWebServerFactoryCustomizerAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;

/**
 *
 * @author riean
 */
@SpringBootApplication(exclude = {EmbeddedWebServerFactoryCustomizerAutoConfiguration.class, WebMvcAutoConfiguration.class})
public class Producer {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(Producer.class);
        application.setWebApplicationType(WebApplicationType.NONE);
        application.run(args);
    }
}
