package com.example.tv360.repository;

import com.example.tv360.entity.Cast;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CastRepository extends JpaRepository<Cast, Long> {

}
