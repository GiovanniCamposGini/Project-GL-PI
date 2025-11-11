package com.gl.project.product;

import com.gl.project.entities.Product;
import com.gl.project.repository.ProductRepository;
import com.gl.project.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;
    private Product product;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        product = new Product("Notebook", "Computador portátil", 4500.0, "img/notebook.png");
        product.setId(1L);
    }

    @Test
    void deveRetornarTodosOsProdutos() {

        when(productRepository.findAll()).thenReturn(List.of(product));


        List<Product> result = productService.findAll();


        assertEquals(1, result.size());
        assertEquals("Notebook", result.get(0).getName());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void deveRetornarProdutoPorId() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        Product result = productService.findById(1L);

        assertEquals("Notebook", result.getName());
        assertEquals(4500.0, result.getPrice());
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    void deveSalvarProduto() {
        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product result = productService.save(product);

        assertNotNull(result);
        assertEquals("Notebook", result.getName());
        verify(productRepository, times(1)).save(product);
    }

    @Test
    void deveCriarProduto() {
        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product result = productService.create(product);

        assertEquals("Notebook", result.getName());
        verify(productRepository, times(1)).save(product);
    }

    @Test
    void deveAtualizarProdutoExistente() {
        Product newProduct = new Product("Smartphone", "Celular Android", 2500.0, "img/smartphone.png");
        newProduct.setId(1L);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product updated = productService.update(1L, newProduct);

        assertEquals("Smartphone", updated.getName());
        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).save(product);
    }
}
