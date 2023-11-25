create table menu(
    item_name varchar(255) primary key,
    price int,
    descrip varchar(255)
);

create table reservation(
    reservation_id int primary key,
    cust_name varchar(255) not null,
    email varchar(255) not null,
    reservation_time time,
    reservation_date date
);

create table tables(
    table_no int primary key,
    table_status varchar(255) check (table_status in ('available', 'unavailable')),
    reservation_id int,
    foreign key (reservation_id) references reservation(reservation_id)
);

create table order_table(
    order_id int primary key,
    order_item varchar(255) not null,
    quantity int not null,
    reservation_id int not null,
    foreign key (order_item) references menu(item_name),
    foreign key (reservation_id) references reservation(reservation_id)
);

create table inventory(
    item_name varchar(255) primary key,
    quantity int not null,
    foreign key (item_name) references menu(item_name)
);

create table payment(
    order_id int primary key,
    total_price int not null,
    payment_status varchar(255) check (payment_status in ('paid', 'unpaid'))
);


INSERT INTO menu (item_name, price, descrip) VALUES
('Spaghetti Carbonara', 12, 'Pasta with bacon, eggs, and cheese'),
('Grilled Salmon', 18, 'Freshly grilled salmon fillet with herbs'),
('Caesar Salad', 9, 'Romaine lettuce, croutons, Parmesan cheese, Caesar dressing'),
('Margherita Pizza', 10, 'Pizza with tomato sauce, mozzarella, and basil'),
('Beef Burger', 14, 'Classic beef patty with lettuce, tomato, onion, and fries'),
('Vegetable Stir-Fry', 11, 'Assorted vegetables stir-fried in a savory sauce'),
('Chicken Alfredo', 15, 'Fettuccine pasta with creamy Alfredo sauce and grilled chicken'),
('Tiramisu', 8, 'Classic Italian dessert with coffee-flavored ladyfingers and mascarpone'),
('Fruit Platter', 12, 'Assorted fresh seasonal fruits'),
('Mango Smoothie', 6, 'Refreshing smoothie made with fresh mangoes and yogurt');

INSERT INTO tables (table_no, table_status) VALUES
(1, 'available'),
(2, 'available'),
(3, 'available'),
(4, 'available'),
(5, 'available'),
(6, 'available'),
(7, 'available'),
(8, 'available'),
(9, 'available'),
(10, 'available');

INSERT INTO inventory (item_name, quantity)
SELECT item_name, 10 AS quantity
FROM menu;

