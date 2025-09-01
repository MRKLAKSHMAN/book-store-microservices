package com.lakshman.bookstore.catalogue.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest(
        properties = {
            "spring.test.database.replace=none",
            "spring.datasource.url=jdbc:tc:postgresql:16-alpine:///db",
        })
// Slice Test(Focused On Repository layer) alone
@Sql("/test-data.sql")
// @Import(TestcontainersConfiguration.class)
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    void shouldGetAllProducts() {
        List<ProductEntity> products = productRepository.findAll();
        assertThat(products).hasSize(14);
    }

    @Test
    void shouldGetProductByCode(){
        ProductEntity product = productRepository.findByCode("P101").orElseThrow();
        assertThat(product.getCode()).isEqualTo("P101");
        assertThat(product.getName()).isEqualTo("The Midnight Library by Matt Haig");
        assertThat(product.getDescription()).isEqualTo("Between life and death there is a library, and within that library, the shelves go on forever. Every book offers a chance to try another life you could have lived. Nora Seed finds herself faced with the possibility of changing her life for a new one – undoing regrets and making things right.");
        assertThat(product.getPrice()).isEqualTo(new BigDecimal("450"));
    }

    @Test
    void shouldReturnEmptyWhenProductCodeNotExists(){
        assertThat(productRepository.findByCode("invalidProductCode")).isEmpty();
    }


}
