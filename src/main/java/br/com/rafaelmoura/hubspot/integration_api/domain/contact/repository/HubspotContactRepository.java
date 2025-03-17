package br.com.rafaelmoura.hubspot.integration_api.domain.contact.repository;

import br.com.rafaelmoura.hubspot.integration_api.domain.contact.entities.HubspotContact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HubspotContactRepository extends JpaRepository<HubspotContact, Long> {
}
