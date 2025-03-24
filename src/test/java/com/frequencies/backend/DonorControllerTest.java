package com.frequencies.backend;

import com.frequencies.backend.controller.DonorController;
import com.frequencies.backend.entity.Donor;
import com.frequencies.backend.repository.DonorRepository;
import jakarta.servlet.ServletContext;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.client.MockMvcWebTestClient;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;

@SpringBootTest
public class DonorControllerTest {
    private static final String MYSQL_IMAGE_NAME = "mysql:9.2.0";

    private static final String MYSQL_PASSWORD = "password";

    private static final String MYSQL_USERNAME = "username";

    private static final String MYSQL_DATABASE = "database";

    @Autowired private DonorController donorController;

    @Autowired private DonorRepository donorRepository;

    @Autowired private ServletContext servletContext;

    @Container
    @ServiceConnection
    static MySQLContainer<?> container = new MySQLContainer<>(MYSQL_IMAGE_NAME).withDatabaseName(MYSQL_IMAGE_NAME)
                                                                               .withPassword(MYSQL_PASSWORD)
                                                                               .withUsername(MYSQL_USERNAME)
                                                                               .withDatabaseName(MYSQL_DATABASE);

    @DynamicPropertySource
    static void mysqlProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", container::getJdbcUrl);
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
