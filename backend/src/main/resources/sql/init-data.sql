insert into netology.erole (name) values ('UPLOAD');
insert into netology.erole (name) values ('DOWNLOAD');
insert into netology.euser (id, login,password) values (1, 'test','$2a$10$NjlsKOVwZZNhZGJyRfZiweg/9IES1pfYY2uVrJl0ogZQ.bZOo0QVm');
insert into netology.euser_roles (euser_id, roles_name) values (1,'UPLOAD');
insert into netology.euser_roles (euser_id, roles_name) values (1,'DOWNLOAD');