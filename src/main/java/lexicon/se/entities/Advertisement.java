package lexicon.se.entities;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

@Entity
public class Advertisement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String title;

    @Column
    private String description;

    @Column
    private String expirationDate;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false) // This is the foreign key column in the advertisement table
    private User user;
}