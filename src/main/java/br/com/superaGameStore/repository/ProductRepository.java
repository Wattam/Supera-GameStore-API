package br.com.superaGameStore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.superaGameStore.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

}
