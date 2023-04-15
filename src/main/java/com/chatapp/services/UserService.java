package com.chatapp.services;


import com.chatapp.entities.User;
import com.chatapp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository; // use user repository

    @Autowired // injects an instance of a dependency into a class, allows the class to use that dependency's methods
    public UserService(UserRepository userRepository) { // expect userRepository
        this.userRepository = userRepository;
    }


    public void createUser(User user) throws Exception {
        if (!user.getPassword().equals(user.getConfirmPassword())) {
            throw new Exception("Passwords do not match.");
        }

        // Check if user with the same username already exists
        User existingUserByUsername = userRepository.findByUserName(user.getUserName());
        if (existingUserByUsername != null) {
            throw new Exception("User already exists with this username.");
        }

        // Check if user with the same email already exists
        User existingUserByEmail = userRepository.findByEmail(user.getEmail());
        if (existingUserByEmail != null) {
            throw new Exception("User already exists with this email.");
        }

        // Save the new user
        userRepository.save(user);
    }



    // method for user checking
    public User verifyUser(User userLoginRequest) throws Exception {
        User foundUser = this.userRepository.findByEmailAndPassword(userLoginRequest.getEmail(), userLoginRequest.getPassword()); // Spring will generate query for us
        if (foundUser == null) {
            throw new Exception("Username or password incorrect");
        } // check if user exist, Spring find this info according to method

        return foundUser;
        // return user if find it
        // in such cases where we verify user we don't need to return all info, in most case it is enough with user id
    }

    public User findUserById(Long userId) throws Exception {
        return this.userRepository.findById(userId).orElseThrow(); // if you don't find, throw exception
    }


    public User findAllUsersById(Long userId) {
        return this.userRepository.findAllById(userId);
    }


    public void updateUser(Long id, User updatedUser) throws Exception {
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User with ID " + id + " not found."));

        // Check if user with the same username already exists
        User existingUserByUsername = userRepository.findByUserName(updatedUser.getUserName());
        if (existingUserByUsername != null && !existingUserByUsername.getId().equals(user.getId())) {
            throw new Exception("User already exists with this username!");
        }

        // Check if user with the same email already exists
        User existingUserByEmail = userRepository.findByEmail(updatedUser.getEmail());
        if (existingUserByEmail != null && !existingUserByEmail.getId().equals(user.getId())) {
            throw new Exception("User already exists with this email!");
        }

        user.setFullName(updatedUser.getFullName());
        user.setUserName(updatedUser.getUserName());
        user.setDateOfBirth(updatedUser.getDateOfBirth());
        user.setLocation(updatedUser.getLocation());
        user.setEmail(updatedUser.getEmail());
        user.setPassword(user.getPassword());
        user.setConfirmPassword(user.getConfirmPassword());
        user.setProfilePictureUrl(updatedUser.getProfilePictureUrl());

        userRepository.save(user);
    }


    public void updateUserPasswordById(Long id, User updatedUser) throws Exception {
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User with ID " + id + " not found"));
        if (user == null) {
            return;
        }

        String currentPassword = updatedUser.getPassword(); // get current password from input field
        String storedPassword = user.getPassword(); // get password from repository

        if (!currentPassword.equals(storedPassword)) {
            System.out.println("CHECK: currentPassword = " + currentPassword);
            System.out.println("CHECK: storedPassword = " + storedPassword);

            throw new Exception("Wrong current password.");
        }


        if (!updatedUser.getNewPassword().equals(updatedUser.getConfirmPassword())) {
            System.out.println("CHECK:" + updatedUser.getNewPassword());
            System.out.println("CHECK:" + updatedUser.getConfirmPassword());
            throw new Exception("New password and confirm password do not match.");
        }
        user.setPassword(updatedUser.getNewPassword());
        user.setConfirmPassword(updatedUser.getConfirmPassword());
        userRepository.save(user);

    }
}