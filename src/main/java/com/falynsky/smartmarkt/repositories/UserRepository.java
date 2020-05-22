package com.falynsky.smartmarkt.repositories;

import com.falynsky.smartmarkt.models.DTO.UserDTO;
import com.falynsky.smartmarkt.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByFirstNameAndLastName(String firstName, String lastName);

    User findFirstByOrderByIdDesc();

    @Query("SELECT new com.falynsky.smartmarkt.models.DTO.UserDTO(u.id, u.firstName, u.lastName, u.accountId.id) FROM User u")
    List<UserDTO> retrieveUserAsDTO();

    @Query("SELECT new com.falynsky.smartmarkt.models.DTO.UserDTO(u.id, u.firstName, u.lastName, u.accountId.id) FROM User u where u.id = :userId")
    UserDTO retrieveUserAsDTObyId(@Param("userId") Integer userId);
}
