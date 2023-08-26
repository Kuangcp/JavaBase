CREATE TABLE customer (
  id bigint(20) auto_increment primary key ,
  name varchar(20) DEFAULT NULL,
  nation int(11) DEFAULT NULL
);
create table normal_order (
    id bigint(20) auto_increment primary key  ,
    user_id bigint(20),
    create_time datetime default CURRENT_TIMESTAMP,
    update_time datetime default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
    detail varchar(30),
    price decimal(6,2),
    discount decimal(6,2),
    num int,
    pay_time datetime
)