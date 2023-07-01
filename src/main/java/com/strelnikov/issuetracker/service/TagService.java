package com.strelnikov.issuetracker.service;

import com.strelnikov.issuetracker.entity.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TagService {

    Tag findById(Long tagId);

    Tag addTag(Long issueId, Tag tag);

    Page<Tag> findByName(String name, Pageable pageable);
}
