package com.example.tv360.repository;

import com.example.tv360.entity.MediaDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MediaDetailRepository extends JpaRepository<MediaDetail, Long> {
    List<MediaDetail> findTop15ByOrderByCreatedAtDesc();
}
