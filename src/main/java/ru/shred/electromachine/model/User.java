package ru.shred.electromachine.model;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;

@Data
public class User implements Serializable {

    private long id;

    private String name;

    private String email;

    private String password;

    private boolean enabled;

    private LocalDate registered;

    private Set<Role> roles;

}
