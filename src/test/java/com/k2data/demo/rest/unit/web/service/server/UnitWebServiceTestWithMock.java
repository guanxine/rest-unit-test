package com.k2data.demo.rest.unit.web.service.server;


import com.k2data.demo.rest.unit.entity.vo.UnitVo;
import com.k2data.demo.rest.unit.service.UnitService;
import com.k2data.demo.rest.unit.web.service.UnitWebService;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.util.SocketUtils;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * Created by guanxine on 18-11-19.
 * 通过 Spring 启动一个 cxf 内嵌的服务，，使用 @Mock  @InjectMocks 来 mock 部分依赖
 */
@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = {"classpath:cxf-test.xml"})
@PowerMockIgnore({"javax.management.*", "javax.net.ssl.*"})
public class UnitWebServiceTestWithMock {

    private String baseUrl;
    private RequestSpecification spec;

    @Autowired
    @InjectMocks
    private UnitWebService unitWebService;

    @Mock
    private UnitService unitService;

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
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void createWithMock() {
        when(unitService.create((UnitVo) Mockito.any())).thenReturn(-1);
        given().spec(spec)
                .contentType(ContentType.JSON)
                .body("{\n" +
                        "    \"id\": 1,\n" +
                        "    \"unit\": \"a rest unit test\"\n" +
                        "}")
                .when().post("unit").then()//res
                .statusCode(400)
                .body("code", equalTo(1));
    }
}