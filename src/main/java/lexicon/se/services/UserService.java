package lexicon.se.services;

import lexicon.se.domain.dto.UserDTO;
import lexicon.se.entities.User;
import lexicon.se.repository.AdvertisementRepository;
import lexicon.se.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdvertisementRepository advertisementRepository;

    // Automatically remove duplicates on startup
    @PostConstruct
    public void checkAndRemoveDuplicatesOnStartup() {
        System.out.println("Checking for duplicate users...");
        removeAllDuplicateUsers();
        System.out.println("Duplicate user check complete.");
    }

    // Method to register a user
    public User registerUser(UserDTO userDTO) {
        User user = new User();
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        user.setContactInfo(userDTO.getContactInfo());
        user.setRole(userDTO.getRole());

        return userRepository.save(user);
    }

    // Find user by email
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // Method to remove all duplicate users
    public void removeAllDuplicateUsers() {
        List<User> allUsers = userRepository.findAll();

        // Group users by email
        Map<String, List<User>> groupedByEmail = allUsers.stream()
                .collect(Collectors.groupingBy(User::getEmail));

        // Remove duplicates for each email
        for (Map.Entry<String, List<User>> entry : groupedByEmail.entrySet()) {
            removeDuplicateUsersByEmail(entry.getKey());  // Call the method to remove duplicates for each email
        }
    }

    // Helper method to remove duplicate users for a specific email
    private void removeDuplicateUsersByEmail(String email) {
        List<User> users = userRepository.findAllByEmail(email);

        if (users.size() > 1) {
            User userToKeep = users.get(0); // Keep the first user
            for (int i = 1; i < users.size(); i++) {
                userRepository.delete(users.get(i));  // Remove duplicate users
            }
            System.out.println("Removed duplicates for email: " + email);
        }
    }
}
