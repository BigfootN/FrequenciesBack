package com.frequencies.backend.entity;

import jakarta.persistence.*;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Donor", indexes = {
        @Index(name = "idx_name", columnList = "name", unique = true),
        @Index(name = "idx_siren", columnList = "siren", unique = true),
        @Index(name = "idx_address", columnList = "address", unique = true),
})
@Transactional
public class Donor {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String siren;

    @Column(nullable = false)
    private String address;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "donor", cascade = CascadeType.ALL, orphanRemoval = true)
    @NonNull
    private List<Donation> donations = new ArrayList<>();

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSiren() {
        return this.siren;
    }

    public void setSiren(String siren) {
        this.siren = siren;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<Donation> getDonations() {
        return this.donations;
    }

    public void setDonations(List<Donation> donations) {
        this.donations = donations;
    }
}
