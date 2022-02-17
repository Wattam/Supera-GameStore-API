package br.com.superaGameStore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.superaGameStore.model.Cart;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

}
