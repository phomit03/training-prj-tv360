package com.example.tv360.repository;

import com.example.tv360.entity.Cast;
import com.example.tv360.entity.Country;
import com.example.tv360.entity.Media;
import com.example.tv360.entity.MediaDetail;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
public interface MediaRepository extends JpaRepository<Media, Long> {
    @Query(value = "SELECT * FROM media WHERE (:title is null or title like CONCAT('%',:title,'%')) "+
            "AND (:type is null or type like CONCAT('%', :type, '%')) " +
            " AND (:status is null or status like CONCAT('%',:status,'%')) ", nativeQuery = true)
    List<Media> searchMedia(@Param("title") String title,
                              @Param("type") Integer type,
                              @Param("status") Integer status, Pageable pageable);

    @Query(value = "SELECT * FROM media WHERE (:title is null or title like CONCAT('%',:title,'%')) "+
            "AND (:type is null or type like CONCAT('%', :type, '%')) " +
            " AND (:status is null or status like CONCAT('%',:status,'%')) ", nativeQuery = true)
    List<Media> searchMedia1(@Param("title") String title,
                           @Param("type") Integer type,
                           @Param("status") Integer status);
    List<Media> findByCategoriesId(Long categoryId);

    @Query("SELECT md FROM MediaDetail md WHERE md.media.id = :mediaId")
    List<MediaDetail> findMediaDetailsByMediaId(@Param("mediaId") Long mediaId);

}
