package com.nttdata.model;

import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Immutable;

@EqualsAndHashCode(callSuper = true)
@Data
//@Immutable
@Entity(name = "client")
public class ClientView extends PersonView {

    private String clientId;

    private String password;

    private Boolean status;
}
