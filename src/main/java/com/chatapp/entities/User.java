package com.chatapp.entities;

//all from the same package!
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

// Lombok, it generates all methods getters and setters, constructors automatically, using these annotations
@NoArgsConstructor // for empty constructor
@AllArgsConstructor // for constructor
@Data // for getters and setters
@Entity // connecting fields with database
public class User { // all data from fields, represents data in table
    @Id //if mark class as entity you should provide annotation to id, without it doesn't work, it also is primary key
    @GeneratedValue(strategy = GenerationType.AUTO) //generate value and specify how counting should happen using strategy
    private Long id;
    @Column(unique = true)
    private String userName;
    private String password;
    private String confirmPassword;
    private String newPassword;
    private String fullName;
    @Column(unique = true)
    private String email;
    private String location;
    private String chatColor;
    private String dateOfBirth;
    private String profilePictureUrl;
    @CreationTimestamp
    private Timestamp createdAt;
    @UpdateTimestamp
    private Timestamp updatedAt;
}