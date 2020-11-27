package com.xinsteel.epaike.dao;

import com.xinsteel.epaike.pojo.Materia;
import org.springframework.stereotype.Repository;

@Repository
public interface MateriaMapper {
    int insert(Materia record);

    int insertSelective(Materia record);
}