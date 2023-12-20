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

    @Column(name = "type")
    @MapToDTO
    private Integer type;

    @MapToDTO
    @Column(name = "country_id")
    private Long countryId;

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
    @JoinColumn(name = "country_id", referencedColumnName = "id", insertable = false, updatable = false)
    @MapToDTO
    private Country country;
}
