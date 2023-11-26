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
    reservation_date date,
    numPeople int not null
);

create table tables(
    table_no int primary key,
    table_status varchar(255) check (table_status in ('available', 'unavailable')),
    numSeats int,
    reservation_id int,
    foreign key (reservation_id) references reservation(reservation_id)
);

CREATE TABLE orders (
    order_id INT PRIMARY KEY,
    reservation_id INT NOT NULL,
    FOREIGN KEY (reservation_id) REFERENCES reservation(reservation_id)
);

CREATE TABLE order_items (
    order_item_id INT PRIMARY KEY,
    order_id INT NOT NULL,
    menu_item_name VARCHAR(255) NOT NULL,
    quantity INT NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders(order_id),
    FOREIGN KEY (menu_item_name) REFERENCES menu(item_name)
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

INSERT INTO reservation (reservation_id, cust_name, email, reservation_time, reservation_date, numPeople) VALUES 
(1, 'John Doe', 'john@example.com', '12:00:00', '2023-11-24', 4),
(2, 'Jane Smith', 'jane@example.com', '18:30:00', '2023-11-25', 2);

INSERT INTO tables (table_no, table_status, numSeats, reservation_id) VALUES 
(1, 'unavailable', 4, 1),
(2, 'unavailable', 2, 2),
(3, 'available', 4, NULL),
(4, 'available', 4, NULL),
(5, 'available', 4, NULL),
(6, 'available', 4, NULL),
(7, 'available', 4, NULL),
(8, 'available', 4, NULL),
(9, 'available', 4, NULL),
(10, 'available', 4, NULL);

