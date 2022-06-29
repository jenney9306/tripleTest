package com.triple.test.dao;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "TR_ATTACH")
public class TrAttach {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SEQ", nullable = false)
    private Integer seq;

    @Column(name = "REVIEW_ID", length = 36, nullable = false)
    private String reviewId;

    @Column(name = "USER_ID", length = 36, nullable = false)
    private String userId;

    @Column(name = "ATTACHED_ID",  length = 36, nullable = false)
    private String attachedId;

    @Type(type = "yes_no")
    @Column(name = "USE_YN", length = 1, nullable = false)
    private Boolean useYn;


}
