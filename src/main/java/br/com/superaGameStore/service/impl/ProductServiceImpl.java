package br.com.superaGameStore.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.superaGameStore.model.Product;
import br.com.superaGameStore.repository.ProductRepository;
import br.com.superaGameStore.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<Product> index() {

        return productRepository
                .findAll()
                .stream()
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Product> show(long id) {

        return productRepository.findById(id);
    }

    @Override
    public Product store(Product product) {

        return productRepository.save(product);
    }

    @Override
    public Product update(Product product, long id) {

        product.setId(id);
        return productRepository.save(product);
    }

    @Override
    public void delete(long id) {

        productRepository.deleteById(id);
    }
}