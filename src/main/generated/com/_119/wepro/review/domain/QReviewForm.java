package com._119.wepro.review.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QReviewForm is a Querydsl query type for ReviewForm
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReviewForm extends EntityPathBase<ReviewForm> {

    private static final long serialVersionUID = 1334499782L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QReviewForm reviewForm = new QReviewForm("reviewForm");

    public final com._119.wepro.global.QBaseEntity _super = new com._119.wepro.global.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com._119.wepro.member.domain.QMember member;

    public final com._119.wepro.project.domain.QProject project;

    public final ListPath<ReviewFormQuestion, QReviewFormQuestion> reviewFormQuestions = this.<ReviewFormQuestion, QReviewFormQuestion>createList("reviewFormQuestions", ReviewFormQuestion.class, QReviewFormQuestion.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QReviewForm(String variable) {
        this(ReviewForm.class, forVariable(variable), INITS);
    }

    public QReviewForm(Path<? extends ReviewForm> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QReviewForm(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QReviewForm(PathMetadata metadata, PathInits inits) {
        this(ReviewForm.class, metadata, inits);
    }

    public QReviewForm(Class<? extends ReviewForm> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new com._119.wepro.member.domain.QMember(forProperty("member")) : null;
        this.project = inits.isInitialized("project") ? new com._119.wepro.project.domain.QProject(forProperty("project")) : null;
    }

}

