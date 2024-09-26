package com._119.wepro.review.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QChoiceOption is a Querydsl query type for ChoiceOption
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QChoiceOption extends EntityPathBase<ChoiceOption> {

    private static final long serialVersionUID = -1124669312L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QChoiceOption choiceOption = new QChoiceOption("choiceOption");

    public final com._119.wepro.global.QBaseEntity _super = new com._119.wepro.global.QBaseEntity(this);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QQuestion question;

    public final NumberPath<Integer> sequence = createNumber("sequence", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QChoiceOption(String variable) {
        this(ChoiceOption.class, forVariable(variable), INITS);
    }

    public QChoiceOption(Path<? extends ChoiceOption> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QChoiceOption(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QChoiceOption(PathMetadata metadata, PathInits inits) {
        this(ChoiceOption.class, metadata, inits);
    }

    public QChoiceOption(Class<? extends ChoiceOption> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.question = inits.isInitialized("question") ? new QQuestion(forProperty("question")) : null;
    }

}

