package com.jalin.jalinappbackend.module.gamification.point.entity;

import com.jalin.jalinappbackend.module.authentication.entity.User;
import com.jalin.jalinappbackend.module.gamification.leaderboard.model.ListPointRankDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.math.BigInteger;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "points")
@NamedNativeQuery(
        name = "find_rank",
        query =
                "SELECT " +
                        "points.user_id AS userId, " +
                        "full_name AS fullName, " +
                        "total_points AS totalPoints, " +
                        "DENSE_RANK() OVER(ORDER BY total_points DESC) rank " +
                        "FROM points " +
                        "INNER JOIN user_details ON points.user_id = user_details.user_id "+
                        "LIMIT 10;",
        resultSetMapping = "rank_dto"
)
@SqlResultSetMapping(
        name = "rank_dto",
        classes = @ConstructorResult(
                targetClass = ListPointRankDto.class,
                columns = {
                        @ColumnResult(name = "userId", type = UUID.class),
                        @ColumnResult(name = "fullName", type = String.class),
                        @ColumnResult(name = "totalPoints", type = Integer.class),
                        @ColumnResult(name = "rank", type = BigInteger.class)
                }
        )
)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Point {
    @Id
    private UUID pointId;
    private Integer totalPoints = 0;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @CreationTimestamp
    private Instant createdDate;

    @UpdateTimestamp
    private Instant modifiedDate;
}
