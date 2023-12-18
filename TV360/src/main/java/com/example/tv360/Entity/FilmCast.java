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
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "film_cast")
public class FilmCast {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @MapToDTO
    private Long id;

    @MapToDTO
    @Column(name = "media_id")
    private Long mediaId;

    @MapToDTO
    @Column(name = "cast_id")
    private Long castId;

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

    @ManyToOne
    @JoinColumn(name = "media_id", referencedColumnName = "id", insertable = false, updatable = false)
    @MapToDTO
    private Media media;          //type: series_movie, movie

    @ManyToOne
    @JoinColumn(name = "cast_id", referencedColumnName = "id", insertable = false, updatable = false)
    @MapToDTO
    private Cast cast;
}
