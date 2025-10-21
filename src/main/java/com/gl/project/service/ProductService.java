package com.gl.project.service;

import com.gl.project.entities.Product;
import com.gl.project.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    private ProductRepository ProductRepository;

    public List<Product> findAll() {
        return ProductRepository.findAll();
    }

    public Product findById(Long id) {
        Optional<Product> Product = ProductRepository.findById(id);
        return Product.get();
    }

    public Product save(Product Product) {
        return ProductRepository.save(Product);
    }

    public Product create(Product Product) {

        return ProductRepository.save(Product);
    }

    public Product update(Long id, Product newProduct) {
        Product Product = ProductRepository.findById(id).get();

        Product.setName(newProduct.getName());
        return ProductRepository.save(Product);
    }


}
