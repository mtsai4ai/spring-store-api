create table orders
(
    id          bigint auto_increment
        primary key,
    customer_id bigint                             not null,
    status      varchar(20)                        not null,
    created_at  datetime default current_timestamp not null,
    total_price DECIMAL(10, 2)                     null,
    constraint orders_customer__fk
        foreign key (customer_id) references users (id)
);

