package com.example.tv360.Repository;

import com.example.tv360.Entity.MediaCategory;
import com.example.tv360.Entity.MediaItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MediaItemRepository extends JpaRepository<MediaItem, Long> {

}
