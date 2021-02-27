package com.management.crudsystem.mapper;

import com.management.crudsystem.pojo.Employee;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;

@Mapper
@Repository
public interface DataInputMapper {
    //增加一个员工的原本所有字段的信息
    int addEmployee(Employee employee);

    //增加一个员工的其余字段的信息
    int addEmployeeOther(@Param("userid") int userid, @Param("otherFieldsMap") HashMap<String,String> otherFieldsMap);

    //删除一个员工的所有原本字段的信息
    int deleteEmployee(int userid);

    //删除一个员工的所有新增字段的信息
    int deleteNewFieldValue(int userid);

    //更新一个员工的原本所有字段的信息
    int updateEmployee(Employee employee);

    //更新一个员工的其余字段的信息
    int updateEmployeeOther(@Param("userid") int userid, @Param("otherFieldsMap") HashMap<String,String> otherFieldsMap);

    //查询新增的字段及其web名字
    ArrayList<HashMap<String, Object>> queryNewFieldName();

    //插入新增的属性字段
    int insertNewField(@Param("newFieldName") String newFieldName, @Param("newFieldWebName") String newFieldWebName);

    //删除新增的属性字段
    int deleteField(@Param("deleteFieldName") String deleteFieldName);

}
