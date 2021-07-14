package com.jalin.jalinappbackend.module.gamification.leaderboard.repository.model;

import com.jalin.jalinappbackend.module.gamification.leaderboard.model.LeaderboardAdminDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.UUID;

@Entity
// Query get top 3
@NamedNativeQuery(
        name = "find_rank_top3_in_admin",
        query =
                "SELECT " +
                        "ROW_NUMBER() OVER(ORDER BY total_points DESC) rank, " +
                        "profile_picture AS profilePicture, " +
                        "jalin_id AS jalinId, " +
                        "total_points AS totalPoints " +
                        "FROM points " +
                        "INNER JOIN user_details ON points.user_id = user_details.user_id "+
                        "LIMIT 3;",
        resultSetMapping = "rank_dto_top3_in_admin"
)
@SqlResultSetMapping(
        name = "rank_dto_top3_in_admin",
        classes = @ConstructorResult(
                targetClass = LeaderboardAdminDto.class,
                columns = {
                        @ColumnResult(name = "rank", type = BigInteger.class),
                        @ColumnResult(name = "profilePicture", type =  String.class),
                        @ColumnResult(name = "jalinId", type = String.class),
                        @ColumnResult(name = "totalPoints", type = Integer.class)
                }
        )
)

// Query get after top 3
@NamedNativeQuery(
        name = "find_rank_in_admin",
        query =
                "SELECT " +
                        "ROW_NUMBER() OVER(ORDER BY total_points DESC) rank, " +
                        "profile_picture AS profilePicture, " +
                        "jalin_id AS jalinId, " +
                        "total_points AS totalPoints " +
                        "FROM points " +
                        "INNER JOIN user_details ON points.user_id = user_details.user_id " +
                        "OFFSET 3;",
        resultSetMapping = "rank_dto_in_admin"
)
@SqlResultSetMapping(
        name = "rank_dto_in_admin",
        classes = @ConstructorResult(
                targetClass = LeaderboardAdminDto.class,
                columns = {
                        @ColumnResult(name = "rank", type = BigInteger.class),
                        @ColumnResult(name = "profilePicture", type =  String.class),
                        @ColumnResult(name = "jalinId", type = String.class),
                        @ColumnResult(name = "totalPoints", type = Integer.class)
                }
        )
)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class QueryForAdmin {
    @Id
    @GeneratedValue
    private UUID id;
}
