package com.example.tv360.repository;

import com.example.tv360.entity.Cast;
import com.example.tv360.entity.Category;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query(value = "SELECT * FROM category WHERE (:name is null or name like CONCAT('%',:name,'%')) "+
            "AND (:type is null or type like CONCAT('%', :type, '%')) " +
            " AND (:status is null or status like CONCAT('%',:status,'%')) ", nativeQuery = true)
    List<Category> searchCategories(@Param("name") String name,
                           @Param("type") Integer type,
                           @Param("status") Integer status, Pageable pageable);

    @Query(value = "SELECT * FROM category WHERE (:name is null or name like CONCAT('%',:name,'%')) "+
            "AND (:type is null or type like CONCAT('%', :type, '%')) " +
            " AND (:status is null or status like CONCAT('%',:status,'%')) ", nativeQuery = true)
    List<Category> searchCategories1(@Param("name") String name,
                                @Param("type") Integer type,
                                @Param("status") Integer status);

    @Query("SELECT c FROM Category c " +
            "LEFT JOIN FETCH c.media m " +
            "WHERE m IS NOT NULL")
    Set<Category> findAllCategoriesWithMedia();

    List<Category> findByType(Integer type);

    //soft-delete category (check media)
    Optional<Category> findByIdAndStatus(Long id, String status);
    @Query("SELECT COUNT(m) FROM Media m JOIN m.categories c WHERE c.id = :categoryId")
    int countMediaByCategoryId(@Param("categoryId") Long categoryId);
}
