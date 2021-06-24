package com.jalin.jalinappbackend.module.banking.entity;

import com.jalin.jalinappbackend.module.authentication.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "transfer_list")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TransferList {
    @Id
    private UUID transferListId;
    private String corporateId;
    private String accountNumber;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    @CreationTimestamp
    private Instant createdDate;
    @UpdateTimestamp
    private Instant modifiedDate;
}
