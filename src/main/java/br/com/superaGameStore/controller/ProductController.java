package br.com.superaGameStore.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.superaGameStore.controller.exception.RecordNotFoundException;
import br.com.superaGameStore.model.Product;
import br.com.superaGameStore.service.ProductService;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/get")
    @ResponseStatus(HttpStatus.OK)
    public List<Product> getAll() {

        List<Product> products = productService.getAllProducts();
        if (products == null || products.isEmpty()) {
            throw new RecordNotFoundException("no product found");
        }
        return products;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Product get(@PathVariable long id) {

        return productService
                .getProduct(id)
                .orElseThrow(() -> new RecordNotFoundException("no product with the ID: " + id));
    }

    @PostMapping("/post")
    @ResponseStatus(HttpStatus.CREATED)
    public Product post(@Valid @RequestBody Product product) {

        return productService.addProduct(product);
    }

    @PutMapping("/put")
    @ResponseStatus(HttpStatus.OK)
    public Product put(@Valid @RequestBody Product product) {

        if (productService.getProduct(product.getId()).isEmpty()) {
            throw new RecordNotFoundException("no product with the ID: " + product.getId());
        }
        return productService.addProduct(product);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long id) {

        if (productService.getProduct(id).isEmpty()) {
            throw new RecordNotFoundException("no product with the ID: " + id);
        }
        productService.deleteProduct(id);
    }
}
