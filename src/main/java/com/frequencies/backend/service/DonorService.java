package com.frequencies.backend.service;

import com.frequencies.backend.entity.Donor;
import com.frequencies.backend.repository.DonorRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DonorService {
    private final DonorRepository donorRepository;

    @Autowired
    public DonorService(DonorRepository donorRepository) {
        this.donorRepository = donorRepository;
    }

    public Donor insertDonor(@NonNull Donor donor) {
        return donorRepository.save(donor);
    }

    public void deleteDonor(@NonNull Donor donor) {
        donorRepository.delete(donor);
    }

    public void deleteDonorById(@NonNull Long id) {
        donorRepository.deleteById(id);
    }

    public Donor updateDonor(@NonNull Donor donor) {
        return donorRepository.save(donor);
    }

    @Transactional
    public Donor findDonorById(@NonNull Long id) {
        Donor donor = donorRepository.findDonorById(id)
                                     .orElseThrow();
        Hibernate.initialize(donor.getDonations());
        return donor;
    }
}
