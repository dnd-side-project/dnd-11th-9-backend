package com._119.wepro.project.domain;

import com._119.wepro.global.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;

@Entity
@Getter
public class Project extends BaseEntity {
    @Id
    private Long id;
}
