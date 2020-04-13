package com.falynsky.smartmarkt.repositories;

import com.falynsky.smartmarkt.models.AppUser;
import com.falynsky.smartmarkt.models.DTO.AppUserDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Integer> {

    Optional<AppUser> findByUsername(String username);

    AppUser findFirstByOrderByIdDesc();

    @Query("SELECT new com.falynsky.smartmarkt.models.DTO.AppUserDTO(u.id, u.username, u.password, u.role) FROM AppUser u")
    List<AppUserDTO> retrieveAppUserAsDTO();

    @Query("SELECT new com.falynsky.smartmarkt.models.DTO.AppUserDTO(u.id, u.username, u.password, u.role) FROM AppUser u where u.id = :userId")
    AppUserDTO retrieveAppUserAsDTObyId(@Param("userId") Integer userId);
}
