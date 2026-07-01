-- src/test/resources/schema.sql
CREATE TABLE carro (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    modelo VARCHAR(255),
    ano INTEGER NOT NULL
);