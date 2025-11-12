package com.gl.project.category;

import com.gl.project.entities.Category;
import com.gl.project.entities.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class CategoryTest {

    private Category category;

    @Mock
    private Product productMock;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        category = new Category(1L, "Eletrônicos");
    }

    @Test
    void testConstructorAndGetters() {
        assertEquals(1L, category.getId());
        assertEquals("Eletrônicos", category.getName());
        assertTrue(category.getProducts().isEmpty());
    }

    @Test
    void testSetters() {
        category.setId(2L);
        category.setName("Roupas");

        assertEquals(2L, category.getId());
        assertEquals("Roupas", category.getName());
    }

    @Test
    void testEqualsAndHashCode() {
        Category c1 = new Category(1L, "Eletrônicos");
        Category c2 = new Category(1L, "Eletrônicos");
        Category c3 = new Category(2L, "Móveis");

        assertEquals(c1, c2);
        assertNotEquals(c1, c3);
        assertEquals(c1.hashCode(), c2.hashCode());
        assertNotEquals(c1.hashCode(), c3.hashCode());
    }

    @Test
    void testAddProductMocked() {
        Set<Product> products = new HashSet<>();
        products.add(productMock);

        category.getProducts().add(productMock);

        assertFalse(category.getProducts().isEmpty());
        assertTrue(category.getProducts().contains(productMock));
    }

    @Test
    void testDefaultConstructor() {
        Category emptyCategory = new Category();
        assertNull(emptyCategory.getId());
        assertNull(emptyCategory.getName());
        assertTrue(emptyCategory.getProducts().isEmpty());
    }
}
