package com.k2data.demo.rest.unit.web.service;

import com.k2data.demo.rest.unit.common.JsonResult;
import com.k2data.demo.rest.unit.entity.vo.UnitVo;
import com.k2data.demo.rest.unit.service.UnitService;
import org.junit.Assert;
import org.junit.Before;
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

import javax.ws.rs.core.Response;

import static org.powermock.api.mockito.PowerMockito.when;

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
