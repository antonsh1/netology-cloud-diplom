create table erole
(
    name varchar(20) not null
        primary key
        constraint erole_name_check
            check ((name)::text = ANY
                   ((ARRAY ['UPLOAD'::character varying, 'DOWNLOAD'::character varying, 'RENAME'::character varying])::text[]))
);

alter table erole
    owner to postgres;

create table euser
(
    id       bigserial
        primary key,
    login    varchar(50)  not null,
    password varchar(100) not null,
    token    varchar(255)
);

alter table euser
    owner to postgres;

create table euser_roles
(
    euser_id   bigint      not null
        constraint fkk4jqyxw12die2tdwjpaxttna5
            references euser,
    roles_name varchar(20) not null
        constraint uk_llt2f6ljpse7mtulb0uo33ile
            unique
        constraint fkdqwlxb08lnekp7ppr3u79jisk
            references erole
);

alter table euser_roles
    owner to postgres;