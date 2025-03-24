package com.frequencies.backend.service;

import com.frequencies.backend.entity.Donation;
import com.frequencies.backend.repository.DonationRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
        Hibernate.initialize(donation.getDonor());
        return donation;
    }

    public List<Donation> findAllDonationsByDonorId(@NonNull Long id) {
        return donationRepository.findAllDonationsByDonorId(id)
                                 .orElseThrow();
    }
}
