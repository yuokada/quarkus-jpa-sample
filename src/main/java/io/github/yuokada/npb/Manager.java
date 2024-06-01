package io.github.yuokada.npb;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.github.yuokada.npb.serializer.LocalDateTimeSerializer;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "manager")
public class Manager {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;

    @OneToOne(fetch = FetchType.LAZY)
    public Team team;

    @Column(nullable = false)
    @Comment("監督名")
    public String name;

    @Column(name = "is_active")
    @ColumnDefault("true")
    @JsonIgnore
    public Boolean isActive;

    // @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @Column(name = "created_at", updatable = false, nullable = false, columnDefinition = "timestamp DEFAULT CURRENT_TIMESTAMP")
    @CreationTimestamp
    public LocalDateTime createdAt = LocalDateTime.now();

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @Column(name = "updated_at", nullable = false, columnDefinition = "timestamp DEFAULT CURRENT_TIMESTAMP")
    @UpdateTimestamp
    public LocalDateTime updatedAt = LocalDateTime.now();

    @Override
    public String toString() {
        return "Manager{" +
            "id=" + id +
            ", team=" + team.id +
            ", name='" + name + '\'' +
            ", isActive=" + isActive +
            ", createdAt=" + createdAt +
            ", updatedAt=" + updatedAt +
            '}';
    }
}
