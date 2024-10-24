package lexicon.se.repository;

import lexicon.se.entities.Advertisement;
import lexicon.se.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface AdvertisementRepository extends JpaRepository<Advertisement, Integer> {

    List<Advertisement> findByUser(User user);

    List<Advertisement> findAllByExpirationDateAfter(LocalDate date);

    @Query("SELECT a FROM Advertisement a WHERE a.expirationDate > :date " +
            "AND (:title IS NULL OR a.title LIKE %:title%) " +
            "AND (:description IS NULL OR a.description LIKE %:description%)")
    List<Advertisement> findFilteredAdvertisements(
            @Param("date") LocalDate date,
            @Param("title") String title,
            @Param("description") String description);
}