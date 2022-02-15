package br.com.superaGameStore.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.superaGameStore.dto.ProductDto;
import br.com.superaGameStore.model.Product;
import br.com.superaGameStore.repository.ProductRepository;
import br.com.superaGameStore.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<ProductDto> getAllProducts() {

        return productRepository
                .findAll()
                .stream()
                .map(ProductDto::of)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ProductDto> getProduct(Long id) {

        return productRepository.findById(id).map(ProductDto::of);
    }

    @Override
    public ProductDto addProduct(ProductDto productDto) {

        Product postedProduct = productRepository.save(productDto.toEntity());
        return ProductDto.of(postedProduct);
    }

    @Override
    public void deleteProduct(Long id) {

        productRepository.deleteById(id);
    }
}