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
//    private List<Long> mediaDetailId;
    private String title;
    private Integer episode;
    private String thumbnail;
    private String mediaDescription;
//    private Integer castTypes;
//    private String castFullNames;
//    private String categoryNames;
    private List<Integer> listCastTypes; // List of cast full names
    private List<String> listCastFullNames; // List of cast full names
    private List<String> listCategoryNames; // List of category names
    private String countryName;
    private String sourceUrl;
    private String duration;
    private String quality;
//    private  List<Integer> episodes;

    public MediaDetailResponse(Long id,
                               String title,
                               Integer episode,
                               String thumbnail,
                               String mediaDescription,
//                               Integer castTypes,
//                               String castFullNames,
//                               String categoryNames,
                               String countryName,
                               String sourceUrl,
                               String duration,
                               String quality
    ) {
        this.id = id;
        this.title = title;
        this.episode = episode;
        this.thumbnail = thumbnail;
        this.mediaDescription = mediaDescription;
//        this.castTypes = castTypes;
//        this.castFullNames = castFullNames;
//        this.categoryNames = categoryNames;
        this.countryName = countryName;
        this.sourceUrl = sourceUrl;
        this.duration = duration;
        this.quality = quality;
    }
}