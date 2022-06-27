package com.triple.test.service;

import com.triple.test.common.ActionEnum;
import com.triple.test.config.DefaultExtendException;
import com.triple.test.dao.*;
import com.triple.test.dto.ReviewRequest;
import com.triple.test.orm.attach.AttachRepository;
import com.triple.test.orm.place.PlaceRepository;
import com.triple.test.orm.review.ReviewRepository;
import com.triple.test.orm.score.ScoreRepository;
import com.triple.test.orm.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ScoreRepository scoreRepository;
    private final UserRepository userRepository;
    private final PlaceRepository placeRepository;
    private final AttachRepository attachRepository;

    public String events(ReviewRequest reviewRequest) throws Exception{
        if(!reviewRequest.getType().equals("REVIEW")){
            return "not review";
        }

        if(reviewRequest.getAction().equals(ActionEnum.ADD.name())){
            return addReview(reviewRequest);

        //else if(reviewRequest.getAction().equals(ActionEnum.MOD)){
//            modReview();
        } else if(reviewRequest.getAction().equals(ActionEnum.DELETE.name())){
            return deleteReview(reviewRequest);
        }


        return "success";
    }

    @Transactional(rollbackFor = Exception.class)
    private String addReview(ReviewRequest rvReq) throws Exception{
        // 요구사항
        // 리뷰 등록
        // 첫장소일 경우 장소 등록
        // 사용자 점수 등록
        // 포인트만큼 히스토리 모두 저장
        // 현재 사용자가 해당 리뷰에 작성한건이 있을 경우
        Optional<TrReview> optional = reviewRepository.findByUserIdAndPlaceIdAndUseYn(rvReq.getUserId(), rvReq.getPlaceId(), Boolean.TRUE);
        if(optional.isPresent()){
            throw new Exception("해당 장소에 이미 리뷰를 작성하셨습니다.");
        }

        boolean frstPlace = false;
        List<TrScoreHis> scoreHisList = new ArrayList<>();

        // 점수 계산
        if( rvReq.getContent().length() > 0 ){
            TrScoreHis trScoreHis = new TrScoreHis();
            trScoreHis.setScore(1);
            trScoreHis.setReviewId(rvReq.getReviewId());
            trScoreHis.setAction(rvReq.getAction());
            trScoreHis.setUserId(rvReq.getUserId());
            trScoreHis.setPlaceId(rvReq.getPlaceId());

            trScoreHis.setCreateDate(new Date());
            trScoreHis.setCreateUser(rvReq.getUserId());
            trScoreHis.setModDate(new Date());
            trScoreHis.setModUser(rvReq.getUserId());
            trScoreHis.setReason("content");
            scoreHisList.add(trScoreHis);
        }
        if( rvReq.getAttachedPhotoIds().length > 0 ){
            TrScoreHis trScoreHis = new TrScoreHis();
            trScoreHis.setScore(1);
            trScoreHis.setReviewId(rvReq.getReviewId());
            trScoreHis.setAction(rvReq.getAction());
            trScoreHis.setUserId(rvReq.getUserId());
            trScoreHis.setPlaceId(rvReq.getPlaceId());

            trScoreHis.setCreateDate(new Date());
            trScoreHis.setCreateUser(rvReq.getUserId());
            trScoreHis.setModDate(new Date());
            trScoreHis.setModUser(rvReq.getUserId());
            trScoreHis.setReason("attach");
            scoreHisList.add(trScoreHis);
        }
        if(!placeRepository.findById(rvReq.getPlaceId()).isPresent()){
            TrPlace trPlace = new TrPlace();
            trPlace.setPlaceId(rvReq.getPlaceId());
            placeRepository.save(trPlace);

            TrScoreHis trScoreHis = new TrScoreHis();
            trScoreHis.setScore(1);
            trScoreHis.setReviewId(rvReq.getReviewId());
            trScoreHis.setAction(rvReq.getAction());
            trScoreHis.setUserId(rvReq.getUserId());
            trScoreHis.setPlaceId(rvReq.getPlaceId());

            trScoreHis.setCreateDate(new Date());
            trScoreHis.setCreateUser(rvReq.getUserId());
            trScoreHis.setModDate(new Date());
            trScoreHis.setModUser(rvReq.getUserId());
            trScoreHis.setReason("bonus");
            scoreHisList.add(trScoreHis);

            frstPlace = true;
        }

        scoreRepository.saveAll(scoreHisList);

        // 리뷰 저장
        TrReview trReview = new TrReview();
        trReview.setReviewId(rvReq.getReviewId());
        trReview.setContents(rvReq.getContent());
        trReview.setUseYn(Boolean.TRUE);
//        trReview.setScore(score);
        trReview.setUserId(rvReq.getUserId());
        trReview.setPlaceId(rvReq.getPlaceId());
        trReview.setFrstPlace(frstPlace);
        trReview.setCreateDate(new Date());
        trReview.setCreateUser(rvReq.getUserId());
        trReview.setModDate(new Date());
        trReview.setModUser(rvReq.getUserId());
        reviewRepository.save(trReview);

        // 첨부파일
        if(rvReq.getAttachedPhotoIds().length > 0){
            List<TrAttach> attaches = Arrays.stream(rvReq.getAttachedPhotoIds())
                    .map( attach ->{
                        TrAttach a = new TrAttach();
                        a.setUseYn(Boolean.TRUE);
                        a.setReviewId(rvReq.getReviewId());
                        a.setAttachedId(attach);
                        return a;
                    })
                    .collect(Collectors.toList());
            attachRepository.saveAll(attaches);
        }

        // 유저 저장
        Optional<TrUser> user = userRepository.findById(rvReq.getUserId());
        TrUser trUser = new TrUser();
        if(user.isPresent()){
            trUser = user.get();
            trUser.setScoreSum(trUser.getScoreSum() + scoreHisList.size());
        } else {
            trUser.setUserId(rvReq.getUserId());
            trUser.setScoreSum(scoreHisList.size());
        }
        userRepository.save(trUser);

        return "success";
    }

    @Transactional(rollbackFor = Exception.class)
    private String modReview(ReviewRequest rvReq) throws Exception{
        Optional<TrReview> optional = reviewRepository.findByUserIdAndPlaceIdAndUseYn(rvReq.getUserId(), rvReq.getPlaceId(), Boolean.TRUE);
        if(!optional.isPresent()){
            return "작성한 리뷰가 없습니다.";
        }

        TrReview trReview = optional.get();

        List<TrScoreHis> scoreHis = scoreRepository.findByUserIdAndPlaceId(rvReq.getUserId(), rvReq.getPlaceId());
        if(!CollectionUtils.isEmpty(scoreHis)){

        }

        return "success";

    }

    @Transactional(rollbackFor = Exception.class)
    private String deleteReview(ReviewRequest rvReq) throws Exception{
        // 요구사항
        // 리뷰 제거
        // 사용자 점수 제거
        // 포인트 삭제 히스토리 적재
        // 장소 첫 리뷰자 였을 경우 장소 데이터 삭제
        Optional<TrReview> optional = reviewRepository.findByUserIdAndPlaceIdAndUseYn(rvReq.getUserId(), rvReq.getPlaceId(), Boolean.TRUE);
        if(!optional.isPresent()){
            return  " 작성한 리뷰가 없습니다.";
//            throw new DefaultExtendException(HttpStatus.UNAUTHORIZED,);
        }

        // 리뷰제거
        TrReview trReview = optional.get();
        trReview.setUseYn(Boolean.FALSE);
        reviewRepository.save(trReview);

        if(trReview.getFrstPlace()){
            Optional<TrPlace> p = placeRepository.findById(rvReq.getPlaceId());
            if(p.isPresent()){
                placeRepository.delete(p.get());
            }
        }

        // 포인트 회수
        List<TrScoreHis> history = scoreRepository.findByUserIdAndPlaceIdAndAction(rvReq.getUserId(),rvReq.getPlaceId(),rvReq.getAction());
        if(!CollectionUtils.isEmpty(history)) {
            int score = history.size(); // 회수할 점수

            TrScoreHis deleteScore = new TrScoreHis();
            deleteScore.setScore(score);
            deleteScore.setReviewId(rvReq.getReviewId());
            deleteScore.setAction(rvReq.getAction());
            deleteScore.setUserId(rvReq.getUserId());
            deleteScore.setPlaceId(rvReq.getPlaceId());
            deleteScore.setCreateDate(new Date());
            deleteScore.setCreateUser(rvReq.getUserId());
            deleteScore.setModDate(new Date());
            deleteScore.setModUser(rvReq.getUserId());
            scoreRepository.save(deleteScore);

            Optional<TrUser> user = userRepository.findById(rvReq.getUserId());
            if(user.isPresent()){
                TrUser newUser = user.get();
                int sumScore = user.get().getScoreSum();
                newUser.setScoreSum(sumScore-score < 0 ? 0 : sumScore-score);
                userRepository.save(newUser);
            }
        }

        return  "success";

    }

}
