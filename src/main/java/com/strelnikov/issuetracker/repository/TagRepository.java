package com.strelnikov.issuetracker.repository;

import com.strelnikov.issuetracker.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag, Long> {

    List<Tag> findByNameContaining(String name);

    Tag findByName(String name);
}
