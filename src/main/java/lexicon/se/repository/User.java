package lexicon.se.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface User extends JpaRepository<User, Integer> {
}
