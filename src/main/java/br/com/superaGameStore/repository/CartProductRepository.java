package br.com.superaGameStore.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.superaGameStore.model.CartProduct;
import br.com.superaGameStore.model.CartProductKey;

@Repository
public interface CartProductRepository extends JpaRepository<CartProduct, CartProductKey> {

    public List<CartProduct> findByCpkCartId(long id, Sort sort);
}
