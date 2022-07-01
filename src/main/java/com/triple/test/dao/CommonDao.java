package com.triple.test.dao;

import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.util.Date;

@Setter
@MappedSuperclass
public class CommonDao {
    @Column(name = "CATE_DATE",  nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '등록 날짜'")
    private Date createDate;

    @Column(name = "CATE_USER",  nullable = false, length = 36)
    private String createUser;

    @Column(name = "MOD_DATE",  nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '수정 날짜'")
    private Date modDate;

    @Column(name = "MOD_USER",  nullable = false, length = 36)
    private String modUser;

}
