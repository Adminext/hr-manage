package com.management.crudsystem.mapper;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

@Mapper
@Repository
public interface DataQueryMapper {

    //查询所有的字段及其web名字
    ArrayList<HashMap<String, Object>> queryAllFieldName();

    //查询所有员工的原本字段的信息
    ArrayList<HashMap<String, Object>> queryAllEmployee(@Param("pageStart") int pageStart, @Param("recordCount") int recordCount);

    //查询所有员工的原本字段的信息
    ArrayList<HashMap<String, Object>> queryAllEmployeeNotPage();

    //查询某个ID的员工的其他字段及其信息
    ArrayList<HashMap<String, Object>> queryOtherFieldsByEmployeeID(@Param("userid") int userid);

    //查询某个ID的员工的原本字段及其信息
    ArrayList<HashMap<String, Object>> queryEmployeeById(@Param("userid") int userid);

    //按名字查询员工原本字段的信息
    ArrayList<HashMap<String, Object>> dataQueryByName(@Param("name") String name, @Param("pageStart") int pageStart, @Param("recordCount") int recordCount);

    //按名字查询员工原本字段的信息
    ArrayList<HashMap<String, Object>> dataQueryByNameNotPage(@Param("name") String name);

    //按身份证号查询员工原本字段的信息
    ArrayList<HashMap<String, Object>> dataQueryByNumber(@Param("idnumber") String idnumber, @Param("pageStart") int pageStart, @Param("recordCount") int recordCount);

    //按身份证号查询员工原本字段的信息
    ArrayList<HashMap<String, Object>> dataQueryByNumberNotPage(@Param("idnumber") String idnumber);

    //按员工序号查询员工原本字段的信息
    ArrayList<HashMap<String, Object>> dataQueryByUserIDNotPage(@Param("userid") int userid);

    //查询原本的字段有有哪些
    ArrayList<HashMap<String,String>> QueryOriginalField();

    int queryRecordTotalCount();


}
