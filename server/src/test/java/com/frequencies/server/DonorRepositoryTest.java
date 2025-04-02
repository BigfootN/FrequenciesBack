package com.frequencies.server;

import com.frequencies.server.entity.Donor;
import com.frequencies.server.repository.DonorRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class DonorRepositoryTest {
    private static final String PG_IMAGE = "postgres:17.4";

    private static final String PG_PASSWORD = "password";

    private static final String PG_USERNAME = "username";

    private static final String PG_DATABASE = "database";

    static final PostgreSQLContainer<?> container = new PostgreSQLContainer<>(PG_IMAGE).withPassword(PG_PASSWORD)
                                                                                       .withUsername(PG_USERNAME)
                                                                                       .withDatabaseName(PG_DATABASE);
    @Autowired
    private DonorRepository donorRepository;

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.password", container::getPassword);
        registry.add("spring.datasource.username", container::getUsername);
        registry.add("spring.datasource.driver-class-name", container::getDriverClassName);
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
    void insertValidDonor() {
        System.out.println("Insert valid donor");
        Donor donor = new Donor();
        donor.setAddress("address");
        donor.setName("name");
        donor.setSiren("siren");

        donorRepository.save(donor);

        Optional<Donor> foundDonor = donorRepository.findById(donor.getId());
        assert foundDonor.isPresent();
        assert foundDonor.get()
                         .getAddress()
                         .equals(donor.getAddress());
        assert foundDonor.get()
                         .getName()
                         .equals(donor.getName());
        assert foundDonor.get()
                         .getSiren()
                         .equals(donor.getSiren());
    }

    @Test
    @DisplayName("Insert donor without siren")
    public void insertDonorWithoutSiren() {
        Donor donor = new Donor();
        donor.setAddress("address");
        donor.setName("name");

        assertThrows(DataIntegrityViolationException.class, () -> donorRepository.save(donor));
    }

    @Test
    @DisplayName("Insert donor without address and siren")
    public void insertDonorWithoutAddressAndSiren() {
        Donor donor = new Donor();
        donor.setName("name");

        assertThrows(DataIntegrityViolationException.class, () -> donorRepository.save(donor));
    }

    @Test
    @DisplayName("Insert donor without address")
    public void insertDonorWithoutAddress() {
        Donor donor = new Donor();
        donor.setSiren("siren");
        donor.setName("name");

        assertThrows(DataIntegrityViolationException.class, () -> donorRepository.save(donor));
    }

    @Test
    @DisplayName("Insert donors with same siren")
    public void insertDonorsWithSameSiren() {
        Donor firstDonor = new Donor();
        firstDonor.setAddress("addressFirst");
        firstDonor.setName("nameFirst");
        firstDonor.setSiren("siren");

        Donor secondDonor = new Donor();
        secondDonor.setAddress("addressSecond");
        secondDonor.setName("nameSecond");
        secondDonor.setSiren("siren");

        donorRepository.save(firstDonor);
        assertThrows(DataIntegrityViolationException.class, () -> donorRepository.save(secondDonor));
    }

    @Test
    @DisplayName("Insert donors with same address")
    public void insertDonorsWithSameAddress() {
        Donor firstDonor = new Donor();
        firstDonor.setAddress("address");
        firstDonor.setName("nameFirst");
        firstDonor.setSiren("sirenFirst");

        Donor secondDonor = new Donor();
        secondDonor.setAddress("address");
        secondDonor.setName("nameSecond");
        secondDonor.setSiren("sirenSecond");

        donorRepository.save(firstDonor);
        assertThrows(DataIntegrityViolationException.class, () -> donorRepository.save(secondDonor));
    }

    @Test
    @DisplayName("Insert donors with same name")
    public void insertDonorsWithSameName() {
        Donor firstDonor = new Donor();
        firstDonor.setAddress("addressFirst");
        firstDonor.setName("name");
        firstDonor.setSiren("sirenFirst");

        Donor secondDonor = new Donor();
        secondDonor.setAddress("addressSecond");
        secondDonor.setName("name");
        secondDonor.setSiren("sirenSecond");

        donorRepository.save(firstDonor);
        assertThrows(DataIntegrityViolationException.class, () -> donorRepository.save(secondDonor));
    }
}
