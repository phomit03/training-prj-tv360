package com.example.tv360.dto.response;

import com.example.tv360.entity.Cast;
import com.example.tv360.entity.Category;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MediaCacheResponse {
    private List<MediaDetailResponse> episodeDetails;
    private List<Category> categories;
    private List<Cast> castList;

}
