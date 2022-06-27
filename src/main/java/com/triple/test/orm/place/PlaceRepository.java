package com.triple.test.orm.place;

import com.triple.test.dao.TrPlace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PlaceRepository extends JpaRepository<TrPlace, String>, JpaSpecificationExecutor<String> {
}

