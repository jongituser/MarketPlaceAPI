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


    @PostConstruct
    public void checkAndRemoveDuplicatesOnStartup() {
        System.out.println("Checking for duplicate users...");
        removeAllDuplicateUsers();
        System.out.println("Duplicate user check complete.");
    }


    public User registerUser(UserDTO userDTO) {
        User user = new User();
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        user.setContactInfo(userDTO.getContactInfo());
        user.setRole(userDTO.getRole());

        return userRepository.save(user);
    }


    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }


    public void removeAllDuplicateUsers() {
        List<User> allUsers = userRepository.findAll();


        Map<String, List<User>> groupedByEmail = allUsers.stream()
                .collect(Collectors.groupingBy(User::getEmail));


        for (Map.Entry<String, List<User>> entry : groupedByEmail.entrySet()) {
            removeDuplicateUsersByEmail(entry.getKey());
        }
    }


    private void removeDuplicateUsersByEmail(String email) {
        List<User> users = userRepository.findAllByEmail(email);

        if (users.size() > 1) {
            User userToKeep = users.get(0);
            for (int i = 1; i < users.size(); i++) {
                userRepository.delete(users.get(i));
            }
            System.out.println("Removed duplicates for email: " + email);
        }
    }
}
