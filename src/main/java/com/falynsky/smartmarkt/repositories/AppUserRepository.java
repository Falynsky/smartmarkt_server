package com.falynsky.smartmarkt.repositories;

import com.falynsky.smartmarkt.models.AppUsers;
import com.falynsky.smartmarkt.models.DTO.AppUsersDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AppUserRepository extends JpaRepository<AppUsers, Integer> {

    Optional<AppUsers> findByUsername(String username);

    AppUsers findFirstByOrderByIdDesc();

    @Query("SELECT new com.falynsky.smartmarkt.models.DTO.AppUsersDTO(u.id, u.username, u.password, u.role) FROM AppUsers u")
    List<AppUsersDTO> retrieveAppUsersAsDTO();

    @Query("SELECT new com.falynsky.smartmarkt.models.DTO.AppUsersDTO(u.id, u.username, u.password, u.role) FROM AppUsers u where u.id = :userId")
    AppUsersDTO retrieveAppUsersAsDTObyId(@Param("userId") Integer userId);
}
