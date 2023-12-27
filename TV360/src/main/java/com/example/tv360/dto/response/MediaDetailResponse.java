package com.example.tv360.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
//@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MediaDetailResponse {
    private String title;
    private Integer mediaType;
    private String mediaDescription;
    private String castFullname;
    private Integer castType;
    private Integer categoryType;
    private String categoryName;
    private String countryName;
    private String sourceUrl;
    private String duration;
    private String quality;
    private Integer mediaDetailEpisode;

    public MediaDetailResponse(String title, Integer mediaType, String mediaDescription, String castFullname, Integer castType, Integer categoryType, String categoryName, String countryName, String sourceUrl, String duration, String quality, Integer mediaDetailEpisode) {
        this.title = title;
        this.mediaType = mediaType;
        this.mediaDescription = mediaDescription;
        this.castFullname = castFullname;
        this.castType = castType;
        this.categoryType = categoryType;
        this.categoryName = categoryName;
        this.countryName = countryName;
        this.sourceUrl = sourceUrl;
        this.duration = duration;
        this.quality = quality;
        this.mediaDetailEpisode = mediaDetailEpisode;
    }
}