package com.frequencies.backend;

import com.frequencies.backend.entity.Donation;
import com.frequencies.backend.entity.Donor;
import com.frequencies.backend.entity.PaymentType;
import com.frequencies.backend.repository.DonationRepository;
import com.frequencies.backend.repository.DonorRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Date;
import java.util.List;
import java.util.Optional;


@SpringBootTest
@Testcontainers
public class DonationRepositoryTest {
    private static final String MYSQL_IMAGE_NAME = "mysql:9.2.0";

    private static final String MYSQL_PASSWORD = "password";

    private static final String MYSQL_USERNAME = "username";

    private static final String MYSQL_DATABASE = "database";

    @Autowired
    DonorRepository donorRepository;

    @Autowired
    DonationRepository donationRepository;

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

    @Test
    @DisplayName("Find donor from donation")
    public void findDonorFromDonation() {
        Donor donor = createDonor();

        Donation donation = new Donation();
        donation.setAmount(500L);
        donation.setDate(new Date());
        donation.setDonor(donor);

        Donation savedDonation = donationRepository.save(donation);

        Optional<Donation> foundDonation = donationRepository.findById(savedDonation.getId());
        assert foundDonation.isPresent();


        Donor associatedDonor = foundDonation.get()
                                             .getDonor();

        List<Donation> donations = associatedDonor.getDonations();
        assert donations != null;
        assert donations.size() == 1;

        assert associatedDonor.getDonations()
                              .size() == 1;
        assert associatedDonor.getDonations()
                              .getFirst()
                              .getDonor()
                              .getName()
                              .equals(donor.getName());
        assert associatedDonor.getDonations()
                              .getFirst()
                              .getDonor()
                              .getAddress()
                              .equals(donor.getAddress());
        assert associatedDonor.getDonations()
                              .getFirst()
                              .getDonor()
                              .getSiren()
                              .equals(donor.getSiren());
        assert associatedDonor.getDonations()
                              .getFirst()
                              .getDonor()
                              .getId()
                              .equals(donor.getId());
    }
}
