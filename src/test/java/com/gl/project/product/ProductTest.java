package com.gl.project.product;

import com.gl.project.entities.Category;
import com.gl.project.entities.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product("Notebook", "Computador portátil", 4500.0, "img/notebook.png");
        product.setId(1L);
    }

    @Test
    void deveCriarProdutoComValoresCorretos() {
        assertEquals(1L, product.getId());
        assertEquals("Notebook", product.getName());
        assertEquals("Computador portátil", product.getDescription());
        assertEquals(4500.0, product.getPrice());
        assertEquals("img/notebook.png", product.getImgURL());
    }

    @Test
    void devePermitirAlterarAtributosDoProduto() {
        product.setName("Smartphone");
        product.setDescription("Celular Android");
        product.setPrice(2500.0);
        product.setImgURL("img/smartphone.png");

        assertAll(
                () -> assertEquals("Smartphone", product.getName()),
                () -> assertEquals("Celular Android", product.getDescription()),
                () -> assertEquals(2500.0, product.getPrice()),
                () -> assertEquals("img/smartphone.png", product.getImgURL())
        );
    }

    @Test
    void listaDeCategoriasDeveIniciarVaziaEModificavel() {
        assertNotNull(product.getCategories());
        assertTrue(product.getCategories().isEmpty());

        Category category = new Category();
        category.setId(10L);
        category.setName("Eletrônicos");

        product.getCategories().add(category);

        assertEquals(1, product.getCategories().size());
        assertTrue(product.getCategories().contains(category));
    }

    @Test
    void equalsDeveRetornarTrueParaIdsIguais() {
        Product p1 = new Product("Notebook", "Descrição", 1000.0, "img1");
        Product p2 = new Product("TV", "Outra desc", 2000.0, "img2");

        p1.setId(1L);
        p2.setId(1L);

        assertEquals(p1, p2);
        assertEquals(p1.hashCode(), p2.hashCode());
    }

    @Test
    void equalsDeveRetornarFalseParaIdsDiferentes() {
        Product p1 = new Product("Notebook", "Descrição", 1000.0, "img1");
        Product p2 = new Product("TV", "Outra desc", 2000.0, "img2");

        p1.setId(1L);
        p2.setId(2L);

        assertNotEquals(p1, p2);
    }

    @Test
    void equalsDeveRetornarFalseQuandoUmIdForNulo() {
        Product p1 = new Product("Notebook", "Descrição", 1000.0, "img1");
        Product p2 = new Product("TV", "Outra desc", 2000.0, "img2");

        p1.setId(null);
        p2.setId(2L);

        assertNotEquals(p1, p2);
    }
}
