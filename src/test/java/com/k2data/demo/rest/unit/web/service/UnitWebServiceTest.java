package com.k2data.demo.rest.unit.web.service;

import com.k2data.demo.rest.unit.common.JsonResult;
import com.k2data.demo.rest.unit.entity.vo.UnitVo;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.ws.rs.core.Response;
import java.util.Map;

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
    @Autowired  // 将 unitWebService 注入到 UnitWebServiceTestWithMock
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
