package com.triple.test.orm.attach;

import com.triple.test.dao.TrAttach;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface AttachRepository extends JpaRepository<TrAttach, Integer>, JpaSpecificationExecutor<Integer> {
    List<TrAttach> findByReviewIdAndUserIdAndUseYn(String reviewId, String userId, boolean useYn);
}

