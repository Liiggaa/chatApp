package com.chatapp.controllers;

import com.chatapp.entities.Chat;
import com.chatapp.repositories.ChatRepository;
import com.chatapp.services.ChatService;
import com.chatapp.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ChatController {
    private final ChatService chatService;
    private final UserService userService;

    public ChatController(UserService userService, ChatService chatService) {
        this.userService = userService;
        this.chatService = chatService;
    }

    @GetMapping("group-chat/{userId}")
    public String displayChatRoom(@PathVariable Long userId, Model model,
                                  @CookieValue(value = "userId") String userIdFromCookie) {
        try {
            model.addAttribute("user", userService.findUserById(Long.valueOf(userIdFromCookie)));
            model.addAttribute("chats", chatService.getAllChats()); // with model make the data available for rendering in the view
            return "groupChat";
        } catch (Exception e) {
            return "redirect:/login?message=user_not_found";
        }
    }

    @PostMapping("group-chat/{userId}")
    public String sendChat(@PathVariable Long userId, Chat chat) {
        try {
            chatService.creatChat(chat);
            return "redirect:/group-chat/" + userId;
        } catch (Exception e) {
            return "redirect:/group-chat/" + userId + "?message=send_chat_failed";
        }
    }
}