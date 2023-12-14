package com.example.tv360.Entity;

import com.example.tv360.Utils.MapToDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Set;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "media")
public class Media {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @MapToDTO
    private Long id;

    @MapToDTO
    private String thumbnail;

    @MapToDTO
    private String title;

    @MapToDTO
    private String description;

    @MapToDTO
    private String evaluate;

    @MapToDTO
    private String duration;

    @MapToDTO
    private String quality;

    @MapToDTO
    private String country_id;

    @Column(name = "type")
    @MapToDTO
    private Integer type;

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

    // KHOA TRUNG GIAN
    @MapToDTO
    @OneToMany(mappedBy = "mediaId", cascade = CascadeType.ALL)
    private Set<FilmCast> filmCasts;

    @MapToDTO
    @OneToMany(mappedBy = "mediaId", cascade = CascadeType.ALL)
    private Set<MediaCategory> mediaCategories;

    @MapToDTO
    @OneToMany(mappedBy = "mediaId", cascade = CascadeType.ALL)
    private Set<MediaItem> mediaItems;

    @OneToOne
    @JoinColumn(name = "country_id", referencedColumnName = "id", insertable = false, updatable = false)
    @MapToDTO
    private Country country;

}
