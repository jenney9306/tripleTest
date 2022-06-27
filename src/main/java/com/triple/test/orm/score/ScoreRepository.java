package com.triple.test.orm.score;

import com.triple.test.dao.TrScoreHis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ScoreRepository extends JpaRepository<TrScoreHis, Integer>, JpaSpecificationExecutor<Integer> {
    List<TrScoreHis> findByUserIdAndPlaceId(String userId, String placeId);
    List<TrScoreHis> findByUserIdAndPlaceIdAndAction(String userId, String placeId, String action);
}
