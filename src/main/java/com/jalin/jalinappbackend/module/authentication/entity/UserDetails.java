package com.jalin.jalinappbackend.module.authentication.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "user_details")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserDetails {
    @Id
    private UUID userDetailsId;
    private String idCardNumber;
    private String fullName;
    private LocalDate dateOfBirth;
    private String address;
    private String currentAddress;
    private String idCardImage;

    private String bankingGoals;
    private String occupation;
    private String sourceOfIncome;
    private String incomeRange;
    private String npwpImage;

    private String accountNumber;

    private String jalinId;
    private String displayName;
    private String mobileNumber;
    private String profilePicture;
    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    @CreationTimestamp
    private Instant createdDate;
    @UpdateTimestamp
    private Instant modifiedDate;
}
