CREATE TABLE category (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    name NVARCHAR(100) NOT NULL UNIQUE
);

-- 2. Bảng product: Sản phẩm
CREATE TABLE product (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    name NVARCHAR(255) NOT NULL,
    barcode NVARCHAR(100) UNIQUE,
    category_id BIGINT REFERENCES category(id),
    quantity_in_stock INT NOT NULL DEFAULT 0,
    price DECIMAL(18,2) NOT NULL,
    expiry_date DATE NULL,
    manufacturing_date DATE NULL,
    import_price DECIMAL(18,2) NOT NULL DEFAULT 0.00
);

-- 3. Bảng inventory_log: Nhập/Xuất kho
CREATE TABLE inventory_log (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    product_id BIGINT NOT NULL REFERENCES product(id),
    event_type NVARCHAR(20) NOT NULL, -- 'IN' hoặc 'OUT'
    quantity INT NOT NULL,
    event_time DATETIME2 NOT NULL DEFAULT GETDATE(),
    note NVARCHAR(500) NULL
);

-- 4. Bảng retail_sale: Giao dịch bán lẻ
CREATE TABLE retail_sale (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    timestamp DATETIME2 NOT NULL DEFAULT GETDATE(),
    total_amount DECIMAL(18,2) NOT NULL,
    payment_method NVARCHAR(50) NOT NULL -- 'CASH' hoặc 'TRANSFER'
);

-- 5. Bảng retail_detail: Chi tiết sản phẩm trong bán lẻ
CREATE TABLE retail_detail (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    sale_id BIGINT NOT NULL REFERENCES retail_sale(id),
    product_id BIGINT NOT NULL REFERENCES product(id),
    quantity INT NOT NULL,
    unit_price DECIMAL(18,2) NOT NULL
);

-- 6. Bảng partner: Đối tác (nhà hàng/quán ăn)
CREATE TABLE partner (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    name NVARCHAR(255) NOT NULL,
    address NVARCHAR(255) NOT NULL,
    contact_info NVARCHAR(500) NULL
);

-- 7. Bảng partner_order: Hóa đơn bán buôn
CREATE TABLE partner_order (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    partner_id BIGINT NOT NULL REFERENCES partner(id),
    order_date DATETIME2 NOT NULL DEFAULT GETDATE(),
    total_amount DECIMAL(18,2) NOT NULL,
    payment_status NVARCHAR(50) NOT NULL -- 'PENDING', 'PAID', 'TRANSFERRED'
);

-- 8. Bảng partner_detail: Chi tiết sản phẩm trong đơn bán buôn
CREATE TABLE partner_detail (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    order_id BIGINT NOT NULL REFERENCES partner_order(id),
    product_id BIGINT NOT NULL REFERENCES product(id),
    quantity INT NOT NULL,
    unit_price DECIMAL(18,2) NOT NULL
);

-- 9. Bảng risk_record: Ghi nhận rủi ro
CREATE TABLE risk_record (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    event_time DATETIME2 NOT NULL DEFAULT GETDATE(),
    description NVARCHAR(500) NOT NULL,
    severity NVARCHAR(50) NULL
);

-- Categories
INSERT INTO category (name) VALUES
('Thực phẩm'), ('Đồ uống'), ('Đồ gia dụng');

-- Products
INSERT INTO product (name, barcode, category_id, quantity_in_stock, price, expiry_date, manufacturing_date, import_price) VALUES
('Gạo tám', '8901234567894', 1, 100, 15.50, '2025-12-31', '2024-01-01', 12.00),
('Nước suối 500ml', '8934567890123', 2, 200, 7.00, '2026-06-30', '2024-02-15', 5.00),
('Chai xịt đa năng', '8935678901234', 3, 50, 45.00, NULL, '2023-11-01', 30.00);

-- Partners
INSERT INTO partner (name, address, contact_info) VALUES
('Nhà hàng Hoa Viên', '123 Đường ABC, Quận 1', '0123456789'),	
('Quán Cafe Sáng', '456 Đường DEF, Quận 3', '0987654321');

-- Inventory Logs
INSERT INTO inventory_log (product_id, event_type, quantity, note) VALUES
(1, 'IN', 100, 'Nhập kho ban đầu'),
(2, 'IN', 200, 'Nhập kho ban đầu'),
(3, 'IN', 50, 'Nhập kho ban đầu');

Alter table product
add unit nvarchar(30)

select * from product