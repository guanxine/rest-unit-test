package com.k2data.demo.rest.unit.web.service;


import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.util.SocketUtils;

import static io.restassured.RestAssured.given;

/**
 * Created by guanxine on 18-11-19.
 */
@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = {"classpath:test-application.xml"})
@PowerMockIgnore({"javax.management.*", "javax.net.ssl.*"})
public class ENEWebServiceTest {


    private String baseUrl;
    private RequestSpecification spec;

    @BeforeClass
    public static void configure() {
        // fix address already in use in multiple test cases
        System.setProperty("servicePort", String.valueOf(SocketUtils.findAvailableTcpPort()));
    }

    @Before
    public void setUp() {
        this.baseUrl = "http://localhost:" + System.getProperty("servicePort") + "/rest-unit-test";

        spec = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setBaseUri(baseUrl)
                .addFilter(new ResponseLoggingFilter())//log request and response for better debugging. You can also only log if a requests fails.
                .addFilter(new RequestLoggingFilter())
                .build();
    }


    @Test
    public void webUnit() {
        given().spec(spec)
                .when()// req
                .get("unit/web")// res
                .then()//res
                .statusCode(200);

    }
}