CREATE TABLE `user` (
  `id` bigint(20) primary key ,
  `name` varchar(20) DEFAULT NULL,
  `nation` int(11) DEFAULT NULL
);

create table user_order (
    id bigint(20) primary key ,
    user_id bigint(20),
    create_time datetime,
    update_time datetime,
    detail varchar(30),
    price decimal(6,2),
    discount decimal(6,2),
    count int,
    pay_time datetime
)