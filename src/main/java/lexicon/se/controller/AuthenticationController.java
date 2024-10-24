package lexicon.se.controller;

import lexicon.se.domain.dto.LoginDTO;
import lexicon.se.entities.Advertisement;
import lexicon.se.services.AdvertisementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private AdvertisementService advertisementService;


    @PostMapping("/login")
    public ResponseEntity<List<Advertisement>> authenticateAndGetAds(@Valid @RequestBody LoginDTO loginDTO) {
        try {
            List<Advertisement> ads = advertisementService.authenticateAndGetAds(loginDTO);
            return new ResponseEntity<>(ads, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
    }
}
