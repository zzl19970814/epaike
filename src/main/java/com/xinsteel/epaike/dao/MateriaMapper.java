package com.xinsteel.epaike.dao;

import com.xinsteel.epaike.pojo.Materia;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MateriaMapper {
    int insert(Materia record);

    int insertSelective(Materia record);

    Materia selectMaterialByMaterialNo(String materialno);

    List selectAllMaterialNo();
}