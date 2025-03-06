package me.nzuguem.petstore.controllers;

import io.restassured.RestAssured;
import me.nzuguem.petstore.BaseE2ETests;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.web.server.LocalServerPort;

abstract class BaseE2EControllerTests extends BaseE2ETests {

    @LocalServerPort
    private Integer port;


    @BeforeEach
    void setUp() {
        RestAssured.port = this.port;
    }
}
