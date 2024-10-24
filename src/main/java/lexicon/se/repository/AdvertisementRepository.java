package lexicon.se.repository;

import lexicon.se.entities.Advertisement;
import lexicon.se.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdvertisementRepository extends JpaRepository<Advertisement, Integer> {

    List<Advertisement> findByUser(User user);
}