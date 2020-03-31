package org.example.userservice.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * User entity.
 */
@Entity
@Getter
@Setter
@Table(name = "users")
public class User {

    /**
     * Identifier.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * user name.
     */
    private String username;

    /**
     * name of the user.
     */
    private String name;

    /**
     * password.
     */
    private String password;

    /**
     * status of user.
     */
    @Column(name = "is_active")
    private Boolean isActive;

    /**
     * when user is created.
     */
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    /**
     * last updated time of user.
     */
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

}
