package com.michael.ecommerce;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

@QuarkusTest
public class OrderFlowTest {

    @Test
    public void testCompleteOrderFlow() {
        String categoryBody = """
                {
                  "name": "Electronics",
                  "description": "Electronic devices and accessories"
                }
                """;

        Long categoryId = given()
                .contentType("application/json")
                .body(categoryBody)
                .when()
                .post("/api/categories")
                .then()
                .statusCode(201)
                .extract().jsonPath().getLong("id");

        String productBody = """
                {
                   "name": "iPhone 15",
                   "description": "Apple smartphone 128GB",
                   "price": 999.99,
                   "stock": 50,
                   "categoryId": %d
                 }
                """.formatted(categoryId);

        Long productId = given()
                .contentType("application/json")
                .body(productBody)
                .when()
                .post("/api/products")
                .then()
                .statusCode(201)
                .extract().jsonPath().getLong("id");

        String email = "test-" + UUID.randomUUID() + "@test.com";

        String customerBody = """
                {
                  "name": "Jane",
                  "lastName": "Doe",
                  "email": "%s",
                  "phone": "8415674171",
                  "address": "25st"
                }
                """.formatted(email);

        Long customerId = given()
                .contentType("application/json")
                .body(customerBody)
                .when()
                .post("/api/customers/")
                .then()
                .statusCode(201)
                .extract().jsonPath().getLong("id");

        String orderBody = """
                {
                  "customerId": %d,
                  "items": [
                    {
                      "productId": %d,
                      "quantity": 2
                    }
                  ]
                }
                """.formatted(customerId, productId);

        Long orderId = given()
                .contentType("application/json")
                .body(orderBody)
                .when()
                .post("/api/orders/")
                .then()
                .statusCode(201)
                .body("total", equalTo(1999.98f))
                .extract().jsonPath().getLong("id");

        given()
                .when()
                .get("/api/products/" + productId)
                .then()
                .statusCode(200)
                .body("stock", equalTo(48));
    }
}
