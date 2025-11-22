package com.ecommerce.user.controllers;



import com.ecommerce.user.dto.UserDTO;
import com.ecommerce.user.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//User Controller - User Api
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;


    @GetMapping("/")
    public ResponseEntity<List<UserDTO>> getAllUsers(){

        return ResponseEntity.ok(userService.fetchAllUsers());
    }

    @PostMapping("")
    public ResponseEntity<String> createUser(@RequestBody UserDTO user){
        userService.addUser(user);
        return ResponseEntity.ok("User Added Successfully");
    }
    @GetMapping("{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id){
        return userService.fetchUserById(id).map(ResponseEntity::ok).orElseGet(()-> ResponseEntity.notFound().build());
    }
    @PutMapping ("{id}")
    public ResponseEntity<String> updateUser(@PathVariable String id,@RequestBody UserDTO user){
        if(userService.updateUser(user,id) != null)
        return ResponseEntity.ok("User Updated Successfully");
        else
        return ResponseEntity.notFound().build();
    }


}
