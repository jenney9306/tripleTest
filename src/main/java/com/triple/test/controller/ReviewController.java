package com.triple.test.controller;

import com.triple.test.dto.ReviewRequest;
import com.triple.test.service.ReviewService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@Api(tags = {"review event controller"})
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/events")
    public ResponseEntity<String> events(@RequestBody ReviewRequest reviewRequest) throws Exception{
        return ResponseEntity.ok(reviewService.events(reviewRequest));
    }
}
