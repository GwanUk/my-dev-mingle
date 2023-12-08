package com.example.dm.repository;

import com.example.dm.entity.Categories;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoriesRepository extends JpaRepository<Categories, Long> {

    Optional<Categories> findByName(String name);
}