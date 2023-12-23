package com.example.tv360.repository;

import com.example.tv360.entity.FilmCast;
import com.example.tv360.entity.Media;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FilmCastRepository extends JpaRepository<FilmCast, Long> {
}
