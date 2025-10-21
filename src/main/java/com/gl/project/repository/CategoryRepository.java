package com.gl.project.repository;

import com.gl.project.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;


public interface CategoryRepository  extends JpaRepository<Category, Long> {
}
