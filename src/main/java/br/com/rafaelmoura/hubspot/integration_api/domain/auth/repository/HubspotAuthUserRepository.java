package br.com.rafaelmoura.hubspot.integration_api.domain.auth.repository;

import br.com.rafaelmoura.hubspot.integration_api.domain.auth.entities.HubspotAuthUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HubspotAuthUserRepository extends JpaRepository<HubspotAuthUser, Long> {

    Optional<HubspotAuthUser> findByUser(String user);

}
