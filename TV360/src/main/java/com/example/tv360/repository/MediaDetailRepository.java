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

//    @Query("SELECT new com.example.tv360.dto.response.MediaDetailResponse(" +
//            "m.title, " +
//            "m.type, " +
//            "m.description, " +
//            "c.fullName, " +
//            "c.type, " +
//            "ct.type, " +
//            "ct.name, " +
//            "co.name, " +
//            "md.sourceUrl, " +
//            "md.duration, " +
//            "md.quality, " +
//            "md.episode) " +
//            "FROM MediaDetail md " +
//            "INNER JOIN md.media m " +
//            "INNER JOIN m.cast c " +
//            "INNER JOIN m.categories ct " +
//            "INNER JOIN m.country co")
//    List<MediaDetailResponse> getMediaDetails();

    @Query("SELECT DISTINCT  new com.example.tv360.dto.response.MediaDetailResponse(" +
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
            "LEFT JOIN md.media m " +
            "LEFT JOIN m.cast c " +
            "LEFT JOIN m.categories ct " +
            "LEFT JOIN m.country co " +
            "LEFT JOIN m.categories mct " +
            "LEFT JOIN m.cast mcast " +
            "WHERE (mct IS NOT NULL AND mct.id = ct.id) OR (mcast IS NOT NULL AND mcast.id = c.id)")
    List<MediaDetailResponse> getMediaDetails();




}
