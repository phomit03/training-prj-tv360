package com.example.tv360.repository;

import com.example.tv360.dto.response.MediaDetailResponse;
import com.example.tv360.entity.MediaDetail;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MediaDetailRepository extends JpaRepository<MediaDetail, Long> {
    @Query("SELECT md FROM MediaDetail md JOIN FETCH md.media WHERE md.media.status = 1 AND md.status = 1 ORDER BY md.createdAt DESC")
    List<MediaDetail> findNewRelease(Pageable pageable);

    @Query("SELECT md FROM MediaDetail md JOIN FETCH md.media WHERE md.rate = 5 AND md.media.status = 1 AND md.status = 1 ORDER BY md.createdAt DESC")
    List<MediaDetail> findTopRated(Pageable pageable);

    // getall
    @Query("SELECT DISTINCT  new com.example.tv360.dto.response.MediaDetailResponse(" +
            "m.id, "+
            "m.title, " +
            "m.thumbnail, " +
            "m.description, " +
            "co.name, " +
            "md.sourceUrl, " +
            "md.duration, " +
            "md.quality) " +
            "FROM MediaDetail md " +
            "LEFT JOIN md.media m " +
            "LEFT JOIN m.country co ")
    List<MediaDetailResponse> getMediaDetails();
//    // get mediadetail byid
//    @Query("SELECT DISTINCT new com.example.tv360.dto.response.MediaDetailResponse(" +
//            "m.id, " +
//            "m.title, " +
//            "m.thumbnail, " +
//            "m.type, " +
//            "m.description, " +
//            "c.type, " +
//            "ct.type, " +
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
//            "WHERE ((mct IS NOT NULL AND mct.id = ct.id) OR (mcast IS NOT NULL AND mcast.id = c.id)) " +
//            "AND m.id = :mediaId")
//    MediaDetailResponse getMediaDetailById(@Param("mediaId") Long mediaId);

    // tim theo 1 id media
    @Query("SELECT DISTINCT new com.example.tv360.dto.response.MediaDetailResponse(" +
            "m.id, "+
            "m.title, " +
            "m.thumbnail, " +
            "m.description, " +
            "co.name, " +
            "md.sourceUrl, " +
            "md.duration, " +
            "md.quality) " +
            "FROM MediaDetail md " +
            "LEFT JOIN md.media m " +
            "LEFT JOIN m.country co "+
            "WHERE  m.id = :mediaId")
    List<MediaDetailResponse> getMediaDetailById(@Param("mediaId") Long mediaId);

    // lay ra tat ca castfullname theo mediaDetailID
    @Query("SELECT DISTINCT c.fullName FROM MediaDetail md " +
            "LEFT JOIN md.media m " +
            "LEFT JOIN m.cast c " +
            "WHERE  m.id = :mediaId")
    List<String> getCastFullNamesByMediaDetailId(@Param("mediaId") Long mediaId);

    // lay ra tat ca cap tap

    @Query("SELECT md.episode FROM MediaDetail md " +
            "LEFT JOIN md.media m " +
            "WHERE (m.id = :mediaId)")
    List<Integer> getEpisodesByMediaDetailId(@Param("mediaId") Long mediaId);


    // lay ra tat ca categoryname theo mediaDetailID
    @Query("SELECT DISTINCT ct.name FROM MediaDetail md " +
            "LEFT JOIN md.media m " +
            "LEFT JOIN m.categories ct " +
            "WHERE m.id = :mediaId")
    List<String> getCategoryNamesByMediaDetailId(@Param("mediaId") Long mediaId);


    // loc theo categoryname
    @Query("SELECT DISTINCT new com.example.tv360.dto.response.MediaDetailResponse(" +
            "m.id, " +
            "m.title, " +
            "m.thumbnail, " +
            "m.description, " +
            "co.name, " +
            "md.sourceUrl, " +
            "md.duration, " +
            "md.quality) " +
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

    @Query("SELECT MAX(md.episode) FROM MediaDetail md WHERE md.media.id = :mediaId")
    Integer findMaxEpisodeByMediaId(@Param("mediaId") Long mediaId);


}
