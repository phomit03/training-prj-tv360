package com.example.tv360.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class MediaDetailResponse {
    private Long id;
    private List<Long> mediaDetailId;
    private String title;
    private String thumbnail;
    private String mediaDescription;
    private List<Integer> castTypes; // List of cast full names
    private List<String> castFullNames; // List of cast full names
    private List<String> categoryNames; // List of category names
    private String countryName;
    private String sourceUrl;
    private String duration;
    private String quality;
    private  List<Integer> episodes;

    public MediaDetailResponse(Long id,
                               String title,
                               String thumbnail,
                               String mediaDescription,
                               String countryName,
                               String sourceUrl,
                               String duration,
                               String quality
    ) {
        this.id = id;
        this.title = title;
        this.thumbnail = thumbnail;
        this.mediaDescription = mediaDescription;
        this.countryName = countryName;
        this.sourceUrl = sourceUrl;
        this.duration = duration;
        this.quality = quality;
    }
}