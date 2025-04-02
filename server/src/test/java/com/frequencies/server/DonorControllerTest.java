package com.frequencies.server;

import com.frequencies.server.controller.DonorController;
import com.frequencies.server.entity.Donor;
import com.frequencies.server.repository.DonorRepository;
import jakarta.servlet.ServletContext;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.client.MockMvcWebTestClient;
import org.testcontainers.containers.PostgreSQLContainer;

@SpringBootTest
public class DonorControllerTest {
    private static final String PG_IMAGE = "postgres:17.4-alpine";

    private static final String PG_PASSWORD = "password";

    private static final String PG_USERNAME = "username";

    private static final String PG_DATABASE = "database";

    @Autowired private DonorController donorController;

    @Autowired private DonorRepository donorRepository;

    @Autowired private ServletContext servletContext;

    static PostgreSQLContainer<?> container = new PostgreSQLContainer<>(PG_IMAGE).withPassword(PG_PASSWORD)
                                                                                 .withUsername(PG_USERNAME)
                                                                                 .withDatabaseName(PG_DATABASE);

    @DynamicPropertySource
    static void mysqlProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.username", container::getUsername);
        registry.add("spring.datasource.password", container::getPassword);
    }

    @BeforeAll
    static void setUp() {
        container.start();
    }

    @BeforeEach
    void setUpEach() {
        donorRepository.deleteAll();
    }

    @Test
    @DisplayName("Insert valid donor")
    void testInsertValidDonor() throws Exception {
        Donor donor = new Donor();
        donor.setName("Donor");
        donor.setSiren("Siren");
        donor.setAddress("Address");

        Donor insertedDonor = MockMvcWebTestClient.bindToController(this.donorController, this.servletContext)
                                                  .build()
                                                  .post()
                                                  .uri("/donor")
                                                  .accept(MediaType.APPLICATION_JSON)
                                                  .bodyValue(donor)
                                                  .exchange()
                                                  .expectStatus()
                                                  .isOk()
                                                  .expectBody(Donor.class)
                                                  .returnResult()
                                                  .getResponseBody();

        assert insertedDonor != null;
        assert insertedDonor.getName()
                            .equals("Donor");
        assert insertedDonor.getSiren()
                            .equals("Siren");
        assert insertedDonor.getAddress()
                            .equals("Address");
    }

    @Test
    @DisplayName("Insert two duplicate donors")
    void testInsertDuplicateDonors() throws Exception {
        Donor donor = new Donor();
        donor.setName("Donor");
        donor.setSiren("Siren");
        donor.setAddress("Address");

        Donor insertedDonor = MockMvcWebTestClient.bindToController(this.donorController, this.servletContext)
                                                  .build()
                                                  .post()
                                                  .uri("/donor")
                                                  .accept(MediaType.APPLICATION_JSON)
                                                  .bodyValue(donor)
                                                  .exchange()
                                                  .expectStatus()
                                                  .isOk()
                                                  .expectBody(Donor.class)
                                                  .returnResult()
                                                  .getResponseBody();
    }

    @Test
    @DisplayName("Insert donor with no siren")
    void testInsertDonorWithNoSiren() throws Exception {
        Donor donor = new Donor();
        donor.setName("Donor");
        donor.setAddress("Address");

        MockMvcWebTestClient.bindToController(this.donorController, this.servletContext)
                            .build()
                            .post()
                            .uri("/donor")
                            .accept(MediaType.APPLICATION_JSON)
                            .bodyValue(donor)
                            .exchange()
                            .expectStatus()
                            .is4xxClientError();
    }
}
