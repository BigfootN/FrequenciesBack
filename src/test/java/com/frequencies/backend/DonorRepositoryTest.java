package com.frequencies.backend;

import com.frequencies.backend.entity.Donor;
import com.frequencies.backend.repository.DonorRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

@Testcontainers
@SpringBootTest
public class DonorRepositoryTest {
    private static final String MYSQL_IMAGE_NAME = "mysql:9.2.0";

    private static final String MYSQL_PASSWORD = "password";

    private static final String MYSQL_USERNAME = "username";

    private static final String MYSQL_DATABASE = "database";

    @Container
    @ServiceConnection
    static MySQLContainer<?> container = new MySQLContainer<>(MYSQL_IMAGE_NAME).withDatabaseName(MYSQL_IMAGE_NAME)
                                                                               .withPassword(MYSQL_PASSWORD)
                                                                               .withUsername(MYSQL_USERNAME)
                                                                               .withDatabaseName(MYSQL_DATABASE);
    @Autowired
    private DonorRepository donorRepository;

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
    void insertValidDonor() {
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
