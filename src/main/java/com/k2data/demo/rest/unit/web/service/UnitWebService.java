package com.k2data.demo.rest.unit.web.service;

import com.k2data.demo.rest.unit.service.UnitService;

import javax.annotation.Resource;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
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

    @GET
    @Path("/web")
    public Response webUnit() {

        unitService.serviceUnit();
        Map<String, Object> result = new HashMap();
        result.put("code", 1);
        result.put("message", "success");
        return Response.status(200).entity(result).build();
    }

    @GET
    @Path("simple")
    public Response simpleUnit() {
        Map<String, Object> result = new HashMap();
        result.put("code", 1);
        result.put("message", "success");
        return Response.status(200).entity(result).build();
    }
}
