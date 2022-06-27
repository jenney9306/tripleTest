package com.triple.test.orm.review;

import com.triple.test.dao.TrReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;


public interface ReviewRepository extends JpaRepository<TrReview, String>, JpaSpecificationExecutor<String> {
    Optional<TrReview> findByUserIdAndPlaceIdAndUseYn(String userId, String placeId, Boolean useYn);
}
