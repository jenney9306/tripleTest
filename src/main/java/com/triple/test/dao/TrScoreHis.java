package com.triple.test.dao;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "TR_SCORE_HIS")
public class TrScoreHis extends CommonDao{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SEQ", nullable = false)
    private Integer seq;

    @Column(name = "REVIEW_ID", length = 36, nullable = false)
    private String reviewId;

    @Column(name = "PLACE_ID", length = 36, nullable = false)
    private String placeId;

    @Column(name = "SCORE", nullable = false)
    private Integer score;

    @Column(name = "USER_ID", length = 36, nullable = false)
    private String userId;

    @Column(name = "ACTION", length = 10, nullable = false)
    private String action;

    @Column(name = "reason", length = 20, nullable = true)
    private String reason;
}
