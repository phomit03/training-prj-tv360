package com.example.tv360.repository;

import com.example.tv360.entity.Category;
import com.example.tv360.entity.MediaCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MediaCategoryRepository extends JpaRepository<MediaCategory, Long> {
    List<MediaCategory> findByCategory(Category category);
}
