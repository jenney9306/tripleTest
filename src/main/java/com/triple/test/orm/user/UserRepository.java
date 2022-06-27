package com.triple.test.orm.user;

import com.triple.test.dao.TrUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UserRepository extends JpaRepository<TrUser, String>, JpaSpecificationExecutor<String> {
}
