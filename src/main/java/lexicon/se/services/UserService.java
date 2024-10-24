package lexicon.se.services;


import lexicon.se.domain.dto.UserDTO;
import lexicon.se.entities.User;
import lexicon.se.repository.AdvertisementRepository;
import lexicon.se.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdvertisementRepository advertisementRepository;


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
}
