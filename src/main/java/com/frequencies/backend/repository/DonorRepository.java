package com.frequencies.backend.repository;

import com.frequencies.backend.entity.Donor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DonorRepository extends JpaRepository<Donor, Long> {
//    Optional<Donor> findDonorFromDonationId(Long id);

    Optional<Donor> findDonorById(Long id);

    Optional<Donor> findDonorByName(String name);
}
