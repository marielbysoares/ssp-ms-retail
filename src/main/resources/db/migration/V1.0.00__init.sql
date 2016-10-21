CREATE TABLE store (
  store_id BIGINT NOT NULL AUTO_INCREMENT,
  name VARCHAR(255) NOT NULL,
  PRIMARY KEY (store_id)
);

CREATE TABLE product (
  product_id BIGINT NOT NULL AUTO_INCREMENT,
  store_id BIGINT NOT NULL,
  name VARCHAR(255) NOT NULL,
  description VARCHAR(255) NOT NULL,
  sku VARCHAR(10) NOT NULL,
  price DECIMAL(5, 2) UNSIGNED NOT NULL,
  PRIMARY KEY (product_id),
  FOREIGN KEY (store_id) REFERENCES store (store_id)
);

CREATE TABLE stock (
  store_id BIGINT NOT NULL,
  product_id BIGINT NOT NULL,
  count INTEGER UNSIGNED NOT NULL,
  PRIMARY KEY (product_id),
  FOREIGN KEY (store_id) REFERENCES store (store_id),
  FOREIGN KEY (product_id) REFERENCES product (product_id)
);

CREATE TABLE purchase_order (
  order_id BIGINT NOT NULL AUTO_INCREMENT,
  store_id BIGINT NOT NULL,
  order_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  first_name VARCHAR(255) NOT NULL,
  last_name VARCHAR(255) NOT NULL,
  email VARCHAR(255) NOT NULL,
  phone VARCHAR(10) NOT NULL,
  status INTEGER NOT NULL,
  PRIMARY KEY (order_id),
  FOREIGN KEY (store_id) REFERENCES store (store_id)
);

CREATE TABLE purchase_order_product (
  order_product_id BIGINT NOT NULL AUTO_INCREMENT,
  order_id BIGINT NOT NULL,
  product_id BIGINT NOT NULL,
  count INTEGER UNSIGNED NOT NULL,
  PRIMARY KEY (order_product_id),
  FOREIGN KEY (order_id) REFERENCES purchase_order (order_id),
  FOREIGN KEY (product_id) REFERENCES product (product_id)
);