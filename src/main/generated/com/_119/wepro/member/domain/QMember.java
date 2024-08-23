package com._119.wepro.member.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMember is a Querydsl query type for Member
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMember extends EntityPathBase<Member> {

    private static final long serialVersionUID = -748133146L;

    public static final QMember member = new QMember("member1");

    public final com._119.wepro.global.QBaseEntity _super = new com._119.wepro.global.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final DateTimePath<java.time.LocalDateTime> inactiveDate = createDateTime("inactiveDate", java.time.LocalDateTime.class);

    public final StringPath name = createString("name");

    public final StringPath position = createString("position");

    public final StringPath profile = createString("profile");

    public final SetPath<com._119.wepro.project.domain.ProjectMember, com._119.wepro.project.domain.QProjectMember> projectMembers = this.<com._119.wepro.project.domain.ProjectMember, com._119.wepro.project.domain.QProjectMember>createSet("projectMembers", com._119.wepro.project.domain.ProjectMember.class, com._119.wepro.project.domain.QProjectMember.class, PathInits.DIRECT2);

    public final StringPath role = createString("role");

    public final StringPath socialType = createString("socialType");

    public final StringPath state = createString("state");

    public final StringPath tag = createString("tag");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QMember(String variable) {
        super(Member.class, forVariable(variable));
    }

    public QMember(Path<? extends Member> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMember(PathMetadata metadata) {
        super(Member.class, metadata);
    }

}

