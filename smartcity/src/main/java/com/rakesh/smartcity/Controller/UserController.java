package com.rakesh.smartcity.Controller;


import com.rakesh.smartcity.Dto.UserDto;
import com.rakesh.smartcity.model.Role;
import com.rakesh.smartcity.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    UserService userService;

    // GET  /api/users
    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUser());
    }

    // GET  /api/users/{id}
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    // GET  /api/users/role?role=ADMIN
    @GetMapping("/role")
    public ResponseEntity<List<UserDto>> getUsersByRole(@RequestParam Role role) {
        return ResponseEntity.ok(userService.getUsersByRole(role));
    }
}
