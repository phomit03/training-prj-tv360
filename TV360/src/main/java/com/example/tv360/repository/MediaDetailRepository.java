package com.example.tv360.repository;

import com.example.tv360.dto.response.MediaDetailResponse;
import com.example.tv360.entity.MediaDetail;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MediaDetailRepository extends JpaRepository<MediaDetail, Long> {
    @Query("SELECT md FROM MediaDetail md JOIN FETCH md.media ORDER BY md.createdAt DESC")
    List<MediaDetail> findNewRelease(Pageable pageable);

    @Query("SELECT md FROM MediaDetail md JOIN FETCH md.media WHERE md.rate = 5 ORDER BY md.createdAt DESC")
    List<MediaDetail> findTopRated(Pageable pageable);

//    @Query("SELECT md.media AS media_detail_id, " +
//            "md.media AS media_id, " +
//            "m.title AS media_title, " +
//            "m.type AS media_type, " +
//            "m.description AS media_description, " +
//            "c.fullName AS cast_fullname, " +
//            "c.type AS cast_type, " +
//            "ct.type AS category_type, " +
//            "ct.name AS category_name, " +
//            "co.name AS country_name, " +
//            "md.sourceUrl, " +
//            "md.duration, " +
//            "md.quality, " +
//            "md.episode AS media_detail_episode " +
//            "FROM MediaDetail md " +
//            "INNER JOIN md.media m " +
//            "INNER JOIN m.cast c " +
//            "INNER JOIN m.categories ct " +
//            "INNER JOIN m.country co")
//    List<Object[]> getMediaDetails();

//    @Query("SELECT m.title AS media_title, " +
//            "m.type AS media_type, " +
//            "m.description AS media_description, " +
//            "c.fullName AS cast_fullname, " +
//            "c.type AS cast_type, " +
//            "ct.type AS category_type, " +
//            "ct.name AS category_name, " +
//            "co.name AS country_name, " +
//            "md.sourceUrl, " +
//            "md.duration, " +
//            "md.quality, " +
//            "md.episode AS media_detail_episode " +
//            "FROM MediaDetail md " +
//            "INNER JOIN md.media m " +
//            "INNER JOIN m.cast c " +
//            "INNER JOIN m.categories ct " +
//            "INNER JOIN m.country co")
//    List<Object[]> getMediaDetails();

    @Query("SELECT new com.example.tv360.dto.response.MediaDetailResponse(" +
            "m.title, " +
            "m.type, " +
            "m.description, " +
            "c.fullName, " +
            "c.type, " +
            "ct.type, " +
            "ct.name, " +
            "co.name, " +
            "md.sourceUrl, " +
            "md.duration, " +
            "md.quality, " +
            "md.episode) " +
            "FROM MediaDetail md " +
            "INNER JOIN md.media m " +
            "INNER JOIN m.cast c " +
            "INNER JOIN m.categories ct " +
            "INNER JOIN m.country co")
    List<MediaDetailResponse> getMediaDetails();

}
