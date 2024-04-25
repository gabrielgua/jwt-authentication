set foreign_key_checks = 0;

delete from users;

set foreign_key_checks = 1;

alter table users auto_increment = 1;

insert into users(id, name, email, password) values
(1, "Test User", "test.user@email.com", "$2a$10$vQ5jChzMp6sJiGM5Ml41l.WxC5ioYbTSqzlJ6ZkO.Er.ZeGmQx9ni");