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

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/ads")  // Make sure this matches the route you are testing in Postman
public class AdvertisementController {

    @Autowired
    private AdvertisementService advertisementService;

    @Autowired
    private UserService userService;

    // Debugging endpoint to check if the controller is reachable
    @GetMapping("/test")
    public ResponseEntity<String> testEndpoint() {
        return new ResponseEntity<>("Controller is working!", HttpStatus.OK);
    }

    // API to create an advertisement based on user email, name, and password
    @PostMapping("/create")
    public ResponseEntity<Advertisement> createAdForUser(@RequestBody Map<String, String> requestData) {

        // Debugging log to see the received JSON data
        System.out.println("Received request with data: " + requestData);

        // Extract fields from the JSON request body
        String email = requestData.get("email");
        String name = requestData.get("name");
        String password = requestData.get("password");
        String contactInfo = requestData.get("contactInfo");
        String title = requestData.get("title");
        String description = requestData.get("description");
        String expirationDate = requestData.get("expirationDate");

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
