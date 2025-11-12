package com.gl.project.category;

import com.gl.project.entities.Category;
import com.gl.project.repository.CategoryRepository;
import com.gl.project.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    private Category category1;
    private Category category2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        category1 = new Category(1L, "Eletrônicos");
        category2 = new Category(2L, "Roupas");
    }

    @Test
    void testFindAll() {
        when(categoryRepository.findAll()).thenReturn(Arrays.asList(category1, category2));

        List<Category> result = categoryService.findAll();

        assertEquals(2, result.size());
        assertEquals("Eletrônicos", result.get(0).getName());
        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    void testFindById() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category1));

        Category result = categoryService.findById(1L);

        assertNotNull(result);
        assertEquals("Eletrônicos", result.getName());
        verify(categoryRepository, times(1)).findById(1L);
    }

    @Test
    void testSave() {
        when(categoryRepository.save(any(Category.class))).thenReturn(category1);

        Category result = categoryService.save(category1);

        assertEquals("Eletrônicos", result.getName());
        verify(categoryRepository, times(1)).save(category1);
    }

    @Test
    void testCreate() {
        when(categoryRepository.save(any(Category.class))).thenReturn(category2);

        Category result = categoryService.create(category2);

        assertEquals("Roupas", result.getName());
        verify(categoryRepository, times(1)).save(category2);
    }

    @Test
    void testUpdate() {
        Category newCategory = new Category(1L, "Games");

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category1));
        when(categoryRepository.save(any(Category.class))).thenReturn(newCategory);

        Category result = categoryService.update(1L, newCategory);

        assertEquals("Games", result.getName());
        verify(categoryRepository, times(1)).findById(1L);
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    void testFindByIdNotFoundThrowsException() {
        when(categoryRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(java.util.NoSuchElementException.class, () -> categoryService.findById(99L));

        verify(categoryRepository, times(1)).findById(99L);
    }
}
