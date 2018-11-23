[TOC]

# 介绍
一个由 Spring 和 CXF 组成的 maven 项目。

## 测试方法

Rest 项目有两类测试办法
+ 把整个 Rest 项目当作是一个普通的 Spring 项目来进行测试。对于请求路径映射，请求参数赋值，全局异常这类情况测试不到。

+ 另一类是启动一个内嵌的 Servlet 容器, 对 Rest 项目进行测试。

### 当作普通的 Spring 项目测试

#### 不使用 Mock
```java
/**
 *
 * Created by guanxine on 18-11-19.
 * 在 Spring 构建的真实依赖基础上进行测试
 *
 */
@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:application-test.xml"})
@PowerMockIgnore({"javax.management.*"})
public class UnitWebServiceTest {
    @Autowired  // 将 unitWebService 注入到 UnitWebServiceTest
    private UnitWebService unitWebService;
    @Before
    public void setUp() {
    }

    @Test
    public void create() {
       Response resp = unitWebService.create(new UnitVo().setId(1).setUnit("for unit"));
       JsonResult jsonResult = (JsonResult) resp.getEntity();
       Assert.assertTrue(resp.getStatus() == 200);
       Assert.assertTrue(jsonResult.getCode() == 0);
       Assert.assertTrue(((Map) jsonResult.getBody()).get("id").equals(1));
    }
}
```

#### 使用 Mock 
```java
/**
 * Created by guanxine on 18-11-19.
 *
 *  在 Spring 构建的真实依赖基础上，使用 @Mock  @InjectMocks 来 mock 部分依赖
 *
 */
@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:application-test.xml"})
@PowerMockIgnore({"javax.management.*"})
public class UnitWebServiceTestWithMock {

    @Autowired  // 将 unitWebService 注入到 UnitWebServiceTestWithMock
    @InjectMocks // 将  @Mock 的 unitService 实例注入到 unitWebService
    private UnitWebService unitWebService;
    @Mock
    private UnitService unitService;

    @Before
    public void setUp() {
       MockitoAnnotations.initMocks(this);
    }

    @Test
    public void createWithMock() {
       when(unitService.create((UnitVo) Mockito.any())).thenReturn(-1);

        Response fail = unitWebService.create(new UnitVo().setId(1).setUnit("for unit"));
        JsonResult jsonResult = (JsonResult) fail.getEntity();
        
        Assert.assertTrue(fail.getStatus() == 400);
        Assert.assertTrue(jsonResult.getCode() == 1);
    }
}
```

### 启动内嵌的容器测试
+ 不依赖本地环境，通过 Spring 启动内嵌 CXF 容器，对 Rest 服务进行单元测试， 不过这种方式测试不到 Filter, Listener 这些组件 (目前没有找到)
+ spring-test 为 web 测试提供了一些 mock  类， 例如 MockHttpSession， MockHttpServletRequest 

#### 不使用 Mock
```java
/**
 * Created by guanxine on 18-11-19.
 * 通过 Spring 启动一个 cxf 内嵌的服务
 */
@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = {"classpath:cxf-test.xml"})
@PowerMockIgnore({"javax.management.*", "javax.net.ssl.*"})
public class UnitWebServiceTest {

    private String baseUrl;
    private RequestSpecification spec;

    @Autowired
    MockHttpSession session;
    @Autowired
    MockHttpServletRequest request;

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
    public void create() {
       given().spec(spec)
               .contentType(ContentType.JSON)
               .body("{\n" +
                       "    \"id\": 1,\n" +
                       "    \"unit\": \"a rest unit test\"\n" +
                       "}")
               .when().post("unit").then()//res
               .statusCode(200)
               .body("code", equalTo(0))
               .body("body.id", equalTo(1));
    }
}
```
#### 使用 Mock
```java
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
```

## 总结
一个 REST 应用一般会分层，不论怎么分层，应该尽量把 Rest 层写的简单。
比如一个 REST 应用分为了 Dao, Service, Rest 这三层。在 Rest 层，只做请求参数的校验，响应结果的封装等简单逻辑。在 Dao, Service 尽量不要有 Servlet 相关代码的使用。这样我们就可以把 Dao, Service 当作是普通的 Spring 项目或者 Java 项目来写UT。然后在 Rest 层可以通过启动内嵌的容器来进行测试。
