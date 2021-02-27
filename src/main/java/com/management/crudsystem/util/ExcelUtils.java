package com.management.crudsystem.util;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

public class ExcelUtils {
    static final short borderpx = 1;


    /**
     * 导出excel表格
     * @param head
     * @param body
     * @return
     */
    public static HSSFWorkbook expExcel(ArrayList<String> head, ArrayList<ArrayList<String>> body){

        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Sheet1");
        HSSFRow row = sheet.createRow(0);
        HSSFCell cell = null;

        HSSFCellStyle cellStyle = workbook.createCellStyle();
        setBorderStyle(cellStyle,borderpx);
        cellStyle.setFont(setFontStyle(workbook,"黑体",(short)14));
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        sheet.createFreezePane(0,1,0,1);

        Iterator<String> iterator = head.iterator();
        for(int i=0;iterator.hasNext();i++){
            String s = iterator.next();
            cell = row.createCell(i);
            cell.setCellValue(s);
            cell.setCellStyle(cellStyle);

        }
//        for (int i = 0; i < head.size(); i++) {
//            cell= row.createCell(i);
//            cell.setCellValue(head.get(i));
//            cell.setCellStyle(cellStyle);
//        }

        HSSFCellStyle cellStyle2 = workbook.createCellStyle();
        setBorderStyle(cellStyle2,borderpx);
        cellStyle2.setFont(setFontStyle(workbook,"宋体",(short)12));
        cellStyle2.setAlignment(HorizontalAlignment.CENTER);

        Iterator<ArrayList<String>> arrayListIterator = body.iterator();
        for(int i=0;arrayListIterator.hasNext();i++){
            row = sheet.createRow(i+1);
            ArrayList<String> strings = arrayListIterator.next();
            Iterator<String>  strIter = strings.iterator();
            for(int j=0; strIter.hasNext();j++){
                String s = strIter.next();
                cell = row.createCell(j);
                cell.setCellValue(s);
                cell.setCellStyle(cellStyle2);
            }
        }


//        for (int i = 0; i < body.size(); i++) {
//            row = sheet.createRow(i+1);
//            ArrayList<String> paramList = body.get(i);
//            for (int j = 0; j < paramList.size(); j++) {
//                cell = row.createCell(j);
//                cell.setCellValue(paramList.get(i));
//                cell.setCellStyle(cellStyle2);
//            }
//        }

        for (int i = 0, isize = head.size(); i < isize; i++) {
            sheet.autoSizeColumn(i);
        }
        return workbook;
    }

    public static void outFile(HSSFWorkbook workbook, String path, HttpServletResponse response){
        SimpleDateFormat fdate = new SimpleDateFormat("yyyyMMdd");
        path = path.substring(0,path.lastIndexOf('.'))+fdate.format(new Date())+path.substring(path.lastIndexOf('.'));

        OutputStream os =null;
        File file = null;
        try {

            file = new File(path);
            String filename = file.getName();
            os = new FileOutputStream(file);

            response.addHeader("Content-Disposition","attachment;filename="+ URLEncoder.encode(filename,"UTF-8"));
            os= new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            workbook.write(os);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            os.flush();
            if(os!=null){
                os.close();
            }
            System.gc();
            System.out.println(file.delete());

        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    /**
     * 设置字体样式
     * @param workbook 工作簿
     * @param name 字体名字/类型
     * @param height 字体大小
     * @return HSSFFont
     */
    private static HSSFFont setFontStyle(HSSFWorkbook workbook, String name, short height){
        HSSFFont font = workbook.createFont();
        font.setFontHeightInPoints(height);
        font.setFontName(name);
        return font;
    }

    /**
     * 设置单元格的边框
     * @param cellStyle
     * @param border
     */
    private static void setBorderStyle(HSSFCellStyle cellStyle, short border){
        cellStyle.setBorderBottom(BorderStyle.values()[border]);
        cellStyle.setBorderLeft(BorderStyle.values()[border]);
        cellStyle.setBorderTop(BorderStyle.values()[border]);
        cellStyle.setBorderTop(BorderStyle.values()[border]);
    }
}
