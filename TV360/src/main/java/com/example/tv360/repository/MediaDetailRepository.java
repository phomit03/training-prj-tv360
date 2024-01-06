package com.example.tv360.repository;

import com.example.tv360.dto.response.MediaDetailResponse;
import com.example.tv360.entity.Cast;
import com.example.tv360.entity.MediaDetail;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MediaDetailRepository extends JpaRepository<MediaDetail, Long> {
    @Query("SELECT md FROM MediaDetail md JOIN FETCH md.media WHERE md.media.status = 1 AND md.status = 1 ORDER BY md.createdAt DESC")
    List<MediaDetail> findNewRelease(Pageable pageable);

    @Query("SELECT md FROM MediaDetail md JOIN FETCH md.media WHERE md.rate = 5 AND md.media.status = 1 AND md.status = 1 ORDER BY md.createdAt DESC")
    List<MediaDetail> findTopRated(Pageable pageable);

    // getall
    @Query("SELECT DISTINCT  new com.example.tv360.dto.response.MediaDetailResponse(" +
            "md.id, "+
            "m.title, " +
            "m.thumbnail, " +
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

    // tim theo 1 id media
//    @Query("SELECT DISTINCT new com.example.tv360.dto.response.MediaDetailResponse(" +
//            "m.id, "+
//            "m.title, " +
//            "m.thumbnail, " +
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
//            "LEFT JOIN md.media m " +
//            "LEFT JOIN m.cast c " +
//            "LEFT JOIN m.categories ct " +
//            "LEFT JOIN m.country co " +
//            "LEFT JOIN m.categories mct " +
//            "LEFT JOIN m.cast mcast " +
//            "WHERE ((mct IS NOT NULL AND mct.id = ct.id) OR (mcast IS NOT NULL AND mcast.id = c.id)) AND m.id = :mediaId")
//    List<MediaDetailResponse> getMediaDetailsById(@Param("mediaId") Long mediaId);
    @Query("SELECT DISTINCT new com.example.tv360.dto.response.MediaDetailResponse(" +
            "md.id, " +
            "m.title, " +
            "m.thumbnail, " +
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
            "WHERE ((mct IS NOT NULL AND mct.id = ct.id) OR (mcast IS NOT NULL AND mcast.id = c.id)) " +
            "AND md.id = :mediaDetailId")
    List<MediaDetailResponse> getMediaDetailById(@Param("mediaDetailId") Long mediaDetailId);


    // loc theo categoryname
    @Query("SELECT DISTINCT new com.example.tv360.dto.response.MediaDetailResponse(" +
            "m.id, " +
            "m.title, " +
            "m.thumbnail, " +
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
            "WHERE ((mct IS NOT NULL AND mct.id = ct.id) OR (mcast IS NOT NULL AND mcast.id = c.id)) AND ct.name = :categoryName")
    List<MediaDetailResponse> getMediaDetailsByCategoryName(@Param("categoryName") String categoryName);



    // phan trang
    @Query(value = "SELECT md.* FROM media_detail md " +
            "LEFT JOIN media m ON md.media_id = m.id " +
            "WHERE (:title is null or m.title  like CONCAT('%', :title, '%')) " +
            "AND (:quality  is null or md.quality  like CONCAT('%', :quality, '%')) " +
            "AND (:episode is null or md.episode like CONCAT('%', :episode, '%')) " +
            "AND (:status is null or md.status like CONCAT('%', :status, '%'))", nativeQuery = true)
    List<MediaDetail> searchMediaDetails(@Param("title") String title,
                                         @Param("quality") String quality,
                                         @Param("episode") Integer episode,
                                         @Param("status") Integer status, Pageable pageable);

    @Query(value = "SELECT md.* FROM media_detail md " +
            "LEFT JOIN media m ON md.media_id = m.id " +
            "WHERE (:title is null or m.title  like CONCAT('%', :title, '%')) " +
            "AND (:quality  is null or md.quality  like CONCAT('%', :quality, '%')) " +
            "AND (:episode is null or md.episode like CONCAT('%', :episode, '%')) " +
            "AND (:status is null or md.status like CONCAT('%', :status, '%'))", nativeQuery = true)
    List<MediaDetail> searchMediaDetails1(@Param("title") String title,
                                         @Param("quality") String quality,
                                         @Param("episode") Integer episode,
                                         @Param("status") Integer status);






}
