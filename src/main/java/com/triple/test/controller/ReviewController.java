package com.triple.test.controller;

import com.triple.test.dto.CommResponse;
import com.triple.test.dto.ReviewRequest;
import com.triple.test.dto.ReviewResponse;
import com.triple.test.dto.UserScore;
import com.triple.test.service.ReviewService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@Api(tags = {"리뷰 이벤트 controller"})
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/events")
    public ResponseEntity<CommResponse> events(@RequestBody ReviewRequest reviewRequest) throws Exception{
        CommResponse<ReviewResponse> response = reviewService.events(reviewRequest);
        return new ResponseEntity(response, HttpStatus.OK);
    }

    @GetMapping("/score/{userId}")
    public ResponseEntity<CommResponse> userScore(@PathVariable() String userId){
        CommResponse<UserScore> response = reviewService.userScore(userId);
        return new ResponseEntity(response, HttpStatus.OK);
    }
}
