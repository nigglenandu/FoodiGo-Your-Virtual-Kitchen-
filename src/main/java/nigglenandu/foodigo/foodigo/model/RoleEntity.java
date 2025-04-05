package nigglenandu.foodigo.foodigo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
public class RoleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private Roles role;
}
