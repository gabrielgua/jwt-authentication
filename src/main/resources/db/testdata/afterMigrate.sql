set foreign_key_checks = 0;

delete from user;

set foreign_key_checks = 1;

alter table user auto_increment = 1;

insert into user(id, name, email, password) values
(1, "admin", "admin@email.com", "$2a$10$vQ5jChzMp6sJiGM5Ml41l.WxC5ioYbTSqzlJ6ZkO.Er.ZeGmQx9ni");