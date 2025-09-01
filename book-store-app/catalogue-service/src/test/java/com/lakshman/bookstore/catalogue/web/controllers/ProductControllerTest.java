package com.lakshman.bookstore.catalogue.web.controllers;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import com.lakshman.bookstore.catalogue.AbstractIT;
import com.lakshman.bookstore.catalogue.domain.Product;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;

@Sql("/test-data.sql")
class ProductControllerTest extends AbstractIT {

    @Test
    void shouldReturnProducts() {
        given().contentType(ContentType.JSON)
                .when()
                .get("/api/products")
                .then()
                .statusCode(200)
                .body("data", hasSize(10))
                .body("totalElements", is(14))
                .body("pageNumber", is(1))
                .body("totalPages", is(2))
                .body("isFirst", is(true))
                .body("isLast", is(false))
                .body("hasNext", is(true))
                .body("hasPrevious", is(false));
    }

    @Test
    void shouldGetProductByCode(){
        Product product = given().contentType(ContentType.JSON)
                .when()
                .get("/api/products/{code}","P101")
                .then()
                .statusCode(200)
                .assertThat()
                .extract()
                .body()
                .as(Product.class);

        assertThat(product.code()).isEqualTo("P101");
        assertThat(product.name()).isEqualTo("The Midnight Library by Matt Haig");
        assertThat(product.description()).isEqualTo("Between life and death there is a library, and within that library, the shelves go on forever. Every book offers a chance to try another life you could have lived. Nora Seed finds herself faced with the possibility of changing her life for a new one – undoing regrets and making things right.");
        assertThat(product.price()).isEqualTo(new BigDecimal("450"));
    }

    @Test
    void shouldReturnNotFoundWhenProductCodeNotExists(){
        String code = "invalidProductCode";
        given().contentType(ContentType.JSON)
                .when()
                .get("/api/products/{code}",code)
                .then()
                .statusCode(404)
                .body("status",is(404))
                .body("title",is("Product Not Found"))
                .body("detail",is("Product with code '"+code+"' not found"));
    }
}
