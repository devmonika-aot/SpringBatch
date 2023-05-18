package com.springbatch.SpringBatchtutorial.repository;

import com.springbatch.SpringBatchtutorial.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {}
