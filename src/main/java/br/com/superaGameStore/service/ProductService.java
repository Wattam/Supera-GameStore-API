package br.com.superaGameStore.service;

import java.util.List;
import java.util.Optional;

import br.com.superaGameStore.dto.ProductDto;

public interface ProductService {

    public List<ProductDto> getAllProducts();

    public Optional<ProductDto> getProduct(Long id);

    public ProductDto addProduct(ProductDto productDto);

    public void deleteProduct(Long id);
}