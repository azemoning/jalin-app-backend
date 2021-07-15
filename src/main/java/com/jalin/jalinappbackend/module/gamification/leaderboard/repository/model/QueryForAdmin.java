package com.jalin.jalinappbackend.module.gamification.leaderboard.repository.model;

import com.jalin.jalinappbackend.module.dashboard.model.LeaderboardDataDto;
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
                "SELECT ROW_NUMBER() OVER(ORDER BY total_points DESC) rank, profilePicture, jalinId, total_points AS totalPoints, missionSolved\n" +
                        "FROM (\n" +
                        "\tSELECT \n" +
                        "\tuser_details.user_id AS userId, jalin_id AS jalinId,\n" +
                        "\tprofile_picture AS profilePicture,\n" +
                        "\tSUM(CASE WHEN status = 'COMPLETED' THEN 1 ELSE 0 END) missionSolved\n" +
                        "\tFROM user_missions\n" +
                        "\tINNER JOIN user_details\n" +
                        "\tON user_missions.user_id = user_details.user_id\n" +
                        "\tGROUP BY user_details.user_id \n" +
                        ") AS us\n" +
                        "INNER JOIN points\n" +
                        "ON points.user_id = us.userId "+
                        "LIMIT 3",
        resultSetMapping = "rank_dto_top3_in_admin"
)
@SqlResultSetMapping(
        name = "rank_dto_top3_in_admin",
        classes = @ConstructorResult(
                targetClass = LeaderboardDataDto.class,
                columns = {
                        @ColumnResult(name = "rank", type = BigInteger.class),
                        @ColumnResult(name = "profilePicture", type =  String.class),
                        @ColumnResult(name = "jalinId", type = String.class),
                        @ColumnResult(name = "totalPoints", type = Integer.class),
                        @ColumnResult(name = "missionSolved", type = BigInteger.class)
                }
        )
)

// Query get after top 3
@NamedNativeQuery(
        name = "find_rank_in_admin",
        query =
                "SELECT ROW_NUMBER() OVER(ORDER BY total_points DESC) rank, profilePicture, jalinId, total_points AS totalPoints, missionSolved\n" +
                        "FROM (\n" +
                        "\tSELECT \n" +
                        "\tuser_details.user_id AS userId, jalin_id AS jalinId,\n" +
                        "\tprofile_picture AS profilePicture,\n" +
                        "\tSUM(CASE WHEN status = 'COMPLETED' THEN 1 ELSE 0 END) missionSolved\n" +
                        "\tFROM user_missions\n" +
                        "\tINNER JOIN user_details\n" +
                        "\tON user_missions.user_id = user_details.user_id\n" +
                        "\tGROUP BY user_details.user_id \n" +
                        ") AS us\n" +
                        "INNER JOIN points\n" +
                        "ON points.user_id = us.userId " +
                        "OFFSET 3 ",
        resultSetMapping = "rank_dto_in_admin"
)
@SqlResultSetMapping(
        name = "rank_dto_in_admin",
        classes = @ConstructorResult(
                targetClass = LeaderboardDataDto.class,
                columns = {
                        @ColumnResult(name = "rank", type = BigInteger.class),
                        @ColumnResult(name = "profilePicture", type =  String.class),
                        @ColumnResult(name = "jalinId", type = String.class),
                        @ColumnResult(name = "totalPoints", type = Integer.class),
                        @ColumnResult(name = "missionSolved", type = BigInteger.class)
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
