package com.springbatch.SpringBatchtutorial.processor;

import com.springbatch.SpringBatchtutorial.model.User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class UserProcessor implements ItemProcessor<User, User>, StepExecutionListener {

  Logger log = LoggerFactory.getLogger(UserProcessor.class);

  @Override
  public User process(User item) throws Exception {

    /*
     * We can add processing logic here
     *
     */
    item.setTime(LocalDate.now().minusDays(1));
    return item;
  }

  /*
  Similarly we have JobExecutionListner, ChunkExecutionListner interfaces where we have two method
  beforeJob/beforeChunk
  afterJob/afteChunk , where we can perform initialization / clean up activities.
  It also provide job/step realted data like when job started ,ended etc
   */
  @Override
  public void beforeStep(StepExecution stepExecution) {
    log.info(
        "beforestep from STEPEXECUTIONLISTNER stepExecution.getJobExecution() "
            + stepExecution.getJobExecution());
    log.info(
        "beforestep from STEPEXECUTIONLISTNER stepExecution.getJobExecution().getExecutionContext() "
            + stepExecution.getJobExecution().getExecutionContext());
    log.info(
        "beforestep from STEPEXECUTIONLISTNER stepExecution.getJobExecution() "
            + stepExecution.getExecutionContext());
  }

  @Override
  public ExitStatus afterStep(StepExecution stepExecution) {
    log.info(
        "afterStep from STEPEXECUTIONLISTNER stepExecution.getJobExecution() "
            + stepExecution.getJobExecution());
    log.info(
        "afterStep from STEPEXECUTIONLISTNER stepExecution.getJobExecution().getExecutionContext() "
            + stepExecution.getJobExecution().getExecutionContext());
    log.info(
        "afterStep from STEPEXECUTIONLISTNER stepExecution.getJobExecution() "
            + stepExecution.getExecutionContext());
    return null;
  }
}
