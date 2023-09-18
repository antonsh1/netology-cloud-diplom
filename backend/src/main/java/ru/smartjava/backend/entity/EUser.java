package ru.smartjava.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.UniqueElements;

import java.util.Collection;

@Entity
@RequiredArgsConstructor
@Getter
public class EUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotNull
    @NotBlank
    @Size(max = 50)
    @UniqueElements
    String login;

    @NotNull
    @NotBlank
    @Size(max = 100)
    String password;

    @OneToMany(fetch = FetchType.EAGER)
    private Collection<ERole> roles;
}
