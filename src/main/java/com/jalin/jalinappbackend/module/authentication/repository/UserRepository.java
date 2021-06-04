package com.jalin.jalinappbackend.module.authentication.repository;

import com.jalin.jalinappbackend.module.authentication.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
