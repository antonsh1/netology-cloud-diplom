create table netology.erole
(
    id   bigserial
        primary key,
    name varchar(100)
        constraint erole_name_check
            check ((name)::text = ANY
                   ((ARRAY ['UPLOAD'::character varying, 'DOWNLOAD'::character varying, 'RENAME'::character varying, 'DELETE'::character varying])::text[]))
);

alter table netology.erole
    owner to postgres;

create table netology.euser
(
    id       bigserial
        primary key,
    login    varchar(50)  not null
        unique,
    password varchar(100) not null,
    token    varchar(255)
);

alter table netology.euser
    owner to postgres;

create table netology.euser_roles
(
    euser_id bigint not null
        constraint fkk4jqyxw12die2tdwjpaxttna5
            references netology.euser,
    roles_id bigint not null
        constraint fkbk4k5nfygayr01telgjp2c6r6
            references netology.erole
);

alter table netology.euser_roles
    owner to postgres;


