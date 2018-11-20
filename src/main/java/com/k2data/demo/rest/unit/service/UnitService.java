package com.k2data.demo.rest.unit.service;

import com.k2data.demo.rest.unit.dao.UnitDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by guanxine on 18-11-19.
 */
@Service
public class UnitService {

    @Resource
    private UnitDao unitDao;

    public void serviceUnit() {

        unitDao.daoUnit();
        System.out.println("test");
    }
}
