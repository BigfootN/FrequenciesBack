package com.frequencies.backend.service;

import com.frequencies.backend.entity.Donation;
import com.frequencies.backend.entity.Donor;
import com.frequencies.backend.repository.DonationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class DonationService {
    private final DonationRepository donationRepository;

    @Autowired
    public DonationService(DonationRepository donationRepository) {
        this.donationRepository = donationRepository;
    }

    public Donation insert(Donation donation) {
        return this.donationRepository.save(donation);
    }

    public Donation updateDonation(Donation donation) {
        return this.donationRepository.save(donation);
    }

    public void deleteDonation(Donation donation) {
        this.donationRepository.delete(donation);
    }

    public void deleteDonationById(Long id) {
        this.donationRepository.deleteById(id);
    }

    @Transactional
    public Donation findById(@NonNull Long id) {
        Donation donation = donationRepository.findById(id)
                                              .orElseThrow();
//        Hibernate.initialize(donation.getDonor());
        return donation;
    }

    public Optional<List<Donation>> findAllDonationsByDonor(@NonNull Donor donor) {
        return Optional.ofNullable(donor.getDonations());
    }

    public Optional<List<Donation>> findAllDonationsByDonorId(@NonNull Long id) {
        return donationRepository.findAllDonationsByDonorId(id);
    }
}
