package com.gl.project.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gl.project.entities.Product;
import com.gl.project.resources.ProductResources;
import com.gl.project.service.ProductService;
import com.gl.project.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ProductResourcesTest {

    private MockMvc mockMvc;

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductResources productResources;

    private Product product;
    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(productResources).build();
        product = new Product("Notebook", "Computador portátil", 4500.0, "img/notebook.png");
        product.setId(1L);
    }

    @Test
    void deveRetornarListaDeProdutos() throws Exception {
        when(productService.findAll()).thenReturn(List.of(product));

        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Notebook"))
                .andExpect(jsonPath("$[0].price").value(4500.0));

        verify(productService, times(1)).findAll();
    }

    @Test
    void deveRetornarProdutoPorId() throws Exception {
        when(productService.findById(1L)).thenReturn(product);

        mockMvc.perform(get("/products/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Notebook"))
                .andExpect(jsonPath("$.price").value(4500.0));

        verify(productService, times(1)).findById(1L);
    }

    @Test
    void deveSalvarNovoProduto() throws Exception {
        when(productService.create(any(Product.class))).thenReturn(product);

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Notebook"))
                .andExpect(jsonPath("$.price").value(4500.0));

        verify(productService, times(1)).create(any(Product.class));
    }

    @Test
    void deveAtualizarProduto() throws Exception {
        Product updated = new Product("Smartphone", "Celular Android", 2500.0, "img/smartphone.png");
        updated.setId(1L);

        when(productService.update(eq(1L), any(Product.class))).thenReturn(updated);

        mockMvc.perform(put("/products/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Smartphone"))
                .andExpect(jsonPath("$.price").value(2500.0));

        verify(productService, times(1)).update(eq(1L), any(Product.class));
    }
}
