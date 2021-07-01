package com.jalin.jalinappbackend.module.gamification.leaderboard.repository;

import com.jalin.jalinappbackend.module.gamification.point.entity.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.UUID;

@Repository
public interface LeaderboardRepository extends JpaRepository<Point, UUID> {
    @Query(value = "SELECT full_name , total_points, RANK() OVER(ORDER BY total_points) rank_number " +
            "FROM points " +
            "INNER JOIN user_details ON points.user_id = user_details.user_id " +
            "ORDER BY rank_number " +
            "LIMIT 10;", nativeQuery = true)
    ArrayList<Object> findUsersLeaderboard();
}
