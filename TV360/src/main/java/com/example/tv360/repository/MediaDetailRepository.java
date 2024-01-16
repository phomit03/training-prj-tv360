package com.example.tv360.repository;

import com.example.tv360.dto.response.CastItem;
import com.example.tv360.dto.response.CategoryItem;
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

    // tim theo 1 id media và sắp xếp theo episode (ASC)
    @Query("SELECT DISTINCT new com.example.tv360.dto.response.MediaDetailResponse(" +
            "m.id, "+
            "m.title, " +
            "m.type, " +
            "md.episode, " +
            "m.thumbnail, " +
            "m.description, " +
            "co.name, " +
            "md.sourceUrl, " +
            "md.duration, " +
            "md.quality, " +
            "md.rate) " +
            "FROM MediaDetail md " +
            "LEFT JOIN md.media m " +
            "LEFT JOIN m.cast c "+
            "LEFT JOIN m.categories ct "+
            "LEFT JOIN m.country co "+
            "WHERE m.id = :mediaId and c.status=1 and ct.status=1 and co.status =1 " +
            "ORDER BY md.episode ASC")
    List<MediaDetailResponse> getMovieDetailByIdASCEpisodes(@Param("mediaId") Long mediaId);

    @Query("SELECT DISTINCT new com.example.tv360.dto.response.MediaDetailResponse(" +
            "m.id, "+
            "m.title, " +
            "m.type, " +
            "m.thumbnail, " +
            "m.description, " +
            "md.sourceUrl, " +
            "md.duration, " +
            "md.quality," +
            "md.rate) " +
            "FROM MediaDetail md " +
            "LEFT JOIN md.media m " +
            "LEFT JOIN m.categories ct "+
            "WHERE m.id = :mediaId and ct.status=1")
    List<MediaDetailResponse> getVideoDetail(@Param("mediaId") Long mediaId);



    // lay ra all category name theo mediaDetailID
    @Query("SELECT DISTINCT ct.name FROM MediaDetail md " +
            "LEFT JOIN md.media m " +
            "LEFT JOIN m.categories ct " +
            "WHERE m.id = :mediaId and ct.status = 1")
    List<String> getCategoryNamesByMediaDetailId(@Param("mediaId") Long mediaId);

    // lay ra cac type va name cua cast
    @Query("SELECT distinct new com.example.tv360.dto.response.CastItem(" +
            "m.id, " +
            "c.id, " +
            "c.fullName, " +
            "c.type) " +
            "FROM MediaDetail md " +
            "LEFT JOIN md.media m " +
            "LEFT JOIN m.cast c " +
            "WHERE m.id = :mediaId and c.status = 1")
    List<CastItem> getCastByMediaDetailId(@Param("mediaId") Long mediaId);

    // lay ra cac type va name cua cast
    @Query("SELECT distinct new com.example.tv360.dto.response.CategoryItem(" +
            "m.id, " +
            "ct.id, " +
            "ct.name) " +
            "FROM MediaDetail md " +
            "LEFT JOIN md.media m " +
            "LEFT JOIN m.categories ct " +
            "WHERE m.id = :mediaId and ct.status = 1")
    List<CategoryItem> getCategoryByMediaDetailId(@Param("mediaId") Long mediaId);

    // loc theo category name
    @Query("SELECT DISTINCT new com.example.tv360.dto.response.MediaDetailResponse(" +
            "m.id, "+
            "m.title, " +
            "m.type, " +
            "md.episode, " +
            "m.thumbnail, " +
            "m.description, " +
            "co.name, " +
            "md.sourceUrl, " +
            "md.duration, " +
            "md.quality," +
            "md.rate) " +
            "FROM MediaDetail md " +
            "LEFT JOIN md.media m " +
            "LEFT JOIN m.cast c " +
            "LEFT JOIN m.categories ct " +
            "LEFT JOIN m.country co " +
            "LEFT JOIN m.categories mct " +
            "LEFT JOIN m.cast mcast " +
            "WHERE ((mct IS NOT NULL AND mct.id = ct.id) OR (mcast IS NOT NULL AND mcast.id = c.id)) AND ct.name = :categoryName and  ct.status = 1")
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
