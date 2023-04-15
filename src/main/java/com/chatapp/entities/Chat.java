package com.chatapp.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class Chat {
    @Id
    @GeneratedValue private Long id;
    @Column(columnDefinition = "TEXT")
    String message;
    @ManyToOne
    @PrimaryKeyJoinColumn(name="USERID", referencedColumnName="id")
    private User user;
    @CreationTimestamp
    Timestamp createdAt;
}