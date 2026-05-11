package com.rakesh.smartcity.service;

import com.rakesh.smartcity.Dto.UserDto;
import com.rakesh.smartcity.Exception.ResourceNotFoundException;
import com.rakesh.smartcity.model.Role;
import com.rakesh.smartcity.model.User;
import com.rakesh.smartcity.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    UserRepo userRepo;

    public UserDto getUserById(Long id){
        User user = userRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with id: " + id));
        return mapToDto(user);
    }

    public List<UserDto> getAllUser()
    {
        List<User> user = userRepo.findAll();

        if (user.isEmpty()) {
            return new ArrayList<>();
        }
        return   user.stream()
                .map(this::mapToDto)
                .collect( Collectors.toList());
    }

    public List<UserDto> getUsersByRole(Role role){
        List<User> user = userRepo.findByRole(role);
        if (user.isEmpty()) {
            return new ArrayList<>();
        }
        return   user.stream()
                .map(this::mapToDto)
                .collect( Collectors.toList());
    }

    private UserDto mapToDto(User user) {
        UserDto dto = new UserDto();

        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setPinCode(user.getPinCode());
        dto.setRole(user.getRole());

        return dto;
    }

}
