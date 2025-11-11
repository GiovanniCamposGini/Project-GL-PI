package com.gl.project.resources;

import com.gl.project.entities.Product;
import com.gl.project.repository.ProductRepository;
import com.gl.project.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/products")
public class ProductResources {

    @Autowired
    private ProductRepository ProductRepository;

    @Autowired
    private ProductService ProductService;

    @GetMapping
    public ResponseEntity<List<Product>> findAll() {
<<<<<<< HEAD
        List<Product> list = ProductRepository.findAll();
=======
        List<Product> list = ProductService.findAll();
>>>>>>> 264a16fbf826ee630aa2bbc602e7497b44616f1d
        return ResponseEntity.ok().body(list);
    }
    @GetMapping(value = "/{id}")
    public ResponseEntity<Product> findById(@PathVariable Long id) {
        Product Product = ProductService.findById(id);
        return ResponseEntity.ok().body(Product);
    }

    @PostMapping
    public ResponseEntity<Product> save(@RequestBody Product newProduct) {
        Product Product = ProductService.create(newProduct);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(Product.getId()).toUri();
        return ResponseEntity.created(uri).body(Product);
    }
    @PutMapping(value = "{id}")
    public ResponseEntity<Product> update(@PathVariable Long id, @RequestBody Product newProduct) {
        Product Product = ProductService.update(id, newProduct);
        return ResponseEntity.ok().body(Product);
    }

}
