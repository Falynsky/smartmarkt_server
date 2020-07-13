package com.falynsky.smartmarkt.repositories;

import com.falynsky.smartmarkt.models.DTO.LicenceDTO;
import com.falynsky.smartmarkt.models.Licence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LicenceRepository extends JpaRepository<Licence, Integer> {

    @Query("SELECT new com.falynsky.smartmarkt.models.DTO.LicenceDTO(l.id, l.licenceKey, l.licenceRole) FROM Licence l where l.licenceKey = :licenceKey")
    LicenceDTO getLicenceByLicenceKey(@Param("licenceKey") String licenceKey);
}
