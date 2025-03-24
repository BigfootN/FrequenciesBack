package com.frequencies.backend.repository;

import com.frequencies.backend.entity.Donation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DonationRepository extends JpaRepository<Donation, Long> {
    abstract Optional<List<Donation>> findAllDonationsByDonorId(@NonNull Long id);
}
