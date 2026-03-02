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
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDateTime;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(
    name = "player",
    uniqueConstraints = @UniqueConstraint(columnNames = {"team_id", "uniform_number"})
)
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "team_id", nullable = false)
    public Team team;

    @Column(name = "name", nullable = false, length = 64)
    @Comment("選手名")
    public String name;

    @Column(name = "uniform_number", nullable = false)
    @Comment("背番号")
    public Integer uniformNumber;

    @Column(name = "position", nullable = false, length = 16)
    @Comment("ポジション")
    public String position;

    @Column(name = "is_active", nullable = false)
    @ColumnDefault("true")
    @JsonIgnore
    public Boolean isActive = true;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @Column(name = "created_at", updatable = false, nullable = false, columnDefinition = "timestamp DEFAULT CURRENT_TIMESTAMP")
    @CreationTimestamp
    public LocalDateTime createdAt = LocalDateTime.now();

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @Column(name = "updated_at", nullable = false, columnDefinition = "timestamp DEFAULT CURRENT_TIMESTAMP")
    @UpdateTimestamp
    public LocalDateTime updatedAt = LocalDateTime.now();
}
