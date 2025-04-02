package com.frequencies.server;

import com.frequencies.server.entity.Donation;
import com.frequencies.server.entity.Donor;
import com.frequencies.server.entity.PaymentType;
import com.frequencies.server.repository.DonationRepository;
import com.frequencies.server.repository.DonorRepository;
import com.frequencies.server.service.DonationService;
import com.frequencies.server.service.DonorService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
public class DonationServiceTest {
    private static final String PG_IMAGE = "postgres:17.4-alpine";

    private static final String PG_PASSWORD = "password";

    private static final String PG_USERNAME = "username";

    private static final String PG_DATABASE = "database";

    static PostgreSQLContainer<?> container = new PostgreSQLContainer<>(PG_IMAGE).withPassword(PG_PASSWORD)
                                                                                 .withUsername(PG_USERNAME)
                                                                                 .withDatabaseName(PG_DATABASE);
    @Autowired
    private DonationService donationService;

    @Autowired
    private DonorRepository donorRepository;

    @Autowired
    private DonationRepository donationRepository;

    @Autowired private DonorService donorService;

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
        donationRepository.deleteAll();
        donorRepository.deleteAll();
    }

    @Test
    @DisplayName("Find donor from donation")
    public void findDonorFromDonation() {
        Donor donor = new Donor();
        donor.setAddress("address");
        donor.setName("name");
        donor.setSiren("siren");

        Donor insertedDonor = donorService.insertDonor(donor);

        LocalDateTime donationDate = LocalDateTime.now();
        Long amount = 500L;
        PaymentType paymentType = PaymentType.Nature;

        Donation donation = new Donation();
        donation.setAmount(amount);
        donation.setDate(donationDate);
        donation.setDonor(insertedDonor);
        donation.setPaymentType(paymentType);

        donationService.insert(donation);

        Donor foundDonor = donorService.findDonorById(donor.getId());

        List<Donation> donations = foundDonor.getDonations();
        assert donations != null;
        assert donations.size() == 1;

        Donation firstDonation = donations.get(0);

        assert firstDonation
                .getAmount() == 500L;
        assert firstDonation.getDate() == donationDate;
        assert firstDonation.getPaymentType() == paymentType;
    }
}
