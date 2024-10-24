package lexicon.se.controller;

import lexicon.se.domain.dto.UserDTO;
import lexicon.se.entities.Advertisement;
import lexicon.se.entities.User;
import lexicon.se.services.AdvertisementService;
import lexicon.se.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

public class AdvertismentController {

    @RestController
    @RequestMapping("/ads")
    public class AdvertisementController {

        @Autowired
        private AdvertisementService advertisementService;

        @Autowired
        private UserService userService;

        // API to create an advertisement based on user email, name, and password
        @PostMapping("/create")
        public ResponseEntity<Advertisement> createAdForUser(
                @RequestParam String email,
                @RequestParam String name,
                @RequestParam String password,
                @RequestParam String contactInfo,
                @RequestParam String title,
                @RequestParam String description,
                @RequestParam String expirationDate) {

            // Check if the user exists by email
            Optional<User> existingUserOpt = userService.findByEmail(email);

            User user;
            if (existingUserOpt.isPresent()) {
                // If the user exists, verify the password and name
                user = existingUserOpt.get();
                if (!user.getPassword().equals(password) || !user.getName().equals(name)) {
                    return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);  // Return unauthorized if the credentials are incorrect
                }
            } else {
                // If the user does not exist, create a new user
                UserDTO newUserDTO = new UserDTO();
                newUserDTO.setEmail(email);
                newUserDTO.setName(name);
                newUserDTO.setPassword(password);
                newUserDTO.setContactInfo(contactInfo);
                newUserDTO.setRole("USER");  // Set a default role

                user = userService.registerUser(newUserDTO);
            }

            // Now create the advertisement for the user
            Advertisement ad = advertisementService.createAdvertisementForUser(user.getId(), title, description, expirationDate);

            return new ResponseEntity<>(ad, HttpStatus.CREATED);
        }
    }
}

