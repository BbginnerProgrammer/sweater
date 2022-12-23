package com.example.sweater.repo;

import com.example.sweater.domain.Message;
import com.example.sweater.domain.User;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface MessageRepo extends CrudRepository<Message, Long> {

    List<Message> findByTag(String filter);

    ArrayList<Message> findAllByAuthor(User author);
}
