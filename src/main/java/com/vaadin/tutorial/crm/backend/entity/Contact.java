package com.vaadin.tutorial.crm.backend.entity;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
public class Contact extends AbstractEntity implements Cloneable {

    public enum Status {
      Interno, Externo, Contratado
    }
    public enum tipo {
        Obrero,Administrativo
    }
    public enum cargo{
        maqunista,batimetrista, operador,otro
    }

    public enum condicion{
        Activo, Reposo, Baja
    }

    @NotNull
    @NotEmpty
    private String firstName = "";

    @NotNull
    @NotEmpty
    private String lastName = "";

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Contact.Status status;

    @Email
    @NotNull
    @NotEmpty
    private String email = "";

    @Enumerated
    private Contact.tipo tipo;

    @Enumerated
    private Contact.cargo cargo;

    @Enumerated
    private Contact.condicion condicion;


    public String getEmail() {
      return email;
    }

    public void setEmail(String email) {
      this.email = email;
    }

    public Status getStatus() {
      return status;
    }

    public void setStatus(Status status) {
      this.status = status;
    }

    public String getLastName() {
      return lastName;
    }

    public void setLastName(String lastName) {
      this.lastName = lastName;
    }

    public String getFirstName() {
      return firstName;
    }

    public void setFirstName(String firstName) {
      this.firstName = firstName;
    }

    public void setCompany(Company company) {
      this.company = company;
    }

    public Company getCompany() {
      return company;
    }

    public Contact.tipo getTipo() {
        return tipo;
    }

    public void setTipo(Contact.tipo tipo) {
        this.tipo = tipo;
    }

    public Contact.cargo getCargo() {
        return cargo;
    }

    public void setCargo(Contact.cargo cargo) {
        this.cargo = cargo;
    }

    public Contact.condicion getCondicion() {
        return condicion;
    }

    public void setCondicion(Contact.condicion condicion) {
        this.condicion = condicion;
    }

    @Override
    public String toString() {
      return firstName + " " + lastName;
    }
  }
