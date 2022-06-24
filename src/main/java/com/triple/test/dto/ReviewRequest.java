package com.triple.test.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ReviewRequest {
    @ApiModelProperty(notes   = "타입",            example = "1")
    private String type;

    @ApiModelProperty(notes   = "액션",            example = "ADD/MOD/DELETE")
    private String action;

    @ApiModelProperty(notes   = "리뷰 번호",            example = "240a0658-dc5f-4878-9381-ebb7b2667772")
    private String reviewId;

    @ApiModelProperty(notes   = "리뷰 내용",            example = "좋아요")
    private String content;

    @ApiModelProperty(notes   = "첨부파일 이미지",       example = "")
    private String[] attachedPhotoIds;

    @ApiModelProperty(notes   = "작성자 아이디",            example = "")
    private String userId;

    @ApiModelProperty(notes   = "장소 아이디",            example = "")
    private String placeId;

}
