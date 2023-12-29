package com.example.tv360.repository;

import com.example.tv360.entity.Media;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
public interface MediaRepository extends JpaRepository<Media, Long> {


}
