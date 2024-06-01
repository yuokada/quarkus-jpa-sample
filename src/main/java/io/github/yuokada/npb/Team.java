package io.github.yuokada.npb;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.github.yuokada.npb.serializer.LocalDateTimeSerializer;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "team")
public class Team {

    // see: https://quarkus.io/guides/hibernate-orm-panache#custom-ids
//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    @SequenceGenerator(
        name = "teamSequence",
        sequenceName = "team_seq",
        allocationSize = 1,
        initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "teamSequence")
    public Integer id;

    @Column(length = 64, nullable = false, unique = true)
    public String name;

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

    @Override
    public String toString() {
        return "Team{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", isActive=" + isActive +
            '}';
    }
}
