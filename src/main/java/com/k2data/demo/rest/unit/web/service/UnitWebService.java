package com.k2data.demo.rest.unit.web.service;

import com.k2data.demo.rest.unit.common.JsonResult;
import com.k2data.demo.rest.unit.entity.vo.UnitVo;
import com.k2data.demo.rest.unit.service.UnitService;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by guanxine on 18-11-19.
 */
@Path("/unit")
@Produces(MediaType.APPLICATION_JSON)
public class UnitWebService {
    @Resource
    private UnitService unitService;
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(UnitVo unitVo) {
        // check unit
        int id = unitService.create(unitVo);// 该方法，返回 -1 表示异常，返回大于1的值表示正常。
        if (id == -1) {
            return Response.status(400).entity(
                    new JsonResult().setCode(1).setMessage("Failed")
            ).build();
        }
        else {
            Map<String, Object> map = new HashMap();
            map.put("id", id);
            return Response.status(200).entity(
                    new JsonResult().setCode(0).setMessage("Successfully").setBody(map)
            ).build();
        }

    }
}
