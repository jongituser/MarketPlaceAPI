package lexicon.se.services;

import lexicon.se.domain.dto.LoginDTO;
import lexicon.se.entities.Advertisement;
import lexicon.se.entities.User;
import lexicon.se.repository.AdvertisementRepository;
import lexicon.se.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdvertisementService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdvertisementRepository advertisementRepository;

    public Advertisement createAdvertisementForUser(Integer userId, String title, String description, String expirationDate) {
        // Find the user by ID
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Create a new advertisement
        Advertisement advertisement = new Advertisement();
        advertisement.setTitle(title);
        advertisement.setDescription(description);
        advertisement.setExpirationDate(expirationDate);
        advertisement.setUser(user);  // Associate ad with the user

        // Save and return the advertisement
        return advertisementRepository.save(advertisement);

    }

    public List<Advertisement> authenticateAndGetAds(LoginDTO loginDTO) {
        Optional<User> userOpt = userRepository.findByEmail(loginDTO.getEmail());

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            // Compare the plain-text password (since we're skipping hashing for now)
            if (user.getPassword().equals(loginDTO.getPassword())) {
                // Fetch the advertisements created by this user
                return advertisementRepository.findByUser(user);
            } else {
                throw new IllegalArgumentException("Invalid password");
            }
        } else {
            throw new IllegalArgumentException("User not found");
        }
    }
}
