package com.springbatch.SpringBatchtutorial.writer;

import com.springbatch.SpringBatchtutorial.model.User;
import com.springbatch.SpringBatchtutorial.repository.UserRepository;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserWriter implements ItemWriter<User> {

  @Autowired private UserRepository userRepository;

  private FlatFileItemWriter<User> writer = new FlatFileItemWriter();

  @Override
  public void write(List<? extends User> list) throws Exception {
    System.out.println("list of users " + list);
   userRepository.saveAll(list);
  }
}
