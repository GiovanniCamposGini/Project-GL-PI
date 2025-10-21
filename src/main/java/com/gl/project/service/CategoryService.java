package com.gl.project.service;

import com.gl.project.entities.Category;
import com.gl.project.entities.User;
import com.gl.project.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    public Category findById(Long id) {
        Optional<Category> category = categoryRepository.findById(id);
        return category.get();
    }

    public Category save(Category category) {
        return categoryRepository.save(category);
    }

    public Category create(Category category) {

        return categoryRepository.save(category);
    }

    public Category update(Long id, Category newCategory) {
        Category category = categoryRepository.findById(id).get();

        category.setName(newCategory.getName());
        return categoryRepository.save(category);
    }


}
