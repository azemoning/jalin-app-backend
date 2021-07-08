package com.jalin.jalinappbackend.module.gamification.leaderboard.repository;

import com.jalin.jalinappbackend.module.gamification.leaderboard.model.ListPointRankDto;
import com.jalin.jalinappbackend.module.gamification.point.entity.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface LeaderboardRepository extends JpaRepository<Point, UUID> {
    @Query(name = "find_rank", nativeQuery = true)
    List<ListPointRankDto> findUsersLeaderboard();
    @Query(name = "find_rank_top3",nativeQuery = true)
    List<ListPointRankDto> findUserLeaderBoardTop3();
    @Query(name = "findUser", nativeQuery = true)
    Optional<ListPointRankDto> findUserRank(@Param("fullName") String fullName);
}
