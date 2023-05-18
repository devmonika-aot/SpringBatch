package com.springbatch.SpringBatchtutorial.processor;

import com.springbatch.SpringBatchtutorial.model.Employee;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.stream.Collectors;

@Component
public class JsonProcessor implements ItemProcessor<Employee, Employee> {
  @Override
  public Employee process(Employee employee) throws Exception {

    Arrays.asList(employee.getPhoneNumbers()).stream()
        .forEach(
            phNum -> {
              if (phNum.getType().equalsIgnoreCase("home")) {
                employee.setMobileNumber(phNum.getNumber());
              }
              if (phNum.getType().equalsIgnoreCase("fax")) {
                employee.setFaxNumer(phNum.getNumber());
              }
            });
    return employee;
  }
}
