create table order_items
(
    id          bigint auto_increment
        primary key,
    order_id    bigint         not null,
    product_id  bigint         not null,
    unit_price  decimal(10, 2) not null,
    quantity    int            not null,
    total_price decimal(10, 2) not null,
    constraint order_items_orders__fk
        foreign key (order_id) references orders (id),
    constraint order_items_product__fk
        foreign key (product_id) references products (id)
);
