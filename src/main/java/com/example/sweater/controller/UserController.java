package com.example.sweater.controller;

import com.example.sweater.domain.Message;
import com.example.sweater.domain.Role;
import com.example.sweater.domain.User;
import com.example.sweater.repo.MessageRepo;
import com.example.sweater.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private MessageRepo messageRepo;

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public String userList(Model model){
        model.addAttribute("users", userService.findAll());
        return "userList";
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("{user}")
    public String useEditForm(@PathVariable User user, Model model){
        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());
        return "userEdit";
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public String userSave(@RequestParam("userId") User user,
                           @RequestParam Map<String, String> form,
                           @RequestParam String username){
        userService.saveUser(user, username, form);
        return "redirect:/students";
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping(value = "/{id}/remove", method = RequestMethod.GET)
    public String userRemove(@PathVariable(value = "id") long id,
                             @AuthenticationPrincipal User user,
                             Model model)
    {
        if(!user.getUsername().equals(userService.getUser(id).getUsername())){
            ArrayList<Message> message = messageRepo.findAllByAuthor(userService.getUser(id));
            for (Message value : message) {
                userService.removeMessage(value.getId());
            }
            userService.removeUser(id);
        }
        model.addAttribute("users", userService.findAll());
        return "userList";
    }




    @GetMapping("profile")
    public String getProfile(Model model,
                             @AuthenticationPrincipal User user){
        model.addAttribute("username", user.getUsername());
        model.addAttribute("email", user.getEmail());

        return "profile";
    }

    @PostMapping("profile")
    public String updateProfile(@AuthenticationPrincipal User user,
                                @RequestParam String password,
                                @RequestParam String email){
        userService.updateProfile(user, password, email);
        return "redirect:/profile";
    }
}
