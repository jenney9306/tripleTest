package com.triple.test.dao;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "TR_REVIEW")
@NoArgsConstructor
public class TrReview extends CommonDao{
    @Id
    @Column(name = "REVIEW_ID", length = 36, nullable = false)
    private String reviewId;

    @Column(name = "CONTENTS",  length = 300, nullable = false)
    private String contents;

    @Column(name = "USER_ID", length = 36, nullable = false)
    private String userId;

    @Type(type = "yes_no")
    @Column(name = "USE_YN", length = 1, nullable = false)
    private Boolean useYn;

    @Column(name = "PLACE_ID", length = 36, nullable = false)
    private String placeId;

    @Type(type = "yes_no")
    @Column(name = "FRST_PLACE", length = 1, nullable = false)
    private Boolean frstPlace;

}
