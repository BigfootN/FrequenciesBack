package com.frequencies.backend.controller;

import com.frequencies.backend.entity.Donation;
import com.frequencies.backend.service.DonationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("donation")
public class DonationController {
    @NonNull
    private DonationService donationService;

    @Autowired
    public DonationController(@NonNull DonationService donationService) {
        this.donationService = donationService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Donation> getDonations(@PathVariable Long id) {
        try {
            return new ResponseEntity(this.donationService.findById(id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/all?donor_id={id}")
    public ResponseEntity<List<Donation>> getAllDonations(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(this.donationService.findAllDonationsByDonorId(id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(produces = "application/json", consumes = "application/json")
    public ResponseEntity<Donation> saveDonor(@Valid @RequestBody Donation donation) {
        try {
            return new ResponseEntity<>(this.donationService.insert(donation), HttpStatus.OK);
        } catch (DataIntegrityViolationException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
