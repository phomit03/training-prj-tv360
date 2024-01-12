package com.example.tv360.repository;

import com.example.tv360.entity.Category;
import com.example.tv360.entity.Country;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CountryRepository extends JpaRepository<Country, Long> {
    @Query(value = "SELECT * FROM country WHERE (:name is null or name like CONCAT('%',:name,'%')) "+
            " AND (:status is null or status like CONCAT('%',:status,'%')) ", nativeQuery = true)
    List<Country> searchCategories(@Param("name") String name,
                                    @Param("status") Integer status, Pageable pageable);

    @Query(value = "SELECT * FROM country WHERE (:name is null or name like CONCAT('%',:name,'%')) "+
            " AND (:status is null or status like CONCAT('%',:status,'%')) ", nativeQuery = true)
    List<Country> searchCategories1(@Param("name") String name,
                                   @Param("status") Integer status);

    //soft-delete country (check media)
    Optional<Country> findByIdAndStatus(Long id, String status);
    @Query("SELECT COUNT(m) FROM Media m JOIN m.country c WHERE c.id = :countryId")
    int countMediaByCountryId(@Param("countryId") Long countryId);
}
