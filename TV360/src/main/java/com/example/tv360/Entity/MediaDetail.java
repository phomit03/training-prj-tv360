package com.example.tv360.Entity;

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
@Table(name = "media_detail")
public class MediaDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "media_url")
    private String mediaUrl;

    @Column(name = "type")
    private Integer type;   //type: vod, film

    @Column(name = "status", columnDefinition = "INT DEFAULT 1")
    private Integer status;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Timestamp updatedAt;

    // KHOA TRUNG GIAN
    @OneToMany(mappedBy = "itemId", cascade = CascadeType.ALL)
    private Set<MediaItem> mediaItems;
}
