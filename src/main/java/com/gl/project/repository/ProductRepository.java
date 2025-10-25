package com.gl.project.repository;


import com.gl.project.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ProductRepository  extends JpaRepository<Product, Long> {
}
