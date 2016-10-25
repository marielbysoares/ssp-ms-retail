CREATE TABLE store (
  store_id BIGINT NOT NULL AUTO_INCREMENT COMMENT 'Store identifier',
  name VARCHAR(255) NOT NULL COMMENT 'Store name',
  PRIMARY KEY (store_id)
);

CREATE TABLE product (
  product_id BIGINT NOT NULL AUTO_INCREMENT COMMENT 'Product identifier',
  store_id BIGINT NOT NULL COMMENT 'Store id referencing the store table',
  name VARCHAR(255) NOT NULL COMMENT 'Product name',
  description VARCHAR(255) NOT NULL COMMENT 'Product description',
  sku VARCHAR(10) NOT NULL COMMENT 'Product SKU (stock keeping unit)',
  price DECIMAL(5, 2) UNSIGNED NOT NULL COMMENT 'Product price as Decimal with 2 precision digits',
  PRIMARY KEY (product_id),
  FOREIGN KEY (store_id) REFERENCES store (store_id)
);

CREATE TABLE stock (
  product_id BIGINT NOT NULL COMMENT 'Product id of the stock referencing the product table',
  store_id BIGINT NOT NULL COMMENT 'Store id referencing the store table',
  count INTEGER UNSIGNED NOT NULL COMMENT 'Total count in stock',
  PRIMARY KEY (product_id),
  FOREIGN KEY (store_id) REFERENCES store (store_id),
  FOREIGN KEY (product_id) REFERENCES product (product_id)
);

CREATE TABLE purchase_order (
  order_id BIGINT NOT NULL AUTO_INCREMENT COMMENT 'Order identifier',
  store_id BIGINT NOT NULL COMMENT 'Store id referencing the store table',
  order_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Date of the order',
  first_name VARCHAR(255) NOT NULL COMMENT 'Purchaser first name',
  last_name VARCHAR(255) NOT NULL COMMENT 'Purchaser last name',
  email VARCHAR(255) NOT NULL COMMENT 'Purchaser email address',
  phone VARCHAR(10) NOT NULL COMMENT 'Purchaser phone number',
  status INTEGER NOT NULL COMMENT 'Status of the order',
  PRIMARY KEY (order_id),
  FOREIGN KEY (store_id) REFERENCES store (store_id)
);

CREATE TABLE purchase_order_product (
  order_product_id BIGINT NOT NULL AUTO_INCREMENT COMMENT 'Id of the product order item',
  order_id BIGINT NOT NULL COMMENT 'Order id referencing the purchase order table',
  product_id BIGINT NOT NULL COMMENT 'Product id referencing the product table',
  count INTEGER UNSIGNED NOT NULL COMMENT 'Amount of the product purchased',
  PRIMARY KEY (order_product_id),
  FOREIGN KEY (order_id) REFERENCES purchase_order (order_id),
  FOREIGN KEY (product_id) REFERENCES product (product_id)
);