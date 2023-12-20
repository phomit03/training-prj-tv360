package com.example.tv360.repository;

import com.example.tv360.entity.MediaDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MediaDetailRepository extends JpaRepository<MediaDetail, Long> {
    MediaDetail findByMediaId(Long mediaId);
}
