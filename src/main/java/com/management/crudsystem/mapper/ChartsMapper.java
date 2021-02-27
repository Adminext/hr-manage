package com.management.crudsystem.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;

@Mapper
@Repository
public interface ChartsMapper {
    ArrayList<HashMap<String,Long>> queryyouthposition();

    ArrayList<HashMap<String,Long>> querysex();

    ArrayList<HashMap<String, String>> queryage();

    ArrayList<HashMap<String,Long>> querypoliticsstatus();

    ArrayList<HashMap<String,Object>> querysector();

    ArrayList<HashMap<String, Object>> queryprofession();

    ArrayList<HashMap<String,Object>> querynation();
}
