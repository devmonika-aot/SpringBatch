package com.springbatch.SpringBatchtutorial.writer;

import com.springbatch.SpringBatchtutorial.model.CustomerLargeDataSet;
import lombok.SneakyThrows;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemWriter;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class ExcelItemWriter implements ItemWriter<CustomerLargeDataSet>, StepExecutionListener {

  String fileName;
  FileOutputStream out;
  Workbook workbook;
  Sheet sheet;
  String headers[];
  int rowIndex = 1;

  public ExcelItemWriter(String fname) throws FileNotFoundException {
    this.fileName = fname;
  }

  @Override
  public void beforeStep(StepExecution stepExecution) {
    try {
      out = new FileOutputStream("users"+System.currentTimeMillis()+".xlsx");
      headers =
          new String[] {
            "ID", "Occupation", "Name", "CustId", "Salary", "State", "Designation", "Efficiency"
          };

      workbook = WorkbookFactory.create(true);
      sheet = workbook.createSheet("cust");
      Row headerRow = sheet.createRow(0);
      for (int i = 0; i < headers.length; i++) {
        headerRow.createCell(i).setCellValue(headers[i]);
      }

    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public ExitStatus afterStep(StepExecution stepExecution) {
     rowIndex = 1;
    try {
      workbook.write(out);
      workbook.close();
      out.close();
    } catch (IOException e) {
      e.printStackTrace();
    }

    return null;
  }

  @Override
  public void write(List<? extends CustomerLargeDataSet> list) throws Exception {

    for (CustomerLargeDataSet user : list) {
      Row row = sheet.createRow(rowIndex++);
      row.createCell(0).setCellValue(user.getId());
      row.createCell(1).setCellValue(user.getOccupation());
      row.createCell(2).setCellValue(user.getName());
      row.createCell(3).setCellValue(user.getCustId());
      row.createCell(4).setCellValue(user.getSalary());
      row.createCell(5).setCellValue(user.getState());
      row.createCell(6).setCellValue(user.getDesignation());
      row.createCell(7).setCellValue(user.getEfficiency());
    }

  }
}
