package com.strelnikov.issuetracker.service;

import com.strelnikov.issuetracker.entity.Issue;
import com.strelnikov.issuetracker.entity.Tag;
import com.strelnikov.issuetracker.exception.IssueNotFoundException;
import com.strelnikov.issuetracker.exception.TagNotFoundException;
import com.strelnikov.issuetracker.repository.IssueRepository;
import com.strelnikov.issuetracker.repository.TagRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;
    private final IssueRepository issueRepository;

    public TagServiceImpl(TagRepository tagRepository, IssueRepository issueRepository) {
        this.tagRepository = tagRepository;
        this.issueRepository = issueRepository;
    }

    @Override
    public Tag addTag(Long issueId, Tag tagRequest) {
        Issue issue = issueRepository.findById(issueId).orElseThrow(() -> new IssueNotFoundException(issueId));
        Tag tag = null;
        if (tagRequest.getId() == null) tagRequest.setId(0L);
        Long tagId = tagRequest.getId();
        if (tagId != 0L) {
            tag = tagRepository.findById(tagId).orElseThrow(() -> new TagNotFoundException(tagId));
        }
        else {
            tag = tagRepository.findByName(tagRequest.getName());
            if (tag == null) {
                tag = tagRepository.save(tagRequest);
            }
        }
        issue.addTag(tag);
        issueRepository.save(issue);
        return tag;
    }

    @Override
    public Tag findById(Long tagId) {
        return tagRepository.findById(tagId).orElseThrow(() -> new TagNotFoundException(tagId));
    }

    @Override
    public List<Tag> findByName(String name) {
        if (name != null) {
        return tagRepository.findByNameContaining(name);
        }
        return tagRepository.findAll();
    }
}
