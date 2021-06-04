package com.jalin.jalinappbackend.module.authentication.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "user_details")
@Data
public class UserDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    private String avatar_url;
    private String account_number;
    private String jalin_id;
    private Integer pin;
    private Integer point;
}
