package com.frequencies.server.repository;

import com.frequencies.server.entity.Donor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DonorRepository extends JpaRepository<Donor, Long> {
    Optional<Donor> findDonorById(Long id);

    Optional<Donor> findDonorByName(String name);

    Optional<Donor> findDonorBySiren(String siren);
}
