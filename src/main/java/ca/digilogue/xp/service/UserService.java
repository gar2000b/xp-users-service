package ca.digilogue.xp.service;

import ca.digilogue.xp.model.User;
import ca.digilogue.xp.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(String id) {
        return userRepository.findById(id);
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public User updateUser(User user) {
        return userRepository.update(user);
    }

    public User partialUpdateUser(String id, Map<String, Object> updates) {
        return userRepository.partialUpdate(id, updates);
    }

    public boolean deleteUser(String id) {
        return userRepository.deleteById(id);
    }
}
