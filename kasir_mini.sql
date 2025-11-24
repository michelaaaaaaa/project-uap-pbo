CREATE DATABASE kasir_mini;
USE kasir_mini;

CREATE TABLE users (
    id_users INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    nama VARCHAR(100) NOT NULL,
    role ENUM('admin', 'kasir') NOT NULL
);

CREATE TABLE barang (
    id_barang INT AUTO_INCREMENT PRIMARY KEY,
    nama_barang VARCHAR(100) NOT NULL,
    harga DOUBLE NOT NULL,
    stok INT NOT NULL
);

CREATE TABLE transaksi (
    id_transaksi INT AUTO_INCREMENT PRIMARY KEY,
    tanggal DATETIME NOT NULL,
    total DOUBLE NOT NULL,
    id_users INT NOT NULL,
    FOREIGN KEY (id_users) REFERENCES users(id_users)
);

CREATE TABLE detail_transaksi (
    id_detail INT AUTO_INCREMENT PRIMARY KEY,
    id_transaksi INT NOT NULL,
    id_barang INT NOT NULL,
    qty INT NOT NULL,
    harga_satuan DOUBLE NOT NULL,
    subtotal DOUBLE NOT NULL,
    FOREIGN KEY (id_transaksi) REFERENCES transaksi(id_transaksi),
    FOREIGN KEY (id_barang) REFERENCES barang(id_barang)
);

INSERT INTO users (username, password, nama, role) VALUES 
('admin', 'admin123', 'Administrator', 'admin'),
('kasir1', 'kasir123', 'Kasir Satu', 'kasir');
