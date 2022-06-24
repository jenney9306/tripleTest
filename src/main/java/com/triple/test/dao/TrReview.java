package com.triple.test.dao;


import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "TR_REVIEW")
public class TrReview extends CommonDao{
    @Id
    @Column(name = "REVIEW_ID", length = 36, nullable = false)
    private String reviewId;

    @Column(name = "CONTENTS",  length = 300, nullable = false)
    private String contents;

    @Column(name = "SCORE", nullable = false)
    private Integer score;

    @Column(name = "USER_ID", length = 36, nullable = false)
    private String userId;

    @Type(type = "yes_no")
    @Column(name = "USE_YN", length = 1, nullable = false)
    private Boolean useYn;

    @Column(name = "ATTACHED", nullable = false)
    private String[] attached;
}
