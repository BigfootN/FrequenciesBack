package com.frequencies.server.entity;

import jakarta.persistence.*;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.lang.NonNull;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

@Entity
@Table(name = "donation")
public class Donation {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "donor_id")
    Donor donor;

    @Column(nullable = false)
    private Timestamp date;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private PaymentType paymentType;

    @Column(nullable = false)
    private Long amount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Donor getDonor() {
        return donor;
    }

    public void setDonor(@NonNull final Donor donor) {
        this.donor = donor;
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(@NonNull final PaymentType paymentType) {
        this.paymentType = paymentType;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(@NonNull final Long amount) {
        this.amount = amount;
    }

    public Date getDate() {
        return this.date;
    }

    public void setDate(@NonNull final Date date) {
        this.date = new Timestamp(DateUtils.round(date, Calendar.MINUTE)
                                           .getTime());
    }
}
