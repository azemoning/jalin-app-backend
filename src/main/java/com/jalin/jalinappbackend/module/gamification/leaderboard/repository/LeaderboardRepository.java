package com.jalin.jalinappbackend.module.gamification.leaderboard.repository;

import com.jalin.jalinappbackend.module.authentication.entity.User;
import com.jalin.jalinappbackend.module.dashboard.model.LeaderboardDataDto;
import com.jalin.jalinappbackend.module.gamification.leaderboard.model.LeaderboardDto;
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
    // leaderboard in android
    @Query(name = "find_rank", nativeQuery = true)
    List<ListPointRankDto> getUsersLeaderboard();
    @Query(name = "find_rank_top3",nativeQuery = true)
    List<ListPointRankDto> getUserLeaderBoardTop3();
    @Query(name = "findUser", nativeQuery = true)
    List<ListPointRankDto> findUserInLeadrrboard(@Param("name") String name);
    @Query(name = "findUserRank", nativeQuery = true)
    Optional<ListPointRankDto> getUserRankAndPoint(@Param("email") String email);
    // leaderboard in admin
    @Query(name = "find_rank_top3_in_admin", nativeQuery = true)
    List<LeaderboardDataDto> getUserLeaderboardTop3InAdmin();
    @Query(name = "find_rank_in_admin", nativeQuery = true)
    List<LeaderboardDataDto> getAlluserLeaderboardAfterTop3InAdmin();

    List<Point> findTop10ByOrderByTotalPointsDesc();
    List<Point> findAllByOrderByTotalPointsDesc();
    Point findPointByUser(User user);
}
