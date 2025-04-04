package com.frequencies.server;

import com.frequencies.server.entity.Donation;
import com.frequencies.server.entity.Donor;
import com.frequencies.server.entity.PaymentType;
import com.frequencies.server.repository.DonationRepository;
import com.frequencies.server.repository.DonorRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.Date;

@SpringBootTest
public class DonationRepositoryTest {
    private static final String PG_IMAGE = "postgres:17.4-alpine";

    private static final String PG_PASSWORD = "password";

    private static final String PG_USERNAME = "username";

    private static final String PG_DATABASE = "database";

    @Autowired
    DonorRepository donorRepository;

    @Autowired
    DonationRepository donationRepository;

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

    private Donor createDonor() {
        Donor user = new Donor();

        user.setSiren("Siren");
        user.setName("Donor");
        user.setAddress("Address");

        return donorRepository.save(user);
    }

    @Test
    @DisplayName("Insert valid donation")
    public void insertValidDonation() {
        Donor donor = createDonor();

        Donation donation = new Donation();
        donation.setAmount(500L);
        donation.setDate(new Date());
        donation.setPaymentType(PaymentType.Nature);
        donation.setDonor(donor);

        donationRepository.save(donation);
    }
}
