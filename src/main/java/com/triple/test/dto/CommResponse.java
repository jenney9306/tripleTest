package com.triple.test.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class CommResponse<T> implements Serializable {
    private String returnCode;
    private String message;
    private T data;

}
