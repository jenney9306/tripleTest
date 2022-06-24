package com.triple.test.dao;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import javax.persistence.Column;
import java.util.Date;

@Getter
@Setter
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
