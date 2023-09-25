create table netology.erole
(
    name varchar(20) not null
        primary key
        constraint erole_name_check
            check ((name)::text = ANY
                   (ARRAY [
                       ('UPLOAD'::character varying)::text,
                       ('DOWNLOAD'::character varying)::text,
                       ('RENAME'::character varying)::text,
                        ('DELETE'::character varying)::text
                       ])
                )
);

alter table netology.erole
    owner to postgres;

create table netology.euser
(
    id       bigserial
        primary key,
    login    varchar(50)  not null,
    password varchar(100) not null,
    token    varchar(255)
);

alter table netology.euser
    owner to postgres;

create table netology.euser_roles
(
    euser_id   bigint      not null
        constraint fkk4jqyxw12die2tdwjpaxttna5
            references netology.euser,
    roles_name varchar(20) not null
        constraint uk_llt2f6ljpse7mtulb0uo33ile
            references netology.erole
);

alter table netology.euser_roles
    owner to postgres;