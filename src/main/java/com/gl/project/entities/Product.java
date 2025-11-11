package com.gl.project.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
<<<<<<< HEAD
=======
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
>>>>>>> 264a16fbf826ee630aa2bbc602e7497b44616f1d

import java.io.Serial;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "tb_product")

public class Product implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

<<<<<<< HEAD
    private String name;
    private String descriprion;
    private Double price;
=======
    @NotBlank(message = "Nome do produto não pode estar em branco")
    @Size(min = 3, message = "Nome do produto deve ter no mínimo 3 caracteres")
    private String name;

    @NotBlank(message = "Descrição não pode estar em branco")
    @Size(min = 10, message = "Descrição deve ter no mínimo 10 caracteres")
    private String description;

    @NotNull(message = "Preço não pode ser nulo")
    @Positive(message = "Preço deve ser um valor positivo")
    private Double price;

    @NotBlank(message = "URL da imagem não pode estar em branco")
>>>>>>> 264a16fbf826ee630aa2bbc602e7497b44616f1d
    private String imgURL;

    @ManyToMany
    @JoinTable(name = "tb_product_category",  joinColumns = @JoinColumn(name = "product_id"), inverseJoinColumns = @JoinColumn(name = "category_id"))
    private Set<Category> categories = new HashSet<>();

    @OneToMany(mappedBy = "id.product")
    private Set<OrderItem> items = new HashSet<>();

    public Product() {
    }

<<<<<<< HEAD
    public Product(String name, String descriprion, Double price, String imgURL) {
        super();
        this.id = id;
        this.name = name;
        this.descriprion = descriprion;
=======
    public Product(String name, String description, Double price, String imgURL) {
        super();
        this.id = id;
        this.name = name;
        this.description = description;
>>>>>>> 264a16fbf826ee630aa2bbc602e7497b44616f1d
        this.price = price;
        this.imgURL = imgURL;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

<<<<<<< HEAD
    public String getDescriprion() {
        return descriprion;
    }

    public void setDescriprion(String descriprion) {
        this.descriprion = descriprion;
=======
    public String getDescription() {
        return description;
    }

    public void setDescription(String descriprion) {
        this.description = descriprion;
>>>>>>> 264a16fbf826ee630aa2bbc602e7497b44616f1d
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<Category> getCategories() {
        return categories;
    }

    @JsonIgnore
    public Set<Order> getOrders() {
        Set<Order> set = new HashSet<>();
        for (OrderItem x : items) {
            set.add(x.getOrder());
        }
        return set;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(id, product.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
