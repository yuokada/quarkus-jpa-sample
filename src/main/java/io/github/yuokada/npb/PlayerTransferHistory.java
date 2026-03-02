package io.github.yuokada.npb;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.github.yuokada.npb.serializer.LocalDateTimeSerializer;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import org.hibernate.annotations.Check;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(
    name = "player_transfer_history",
    indexes = {
        @Index(name = "idx_player_transfer_history_player_transferred_at", columnList = "player_id, transferred_at")
    }
)
@Check(constraints = "from_team_id is null or from_team_id <> to_team_id")
public class PlayerTransferHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "player_id", nullable = false)
    @JsonIgnore
    public Player player;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_team_id")
    @JsonIgnore
    public Team fromTeam;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "to_team_id", nullable = false)
    @JsonIgnore
    public Team toTeam;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @Column(name = "transferred_at", nullable = false, columnDefinition = "timestamp DEFAULT CURRENT_TIMESTAMP")
    @Comment("移籍日時")
    public LocalDateTime transferredAt = LocalDateTime.now();

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @Column(name = "created_at", nullable = false, updatable = false, columnDefinition = "timestamp DEFAULT CURRENT_TIMESTAMP")
    @CreationTimestamp
    public LocalDateTime createdAt = LocalDateTime.now();
}
