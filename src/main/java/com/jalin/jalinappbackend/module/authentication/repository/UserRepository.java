package com.jalin.jalinappbackend.module.authentication.repository;

import com.jalin.jalinappbackend.module.authentication.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
}
