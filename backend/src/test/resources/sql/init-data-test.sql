insert into netology.erole (id, name) values (0, 'UPLOAD');
insert into netology.erole (id, name) values (1, 'DOWNLOAD');
insert into netology.erole (id, name) values (2, 'RENAME');
insert into netology.erole (id, name) values (3, 'DELETE');
insert into netology.euser (id, login,password) values (0, 'test','$2a$10$NjlsKOVwZZNhZGJyRfZiweg/9IES1pfYY2uVrJl0ogZQ.bZOo0QVm');
insert into netology.euser_roles (euser_id, roles_id) values (0,0);
insert into netology.euser_roles (euser_id, roles_id) values (0,1);
insert into netology.euser_roles (euser_id, roles_id) values (0,2);
insert into netology.euser_roles (euser_id, roles_id) values (0,3);
