package com.jalin.jalinappbackend.module.banking.entity;

import com.jalin.jalinappbackend.module.authentication.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "transactions")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Transaction {
    @Id
    private String transactionId;
    private LocalDate transactionDate;
    private String transactionType;
    private String currency;
    private BigDecimal amount;
    private String corporateId;
    private String accountNumber;
    private String transactionName;
    private String transactionDescription;
    private String transactionMessage;
    private String transactionNote;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    @CreationTimestamp
    private Instant createdDate;
}
