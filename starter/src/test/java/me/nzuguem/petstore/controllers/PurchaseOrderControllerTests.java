package me.nzuguem.petstore.controllers;

import static io.restassured.RestAssured.*;

import io.restassured.http.ContentType;
import static org.hamcrest.CoreMatchers.*;
import org.junit.jupiter.api.Test;


class PurchaseOrderControllerTests extends BaseE2EControllerTests {

    @Test
    void should_place_order() {

        // ACT + ASSERT
        given().contentType(ContentType.JSON)
                .body(BASE_CTX)
                .when()
                .post()
                .then()
                .contentType(ContentType.JSON)
                .statusCode(202)
                .body("transactionId", notNullValue());
    }

    @Test
    void should_accepted_request_when_bad_customer_email() {

        // ARRANGE
        var ctx = BASE_CTX.toBuilder()
                .customerEmail("bad_customer@foo.com")
                .build();

        // ACT + ASSERT
        given().contentType(ContentType.JSON)
                .body(ctx)
                .when()
                .post()
                .then()
                .contentType(ContentType.JSON)
                .statusCode(202)
                .body("transactionId", notNullValue());
    }
}
