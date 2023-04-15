package com.chatapp.services;

import com.chatapp.entities.Chat;
import com.chatapp.repositories.ChatRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class ChatService {
    private final ChatRepository chatRepository;

    public ChatService(ChatRepository chatRepository){
        this.chatRepository = chatRepository;
    }
    public ArrayList<Chat> getAllChats() {
        return (ArrayList<Chat>) this.chatRepository.findAll();
    }

    public void creatChat(Chat chat) throws Exception {
        this.chatRepository.save(chat);
    }

}
