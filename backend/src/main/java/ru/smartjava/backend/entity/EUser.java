package ru.smartjava.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.UniqueElements;

import java.util.Collection;

@Entity
@RequiredArgsConstructor
@Getter
@Setter
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

    @Size(max = 255)
    String token;

    @OneToMany(fetch = FetchType.EAGER)
    private Collection<ERole> roles;
}
