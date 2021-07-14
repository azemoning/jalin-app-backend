package com.jalin.jalinappbackend.module.gamification.leaderboard.repository.model;

import com.jalin.jalinappbackend.module.gamification.leaderboard.model.ListPointRankDto;
import com.jalin.jalinappbackend.module.gamification.leaderboard.model.UserRankDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.UUID;


@Entity
// Query get user point and user rank
@NamedNativeQuery(
        name = "findUserRank",
        query =
                "SELECT totalPoints, rank\n" +
                        "FROM\n" +
                        "\t(SELECT points.user_id AS userId,\n" +
                        "\tfull_name AS fullName, total_points AS totalPoints, \n" +
                        "\tROW_NUMBER() OVER(ORDER BY total_points DESC) rank \n" +
                        "\tFROM points \n" +
                        "\tINNER JOIN user_details ON points.user_id = user_details.user_id ) AS usera\n" +
                        "INNER JOIN users\n" +
                        "ON users.user_id=usera.userId\n" +
                        "WHERE email LIKE CONCAT ('%',:email,'%')",
        resultSetMapping = "findUserRankDto"
)
@SqlResultSetMapping(
        name = "findUserRankDto",
        classes = @ConstructorResult(
                targetClass = UserRankDto.class,
                columns = {
                        @ColumnResult(name = "totalPoints", type = Integer.class),
                        @ColumnResult(name = "rank", type = BigInteger.class)
                }
        )
)

// Query get top 3
@NamedNativeQuery(
        name = "find_rank_top3",
        query =
                "SELECT " +
                        "jalin_id AS jalinId, " +
                        "total_points AS totalPoints, " +
                        "ROW_NUMBER() OVER(ORDER BY total_points DESC) rank " +
                        "FROM points " +
                        "INNER JOIN user_details ON points.user_id = user_details.user_id "+
                        "LIMIT 3;",
        resultSetMapping = "rank_dto_top3"
)
@SqlResultSetMapping(
        name = "rank_dto_top3",
        classes = @ConstructorResult(
                targetClass = ListPointRankDto.class,
                columns = {
                        @ColumnResult(name = "jalinId", type = String.class),
                        @ColumnResult(name = "totalPoints", type = Integer.class),
                        @ColumnResult(name = "rank", type = BigInteger.class)
                }
        )
)

// Query get after top 3
@NamedNativeQuery(
        name = "find_rank",
        query =
                "SELECT " +
                        "jalin_id AS jalinId, " +
                        "total_points AS totalPoints, " +
                        "ROW_NUMBER() OVER(ORDER BY total_points DESC) rank " +
                        "FROM points " +
                        "INNER JOIN user_details ON points.user_id = user_details.user_id " +
                        "OFFSET 3;",
        resultSetMapping = "rank_dto"
)
@SqlResultSetMapping(
        name = "rank_dto",
        classes = @ConstructorResult(
                targetClass = ListPointRankDto.class,
                columns = {
                        @ColumnResult(name = "jalinId", type = String.class),
                        @ColumnResult(name = "totalPoints", type = Integer.class),
                        @ColumnResult(name = "rank", type = BigInteger.class)
                }
        )
)

// Query search
@NamedNativeQuery(
        name = "findUser",
        query =
                "SELECT " +
                        "jalin_id AS jalinId, " +
                        "total_points AS totalPoints, " +
                        "ROW_NUMBER() OVER(ORDER BY total_points DESC) rank " +
                        "FROM points " +
                        "INNER JOIN user_details ON points.user_id = user_details.user_id "+
                        "WHERE full_name LIKE CONCAT ('%',:name,'%')",
        resultSetMapping = "findUserDto"
)
@SqlResultSetMapping(
        name = "findUserDto",
        classes = @ConstructorResult(
                targetClass = ListPointRankDto.class,
                columns = {
                        @ColumnResult(name = "jalinId", type = String.class),
                        @ColumnResult(name = "totalPoints", type = Integer.class),
                        @ColumnResult(name = "rank", type = BigInteger.class)
                }
        )
)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Query {
    @Id
    @GeneratedValue
    private UUID id;
}
