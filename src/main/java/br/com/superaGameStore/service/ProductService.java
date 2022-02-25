package br.com.superaGameStore.service;

import java.util.List;
import java.util.Optional;

import br.com.superaGameStore.model.Product;

public interface ProductService {

    public List<Product> index();

    public Optional<Product> show(long id);

    public Product store(Product product);

    public Product update(Product product, long id);

    public void delete(long id);
}