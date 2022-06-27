package com.triple.test.orm.attach;

import com.triple.test.dao.TrAttach;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AttachRepository extends JpaRepository<TrAttach, Integer>, JpaSpecificationExecutor<Integer> {
}

