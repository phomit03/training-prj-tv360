package com.example.tv360.dto.response;

import com.example.tv360.dto.CastDTO;
import com.example.tv360.entity.Media;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class MediaDetailResponse {
    private Long id;
    private String title;
    private Integer episode;
    private String thumbnail;
    private String mediaDescription;
    private List<CastItem> castList;
    private List<CategoryItem> categoryList;
    private List<String> listCategoryNames;
    private String countryName;
    private String sourceUrl;
    private String duration;
    private String quality;

    public MediaDetailResponse(Long id,
                               String title,
                               Integer episode,
                               String thumbnail,
                               String mediaDescription,
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
        this.countryName = countryName;
        this.sourceUrl = sourceUrl;
        this.duration = duration;
        this.quality = quality;
    }
}