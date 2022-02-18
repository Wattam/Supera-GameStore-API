package br.com.superaGameStore.service;

import java.util.List;
import java.util.Optional;

import br.com.superaGameStore.model.Product;

public interface ProductService {

    public List<Product> getAllProducts();

    public Optional<Product> getProduct(long id);

    public Product addProduct(Product product);

    public void deleteProduct(long id);
}