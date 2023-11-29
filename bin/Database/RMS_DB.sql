create table menu(
    item_name varchar(255) primary key,
    price int,
    descrip varchar(255)
);

create table reservation(
    email varchar(255) primary key,
    cust_name varchar(255) not null,
    reservation_time time,
    reservation_date date,
    numPeople int not null
);

CREATE TABLE orders (
    order_id INT PRIMARY KEY,
    reservation_id varchar(255) NOT NULL,
    FOREIGN KEY (reservation_id) REFERENCES reservation(email)
);

CREATE TABLE order_items (
    order_item_id INT PRIMARY KEY,
    order_id INT NOT NULL,
    menu_item_name VARCHAR(255) NOT NULL,
    quantity INT NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders(order_id),
    FOREIGN KEY (menu_item_name) REFERENCES menu(item_name)
);

delete from orders

ALTER TABLE order_items DROP PRIMARY KEY;

ALTER TABLE order_items MODIFY COLUMN order_item_id INT AUTO_INCREMENT PRIMARY KEY;

ALTER TABLE order_items AUTO_INCREMENT = 1;


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

INSERT INTO inventory (item_name, quantity)
SELECT item_name, 10 AS quantity
FROM menu;

INSERT INTO reservation (cust_name, email, reservation_time, reservation_date, numPeople) VALUES 
('John Doe', 'john@example.com', '12:00:00', '2023-11-24', 4),
('Jane Smith', 'jane@example.com', '18:30:00', '2023-11-25', 2);


