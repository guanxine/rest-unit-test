package com.k2data.demo.rest.unit.dao;

import com.k2data.demo.rest.unit.entity.UnitDo;
import org.springframework.stereotype.Repository;

/**
 * Created by guanxine on 18-11-19.
 */
@Repository
public class UnitDao {

    public int create(UnitDo unitDo) {
        // save unit do
        return unitDo.getId();
    }
}
