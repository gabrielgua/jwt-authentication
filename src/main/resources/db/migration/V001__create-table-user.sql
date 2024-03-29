create table user(
    id bigint auto_increment,
    name varchar(255) not null,
    email varchar(255) not null,
    password varchar(255) not null,

    primary key (id)

) engine=InnoDB default charset=UTF8MB4;