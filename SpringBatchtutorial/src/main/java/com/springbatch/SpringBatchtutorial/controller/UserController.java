package com.springbatch.SpringBatchtutorial.controller;

import com.springbatch.SpringBatchtutorial.model.User;
import com.springbatch.SpringBatchtutorial.repository.UserRepository;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1")
public class UserController {

  @Autowired JobLauncher jobLauncher;
  @Autowired Job job;
  @Autowired UserRepository userDao;

  @GetMapping("/load")
  public BatchStatus loadData()
      throws JobExecutionAlreadyRunningException, JobRestartException,
          JobInstanceAlreadyCompleteException, JobParametersInvalidException {

    JobParameters parameters =
        new JobParametersBuilder().addLong("startAt", System.currentTimeMillis()).toJobParameters();
    JobExecution jobExecution = jobLauncher.run(job, parameters);

    System.out.println("JobExecution: " + jobExecution.getStatus());

    System.out.println("Batch is Running...");
    while (jobExecution.isRunning()) {
      System.out.println("...");
    }

    return jobExecution.getStatus();
  }

  // ******CHECKING DATABASE CONNECTIVITY************
  /*


  @PostMapping("/saveuser")
  public User saveUser(@RequestBody User user) {

    // user.setTime(LocalDate.now());
    return userDao.save(user);
  }

  @GetMapping("/getuser")
  public List<User> saveUser(@RequestBody User user) {

    // user.setTime(LocalDate.now());
    return userDao.findAll();
  }
  */

}
