package org.example.userservice.repository;

import org.example.userservice.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * JPA repository for user.
 */
@Repository
public interface UserRepository extends CrudRepository<User, Long> {
}
