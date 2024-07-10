package com.nttdata.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Immutable;


@Data
@Immutable
@Entity(name = "person")
public class PersonView {

    @Id
    @Column(name = "person_id")
    private Long personId;

    private String identificacion;

    private String name;

    private String gender;

    private Integer age;

    private String address;

    private String phone;
}