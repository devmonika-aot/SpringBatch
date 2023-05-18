package com.springbatch.SpringBatchtutorial.reader;

import com.springbatch.SpringBatchtutorial.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.*;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Component
public class UserReader implements ItemReader<User> , ItemStream{

  private long counter = 0L;
  private boolean endOfResultSet = false;
  FlatFileItemReader<User> itemReader = new FlatFileItemReader<>();
  Logger log = LoggerFactory.getLogger(UserReader.class);

  // @BeforeStep Annotation is coming from StepExecutionListner.
  // so if we implement then we have to overrride both method
  // 1)beforeStep() and 2) afterStep()
  // where as by using annotation we can use only needy one.
  @BeforeStep
  public void beforeStep() {

    // flatFileItemReader.setResource(new
    // FileSystemResource("D://desktop/user.csv"));
    // URLResiurce(url link)
    itemReader.setResource(new ClassPathResource("users.csv"));
    itemReader.setName("UserItemReader");
    itemReader.setLinesToSkip(1);
    itemReader.setLineMapper(lineMapper());
    log.info("{1} has been read successfully" + itemReader.getName());
  }

  @Override
  public User read()
      throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {

    if (this.endOfResultSet) {
      return null;
    }
    User user = this.itemReader.read();
    if (user == null) {
      endOfResultSet = true;
    } else {
      counter++;
    }
    return user;
  }

  private LineMapper<User> lineMapper() {
    DefaultLineMapper<User> defaultLineMapper = new DefaultLineMapper<>();
    DelimitedLineTokenizer delimitedLineTokenizer = new DelimitedLineTokenizer();
    delimitedLineTokenizer.setDelimiter(",");
    delimitedLineTokenizer.setStrict(false);
    delimitedLineTokenizer.setNames("id", "name", "dept", "salary");

    BeanWrapperFieldSetMapper<User> beanWrapperFieldSetMapper = new BeanWrapperFieldSetMapper<>();
    beanWrapperFieldSetMapper.setTargetType(User.class);
    defaultLineMapper.setLineTokenizer(delimitedLineTokenizer);
    defaultLineMapper.setFieldSetMapper(beanWrapperFieldSetMapper);

    return defaultLineMapper;
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
    log.info("total number of line read " + counter);
    this.itemReader.close();
  }
}
