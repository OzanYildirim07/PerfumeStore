package com.perfumestore.repository;

import com.perfumestore.entity.Content;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContentRepository extends JpaRepository<Content, Long> {

    Optional<Content> findByName(String name);

    List<Content> findByType(Content.ContentType type);

    boolean existsByName(String name);
}
