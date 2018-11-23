package com.k2data.demo.rest.unit.service;

import com.k2data.demo.rest.unit.dao.UnitDao;
import com.k2data.demo.rest.unit.entity.UnitDo;
import com.k2data.demo.rest.unit.entity.vo.UnitVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by guanxine on 18-11-19.
 */
@Service
public class UnitService {

    @Resource
    private UnitDao unitDao;

    public int create(UnitVo unitVo) {
        UnitDo unitDo = new UnitDo();
        unitDo.setId(unitVo.getId());
        unitDo.setUnit(unitVo.getUnit());
        return unitDao.create(unitDo);
    }
}
