package com.triple.test.dao;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "TR_USER")
public class TrUser extends CommonDao{
    @Column(name = "SEQ")
    private Integer seq;

    @Column(name = "USER_ID", length = 36, nullable = false)
    private String userId;

    @Column(name = "SCORE_SUM", nullable = false)
    private Integer scoreSum;


}
