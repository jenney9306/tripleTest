package com.triple.test.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserScore {
    private String userId;
    private Integer scoreSum;
}
