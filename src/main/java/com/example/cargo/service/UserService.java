package com.example.cargo.service;

import com.example.cargo.model.User;
import com.example.cargo.repository.UserRepository;
import com.example.cargo.util.Role;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Сохранение нового пользователя с шифрованием пароля
    public void registerUser(User user) {
        if (userRepository.findByUserName(user.getUserName()).isPresent()) {
            System.out.println("User already exists");
            throw new IllegalArgumentException("Пользователь с таким именем уже существует");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword())); // Шифруем пароль
        if (user.getRole() == null) {
            user.setRole(Role.USER); // Устанавливаем роль по умолчанию
        }
        userRepository.save(user);
    }

    // Поиск пользователя по ID
    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }

    // Поиск пользователя по имени
    public Optional<User> findUserByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    // Удаление пользователя по ID
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }
    public void updateUserRole(Long userId, String newRole) {
        // Находим пользователя по его идентификатору
        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            // Обновляем роль пользователя
            user.setRole(Role.valueOf(newRole));

            // Сохраняем изменения
            userRepository.save(user);
        } else {
            throw new RuntimeException("Пользователь с id " + userId + " не найден");
        }
    }
}
