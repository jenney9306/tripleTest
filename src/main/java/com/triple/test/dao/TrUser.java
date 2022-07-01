package com.triple.test.dao;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "TR_USER")
public class TrUser {
    @Id
    @Column(name = "USER_ID", length = 36, nullable = false)
    private String userId;

    @Column(name = "SCORE_SUM", nullable = false)
    private Integer scoreSum;


}
