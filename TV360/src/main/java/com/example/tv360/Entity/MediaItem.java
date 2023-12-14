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
@Table(name = "media_item")
public class MediaItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @MapToDTO
    private Long id;

    @ManyToOne
    @JoinColumn(name = "media_id", referencedColumnName = "id")
    @MapToDTO
    private Media mediaId;

    @ManyToOne
    @JoinColumn(name = "item_id", referencedColumnName = "id")
    @MapToDTO
    private MediaDetail itemId;     //type: film

    @MapToDTO
    private String position;    //episode

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
}
