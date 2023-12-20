package com.example.tv360.Entity;

import com.example.tv360.Utils.MapToDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "media_category")
public class MediaCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @MapToDTO
    private Long id;

    @MapToDTO
    @Column(name = "media_id")
    private Long mediaId;

    @MapToDTO
    @Column(name = "category_id")
    private Long categoryId;

    @Column(name = "status", columnDefinition = "INT DEFAULT 1")
    @MapToDTO
    private Integer status;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    @MapToDTO
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    @MapToDTO
    private Timestamp updatedAt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "media_id", referencedColumnName = "id", insertable = false, updatable = false)
    @MapToDTO
    private Media media;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id", referencedColumnName = "id", insertable = false, updatable = false)
    @MapToDTO
    private Category category;
}

