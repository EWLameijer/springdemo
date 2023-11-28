package org.ericwubbo.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.client.WebClient;

import static org.springframework.http.HttpHeaders.ACCEPT;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DemoApplicationTests {

    @Autowired
    private WebTestClient webTestClient;


    @Test
    void shouldReturnACashCardWhenDataIsSaved() {
        WebClient webClient = WebClient.create();

//        var movieResult = webClient.post()
//                .uri("/movies")
//                .contentType(MediaType.APPLICATION_JSON)
//                .bodyValue(new MovieDto(null, "Frozen", null))
//                .retrieve()
//                .toBodilessEntity()
//                .block();

        var movieResult = webTestClient.get()
                .uri("/movies/1")
                .header(ACCEPT, APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.title").isEqualTo("Up");

        //.headers(headers -> headers.setBasicAuth("nn", "abc"))
        //.bodyValue(new MovieDto(null, "Frozen", null))
//                .retrieve()
//                .toBodilessEntity()
//                .block();

        /*
        @Test
void shouldReturnThreeDefaultUsers(){
  this.webTestClient
  .get()
  .uri("/api/users")
  .header(ACCEPT,APPLICATION_JSON_VALUE)
  .exchange()
  .expectStatus()
  .is2xxSuccessful()
  .expectHeader()
  .contentType(APPLICATION_JSON)
  .expectBody()
  .jsonPath("$.length()").isEqualTo(3)
  .jsonPath("$[0].id").isEqualTo(1)
  .jsonPath("$[0].name").isEqualTo("duke")
  .jsonPath("$[0].tags").isNotEmpty();
}
         */


        System.out.println(movieResult);
    }
}

// Research: internet example used (Test)RestTemplate, and RestTemplate is marked for deprecation
// https://www.baeldung.com/rest-template
// https://www.viralpatel.net/basic-authentication-spring-webclient/
// Step 1: include WebFlux
/*
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-webflux</artifactId>
        </dependency>

 */