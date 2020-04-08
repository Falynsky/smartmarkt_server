package com.falynsky.smartmarkt.repositories;

import com.falynsky.smartmarkt.models.DTO.Users;
import com.falynsky.smartmarkt.models.UsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<UsersEntity, Integer> {

    @Query("SELECT new com.falynsky.smartmarkt.models.DTO.Users(u.id, u.login, u.password) FROM UsersEntity u")
    List<Users> retrieveUsersAsDTO();

    @Query("SELECT new com.falynsky.smartmarkt.models.DTO.Users(u.id, u.login, u.password) FROM UsersEntity u where u.id = :userId")
    Users retrieveUsersAsDTObyId(@Param("userId") Integer userId);

    //Optional<UsersEntity> findById(@Param("userId") Integer userId);


}
