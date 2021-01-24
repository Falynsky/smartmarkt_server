package com.falynsky.smartmarkt.repositories;

import com.falynsky.smartmarkt.models.DTO.LicencesDTO;
import com.falynsky.smartmarkt.models.Licences;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LicencesRepository extends JpaRepository<Licences, Integer> {

    Licences findFirstByOrderByIdDesc();

    @Query("SELECT new com.falynsky.smartmarkt.models.DTO.LicencesDTO(l.id, l.mail, l.type) FROM Licences l")
    List<LicencesDTO> retrieveLicencesAsDTO();

    @Query("SELECT new com.falynsky.smartmarkt.models.DTO.LicencesDTO(l.id, l.mail, l.type) FROM Licences l WHERE l.mail = :mail")
    LicencesDTO retrieveLicenceAsDTOByMail(@Param("mail") String mail);
}
