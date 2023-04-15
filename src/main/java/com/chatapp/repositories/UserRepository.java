package com.chatapp.repositories;

import com.chatapp.entities.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface UserRepository extends CrudRepository<User,Long>  { // extend CRUD repository, specify entity, which table(entity), for each entity own repository <User, user id type>
    // connect user class with repository, repository always interface
    // entity - class(table)
    // have all CRUD functionalities create, select, update, delete
    // without any  methods there are already CRUD functionality
    // we can override the functionality and add new functionality

    User findByEmailAndPassword(String email, String password); // this is actual functionality to find user, specify function and use
    // generate SQL statement automatically
    // Spring is taking function and is creating select statement as soon extend CrudRepository

    User findByUserName(String username);

    User findByEmail(String email);

    @Override
    ArrayList<User> findAll();  // override function, to change behaviour, for example to change data type

    User findAllById (Long userId);
}
