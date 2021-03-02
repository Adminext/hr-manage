package com.management.crudsystem.controller;

import com.management.crudsystem.mapper.DataInputMapper;
import com.management.crudsystem.mapper.DataQueryMapper;
import com.management.crudsystem.pojo.Employee;
import com.management.crudsystem.util.ExcelUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 已经封装好了多个函数，只需要像平时那样，查询列表，
 * 先进行字段补充
 * ArrayList<HashMap<String, Object>> supplyAllFieldsMany(ArrayList<HashMap<String, Object>> hashMaps)
 * 最后进行字段排序
 * ArrayList<ArrayList<String>> dataQueryArrange(ArrayList<HashMap<String, Object>> hashMaps)
 */

@Controller
public class DataQueryController {

    @Autowired
    private DataQueryMapper dataQueryMapper;

    @Autowired
    private DataInputMapper dataInputMapper;

    @GetMapping("/dataquery")
    public String dataQuery(Model model){
        String fieldname = "none";
        String searchvalue = "无";
        String recordCount = "15";
        String pageNumber = "1";

        addModel(fieldname,searchvalue,recordCount,pageNumber,model);
        //以下是所有字段的Web名字
        ArrayList<String> allFieldsWebName = getAllFieldsWebName();
        model.addAttribute("allFieldsWebName",allFieldsWebName);

        ArrayList<HashMap<String, Object>> arrayLists = dataQueryMapper.queryAllEmployee(0, Integer.parseInt(recordCount));
        ArrayList<ArrayList<String>> arrayListsArranged = null;

        boolean wasSearchAndZero = false;
        if(arrayLists.isEmpty()){
            wasSearchAndZero=true;
        }else{
            arrayListsArranged = dataQueryArrange(supplyAllFieldsMany(arrayLists));
        }
        model.addAttribute("wasSearchAndZero",wasSearchAndZero);
        model.addAttribute("arrayLists",arrayListsArranged);

        int recordTotalCount = dataQueryMapper.queryRecordTotalCount();
        model.addAttribute("recordTotalCount",recordTotalCount);

        return "dataquery";
    }

    @GetMapping("/dataquery/by")
    public String dataqueryByGet(){
        return "redirect:/dataquery";
    }


    @PostMapping("/dataquery/by")
    public  String dataQueryBy(@RequestParam("fieldname") String fieldname,
                               @RequestParam("searchvalue") String searchvalue,
                               @RequestParam("recordCount") String recordCount,
                               @RequestParam("pageNumber") String pageNumber,
                               Model model){
        addModel(fieldname,searchvalue,recordCount,pageNumber,model);
        //以下是所有字段的Web名字
        ArrayList<String> allFieldsWebName = getAllFieldsWebName();
        model.addAttribute("allFieldsWebName",allFieldsWebName);

        int recordCountInt = Integer.parseInt(recordCount);
        int pageNumberInt = Integer.parseInt(pageNumber);
        //sql语句的limit是从0开始的，limit里的limit m,n m是指起始位置，n是指条数
        int start = (pageNumberInt-1)*recordCountInt;

        ArrayList<HashMap<String, Object>> arrayLists = null;

        if("none".equals(fieldname)){
            //这种情况是查看全部数据
            arrayLists = dataQueryMapper.queryAllEmployee(start, recordCountInt);
            System.out.println("不按任何条件进行查询");
        }else if("name".equals(fieldname)){
            arrayLists = dataQueryMapper.dataQueryByName(searchvalue,start,recordCountInt);
            System.out.println("按名字查询");
        }else if("idnumber".equals(fieldname)){
            arrayLists = dataQueryMapper.dataQueryByNumber(searchvalue,start,recordCountInt);
            System.out.println("按身份证号查询");
        }

        ArrayList<ArrayList<String>> arrayListsArranged = null;

        boolean wasSearchAndZero = false;
        if(arrayLists.isEmpty()){
            wasSearchAndZero=true;
        }else{
            arrayListsArranged = dataQueryArrange(supplyAllFieldsMany(arrayLists));
        }
        model.addAttribute("wasSearchAndZero",wasSearchAndZero);
        model.addAttribute("arrayLists",arrayListsArranged);

        int recordTotalCount = dataQueryMapper.queryRecordTotalCount();
        model.addAttribute("recordTotalCount",recordTotalCount);
        return "dataquery";
    }

    public void addModel(String fieldname,String searchvalue,String recordCount, String pageNumber,Model model){
        model.addAttribute("fieldname",fieldname);
        model.addAttribute("searchvalue",searchvalue);
        model.addAttribute("recordCount",recordCount);
        model.addAttribute("pageNumber",pageNumber);
    }



    /**
     * 对完整的员工信息进行排列整理，要求传入的参数是完整字段的
     * @param hashMaps 完整字段
     * @return
     */
    public ArrayList<ArrayList<String>> dataQueryArrange(
            ArrayList<HashMap<String, Object>> hashMaps){
        ArrayList<String> allFieldsInMySql = getAllFields();
        //需要按照一定的顺序将属性排列起来，故转换成ArrayList<ArrayList<String>>
        ArrayList<ArrayList<String>> arrayLists = new ArrayList<ArrayList<String>>();
        Iterator<HashMap<String, Object>> iterator = hashMaps.iterator();
        while(iterator.hasNext()){
            HashMap<String, Object> hashMap = iterator.next();
            ArrayList<String> one = new ArrayList<>();

            for(Iterator<String> iter = allFieldsInMySql.iterator(); iter.hasNext();){
                String fieldName = iter.next();
                String fieldValue = null;
                if(null!=hashMap.get(fieldName)){
                    fieldValue = hashMap.get(fieldName).toString();
                }else{
                    fieldValue="无";
                }
                one.add(fieldValue);
            }
            System.out.println(one.toString());
            arrayLists.add(one);
        }


        return arrayLists;
    }


    /**
     * 查询某个员工的原本字段的信息
     * @param userid
     * @return
     */
    public HashMap<String, Object> queryOriginalFieldsByUserid(int userid){
        ArrayList<HashMap<String, Object>> arrayList = dataQueryMapper.queryEmployeeById(userid);

        HashMap<String, Object> stringObjectHashMap = arrayList.get(0);
         return stringObjectHashMap;
    }



    /**
     * 补充只有原本字段的员工的新增字段的信息，返回完整的所有字段的员工信息
     * @param hashMaps 传入的参数是多个员工信息的arrayList，但这个arrayList只有原本的字段
     * @return 返回拥有所有字段的员工信息arrayList
     */
    public ArrayList<HashMap<String, Object>> supplyAllFieldsMany(ArrayList<HashMap<String, Object>> hashMaps){
//        //已经查询到所有员工的原本字段的信息
//        ArrayList<HashMap<String, Object>> hashMaps = queryOriginalFieldsAll();

        Iterator<HashMap<String, Object>> iterator = hashMaps.iterator();
        while(iterator.hasNext()){
            
            HashMap<String, Object> hashMap = iterator.next();
            String userid = hashMap.get("userid").toString();
            //获取对应id的员工的其他新字段的信息
            HashMap<String, Object> otherFieldsHashMap = queryOtherFieldsByEmployeeID(Integer.parseInt(userid));

            hashMap.putAll(otherFieldsHashMap);
        }

        return hashMaps;
    }


    /**
     * 根据userid来遍历某个员工的新字段的值，并封装成一个hashmap返回
     * @param userid
     * @return 返回新字段的值
     */
    public HashMap<String,Object> queryOtherFieldsByEmployeeID(int userid){
        HashMap<String,Object> newFields = new HashMap<>();

        ArrayList<HashMap<String, Object>> hashMaps = dataQueryMapper.queryOtherFieldsByEmployeeID(userid);

        if(null == hashMaps || hashMaps.isEmpty()) return newFields;
        Iterator<HashMap<String, Object>> mapIterator = hashMaps.iterator();

        while(mapIterator.hasNext()){
            HashMap<String, Object> map = mapIterator.next();
            newFields.put(map.get("newfieldname").toString(),map.get("fieldvalue"));
        }

        return newFields;
    }

    /**
     * 可以用来查询得到所有的数据库信息字段，包括原本的和新增的字段
     * @return
     */
    public ArrayList<String> getAllFields(){

        //每个map中只有两个k v对，key分别是 fieldname和 webname
        ArrayList<HashMap<String, Object>> hashMaps = dataQueryMapper.queryAllFieldName();

        //用来存储fieldname和 webname的ArrayList
        ArrayList<String> fieldNameList = new ArrayList<>();
        ArrayList<String> webNameList = new ArrayList<>();

        Iterator<HashMap<String, Object>> iterator = hashMaps.iterator();
        while (iterator.hasNext()){
            HashMap<String, Object> map = iterator.next();
            for(Map.Entry<String,Object> entry : map.entrySet()){
                if(entry.getKey().equals("fieldname")){
                    fieldNameList.add(map.get(entry.getKey()).toString());
                }else if(entry.getKey().equals(("webname"))){
                    webNameList.add(map.get(entry.getKey()).toString());
                }

            }
        }

        return fieldNameList;

    }

    @PostMapping("/dataquery/export")
    public void dataQueryExport(@RequestParam("fieldname") String fieldname,
                                  @RequestParam("searchvalue") String searchvalue,
                                  HttpServletResponse response){
        System.out.println("导出表格：fieldname为"+fieldname+",seachvalue为"+searchvalue);

        ArrayList<HashMap<String, Object>> arrayLists = null;

        if("none".equals(fieldname)){
            arrayLists = dataQueryMapper.queryAllEmployeeNotPage();
        }else if("name".equals(fieldname)){
            arrayLists = dataQueryMapper.dataQueryByNameNotPage(searchvalue);
        }else if("idnumber".equals(fieldname)){
            arrayLists = dataQueryMapper.dataQueryByNumberNotPage(searchvalue);
        }

        ArrayList<ArrayList<String>> arrayListsArranged = null;
        arrayListsArranged = dataQueryArrange(supplyAllFieldsMany(arrayLists));


        ArrayList<String> allFieldsWebName = getAllFieldsWebName();

        String fileName = "员工信息表.xls";
        HSSFWorkbook excel = ExcelUtils.expExcel(allFieldsWebName,arrayListsArranged);
        ExcelUtils.outFile(excel,"./"+fileName,response);
    }


    @RequestMapping("/uploadToFile")
    @ResponseBody
    public String uploadToUser(@RequestParam("file") MultipartFile file, HttpServletRequest req, Model model) {
        String fileName = file.getOriginalFilename();
        if (fileName.indexOf("\\") != -1) {
            fileName = fileName.substring(fileName.lastIndexOf("\\"));
        }
        // 获取文件存放地址
        String filePath = "Excel/";
        File f = new File(filePath);
        if (!f.exists()) {
            f.mkdirs();// 不存在路径则进行创建
        }

        FileOutputStream out = null;
        try {
            // 重新自定义文件的名称
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            String d = sdf.format(date);// 时间
            filePath = filePath + d + fileName;
            out = new FileOutputStream(filePath);
            out.write(file.getBytes());
            out.flush();
            out.close();
        } catch (Exception e) {
            return "error";
        }

        int errorCode = 0;
        try {
            errorCode = preAddressExcel(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        File excelFile = new File(filePath);
        if(excelFile.exists()){
            //excelFile.delete();
            //System.out.println("上传的excel文件删除成功！");
        }
        switch (errorCode){
            case -1:
                //导入表格的字段值多于实际数据库中的字段值数量
                return "-1";
            case -2:
                //导入表格的字段值少于实际数据库中的字段值数量
                return "-2";
            case -3:
                //导入表格的字段值和实际数据库中的字段值不匹配
                return "-3";
            case -4:
                //导入的表格行数<=1行，没有数据要导入
                return "-4";
            default:
                return errorCode+"";
        }
    }


    public int preAddressExcel(String xlsPath) throws IOException {
        List temp = new ArrayList();
        System.out.println("正在解析的文件为："+xlsPath);
        // 找出.号所在字符串中的位置
        String strOfPoint = xlsPath.substring(0, xlsPath.indexOf("."));
        String type = xlsPath.substring(strOfPoint.length()+1, xlsPath.length());
        System.out.println("文件的类型为："+type);

        FileInputStream fileIn = new FileInputStream(xlsPath);
        Workbook wb0 = null;
        //（1）判断文件后缀名是xls，还是xlsx
        //（2）如果是xls，使用HSSFWorkbook；如果是xlsx，使用XSSFWorkbook
        if("xlsx".equals(type)){
            wb0 = new XSSFWorkbook(fileIn);
        }else if("xls".equals(type)){
            //根据指定的文件输入流导入Excel从而产生Workbook对象
            wb0 = new HSSFWorkbook(fileIn);
        }

        //获取Excel文档中的第一个表单
        Sheet sht0 = wb0.getSheetAt(0);
        //看这个Excel表有多少行，如果只有1行或者0行，则认为没有数据要导入
        int excelRowNum = sht0.getPhysicalNumberOfRows();
        if(excelRowNum<=1){
            System.out.println("导入的表格行数少于等于1行，没有数据可以导入！");
            fileIn.close();
            return -4;
        }

        //或者Excel文档的第一个表单的第一行
        Row first = sht0.getRow(0);
        for(int i = 0;i < first.getPhysicalNumberOfCells(); i++) System.out.println(first.getCell(i).getStringCellValue());
        //判断导入的表格的第一行是否和所有的字段的顺序、数量一致，不一致就报错！
        ArrayList<String> allFieldsWebName = getAllFieldsWebName();
        Iterator<String> iterator = allFieldsWebName.iterator();
        int realFieldNum = allFieldsWebName.size();
        int excelFieldNum = first.getPhysicalNumberOfCells();
        System.out.println("导入的表格字段值数量是："+excelFieldNum+"，数据库中实际字段值数量是："+realFieldNum);
        if(excelFieldNum>realFieldNum){
            fileIn.close();
            return -1;
        }else if(excelFieldNum<realFieldNum){
            fileIn.close();
            return -2;
        }

        for(Cell c : first){
            if(iterator.hasNext()) {
                String webName = iterator.next();
                if (!webName.equals(c.toString())) {
                    System.out.println(c.toString()+"=="+webName+":False");
                    fileIn.close();
                    //-3是导入表格的字段值和实际数据库中的字段值不匹配
                    return -3;
                }
            }
        }
        //原本的字段的数量值为：23
        int fieldNum = 23;
        int i = 0;
        int insertNum = 0;
        //对Sheet中的每一行进行迭代
        for (Row r : sht0) {
            //忽略第一行，因为第一行是表格头的字段值，不需要导入
            if(i==0){
                i++;
                continue;
            }
            Employee emp = new Employee(-1,
                    (r.getCell(1) == null)?"无":r.getCell(1).toString(),
                    (r.getCell(2) == null)?"无":r.getCell(2).toString(),
                    (r.getCell(3) == null)?"无":r.getCell(3).toString(),
                    (r.getCell(4) == null)?"无":r.getCell(4).toString(),
                    (r.getCell(5) == null)?"无":r.getCell(5).toString(),
                    (r.getCell(6) == null)?"无":r.getCell(6).toString(),
                    (r.getCell(7) == null)?"无":r.getCell(7).toString(),
                    (r.getCell(8) == null)?"无":r.getCell(8).toString(),
                    (r.getCell(9) == null)?"无":r.getCell(9).toString(),
                    (r.getCell(10) == null)?"无":r.getCell(10).toString(),
                    (r.getCell(11) == null)?"无":r.getCell(11).toString(),
                    (r.getCell(12) == null)?"无":r.getCell(12).toString(),
                    (r.getCell(13) == null)?"无":r.getCell(13).toString(),
                    (r.getCell(14) == null)?"无":r.getCell(14).toString(),
                    (r.getCell(15) == null)?"无":r.getCell(15).toString(),
                    (r.getCell(16) == null)?"无":r.getCell(16).toString(),
                    (r.getCell(17) == null)?"无":r.getCell(17).toString(),
                    (r.getCell(18) == null)?"无":r.getCell(18).toString(),
                    (r.getCell(19) == null)?"无":r.getCell(19).toString(),
                    (r.getCell(20) == null)?"无":r.getCell(20).toString(),
                    (r.getCell(21) == null)?"无":r.getCell(21).toString(),
                    (r.getCell(22) == null)?"无":r.getCell(22).toString()
                    );
            dataInputMapper.addEmployee(emp);
            System.out.println("插入原本字段成功！");
            int newID = emp.getUserid();

            //以下是数据库表里的所有的新增字段
            ArrayList<HashMap<String, Object>> arrayList = dataInputMapper.queryNewFieldName();
            if(arrayList!=null && !arrayList.isEmpty()){
                Iterator<HashMap<String, Object>> iter = arrayList.iterator();

                HashMap<String,String> otherFieldsMap = new HashMap<>();
                int otherI = 1;
                while(iter.hasNext()){
                    HashMap<String, Object> next = iter.next();
                    String fieldname = next.get("fieldname").toString();
                    otherFieldsMap.put(fieldname,r.getCell(22+otherI).toString());
                    otherI++;
                }
                dataInputMapper.addEmployeeOther(newID,otherFieldsMap);
                System.out.println("插入新增字段成功！");
            }
            insertNum++;
        }
        fileIn.close();
        System.out.println("共插入"+insertNum+"条数据！");
        return insertNum;
    }


    /**
     * 可以用来查询得到所有的数据库信息字段，包括原本的和新增的字段,的对应呈现给用户、在web上的名字
     * @return
     */
    public ArrayList<String> getAllFieldsWebName(){

        //每个map中只有两个k v对，key分别是 fieldname和 webname
        ArrayList<HashMap<String, Object>> hashMaps = dataQueryMapper.queryAllFieldName();
        for(int i = 0;i < hashMaps.size();i++) {
            System.out.println(hashMaps.get(i).values());
        }

        //用来存储fieldname和 webname的ArrayList
        ArrayList<String> fieldNameList = new ArrayList<>();
        ArrayList<String> webNameList = new ArrayList<>();

        Iterator<HashMap<String, Object>> iterator = hashMaps.iterator();
        while (iterator.hasNext()){
            HashMap<String, Object> map = iterator.next();
            for(Map.Entry<String,Object> entry : map.entrySet()){
                if(entry.getKey().equals("fieldname")){
                    fieldNameList.add(map.get(entry.getKey()).toString());
                }else if(entry.getKey().equals(("webname"))){
                    webNameList.add(map.get(entry.getKey()).toString());
                }

            }
        }

        return webNameList;

    }


    @GetMapping("/datainput")
    public String dataInputGet(Model model){

        //以下是所有字段的Web名字
        ArrayList<String> allFieldsWebName = getAllFieldsWebName();
        model.addAttribute("allFieldsWebName",allFieldsWebName);

        ArrayList<HashMap<String, Object>> arrayLists = dataQueryMapper.dataQueryByNameNotPage("王");

        ArrayList<ArrayList<String>> arrayListsArranged = null;

        boolean wasSearchAndZero = false;
        if(arrayLists.isEmpty()){
            wasSearchAndZero=true;
        }else{
            arrayListsArranged = dataQueryArrange(supplyAllFieldsMany(arrayLists));
        }

        model.addAttribute("arrayLists",arrayListsArranged);

        return "datainput";
    }

    @PostMapping("/datainput")
    public String dataInputPost(@Param("username")String username, Model model){
        //以下是所有字段的Web名字
        ArrayList<String> allFieldsWebName = getAllFieldsWebName();
        model.addAttribute("allFieldsWebName",allFieldsWebName);

        ArrayList<HashMap<String, Object>> arrayLists = dataQueryMapper.dataQueryByNameNotPage(username);

        ArrayList<ArrayList<String>> arrayListsArranged = null;

        boolean wasSearchAndZero = false;
        if(arrayLists.isEmpty()){
            wasSearchAndZero=true;
        }else{
            arrayListsArranged = dataQueryArrange(supplyAllFieldsMany(arrayLists));
        }
        model.addAttribute("wasSearchAndZero",wasSearchAndZero);
        model.addAttribute("arrayLists",arrayListsArranged);
        model.addAttribute("username",username);

        return "datainput";
    }

    @GetMapping("/datainput/add")
    public String AddGet(Model model){
        //以下是数据库表里的所有的新增字段
        ArrayList<HashMap<String, Object>> arrayList = dataInputMapper.queryNewFieldName();

        ArrayList<kv> kvList = new ArrayList<>();
        Iterator<HashMap<String, Object>> iterator = arrayList.iterator();

        while(iterator.hasNext()){
            HashMap<String, Object> next = iterator.next();
            String fieldname = next.get("fieldname").toString();
            String fieldwebname = next.get("fieldwebname").toString();

            kv one = new kv(fieldname,fieldwebname,"");
            kvList.add(one);
        }

        model.addAttribute("kvList",kvList);

        return "add";
    }

    @PostMapping("/datainput/addData")
    public String AddData(HttpServletRequest request){
        Employee employee = beanEmployee(request);

        dataInputMapper.addEmployee(employee);
        int newID = employee.getUserid();
        System.out.println(newID);
        beanNewField(request, newID);

        return "redirect:/datainput";
    }

    @PostMapping("/datainput/deleteData")
    public String deleteData(@Param("userid") String userid){

        dataInputMapper.deleteEmployee(Integer.parseInt(userid));
        dataInputMapper.deleteNewFieldValue(Integer.parseInt(userid));

        return "redirect:/datainput";
    }


    @GetMapping("/datainput/addfield")
    public String AddField(Model model){

        ArrayList<HashMap<String, Object>> arrayList = dataInputMapper.queryNewFieldName();
        Iterator<HashMap<String, Object>> iterator = arrayList.iterator();

        ArrayList<Field> arrs = new ArrayList<>();
        while(iterator.hasNext()){
            HashMap<String, Object> next = iterator.next();
            String fieldname = next.get("fieldname").toString();
            String fieldwebname = next.get("fieldwebname").toString();
            System.out.println(fieldname+":"+fieldwebname);
            Field field = new Field(fieldname, fieldwebname);
            arrs.add(field);
            System.out.println(field.toString());
        }

        System.out.println(arrs.toString());
        model.addAttribute("arrs",arrs);

        return "addfield";
    }

    class Field{
        private String fieldname;
        private String fieldwebname;

        public Field() {
        }

        public Field(String fieldname, String fieldwebname) {
            this.fieldname = fieldname;
            this.fieldwebname = fieldwebname;
        }

        public String getFieldname() {
            return fieldname;
        }

        public void setFieldname(String fieldname) {
            this.fieldname = fieldname;
        }

        public String getFieldwebname() {
            return fieldwebname;
        }

        public void setFieldwebname(String fieldwebname) {
            this.fieldwebname = fieldwebname;
        }

        @Override
        public String toString() {
            return "Field{" +
                    "fieldname='" + fieldname + '\'' +
                    ", fieldwebname='" + fieldwebname + '\'' +
                    '}';
        }
    }

    @PostMapping("/datainput/updateData")
    public String UpdateData(HttpServletRequest request){
        int userid = Integer.parseInt(request.getParameter("userid"));

        Employee employee = beanEmployee(request);
        employee.setUserid(userid);
        dataInputMapper.updateEmployee(employee);
        beanUpdateNewField(request,userid);

//        dataInputMapper.addEmployee(employee);
//        int newID = employee.getUserid();
//        System.out.println(newID);
//        beanNewField(request, newID);

        return "redirect:/datainput";
    }

    @PostMapping("/datainput/update")
    public String update(@Param("userid") String userid, Model model){
        model.addAttribute("userid",userid);
        ArrayList<HashMap<String, Object>> arrayList = dataQueryMapper.queryEmployeeById(Integer.parseInt(userid));
        HashMap<String, Object>  stringObjectHashMap = arrayList.get(0);

        ArrayList<HashMap<String, String>> hashMaps = dataQueryMapper.QueryOriginalField();
        Iterator<HashMap<String, String>> iterator = hashMaps.iterator();

        while(iterator.hasNext()){
            String key = iterator.next().get("fieldname");
            String value = "";
            if(stringObjectHashMap.get(key)==null || "".equals(stringObjectHashMap.get(key).toString())){
                value = "无";
            }else{
                value = stringObjectHashMap.get(key).toString();
            }

            model.addAttribute(key,value);
        }

        if(stringObjectHashMap.get("nativeplace")==null || "".equals(stringObjectHashMap.get("nativeplace").toString())){

        }else{
            String nativeplace = stringObjectHashMap.get("nativeplace").toString();
            String nativeplace1 = nativeplace.substring(0,2);
            String nativeplace2 = nativeplace.substring(2,nativeplace.length());
            if("内蒙".equals(nativeplace1)){
                nativeplace1 = "内蒙古";
                nativeplace2 = nativeplace.substring(3,nativeplace.length());
            }else if("黑龙".equals(nativeplace1)){
                nativeplace1 = "黑龙江";
                nativeplace2 = nativeplace.substring(3,nativeplace.length());
            }
            model.addAttribute("nativeplace1",nativeplace1);
            model.addAttribute("nativeplace2",nativeplace2);
        }

        if(stringObjectHashMap.get("birthday")==null || "".equals(stringObjectHashMap.get("birthday").toString())){

        }else{
            String birthday = stringObjectHashMap.get("birthday").toString();
            birthday = birthday.replace("年","");
            birthday = birthday.replace("月","");

            int month = Integer.parseInt(birthday.substring(4,birthday.length()));
            String newMonth = "01";
            if(month<=9){
                newMonth = "0"+month;
            }else{
                newMonth = month+"";
            }
            String newBirthday = birthday.substring(0,4)+"-"+newMonth+"-"+"01";
            model.addAttribute("birthday",newBirthday);
        }

        //以下是数据库表里的所有的新增字段
        ArrayList<HashMap<String, Object>> newFieldNameArrayList = dataInputMapper.queryNewFieldName();

        ArrayList<kv> kvList = new ArrayList<>();
        Iterator<HashMap<String, Object>> fieldIterator = newFieldNameArrayList.iterator();

        ArrayList<HashMap<String, Object>> maps = dataQueryMapper.queryOtherFieldsByEmployeeID(Integer.parseInt(userid));
        HashMap<String,String> otherNewFieldNameToValue = new HashMap<>();
        Iterator<HashMap<String, Object>> mapIterator = maps.iterator();
        while(mapIterator.hasNext()){
            HashMap<String, Object> next = mapIterator.next();
            String newfieldname = next.get("newfieldname").toString();
            String fieldvalue = next.get("fieldvalue").toString();
            otherNewFieldNameToValue.put(newfieldname,fieldvalue);
        }

        while(fieldIterator.hasNext()){
            HashMap<String, Object> next = fieldIterator.next();
            String fieldname = next.get("fieldname").toString();
            String fieldwebname = next.get("fieldwebname").toString();
            String s = "";
            if(otherNewFieldNameToValue.get(fieldname)==null || "".equals(otherNewFieldNameToValue.get(fieldname))){
                s = "无";
            }else{
                s = otherNewFieldNameToValue.get(fieldname);
            }


            kv one = new kv(fieldname,fieldwebname,s);
            kvList.add(one);
        }

        model.addAttribute("kvList",kvList);

        return "update";
    }

    void beanNewField(HttpServletRequest request,int userid){
        //以下是数据库表里的所有的新增字段
        ArrayList<HashMap<String, Object>> arrayList = dataInputMapper.queryNewFieldName();
        Iterator<HashMap<String, Object>> iterator = arrayList.iterator();

        HashMap<String,String> otherFieldsMap = new HashMap<>();

        while(iterator.hasNext()){
            HashMap<String, Object> next = iterator.next();
            String fieldname = next.get("fieldname").toString();
            otherFieldsMap.put(fieldname,request.getParameter(fieldname));
        }
        if(!otherFieldsMap.isEmpty()) {
            dataInputMapper.addEmployeeOther(userid,otherFieldsMap);
        }
    }

    void beanUpdateNewField(HttpServletRequest request,int userid){
        //以下是数据库表里的所有的新增字段
        ArrayList<HashMap<String, Object>> arrayList = dataInputMapper.queryNewFieldName();
        Iterator<HashMap<String, Object>> iterator = arrayList.iterator();

        HashMap<String,String> otherFieldsMap = new HashMap<>();

        while(iterator.hasNext()){
            HashMap<String, Object> next = iterator.next();
            String fieldname = next.get("fieldname").toString();
            String fieldvalue = null;
            if(request.getParameter(fieldname)==null || "".equals(request.getParameter(fieldname).toString())){
                fieldvalue = "无";
            }else{
                fieldvalue = request.getParameter(fieldname).toString();
            }
            otherFieldsMap.put(fieldname,fieldvalue);
        }
        dataInputMapper.updateEmployeeOther(userid,otherFieldsMap);

    }


    class kv{
        String fieldName;
        String webName;
        String fieldValue;

        public kv() {
        }

        public kv(String fieldName,String webName,String fieldValue) {
            this.webName = webName;
            this.fieldName = fieldName;
            this.fieldValue = fieldValue;
        }

        public String getFieldName() {
            return fieldName;
        }

        public void setFieldName(String fieldName) {
            this.fieldName = fieldName;
        }

        public String getWebName() {
            return webName;
        }

        public void setWebName(String webName) {
            this.webName = webName;
        }

        public String getFieldValue() {
            return fieldValue;
        }

        public void setFieldValue(String fieldValue) {
            this.fieldValue = fieldValue;
        }
    }

    private Employee beanEmployee(HttpServletRequest request){
        Employee emp = new Employee();
        System.out.println(request.getParameter("name"));
        emp.setUserid(-1);
        emp.setName(request.getParameter("name")!=null&&!"".equals(request.getParameter("name"))?request.getParameter("name"):"无");
        emp.setSex(request.getParameter("sex")!=null&&!"".equals(request.getParameter("sex"))?request.getParameter("sex"):"无");
        emp.setNation(request.getParameter("nation")!=null&&!"".equals(request.getParameter("nation"))?request.getParameter("nation"):"无");
        emp.setPoliticsstatus(request.getParameter("politicsstatus")!=null&&!"".equals(request.getParameter("politicsstatus"))?request.getParameter("politicsstatus"):"无");
        emp.setBirthday(request.getParameter("birthday")!=null&&!"".equals(request.getParameter("birthday"))?request.getParameter("birthday"):"无");

        String nativeplace1 = request.getParameter("nativeplace1") != null && !"".equals(request.getParameter("nativeplace1")) ? request.getParameter("nativeplace1") : "无";
        String nativeplace2 = request.getParameter("nativeplace2") == null?"":request.getParameter("nativeplace2");

        emp.setNativeplace(nativeplace1+nativeplace2);
        emp.setIdnumber(request.getParameter("idnumber")!=null&&!"".equals(request.getParameter("idnumber"))?request.getParameter("idnumber"):"无");
        emp.setAccademiccareer(request.getParameter("accademiccareer")!=null&&!"".equals(request.getParameter("accademiccareer"))?request.getParameter("accademiccareer"):"无");
        emp.setAccademicdegree(request.getParameter("accademicdegree")!=null&&!"".equals(request.getParameter("accademicdegree"))?request.getParameter("accademicdegree"):"无");
        emp.setStationandposition(request.getParameter("stationandposition")!=null&&!"".equals(request.getParameter("stationandposition"))?request.getParameter("stationandposition"):"无");
        emp.setProfession(request.getParameter("profession")!=null&&!"".equals(request.getParameter("profession"))?request.getParameter("profession"):"无");
        emp.setTitle(request.getParameter("title")!=null&&!"".equals(request.getParameter("title"))?request.getParameter("title"):"无");
        emp.setJobgrade(request.getParameter("jobgrade")!=null&&!"".equals(request.getParameter("jobgrade"))?request.getParameter("jobgrade"):"无");
        emp.setMajorsocialposts(request.getParameter("majorsocialposts")!=null&&!"".equals(request.getParameter("majorsocialposts"))?request.getParameter("majorsocialposts"):"无");
        emp.setImportantawardsandhonors(request.getParameter("importantawardsandhonors")!=null&&!"".equals(request.getParameter("importantawardsandhonors"))?request.getParameter("importantawardsandhonors"):"无");
        emp.setTelephone(request.getParameter("telephone")!=null&&!"".equals(request.getParameter("telephone"))?request.getParameter("telephone"):"无");
        emp.setSector(request.getParameter("sector")!=null&&!"".equals(request.getParameter("sector"))?request.getParameter("sector"):"无");
        emp.setYouthposition(request.getParameter("youthposition")!=null&&!"".equals(request.getParameter("youthposition"))?request.getParameter("youthposition"):"无");
        emp.setNominatedunit(request.getParameter("nominatedunit")!=null&&!"".equals(request.getParameter("nominatedunit"))?request.getParameter("nominatedunit"):"无");
        emp.setSsstimeandjob(request.getParameter("ssstimeandjob")!=null&&!"".equals(request.getParameter("ssstimeandjob"))?request.getParameter("ssstimeandjob"):"无");
        emp.setSssccpjob(request.getParameter("sssccpjob")!=null&&!"".equals(request.getParameter("sssccpjob"))?request.getParameter("sssccpjob"):"无");
        emp.setSsscomments(request.getParameter("ssscomments")!=null&&!"".equals(request.getParameter("ssscomments"))?request.getParameter("ssscomments"):"无");

        String birthday = emp.getBirthday();
        String newBirthday = birthday.substring(0,4)+"年"+birthday.substring(5,7)+"月";
        emp.setBirthday(newBirthday);

        return emp;
    }
}
