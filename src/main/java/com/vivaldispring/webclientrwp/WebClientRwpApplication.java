package com.vivaldispring.webclientrwp;

import com.vivaldispring.webclientrwp.services.WebClientOptions;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
public class WebClientRwpApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(WebClientRwpApplication.class, args);



        WebClientOptions webClientOptions = context.getBean(WebClientOptions.class);
        WebClient client = webClientOptions.WebClientWithResponseTimeOut();
        ResponseEntity<String> response = client.
                get().
                uri("https://countriesnow.space/api/v0.1/countries/population/cities").
                retrieve().
                toEntity(String.class).
                block();

        System.out.println(response);

        context.close();

    }

}
