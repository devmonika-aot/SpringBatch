package com.springbatch.SpringBatchtutorial.reader;

import com.springbatch.SpringBatchtutorial.model.CustomerLargeDataSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemStream;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Component
public class CustomerReader implements ItemReader<CustomerLargeDataSet>, ItemStream {

  private long counter = 0L;
  private boolean endOfResultSet = false;
  FlatFileItemReader<CustomerLargeDataSet> itemReader = new FlatFileItemReader<>();
  Logger log = LoggerFactory.getLogger(CustomerReader.class);

  @BeforeStep
  public void beforeStep() {
    itemReader.setResource(new ClassPathResource("Customer.csv"));
    itemReader.setName("CustomerItemReader");
    // itemReader.setLinesToSkip(1);
    itemReader.setLineMapper(lineMapper());
    log.info("{1} has been read successfully" + itemReader.getName());
  }

  private LineMapper<CustomerLargeDataSet> lineMapper() {
    DefaultLineMapper<CustomerLargeDataSet> defaultLineMapper = new DefaultLineMapper<>();
    DelimitedLineTokenizer delimitedLineTokenizer = new DelimitedLineTokenizer();
    delimitedLineTokenizer.setDelimiter(",");
    delimitedLineTokenizer.setStrict(false);
    delimitedLineTokenizer.setNames(
        "id", "occupation", "name", "custId", "salary", "state", "designation", "efficiency");

    BeanWrapperFieldSetMapper<CustomerLargeDataSet> beanWrapperFieldSetMapper =
        new BeanWrapperFieldSetMapper<>();
    beanWrapperFieldSetMapper.setTargetType(CustomerLargeDataSet.class);
    defaultLineMapper.setLineTokenizer(delimitedLineTokenizer);
    defaultLineMapper.setFieldSetMapper(beanWrapperFieldSetMapper);

    return defaultLineMapper;
  }

  @Override
  public CustomerLargeDataSet read()
      throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {

    if (this.endOfResultSet) {
      return null;
    }
    CustomerLargeDataSet user = this.itemReader.read();
    if (user == null) {
      endOfResultSet = true;
    } else {
      counter++;
    }
    return user;
  }

  @Override
  public void open(ExecutionContext executionContext) throws ItemStreamException {
    this.itemReader.open(executionContext);
  }

  @Override
  public void update(ExecutionContext executionContext) throws ItemStreamException {
    this.itemReader.update(executionContext);
  }

  @Override
  public void close() throws ItemStreamException {
    log.info("total line read "+ counter);
    this.itemReader.close();
  }
}
