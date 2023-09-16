
DROP SEQUENCE borrow_seq;

CREATE SEQUENCE borrow_seq
	INCREMENT BY 1
	START WITH 10000
	MAXVALUE 19999;

DROP SEQUENCE item_seq;

CREATE SEQUENCE item_seq
	INCREMENT BY 1
	START WITH 40000
	MAXVALUE 49999;

DROP SEQUENCE market_seq;

CREATE SEQUENCE market_seq
	INCREMENT BY 1
	START WITH 1
	MAXVALUE 9999;

DROP SEQUENCE order_seq;

CREATE SEQUENCE order_seq
	INCREMENT BY 1
	START WITH 30000
	MAXVALUE 39999;

DROP SEQUENCE review_seq;

CREATE SEQUENCE review_seq
	INCREMENT BY 1
	START WITH 20000
	MAXVALUE 29999;

DROP TABLE CART CASCADE CONSTRAINTS PURGE;

DROP TABLE ORDER_GOODS CASCADE CONSTRAINTS PURGE;

DROP TABLE GOODS CASCADE CONSTRAINTS PURGE;

DROP TABLE BORROW_RESERVATION CASCADE CONSTRAINTS PURGE;

DROP TABLE BORROW CASCADE CONSTRAINTS PURGE;

DROP TABLE REVIEW CASCADE CONSTRAINTS PURGE;

DROP TABLE ORDERS CASCADE CONSTRAINTS PURGE;

DROP TABLE ALERT CASCADE CONSTRAINTS PURGE;

DROP TABLE SHARE_THING CASCADE CONSTRAINTS PURGE;

DROP TABLE ITEM CASCADE CONSTRAINTS PURGE;

DROP TABLE MARKET CASCADE CONSTRAINTS PURGE;

DROP TABLE IMAGE CASCADE CONSTRAINTS PURGE;

DROP TABLE IMAGE_CATEGORY CASCADE CONSTRAINTS PURGE;

DROP TABLE MEMBER CASCADE CONSTRAINTS PURGE;

CREATE TABLE MEMBER
(
	member_id            VARCHAR2(20) NOT NULL ,
	password             VARCHAR2(20) NOT NULL ,
	name                 VARCHAR2(20) NOT NULL ,
	birth                DATE NOT NULL ,
	address              VARCHAR2(500) NOT NULL ,
	address_detail       VARCHAR2(300) NULL ,
	zip                  VARCHAR(100) NOT NULL ,
	phone                VARCHAR2(20) NOT NULL ,
	email                VARCHAR2(50) NOT NULL  CONSTRAINT  email_ck CHECK (email LIKE '%@%'),
	point                NUMBER DEFAULT  0  NOT NULL  CONSTRAINT  point_ck CHECK (point >= 0),
CONSTRAINT  XPKMEMBER PRIMARY KEY (member_id)
);

CREATE TABLE IMAGE_CATEGORY
(
	category_id          VARCHAR2(50) NOT NULL ,
	title                VARCHAR2(50) NOT NULL ,
CONSTRAINT  XPKIMAGE_CATEGORY PRIMARY KEY (category_id)
);


CREATE TABLE IMAGE
(
	member_id            VARCHAR2(20) NOT NULL ,
	category_id          VARCHAR2(50) NOT NULL ,
	image_src            VARCHAR2(500) NOT NULL ,
CONSTRAINT  XPKIMAGE PRIMARY KEY (member_id,category_id,image_src),
CONSTRAINT attatch FOREIGN KEY (member_id) REFERENCES MEMBER (member_id) ON DELETE CASCADE,
CONSTRAINT include3 FOREIGN KEY (category_id) REFERENCES IMAGE_CATEGORY (category_id)
);

CREATE TABLE MARKET
(
	market_id            NUMBER NOT NULL ,
	name                 VARCHAR2(20) NOT NULL ,
	intro                VARCHAR2(500) NULL ,
	contactable_time     VARCHAR2(100) DEFAULT  '00:00 ~ 23:59'  NOT NULL ,
	policy               VARCHAR2(1000) NULL ,
	precaution           VARCHAR2(1000) NULL ,
	open_status          CHAR(1) NOT NULL  CONSTRAINT  market_open_ck CHECK (open_status = '0' OR open_status = '1'),
	member_id            VARCHAR2(20) NOT NULL ,
CONSTRAINT  XPKMARKET PRIMARY KEY (market_id),
CONSTRAINT manage1 FOREIGN KEY (member_id) REFERENCES MEMBER (member_id) ON DELETE CASCADE
);


CREATE TABLE ITEM
(
	item_id              VARCHAR2(50) NOT NULL ,
	name                 VARCHAR2(50) NOT NULL ,
	upload_date          DATE NOT NULL ,
	description          VARCHAR2(500) NOT NULL ,
	open_status          NUMBER NOT NULL  CONSTRAINT  item_open_ck CHECK (open_status BETWEEN 0 AND 1),
	market_id            NUMBER NULL ,
CONSTRAINT  XPKITEM PRIMARY KEY (item_id),
CONSTRAINT own FOREIGN KEY (market_id) REFERENCES MARKET (market_id) ON DELETE SET NULL
);

CREATE TABLE SHARE_THING
(
	item_id              VARCHAR2(50) NOT NULL ,
	precaution           VARCHAR2(1000) NULL ,
	is_borrowed          CHAR(1) DEFAULT  '0'  NOT NULL  CONSTRAINT  share_thing_borrowed_ck CHECK (is_borrowed = '0' OR is_borrowed = '1'),
CONSTRAINT  XPKSHARE_THING PRIMARY KEY (item_id),
CONSTRAINT include2 FOREIGN KEY (item_id) REFERENCES ITEM (item_id) ON DELETE CASCADE
);


CREATE TABLE ALERT
(
	share_thing_id      VARCHAR2(50) NOT NULL ,
	member_id            VARCHAR2(20) NOT NULL ,
	alert_date           DATE NOT NULL ,
	is_read		CHAR(1) DEFAULT  '0'  NOT NULL  CONSTRAINT  alert_is_read_ck CHECK (is_read = '0' OR is_read = '1'),
CONSTRAINT  XPKALERT PRIMARY KEY (share_thing_id,member_id),
CONSTRAINT include7 FOREIGN KEY (share_thing_id) REFERENCES SHARE_THING (item_id),
CONSTRAINT include5 FOREIGN KEY (member_id) REFERENCES MEMBER (member_id) ON DELETE CASCADE
);

CREATE TABLE ORDERS
(
	order_id             NUMBER NOT NULL ,
	order_date           DATE NOT NULL ,
	total_price          NUMBER NOT NULL  CONSTRAINT  order_price_ck CHECK (total_price >= 0),
	card_company         VARCHAR2(50) NOT NULL ,
	card_number          VARCHAR2(50) NOT NULL ,
	expiry_date          DATE NOT NULL ,
	tracking_number      VARCHAR2(50) NULL ,
	order_status         NUMBER DEFAULT  1  NOT NULL  CONSTRAINT  order_status_ck CHECK (order_status BETWEEN 0 AND 2), 
	seller_id            VARCHAR2(20) NULL ,
	buyer_id             VARCHAR2(20) NULL ,
	used_point           NUMBER DEFAULT  0  NOT NULL ,
CONSTRAINT  XPKORDER PRIMARY KEY (order_id),
CONSTRAINT purchase1 FOREIGN KEY (seller_id) REFERENCES MEMBER (member_id) ON DELETE SET NULL,
CONSTRAINT purchase2 FOREIGN KEY (buyer_id) REFERENCES MEMBER (member_id) ON DELETE SET NULL
);

CREATE TABLE REVIEW
(
	review_id            NUMBER NOT NULL ,
	write_date           DATE NOT NULL ,
	content              VARCHAR2(1000) NOT NULL ,
	rating               NUMBER NOT NULL  CONSTRAINT  review_ck CHECK (rating BETWEEN 1 AND 5),
	order_id             NUMBER NOT NULL ,
	item_id              VARCHAR2(50) NOT NULL ,
CONSTRAINT  XPKREVIEW PRIMARY KEY (review_id),
CONSTRAINT evaluate FOREIGN KEY (order_id) REFERENCES ORDERS (order_id),
CONSTRAINT include12 FOREIGN KEY (item_id) REFERENCES ITEM (item_id) 
);

CREATE TABLE BORROW
(
	borrow_id            NUMBER NOT NULL ,
	borrow_date          DATE NOT NULL ,
	return_date          DATE NOT NULL ,
	borrow_status        CHAR(1) DEFAULT  '1'  NOT NULL  CONSTRAINT  borrow_ck CHECK (borrow_status = '0' OR borrow_status = 1),
	is_extended          CHAR(1) DEFAULT  '0'  NOT NULL  CONSTRAINT  borrow_extended_ck CHECK (is_extended = '0' OR is_extended = '1'),
	lender_id            VARCHAR2(20) NULL ,
	borrower_id          VARCHAR2(20) NULL ,
	share_thing_id      VARCHAR2(50) NOT NULL ,
CONSTRAINT  XPKBORROW PRIMARY KEY (borrow_id),
CONSTRAINT include4 FOREIGN KEY (lender_id) REFERENCES MEMBER (member_id) ON DELETE SET NULL,
CONSTRAINT do FOREIGN KEY (borrower_id) REFERENCES MEMBER (member_id) ON DELETE SET NULL,
CONSTRAINT include11 FOREIGN KEY (share_thing_id) REFERENCES SHARE_THING (item_id)
);


CREATE TABLE BORROW_RESERVATION
(
	share_thing_id      VARCHAR2(50) NOT NULL ,
	member_id            VARCHAR2(20) NOT NULL ,
	is_first_booker      CHAR(1) NOT NULL  CONSTRAINT  first_booker_ck CHECK (is_first_booker = '0' OR is_first_booker = '1'),
CONSTRAINT  XPKBORROW_RESERVATION PRIMARY KEY (share_thing_id,member_id),
CONSTRAINT include6 FOREIGN KEY (share_thing_id) REFERENCES SHARE_THING (item_id),
CONSTRAINT reserve FOREIGN KEY (member_id) REFERENCES MEMBER (member_id) ON DELETE CASCADE
);

CREATE TABLE GOODS
(
	item_id              VARCHAR2(50) NOT NULL ,
	tags                 VARCHAR2(500) NULL ,
	price                NUMBER NOT NULL  CONSTRAINT  price_ck CHECK (price >= 0),
	goods_size           VARCHAR2(100) NULL ,
	sales_quantity       NUMBER NOT NULL  CONSTRAINT  sales_quantity_ck CHECK (sales_quantity >= 0),
	remain_quantity      NUMBER NOT NULL  CONSTRAINT  remain_quantity_ck CHECK (remain_quantity >= 0),
	shipping_fee         NUMBER NOT NULL  CONSTRAINT  shipping_fee_ck CHECK (shipping_fee >= 0),
CONSTRAINT  XPKGOODS PRIMARY KEY (item_id),
CONSTRAINT include1 FOREIGN KEY (item_id) REFERENCES ITEM (item_id) ON DELETE CASCADE
);

CREATE TABLE ORDER_GOODS
(
	order_id             NUMBER NOT NULL ,
	goods_id             VARCHAR2(50) NOT NULL ,
	order_quantity       NUMBER NOT NULL  CONSTRAINT  order_quantity_ck CHECK (order_quantity >= 0),
	price                NUMBER NOT NULL  CONSTRAINT  order_goods_price_ck CHECK (price >= 0)
CONSTRAINT  XPKORDER_GOODS PRIMARY KEY (order_id,goods_id),
CONSTRAINT include10 FOREIGN KEY (order_id) REFERENCES ORDERS (order_id),
CONSTRAINT include9 FOREIGN KEY (goods_id) REFERENCES GOODS (item_id)
);

CREATE TABLE CART
(
	member_id            VARCHAR2(20) NOT NULL ,
	goods_id             VARCHAR2(50) NOT NULL ,
	quantity             NUMBER NOT NULL  CONSTRAINT  cart_quantity_ck CHECK (quantity >= 0),
	price                NUMBER NOT NULL  CONSTRAINT  cart_price_ck CHECK (price >= 0),
CONSTRAINT  XPKCART PRIMARY KEY (member_id,goods_id),
CONSTRAINT manage2 FOREIGN KEY (member_id) REFERENCES MEMBER (member_id) ON DELETE CASCADE,
CONSTRAINT include8 FOREIGN KEY (goods_id) REFERENCES GOODS (item_id)
);

