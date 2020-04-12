package com.falynsky.smartmarkt.repositories;

import com.falynsky.smartmarkt.models.DTO.UsersDTO;
import com.falynsky.smartmarkt.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsersRepository extends JpaRepository<Users, Integer> {

    @Query("SELECT new com.falynsky.smartmarkt.models.DTO.UsersDTO(u.id, u.login, u.password) FROM Users u")
    List<UsersDTO> retrieveUsersAsDTO();

    @Query("SELECT new com.falynsky.smartmarkt.models.DTO.UsersDTO(u.id, u.login, u.password) FROM Users u where u.id = :userId")
    UsersDTO retrieveUsersAsDTObyId(@Param("userId") Integer userId);


}
