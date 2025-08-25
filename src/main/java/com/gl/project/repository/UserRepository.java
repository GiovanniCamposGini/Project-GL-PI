package com.gl.project.repository;

import com.gl.project.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository  extends JpaRepository<User, Long> {
}
