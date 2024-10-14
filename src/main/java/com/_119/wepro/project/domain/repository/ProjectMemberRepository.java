package com._119.wepro.project.domain.repository;

import com._119.wepro.project.domain.ProjectMember;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ProjectMemberRepository extends JpaRepository<ProjectMember, Long> {
  @Transactional
  @Modifying
  @Query("DELETE FROM ProjectMember pm WHERE pm.project.id = :projectId")
  void deleteByProjectId(Long projectId);
}