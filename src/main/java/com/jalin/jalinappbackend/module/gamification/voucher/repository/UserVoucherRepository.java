package com.jalin.jalinappbackend.module.gamification.voucher.repository;

import com.jalin.jalinappbackend.module.authentication.entity.User;
import com.jalin.jalinappbackend.module.gamification.voucher.entity.UserVoucher;
import com.jalin.jalinappbackend.module.gamification.voucher.entity.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserVoucherRepository extends JpaRepository<UserVoucher, UUID> {
    List<UserVoucher> findUserVouchersByUserAndIsActiveEquals(User user, boolean isActive);
    Optional<UserVoucher> findUserVoucherByIdAndUserAndIsActiveEquals(UUID id, User user, boolean isActive);
    UserVoucher findUserVoucherByVoucher(Voucher voucher);
}
