package com.strelnikov.issuetracker.repository;

import com.strelnikov.issuetracker.entity.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long> {

    Page<Tag> findByNameContaining(String name, Pageable pageable);

    Page<Tag> findAll(Pageable pageable);

    Tag findByName(String name);
}
