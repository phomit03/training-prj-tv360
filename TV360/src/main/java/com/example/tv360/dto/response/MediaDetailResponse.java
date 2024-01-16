package com.example.tv360.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class MediaDetailResponse {
    private Long id;
    private Long mediaID;
    private String title;
    private Integer mediaType;
    private Integer episode;
    private String thumbnail;
    private String mediaDescription;
    private List<CastItem> castList;
    private List<CategoryItem> categoryList;
    private Long countryId;
    private String countryName;
    private String sourceUrl;
    private String duration;
    private String quality;
    private Integer rating;

    public MediaDetailResponse(Long id,
                               Long mediaID,
                               String title,
                               Integer mediaType,
                               Integer episode,
                               String thumbnail,
                               String mediaDescription,
                               Long countryId,
                               String countryName,
                               String sourceUrl,
                               String duration,
                               String quality,
                               Integer rating
    ) {
        this.id = id;
        this.mediaID = mediaID;
        this.title = title;
        this.mediaType = mediaType;
        this.episode = episode;
        this.thumbnail = thumbnail;
        this.mediaDescription = mediaDescription;
        this.countryId = countryId;
        this.countryName = countryName;
        this.sourceUrl = sourceUrl;
        this.duration = duration;
        this.quality = quality;
        this.rating = rating;
    }

    public MediaDetailResponse(Long id,
                               String title,
                               Integer mediaType,
                               String thumbnail,
                               String mediaDescription,
                               String sourceUrl,
                               String duration,
                               String quality,
                               Integer rating
    ) {
        this.id = id;
        this.title = title;
        this.mediaType = mediaType;
        this.thumbnail = thumbnail;
        this.mediaDescription = mediaDescription;
        this.sourceUrl = sourceUrl;
        this.duration = duration;
        this.quality = quality;
        this.rating = rating;
    }
}