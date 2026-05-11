package com.rakesh.smartcity.service;

import com.rakesh.smartcity.Dto.UserDto;
import com.rakesh.smartcity.Dto.WorkerCreateRequestDto;
import com.rakesh.smartcity.Exception.BadRequestException;
import com.rakesh.smartcity.Exception.ResourceNotFoundException;
import com.rakesh.smartcity.model.Role;
import com.rakesh.smartcity.model.User;
import com.rakesh.smartcity.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class WorkerService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    PasswordEncoder passwordEncoder;

    public UserDto createWorker(WorkerCreateRequestDto workerCreateRequestDto) {


        User workerUser = mapToEntity(workerCreateRequestDto);
        workerUser.setRole(Role.WORKER);

        User adminUser = userRepo.findById(workerCreateRequestDto.getAdminId())
                .orElseThrow(() -> new ResourceNotFoundException("Admin not found with id: " + workerCreateRequestDto.getAdminId()));



        if (adminUser.getRole() != Role.ADMIN) {
            throw new BadRequestException("Selected user is not an ADMIN");
        }

        workerUser.setAdmin(adminUser);
        workerUser.setPinCode(adminUser.getPinCode());

        String email = workerCreateRequestDto.getEmail();
        Optional<User> existingEmail = userRepo.findByEmail(email);
        if (existingEmail.isPresent()) {
            System.out.println("[DEBUG] ERROR: Email already exists");
            throw new BadRequestException("Email already exists");
        }

        String phoneNumber = workerCreateRequestDto.getPhoneNumber();
        Optional<User> existingPhoneNumber = userRepo.findByPhoneNumber(phoneNumber);
        if (existingPhoneNumber.isPresent()) {

            throw new BadRequestException("Phone number already exists");
        }

        User saveWorker = userRepo.save(workerUser);

        return mapToDto(saveWorker);
    }

    public List<UserDto> getAllWorker() {
        List<User> workers = userRepo.findByRole(Role.WORKER);
        return workers.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public UserDto getWorkerById(Long id) {
        User worker = userRepo.findByIdAndRole(id, Role.WORKER)
                .orElseThrow(() -> new ResourceNotFoundException("Worker not found with id: " + id));
        return mapToDto(worker);
    }

    public List<UserDto> getWorkerByPinCode(String pinCode) {
        List<User> workersWithPinCode = userRepo.findByRoleAndPinCode(Role.WORKER, pinCode);
        return workersWithPinCode.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<UserDto> getWorkerByadminId(Long adminId) {
        List<User> workersWithadminId = userRepo.findByRoleAndAdminId(Role.WORKER, adminId);
        return workersWithadminId.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private UserDto mapToDto(User user){
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        userDto.setPhoneNumber(user.getPhoneNumber());
        userDto.setPinCode(user.getPinCode());
        userDto.setRole(user.getRole());
        return userDto;
    }

    private User mapToEntity(WorkerCreateRequestDto workerCreateRequestDto){
        User user = new User();
        user.setName(workerCreateRequestDto.getName());
        user.setEmail(workerCreateRequestDto.getEmail());
        user.setPassword(passwordEncoder.encode(workerCreateRequestDto.getPassword()));
        user.setPhoneNumber(workerCreateRequestDto.getPhoneNumber());
        user.setRole(Role.WORKER);
        return user;
    }
}
