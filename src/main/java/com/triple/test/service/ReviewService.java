package com.triple.test.service;

import com.triple.test.common.ActionEnum;
import com.triple.test.common.MessageException;
import com.triple.test.dao.*;
import com.triple.test.dto.CommResponse;
import com.triple.test.dto.ReviewRequest;
import com.triple.test.dto.ReviewResponse;
import com.triple.test.dto.UserScore;
import com.triple.test.orm.attach.AttachRepository;
import com.triple.test.orm.place.PlaceRepository;
import com.triple.test.orm.review.ReviewRepository;
import com.triple.test.orm.score.ScoreRepository;
import com.triple.test.orm.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ScoreRepository scoreRepository;
    private final UserRepository userRepository;
    private final PlaceRepository placeRepository;
    private final AttachRepository attachRepository;

    private final static Integer CONTENT_SCORE = 1;
    private final static Integer ATACH_SCORE = 1;
    private final static Integer FRST_SCORE = 1;


    public CommResponse<ReviewResponse> events(ReviewRequest reviewRequest) {
        CommResponse<ReviewResponse> result = new CommResponse<>();
        if(!reviewRequest.getType().equals("REVIEW")){
            result.setReturnCode(MessageException.VALIDATION_FAIL_CODE);
            result.setMessage("type is not review");
            return result;
        }
        try {
            if (reviewRequest.getAction().equals(ActionEnum.ADD.name())) {
                return addReview(reviewRequest, result);
            } else if(reviewRequest.getAction().equals(ActionEnum.MOD.name())){
                return modReview(reviewRequest, result);
            } else if (reviewRequest.getAction().equals(ActionEnum.DELETE.name())) {
                return deleteReview(reviewRequest, result);
            }
        }
        catch(Exception e){
            result.setReturnCode(MessageException.ERROR_CODE);
            result.setMessage(MessageException.ERROR_MESSAGE);
        }

        return result;
    }


    /**
     *  ????????????
     *   1. ?????? ??????
     *   2. ??? ????????? ?????? ?????? ??????
     *   3. ????????? ?????? ??????
     *   4. ????????? ?????? ???????????? ?????? ??????
     *   5. ????????? ?????? ???????????? ??????
     * @param rvReq
     * @param result
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    private CommResponse<ReviewResponse> addReview(ReviewRequest rvReq, CommResponse<ReviewResponse> result) {

        Optional<TrReview> optional = reviewRepository.findByUserIdAndPlaceIdAndUseYn(rvReq.getUserId(), rvReq.getPlaceId(), Boolean.TRUE);
        if(optional.isPresent()){
            result.setMessage("?????? ????????? ?????? ????????? ?????????????????????.");
            result.setReturnCode(MessageException.VALIDATION_FAIL_CODE);
            return result;
        }

        boolean frstPlace = false;
        int newScore = 0;

        synchronized(this) {
            if (!placeRepository.findById(rvReq.getPlaceId()).isPresent()) {
                // ??? ?????? ??? ????????? ??????
                TrPlace trPlace = new TrPlace();
                trPlace.setPlaceId(rvReq.getPlaceId());
                placeRepository.save(trPlace);

                frstPlace = true;

                // ??? ?????? ???????????? ??? ??????
                newScore += FRST_SCORE;
                scoreRepository.save(mapperToTrScoreHis(rvReq, FRST_SCORE, "BONUS"));
            }
        }
        newScore += scoreAdd(rvReq);

        // ?????? ??????
        TrReview trReview = new TrReview();
        trReview.setReviewId(rvReq.getReviewId());
        trReview.setContents(rvReq.getContent());
        trReview.setUseYn(Boolean.TRUE);
        trReview.setUserId(rvReq.getUserId());
        trReview.setPlaceId(rvReq.getPlaceId());
        trReview.setFrstPlace(frstPlace);
        trReview.setCreateDate(new Date());
        trReview.setCreateUser(rvReq.getUserId());
        trReview.setModDate(new Date());
        trReview.setModUser(rvReq.getUserId());
        reviewRepository.save(trReview);

        // ????????????
        if(rvReq.getAttachedPhotoIds().length > 0){
            List<TrAttach> attaches = Arrays.stream(rvReq.getAttachedPhotoIds())
                    .map( attach ->{
                        TrAttach a = new TrAttach();
                        a.setUseYn(Boolean.TRUE);
                        a.setReviewId(rvReq.getReviewId());
                        a.setAttachedId(attach);
                        a.setUserId(rvReq.getUserId());
                        return a;
                    })
                    .collect(Collectors.toList());
            attachRepository.saveAll(attaches);
        }

        // ?????? ??????
        Optional<TrUser> user = userRepository.findById(rvReq.getUserId());
        TrUser trUser = new TrUser();
        if(user.isPresent()){
            trUser = user.get();
            trUser.setScoreSum(trUser.getScoreSum() + newScore);
        } else {
            trUser.setUserId(rvReq.getUserId());
            trUser.setScoreSum(newScore);
        }
        userRepository.save(trUser);

        result.setMessage("?????? ?????? ??????");
        ReviewResponse res = new ReviewResponse();
        res.setReviewId(rvReq.getReviewId());
        res.setUserId(rvReq.getUserId());
        res.setPlaceId(rvReq.getPlaceId());
        result.setData(res);
        result.setReturnCode(MessageException.SUCCESS_CODE);
        return result;
    }

    /**
     *  ????????????
     *   1. ?????? ????????? ??????
     *   2. ????????? ?????? ????????? ???????????? ???????????? ????????? ??????
     *   3. ?????? ????????? ?????? ????????? ???????????? ??????
     *   4. ?????? ?????? ?????? ??? ???????????? ??????
     * @param rvReq
     * @param result
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    private CommResponse<ReviewResponse> modReview(ReviewRequest rvReq, CommResponse<ReviewResponse> result) {

        Optional<TrReview> optional = reviewRepository.findByUserIdAndPlaceIdAndUseYn(rvReq.getUserId(), rvReq.getPlaceId(), Boolean.TRUE);
        if(!optional.isPresent()){
            result.setMessage("????????? ????????? ????????????.");
            result.setReturnCode(MessageException.VALIDATION_FAIL_CODE);
            return result;
        }
        List<TrAttach> attachList = attachRepository.findByReviewIdAndUserIdAndUseYn(rvReq.getReviewId(), rvReq.getUserId(), Boolean.TRUE);

        TrReview trReview = optional.get();
        int oldScore = 0;
        if(trReview.getContents().length() > 0) oldScore++;
        if(attachList.size() > 0) oldScore++;
        scoreRepository.save(mapperToTrScoreHis(rvReq, oldScore, "DELETE"));

        int newScore = scoreAdd(rvReq);

        Optional<TrUser> maybe = userRepository.findById(rvReq.getUserId());
        TrUser user = maybe.get();
        int score = user.getScoreSum();
        score = score - oldScore + newScore;
        user.setScoreSum(score);
        userRepository.save(user);


        // ?????? ?????? ??????
        trReview.setContents(rvReq.getContent());
        trReview.setModDate(new Date());
        reviewRepository.save(trReview);

        // ?????? ???????????? ???????????? ?????? ???????????? ??????
        if(!CollectionUtils.isEmpty(attachList)){
            deleteAttachList(attachList);
        }

        // ?????? ???????????? ??????
        if(rvReq.getAttachedPhotoIds().length > 0){
            List<TrAttach> attaches = Arrays.stream(rvReq.getAttachedPhotoIds())
                    .map( attach ->{
                        TrAttach a = new TrAttach();
                        a.setUseYn(Boolean.TRUE);
                        a.setReviewId(rvReq.getReviewId());
                        a.setAttachedId(attach);
                        a.setUserId(rvReq.getUserId());
                        return a;
                    })
                    .collect(Collectors.toList());
            attachRepository.saveAll(attaches);
        }

        result.setMessage("?????? ?????? ??????");
        result.setReturnCode(MessageException.SUCCESS_CODE);
        ReviewResponse res = new ReviewResponse();
        res.setReviewId(rvReq.getReviewId());
        res.setUserId(rvReq.getUserId());
        res.setPlaceId(rvReq.getPlaceId());
        result.setData(res);
        return result;

    }

    /**
     * ????????????
     *  1. ?????? ???????????? ??????
     *  2. ????????? ?????? ??????
     *  3. ????????? ?????? ???????????? ??????
     *  4. ?????? ??? ????????? ?????? ?????? ?????? ????????? ??????
     *  5. ???????????? ?????? ?????? ???????????? ??????
     * @param rvReq
     * @param result
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    private CommResponse<ReviewResponse> deleteReview(ReviewRequest rvReq, CommResponse<ReviewResponse> result) {
        Optional<TrReview> optional = reviewRepository.findByUserIdAndPlaceIdAndUseYn(rvReq.getUserId(), rvReq.getPlaceId(), Boolean.TRUE);
        if(!optional.isPresent()){
            result.setMessage("????????? ????????? ????????????.");
            result.setReturnCode(MessageException.VALIDATION_FAIL_CODE);
            return result;
        }

        // ?????? ?????? ?????? false ??? ??????
        TrReview trReview = optional.get();
        trReview.setUseYn(Boolean.FALSE);
        reviewRepository.save(trReview);

        synchronized(this) {
            if (trReview.getFrstPlace()) {
                Optional<TrPlace> p = placeRepository.findById(rvReq.getPlaceId());
                if (p.isPresent()) {
                    placeRepository.delete(p.get());
                }
            }
        }

        List<TrAttach> attachList = attachRepository.findByReviewIdAndUserIdAndUseYn(rvReq.getReviewId(), rvReq.getUserId(), Boolean.TRUE);

        // ????????? ??????
        int oldScore = 0;
        if(trReview.getContents().length() > 0) oldScore++;
        if(attachList.size() > 0) oldScore++;
        if(trReview.getFrstPlace()) oldScore++;

        if(oldScore > 0) {
            // ?????? ???????????? ??????
            scoreRepository.save(mapperToTrScoreHis(rvReq, oldScore, "DELETE"));
            Optional<TrUser> user = userRepository.findById(rvReq.getUserId());
            if(user.isPresent()){
                TrUser newUser = user.get();
                int sumScore = user.get().getScoreSum();

                newUser.setScoreSum(sumScore-oldScore < 0 ? 0 : sumScore-oldScore);
                userRepository.save(newUser);
            }
        }

        // ???????????? ?????? ???????????? ??????
        if(!CollectionUtils.isEmpty(attachList)){
            deleteAttachList(attachList);
        }

        result.setMessage("?????? ?????? ??????");
        ReviewResponse res = new ReviewResponse();
        res.setUserId(rvReq.getUserId());
        result.setData(res);
        result.setReturnCode(MessageException.SUCCESS_CODE);
        return result;
    }


    public CommResponse<UserScore> userScore(String userId){
        CommResponse<UserScore> userScore = new CommResponse<>();
        Optional<TrUser> maybe = userRepository.findById(userId);
        if(!maybe.isPresent()){
            userScore.setMessage("???????????? ?????? ????????? ?????????.");
            userScore.setReturnCode(MessageException.VALIDATION_FAIL_CODE);
        } else{
            UserScore us = new UserScore();
            us.setUserId(maybe.get().getUserId());
            us.setScoreSum(maybe.get().getScoreSum());
            userScore.setData(us);

            userScore.setMessage(MessageException.SUCCESS_MESSAGE);
            userScore.setReturnCode(MessageException.SUCCESS_CODE);
        }
        return userScore;
    }

    private int scoreAdd(ReviewRequest rvReq){
        int newScore = 0;
        if(rvReq.getContent().length() > 0){
            newScore += CONTENT_SCORE;
        }
        if(rvReq.getAttachedPhotoIds().length > 0){
            newScore += ATACH_SCORE;
        }
        scoreRepository.save(mapperToTrScoreHis(rvReq, newScore, "CONTENT"));
        return newScore;
    }

    private void deleteAttachList(List<TrAttach> attachList){
        attachList.stream()
                .map( attach -> {
                    attach.setUseYn(Boolean.FALSE);
                    return attach;
                })
                .collect(Collectors.toList());
        attachRepository.saveAll(attachList);
    }

    private TrScoreHis mapperToTrScoreHis(ReviewRequest rvReq, Integer typeScore, String type){
        TrScoreHis trScoreHis = new TrScoreHis();
        trScoreHis.setScore(typeScore);
        trScoreHis.setReviewId(rvReq.getReviewId());
        trScoreHis.setAction(rvReq.getAction());
        trScoreHis.setUserId(rvReq.getUserId());
        trScoreHis.setPlaceId(rvReq.getPlaceId());

        trScoreHis.setCreateDate(new Date());
        trScoreHis.setCreateUser(rvReq.getUserId());
        trScoreHis.setModDate(new Date());
        trScoreHis.setModUser(rvReq.getUserId());
        trScoreHis.setReason(type);
        return trScoreHis;
    }
}
