package com.strelnikov.issuetracker.service;

import com.strelnikov.issuetracker.entity.Tag;

import java.util.List;

public interface TagService {

    Tag findById(Long tagId);

    Tag addTag(Long issueId, Tag tag);

    List<Tag> findByName(String name);
}
