package com.gl.project.category;

import com.gl.project.entities.Category;
import com.gl.project.repository.CategoryRepository;
import com.gl.project.resources.CategoryResources;
import com.gl.project.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoryResourcesTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private CategoryResources categoryResources;

    private Category category1;
    private Category category2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        category1 = new Category();
        category1.setId(1L);
        category1.setName("Action");

        category2 = new Category();
        category2.setId(2L);
        category2.setName("Comedy");
    }

    @Test
    void testFindAll() {
        List<Category> categories = Arrays.asList(category1, category2);
        when(categoryRepository.findAll()).thenReturn(categories);

        ResponseEntity<List<Category>> response = categoryResources.findAll();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());
        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    void testFindById() {
        when(categoryService.findById(1L)).thenReturn(category1);

        ResponseEntity<Category> response = categoryResources.findById(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Action", response.getBody().getName());
        verify(categoryService, times(1)).findById(1L);
    }

    @Test
    void testSave() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/categories");
        request.setServerName("localhost");
        request.setServerPort(8080);
        request.setScheme("http");

        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        when(categoryService.create(any(Category.class))).thenReturn(category1);

        ResponseEntity<Category> response = categoryResources.save(category1);

        assertEquals(201, response.getStatusCodeValue());
        assertEquals(category1, response.getBody());
        assertNotNull(response.getHeaders().getLocation());
        assertTrue(response.getHeaders().getLocation().toString().contains("/categories/1"));

        verify(categoryService, times(1)).create(any(Category.class));

        RequestContextHolder.resetRequestAttributes();
    }

    @Test
    void testUpdate() {
        when(categoryService.update(eq(1L), any(Category.class))).thenReturn(category1);

        ResponseEntity<Category> response = categoryResources.update(1L, category1);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Action", response.getBody().getName());
        verify(categoryService, times(1)).update(eq(1L), any(Category.class));
    }
}