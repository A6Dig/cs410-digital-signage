package com.a6dig.digitalsignage.controller;

<<<<<<< HEAD
import com.a6dig.digitalsignage.entity.User;
import com.a6dig.digitalsignage.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
=======
import com.a6dig.digitalsignage.constant.AppConstant;
import com.a6dig.digitalsignage.entity.User;
import com.a6dig.digitalsignage.service.UserService;
import com.a6dig.digitalsignage.util.APIResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
>>>>>>> 28ec52b383dd2e358d2e5711391f9e0f9f3feb92
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
<<<<<<< HEAD
    private UserRepository userRepository;

    @PostMapping("/register")
    public User registerUser(@RequestBody User user) {
        return userRepository.save(user);
    }
    
    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

}
=======
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<APIResponse<User>> registerUser(@RequestBody User user) {
        User createdUser = userService.registerUser(user);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(APIResponse.created(AppConstant.SuccessMessage.User.CREATED, createdUser));
    }

    @GetMapping("/{id}")
    public ResponseEntity<APIResponse<User>> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(APIResponse.success(userService.getUserById(id)));
    }

    @GetMapping("")
    public ResponseEntity<APIResponse<List<User>>> getAllUsers() {
        return ResponseEntity.ok(APIResponse.success(userService.getAllUsers()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<APIResponse<User>> updateUser(@PathVariable Long id, @RequestBody User user) {
        User updatedUser = userService.updateUser(id, user);
        return ResponseEntity.ok(APIResponse.success(AppConstant.SuccessMessage.User.UPDATED, updatedUser));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<APIResponse<Void>> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(APIResponse.success(AppConstant.SuccessMessage.User.DELETED));
    }

    @DeleteMapping("")
    public ResponseEntity<APIResponse<Void>> deleteAllUsers() {
        userService.deleteAllUsers();
        return ResponseEntity.ok(APIResponse.success(AppConstant.SuccessMessage.User.DELETED_ALL));
    }
}
>>>>>>> 28ec52b383dd2e358d2e5711391f9e0f9f3feb92
