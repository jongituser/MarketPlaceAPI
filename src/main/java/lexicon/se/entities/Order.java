package lexicon.se.entities;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String buyerId;

    @Column
    private String totalAmount;

    @Column
    private String orderStatus;

    @Column
    private String paymentMethod;

    @Column
    private String orderDate;
}
