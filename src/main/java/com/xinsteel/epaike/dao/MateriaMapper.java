package com.xinsteel.epaike.dao;

import com.xinsteel.epaike.pojo.Materia;

public interface MateriaMapper {
    int insert(Materia record);

    int insertSelective(Materia record);
}