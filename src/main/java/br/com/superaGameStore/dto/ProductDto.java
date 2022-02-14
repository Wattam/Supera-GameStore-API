package br.com.superaGameStore.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.validation.constraints.NotBlank;

import com.opengamma.strata.collect.ArgChecker;

import org.springframework.beans.BeanUtils;

import br.com.superaGameStore.model.Product;
import lombok.Data;

@Data
public class ProductDto implements Serializable {

    public long id;

    @NotBlank
    public String name;

    @NotBlank
    public String image;

    public short score;

    public BigDecimal price;

    public Product toEntity() {

        Product product = new Product();
        BeanUtils.copyProperties(this, product);
        return product;
    }
    
    public static ProductDto of(Product product) {
        
        ArgChecker.notNull(product, "product");
        ProductDto productDto = new ProductDto();
        BeanUtils.copyProperties(product, productDto);
        return productDto;
    }
}
