package com.management.crudsystem.controller;

import com.management.crudsystem.mapper.DataInputMapper;
import com.management.crudsystem.mapper.DataQueryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Controller
public class DataInputController {

    @Autowired
    private DataQueryMapper dataQueryMapper;

    @Autowired
    private DataInputMapper dataInputMapper;

    @PostMapping("/datainput/delete")
    public String update(Model model){

        return "";
    }

    @PostMapping("/datainput/addfield/add")
    public String addFieldData(@RequestParam("addFieldName") String addFieldName ){

        if("".equals(addFieldName)||addFieldName==null){

        }else{
            System.out.println("新增的WebFieldName为："+addFieldName);
            String newFieldName = "newfieldname"+Long.toString(System.currentTimeMillis());
            dataInputMapper.insertNewField(newFieldName,addFieldName);
        }

        return "redirect:/datainput/addfield";
    }


    @PostMapping("/datainput/deleteField")
    public String deleteFieldData(@RequestParam("deleteFieldName") String deleteFieldName){

        if("".equals(deleteFieldName)||deleteFieldName==null){

        }else{
            System.out.println("要删除的deleteFieldName为："+deleteFieldName);
            dataInputMapper.deleteField(deleteFieldName);
        }

        return "redirect:/datainput/addfield";
    }




}
