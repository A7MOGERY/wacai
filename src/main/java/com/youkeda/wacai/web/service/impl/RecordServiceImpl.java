package com.youkeda.wacai.web.service.impl;

import com.youkeda.wacai.web.model.AccountingRecord;
import com.youkeda.wacai.web.service.RecordService;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RecordServiceImpl implements RecordService {
    @Override
    public void record(AccountingRecord record) {
        File file = new File("./record.xlsx");
        Workbook wb = null;
        Sheet sheet =null;
        if (file.exists()){
            try {
                wb = new XSSFWorkbook(file);
                sheet =  wb.getSheetAt(0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            wb = new XSSFWorkbook();
            sheet =wb.createSheet();
        }
        try {
            int rows = sheet.getPhysicalNumberOfRows();
            Row row = sheet.createRow(rows);
            //把记账时间记录在第一列上
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String time = simpleDateFormat.format(record.getTime());
            row.createCell(0).setCellValue(time);
            //把类型记录在第三列上
            row.createCell(1).setCellValue(record.getType());
            //把科目记录在第三列上
            row.createCell(2).setCellValue(record.getCategory());
            //把发生时间记录在第四列上
            row.createCell(3).setCellValue(record.getCreatTime());
            //把金额记录在第五列
            row.createCell(4).setCellValue(record.getAmount());
            //创建临时文件
            File newfile = new File(file+".bak");
            OutputStream out = new FileOutputStream(newfile);
            wb.write(out);
            wb.close();
            out.close();
            //删除老文件
            file.deleteOnExit();
            //把新的文件更名为老的文件名
            newfile.renameTo(file);
        }catch (IOException e){
            e.printStackTrace();
        }

    }

    @Override
    public List<AccountingRecord> query() {
        File file = new File("./record.xlsx");
        List<AccountingRecord> records = new ArrayList<>();
        //如果文件不存在直接返回数据
        if (!file.exists()){
            return records;
        }
        try {
            Workbook wb = new XSSFWorkbook(file);
            Sheet sheet = wb.getSheetAt(0);
            int rows = sheet.getPhysicalNumberOfRows();
            for (int i = 0;i <rows;i++){
                Row row = sheet.getRow(i);
                AccountingRecord record = new AccountingRecord();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date time = simpleDateFormat.parse(row.getCell(0).getStringCellValue());
                record.setTime(time);
                record.setType(row.getCell(1).getStringCellValue());
                record.setCategory(row.getCell(2).getStringCellValue());
                record.setCreatTime(row.getCell(3).getStringCellValue());
                record.setAmount((int)row.getCell(4).getNumericCellValue());
                records.add(record);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        }catch (ParseException e){
            e.printStackTrace();
        }
        return records;
    }
}
