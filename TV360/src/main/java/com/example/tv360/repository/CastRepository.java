package com.example.tv360.repository;

import com.example.tv360.entity.Cast;
import com.example.tv360.entity.Category;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CastRepository extends JpaRepository<Cast, Long> {
    @Query(value = "SELECT * FROM cast WHERE (:fullName is null or full_name like CONCAT('%',:fullName,'%')) "+
            "AND (:type is null or type like CONCAT('%', :type, '%')) " +
            " AND (:status is null or status like CONCAT('%',:status,'%')) ", nativeQuery = true)
    List<Cast> searchCasts(@Param("fullName") String fullName,
                           @Param("type") Integer type,
                           @Param("status") Integer status, Pageable pageable);

    @Query(value = "SELECT * FROM cast WHERE (:fullName is null or full_name like CONCAT('%',:fullName,'%')) "+
            "AND (:type is null or type like CONCAT('%', :type, '%')) " +
            " AND (:status is null or status like CONCAT('%',:status,'%')) ", nativeQuery = true)
    List<Cast> searchCasts1(@Param("fullName") String fullName,
                           @Param("type") Integer type,
                           @Param("status") Integer status);

    //soft-delete cast (check media)
    Optional<Cast> findByIdAndStatus(Long id, String status);
    @Query("SELECT COUNT(m) FROM Media m JOIN m.cast c WHERE c.id = :castId")
    int countMediaByCastId(@Param("castId") Long castId);
}
