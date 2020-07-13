package com.falynsky.smartmarkt.repositories;

import com.falynsky.smartmarkt.models.Account;
import com.falynsky.smartmarkt.models.DTO.AccountDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {

    Optional<Account> findByUsername(String username);

    Account findFirstByOrderByIdDesc();

    @Query("SELECT new com.falynsky.smartmarkt.models.DTO.AccountDTO(a.id, a.username, a.password, a.role) FROM Account a")
    List<AccountDTO> retrieveAccountAsDTO();

    @Query("SELECT new com.falynsky.smartmarkt.models.DTO.AccountDTO(a.id, a.username, a.password, a.role) FROM Account a where a.id = :accountId")
    AccountDTO retrieveAccountAsDTObyId(@Param("accountId") Integer accountId);
}
