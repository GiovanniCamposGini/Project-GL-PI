package com.gl.project.entities;


import jakarta.persistence.*;

@Entity
@Table(name = "tb_products")

public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String descriprion;

    private Double price;
    private String imgURL;

}
