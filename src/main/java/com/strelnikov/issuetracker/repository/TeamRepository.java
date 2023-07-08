package com.strelnikov.issuetracker.repository;

import com.strelnikov.issuetracker.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Long> {

}
