package com.example.tv360.Repository;

import com.example.tv360.Entity.MediaDetail;
import com.example.tv360.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MediaDetailRepository extends JpaRepository<MediaDetail, Long> {

}
