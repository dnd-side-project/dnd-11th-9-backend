package com._119.wepro.review.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QReviewFormQuestion is a Querydsl query type for ReviewFormQuestion
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReviewFormQuestion extends EntityPathBase<ReviewFormQuestion> {

    private static final long serialVersionUID = -1830005812L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QReviewFormQuestion reviewFormQuestion = new QReviewFormQuestion("reviewFormQuestion");

    public final com._119.wepro.global.QBaseEntity _super = new com._119.wepro.global.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QQuestion question;

    public final QReviewForm reviewForm;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QReviewFormQuestion(String variable) {
        this(ReviewFormQuestion.class, forVariable(variable), INITS);
    }

    public QReviewFormQuestion(Path<? extends ReviewFormQuestion> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QReviewFormQuestion(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QReviewFormQuestion(PathMetadata metadata, PathInits inits) {
        this(ReviewFormQuestion.class, metadata, inits);
    }

    public QReviewFormQuestion(Class<? extends ReviewFormQuestion> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.question = inits.isInitialized("question") ? new QQuestion(forProperty("question")) : null;
        this.reviewForm = inits.isInitialized("reviewForm") ? new QReviewForm(forProperty("reviewForm"), inits.get("reviewForm")) : null;
    }

}

