package com.springbatch.SpringBatchtutorial.batchconfig;

import com.springbatch.SpringBatchtutorial.model.CustomerLargeDataSet;
import com.springbatch.SpringBatchtutorial.model.Employee;
import com.springbatch.SpringBatchtutorial.model.User;
import com.springbatch.SpringBatchtutorial.repository.UserRepository;
import com.springbatch.SpringBatchtutorial.writer.ExcelItemWriter;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileHeaderCallback;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

  @Autowired UserRepository repository;
  @Autowired StepBuilderFactory stepBuilderFactory;
  @Autowired JobBuilderFactory jobBuilderFactory;
  @Autowired ItemProcessor<User, User> itemProcessor;
  @Autowired ItemReader<User> itemReader;
  @Autowired ItemWriter<User> itemWriter;
  @Autowired ItemReader<Employee> employeeItemReader;
  @Autowired ItemProcessor<Employee, Employee> employeeItemProcessor;
  @Autowired ItemReader<CustomerLargeDataSet> customerItemReader;

  private static final int THREAD_COUNT = 10;

  @Bean
  public Step stepToLoadCSVtoDB() throws Exception {
    return stepBuilderFactory
        .get("LOAD-CSV-TO-MYSQL")
        .<User, User>chunk(2)
        .reader(itemReader)
        .processor(itemProcessor)
        .writer(itemWriter)
        .build();
  }

  @Bean
  public Step jsonToXLXSStep() {
    return stepBuilderFactory
        .get("JSON-TO-XLXS-CONVERT-STEP")
        .<Employee, Employee>chunk(2)
        .reader(employeeItemReader)
        .processor(employeeItemProcessor)
        .writer(writerCSV())
        .taskExecutor(taskExecutor())
        .throttleLimit(8)
        .build();
  }

  @Bean
  public TaskExecutor taskExecutor() {
    ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
    taskExecutor.setCorePoolSize(THREAD_COUNT);
    taskExecutor.setMaxPoolSize(THREAD_COUNT);
    taskExecutor.setThreadNamePrefix("myThread-");
    taskExecutor.initialize();
    return taskExecutor;
  }

  @Bean
  public Step readCustomerLargeDataSet() throws IOException {
    return stepBuilderFactory
        .get("MULTIPLE-THREAD-READER-STEP")
        .<CustomerLargeDataSet, CustomerLargeDataSet>chunk(200)
        .reader(customerItemReader)
        .writer(writertoExcel())
        .build();
  }

  @Bean
  public Job job() throws Exception {
    return jobBuilderFactory
        .get("ETL-LOAD-JOB")
        .incrementer(new RunIdIncrementer())
        .start(stepToLoadCSVtoDB())
        .next(jsonToXLXSStep())
        .next(readCustomerLargeDataSet())
        .build();
  }

  @Bean
  public FlatFileItemWriter<Employee> writerCSV() {

    // Create writer instance
    FlatFileItemWriter<Employee> writer = new FlatFileItemWriter<>();

    // Set output file location
    writer.setResource(
        new FileSystemResource("output/outputData" + System.currentTimeMillis() + ".csv"));

    // All job repetitions should "append" to same output file
    writer.setAppendAllowed(true);

    writer.setHeaderCallback(
        new FlatFileHeaderCallback() {
          @Override
          public void writeHeader(Writer writer) throws IOException {
            writer.write("FirstName,LastName,Age,City,State,MobileNumber,FaxNumber");
          }
        });

    // Name field values sequence based on object properties
    writer.setLineAggregator(
        new DelimitedLineAggregator<Employee>() {
          {
            setDelimiter(",");
            setFieldExtractor(
                new BeanWrapperFieldExtractor<Employee>() {
                  {
                    setNames(
                        new String[] {
                          "firstName",
                          "lastName",
                          "age",
                          "address.city",
                          "address.state",
                          "mobileNumber",
                          "faxNumer"
                        });
                  }
                });
          }
        });
    return writer;
  }

  @Bean
  public ExcelItemWriter writertoExcel() throws IOException {

    return new ExcelItemWriter("users" + System.currentTimeMillis() + ".xlsx");
  }
}
