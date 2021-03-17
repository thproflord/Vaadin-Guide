package com.vaadin.tutorial.crm.backend.service;

import com.vaadin.tutorial.crm.backend.entity.Company;
import com.vaadin.tutorial.crm.backend.entity.Contact;
import com.vaadin.tutorial.crm.backend.repository.CompanyRepository;
import com.vaadin.tutorial.crm.backend.repository.ContactRepository;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ContactService {

    private static final Logger LOGGER = Logger.getLogger(ContactService.class.getName());
    private ContactRepository contactRepository;
    private CompanyRepository companyRepository;

    public ContactService(ContactRepository contactRepository, CompanyRepository companyRepository) {
      this.contactRepository = contactRepository;
      this.companyRepository = companyRepository;
    }

    /**
     * Finds all Customer's that match given filter.
     *
     * @param filterText
     *            filter that returned objects should match or null/empty string
     *            if all objects should be returned.
     * @return list a Customer objects
     */
    public  List<Contact> findAll(String filterText) {
        if (filterText.isEmpty() || filterText == null) {
            return contactRepository.findAll();
        } else {
            return contactRepository.search(filterText);
        }
    }

    public long count() {
      return contactRepository.count();
    }

    public void delete(Contact contact) {
      contactRepository.delete(contact);
    }

    public void save(Contact contact) {
      if (contact == null) {
        LOGGER.log(Level.SEVERE, "Contact is null. Are you sure you have connected your form to the application?");
        return;
      }
      contactRepository.save(contact);
    }

    @PostConstruct
    public void populateTestData() {

      // Si company esta vacio
      if (companyRepository.count() == 0) {
        companyRepository.saveAll(
                Stream.of("INC", "Hidrofalcon", "INEA").map(Company::new).collect(Collectors.toSet()));
      }


    }


}


