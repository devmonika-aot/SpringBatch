package com.springbatch.SpringBatchtutorial.reader;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.springbatch.SpringBatchtutorial.model.Employee;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

@Component
public class JSONReader implements ItemReader<Employee> {
  ObjectMapper mapper = new ObjectMapper();
  Employee[] employee;
  private int i = 0;

  @BeforeStep
  public void beforeStep() throws IOException {
    // BufferedReader br = new BufferedReader(new ClassPathResource(""));
    // File file = new ClassPathResource("classpath:Employee.json").getFile();

    // mapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
    /*  ***********NOTE***************
    If we look at the json file we have address like below
      address": {
       "streetAddress": "21 2nd Street",
       "city": "New York",
       "state": "NY",
      "postalCode": 10021
     }
     Ideally List value It should be close with squre brackets. [{},{}]
     If we can't edit the json file i.e can't put this inside squre bracket
     then
          mapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
     will work for us.

     */
    File file = ResourceUtils.getFile("classpath:Employee.json");
    employee = mapper.readValue(file, Employee[].class);

  }

  @Override
  public Employee read()
      throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
    System.out.println("READ" + employee.length);
    /*
    Here we have json like [{},{}] where each curly bracket represent Employee data
    and coz of this, where are mapping to array of employee[] in the ObjectMapper().
    --------Special Note ----
    If we had data like {} i.e only json then we would have mapped to Employee object
    Employee employee = mapper.readValue(file, Employee.class);
    and we would have
    return from read() method itself as read() should have Employee return type( should not be an array).
     */
    if (i > employee.length - 1 ) {
      return null;
    }
    return employee[i++];
  }
}
