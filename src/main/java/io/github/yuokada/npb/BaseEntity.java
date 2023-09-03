package io.github.yuokada.npb;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@MappedSuperclass
public interface BaseEntity {

    @Column(name = "created_at", updatable = false, nullable = false, columnDefinition = "timestamp DEFAULT CURRENT_TIMESTAMP")
    @CreationTimestamp
    public LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false, columnDefinition = "timestamp DEFAULT CURRENT_TIMESTAMP")
    @UpdateTimestamp
    public LocalDateTime updatedAt = LocalDateTime.now();

}