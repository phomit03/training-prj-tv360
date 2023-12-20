package com.example.tv360.repository;

import com.example.tv360.entity.MediaCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MediaCategoryRepository extends JpaRepository<MediaCategory, Long> {

}
