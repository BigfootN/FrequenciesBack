package com.frequencies.backend.controller;

import com.frequencies.backend.entity.Donor;
import com.frequencies.backend.service.DonorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("donor")
public class DonorController {
    @NonNull
    DonorService donorService;

    @Autowired
    public DonorController(@NonNull DonorService donorService) {
        this.donorService = donorService;
    }

    @GetMapping(path = "/{id}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Donor> getDonorById(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(donorService.findDonorById(id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(path = "/siren={siren}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Donor> getAllDonors(@NonNull @PathVariable String siren) {
        try {
            return new ResponseEntity<>(donorService.findDonorBySiren(siren), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(produces = "application/json", consumes = "application/json")
    public ResponseEntity<Donor> saveDonor(@Valid @RequestBody Donor donor) {
        try {
            return new ResponseEntity<>(this.donorService.insertDonor(donor), HttpStatus.OK);
        } catch (DataIntegrityViolationException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
