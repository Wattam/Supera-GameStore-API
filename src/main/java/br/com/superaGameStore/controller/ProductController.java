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
import br.com.superaGameStore.dto.ProductDto;
import br.com.superaGameStore.service.ProductService;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/get")
    @ResponseStatus(HttpStatus.OK)
    public List<ProductDto> getAll() {

        List<ProductDto> products = productService.getAllProducts();
        if (products == null || products.isEmpty()) {
            throw new RecordNotFoundException("no product found");
        }
        return products;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProductDto get(@PathVariable Long id) {

        return productService
                .getProduct(id)
                .orElseThrow(() -> new RecordNotFoundException("no product with the ID: " + id));
    }

    @PostMapping("/post")
    @ResponseStatus(HttpStatus.CREATED)
    public ProductDto post(@Valid @RequestBody ProductDto productDto) {

        return productService.addProduct(productDto);
    }

    @PutMapping("/put")
    @ResponseStatus(HttpStatus.OK)
    public ProductDto put(@Valid @RequestBody ProductDto productDto) {

        if (productService.getProduct(productDto.getId()).isEmpty()) {
            throw new RecordNotFoundException("no product with the ID: " + productDto.getId());
        }
        return productService.addProduct(productDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {

        if (productService.getProduct(id).isEmpty()) {
            throw new RecordNotFoundException("no product with the ID: " + id);
        }
        productService.deleteProduct(id);
    }
}
