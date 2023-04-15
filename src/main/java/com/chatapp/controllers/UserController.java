package com.chatapp.controllers;

import com.chatapp.entities.User;
import com.chatapp.services.UserService;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    } // constructor for service

    @GetMapping("/register")
    public String showRegistrationPage() {
        return "register";
    }

    @PostMapping("/register")
    public String handleUserRegistration(User user, Model model) {
        try {
            this.userService.createUser(user);
        } catch (Exception e) {
            model.addAttribute("message", "signup_failed");
            System.out.println(e);
            model.addAttribute("error", e.getMessage());
            model.addAttribute("user", user);
            return "register";
        }

        return "redirect:login?message=signup_success";
    }

    @GetMapping("/login")
    public String showLoginPage(
            @RequestParam(name = "message", required = false) String message,
            Model model
    ) {
        model.addAttribute("message", message);
        return "login";
    }

    // collect info from login page and use that for verifying user
    @PostMapping("/login")
    public String handleUserLogin(User user, HttpServletResponse response) {
        try {
            User loggedInUser = userService.verifyUser(user);
            Cookie cookie = new Cookie("userId", loggedInUser.getId().toString());

            response.addCookie(cookie);
            response.addCookie(new Cookie("userIsLoggedIn", "true"));
            return "redirect:profile/" + loggedInUser.getId();
        } catch (Exception e) {
            return "redirect:login?message=login_failed&error=" + e.getMessage();
        }
    }


    // Show user profile
    @GetMapping("/profile/{userId}")
    public String showUserProfile(@RequestParam(name = "profileUpdateMessage", required = false) String profileUpdateMessage, Model model,
                                  @PathVariable Long userId, @CookieValue(value = "userId") String userIdFromCookie) throws Exception {
        model.addAttribute("userId", userIdFromCookie);
        model.addAttribute("user", userService.findAllUsersById(Long.valueOf(userIdFromCookie)));
        model.addAttribute("profileUpdateMessage", profileUpdateMessage);
        return "profile";
    }


    @PostMapping("/profile/{userId}")
    public String updateUser(@PathVariable Long userId, Model model, @CookieValue(value = "userId") String userIdCookie, User updatedUser) throws Exception {
        try {
            model.addAttribute("userId", userIdCookie);
            this.userService.updateUser(Long.valueOf(userIdCookie), updatedUser);
        } catch (Exception e) {
            System.out.println("DEBUG: Exception caught: " + e.getMessage());
            model.addAttribute("profileUpdateMessage", "profile_update_failed");
            model.addAttribute("error", e.getMessage());
            return "redirect:/profile/{userId}?profileUpdateMessage=profile_update_failed&error=" + e.getMessage();
        }

        return "redirect:/profile/"+ userId+"?profileUpdateMessage=profile_updated";
    }


    @GetMapping("/profile/{userId}/changePassword")
    public String showUpdateUserPassword(@PathVariable Long userId, @CookieValue("userId") String userIdCookie, Model model, @RequestParam(name = "passwordUpdateMessage", required = false) String passwordUpdateMessage) {
        try {
            model.addAttribute("userId", userIdCookie);
            model.addAttribute("user", userService.findUserById(Long.valueOf(userIdCookie)));
            if (passwordUpdateMessage != null && !passwordUpdateMessage.isEmpty()) {
                model.addAttribute("passwordUpdateMessage", passwordUpdateMessage);
            }
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/profile/{userId}";
        }

        return "changePassword";
    }


    @PostMapping("/profile/{userId}/changePassword")
    public String updateUserPassword(@PathVariable(name="userId")  Long userId, User updatedUser,Model model,@CookieValue(value = "userId")String userIdFromCookie){

        try {
            System.out.println("DEBUG: userIdFromCookie = " + userIdFromCookie);
            model.addAttribute("userId",userIdFromCookie);
            this.userService.updateUserPasswordById(Long.valueOf(userIdFromCookie), updatedUser);
        } catch (Exception e) {
            System.out.println("DEBUG: Exception caught: " + e.getMessage());
            model.addAttribute("passwordUpdateMessage", "update_failed");
            model.addAttribute("error", e.getMessage());
            return "redirect:/profile/{userId}/changePassword?passwordUpdateMessage=update_failed&error=" + e.getMessage();
        }
//        return "redirect:/profile/{userId}?passwordUpdateMessage=password_updated";
        return "redirect:/profile/{userId}/changePassword?passwordUpdateMessage=password_updated";
    }


    @GetMapping("/")
    public String logoutCustomerAndDeleteCookies(HttpServletResponse response){
        try {
            Cookie deleteServletCookie = new Cookie("userId", null);
            deleteServletCookie.setMaxAge(0);
            response.addCookie(deleteServletCookie);

            Cookie deleteLoggedInCookie = new Cookie("userIsLoggedIn", null);
            deleteLoggedInCookie.setMaxAge(0);
            response.addCookie(deleteLoggedInCookie);

            return   "redirect:index.html";
        }catch (Exception e){
            return "redirect:/login";
        }
    }

}