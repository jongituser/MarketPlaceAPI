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

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/ads")
public class AdvertisementController {

    @Autowired
    private AdvertisementService advertisementService;

    @Autowired
    private UserService userService;

    @GetMapping("/test")
    public ResponseEntity<String> testEndpoint() {
        return new ResponseEntity<>("Controller is working!", HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<Advertisement> createAdForUser(@RequestBody Map<String, String> requestData) {
        System.out.println("Received request with data: " + requestData);

        String email = requestData.get("email");
        String name = requestData.get("name");
        String password = requestData.get("password");
        String contactInfo = requestData.get("contactInfo");
        String title = requestData.get("title");
        String description = requestData.get("description");
        String expirationDateString = requestData.get("expirationDate");

        LocalDate expirationDate;
        try {

            expirationDate = LocalDate.parse(expirationDateString);
        } catch (DateTimeParseException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        Optional<User> existingUserOpt = userService.findByEmail(email);
        User user;

        if (existingUserOpt.isPresent()) {
            user = existingUserOpt.get();
            if (!user.getPassword().equals(password) || !user.getName().equals(name)) {
                return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
            }
        } else {

            UserDTO newUserDTO = new UserDTO();
            newUserDTO.setEmail(email);
            newUserDTO.setName(name);
            newUserDTO.setPassword(password);
            newUserDTO.setContactInfo(contactInfo);
            newUserDTO.setRole("USER");

            user = userService.registerUser(newUserDTO);
        }

        Advertisement ad = advertisementService.createAdvertisementForUser(user.getId(), title, description, expirationDate);

        return new ResponseEntity<>(ad, HttpStatus.CREATED);
    }

    @GetMapping("/list")
    public ResponseEntity<List<Advertisement>> listActiveAdvertisements(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String description) {

        List<Advertisement> activeAds = advertisementService.getActiveAdvertisements(title, description);
        return new ResponseEntity<>(activeAds, HttpStatus.OK);
    }
}
