package com.example.tv360.dto;

import com.example.tv360.entity.Media;
import com.example.tv360.utils.MapToModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class MediaDetailDTO {
    @MapToModel("id")
    private Long id;

    @MapToModel("sourceUrl")
    private String sourceUrl;

    @MapToModel("typeUrl")
    private String typeUrl;

    @MapToModel("rate")
    private Integer rate;

    @MapToModel("duration")
    private String duration;

    @MapToModel("quality")
    private String quality;

    @MapToModel("mediaId")
    private Long mediaId;

    @MapToModel("status")
    private Integer status;

    @MapToModel("createdAt")
    private Timestamp createdAt;

    @MapToModel("updatedAt")
    private Timestamp updatedAt;

    @MapToModel("media")
    private Media media;
}
