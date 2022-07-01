package com.triple.test.controller;

import com.triple.test.dto.ReviewRequest;
import com.triple.test.service.ReviewService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.springframework.beans.factory.annotation.Autowired;

//@DataJpaTest
@MockitoSettings
@ExtendWith(MockitoExtension.class)
public class ReviewControllerTest {
    private ReviewRequest rv1;
    private ReviewRequest rv2;

    @Autowired
    ReviewService service;

    @BeforeEach
    void setup(){
        //user 1
        rv1 = new ReviewRequest();
        rv1.setAction("ADD");
        rv1.setType("REVIEW");
        rv1.setContent("good");
        rv1.setAttachedPhotoIds(new String[]{"e4d1a64e-a531-46de-88d0-ff0ed70c0bb8", "afb0cef2-851d-4a50-bb07-9cc15cbdc332"});
        rv1.setReviewId("111a0658-dc5f-4878-9381-ebb7b2667772");
        rv1.setUserId("111e0ef2-92b7-4817-a5f3-0c575361f745");
        rv1.setPlaceId("111baf1c-5acb-4efb-a1af-eddada31b00f");


        //user 2
        rv2 = new ReviewRequest();
        rv2.setAction("ADD");
        rv2.setType("REVIEW");
        rv2.setContent("good22");
        rv2.setAttachedPhotoIds(new String[]{"e4d1a64e-a531-46de-88d0-ff0ed70c0bb8", "afb0cef2-851d-4a50-bb07-9cc15cbdc332"});
        rv2.setReviewId("222a0658-dc5f-4878-9381-ebb7b2667772");
        rv2.setUserId("222e0ef2-92b7-4817-a5f3-0c575361f745");
        rv2.setPlaceId("222baf1c-5acb-4efb-a1af-eddada31b00f");
    }

    @Test
    public void event(){
        //given
//        service.events(rv1);

        //when
//        CommResponse<UserScore> userDto = service.userScore(rv1.getUserId());

        //then
//        assertThat(userDto.getData().getScoreSum()).isEqualTo(3);
    }
}
