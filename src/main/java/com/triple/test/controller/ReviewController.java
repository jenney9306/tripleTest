package com.triple.test.controller;

import com.triple.test.dto.ReviewRequest;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@Api(tags = {"review event controller"})
@RequiredArgsConstructor
public class ReviewController {

    @PostMapping("/event")
    public String event(@RequestBody ReviewRequest reviewRequest){
        return "success";
    }
}
