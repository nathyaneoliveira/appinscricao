CREATE TABLE tb_clientes (
    id BINARY(16) NOT NULL,
    nome VARCHAR(150) NOT NULL,
    email VARCHAR(255) NOT NULL,

    CONSTRAINT pk_tb_clientes PRIMARY KEY (id),
    CONSTRAINT uk_tb_clientes_email UNIQUE (email)
);

CREATE TABLE tb_compras (
    id BINARY(16) NOT NULL,
    cliente_id BINARY(16) NOT NULL,
    valor_usd DECIMAL(19, 2) NOT NULL,
    cotacao_utilizada DECIMAL(19, 6) NOT NULL,
    valor_convertido DECIMAL(19, 2) NOT NULL,
    taxa_servico DECIMAL(19, 2) NOT NULL,
    tributo_simulado DECIMAL(19, 2) NOT NULL,
    valor_final_brl DECIMAL(19, 2) NOT NULL,
    data_cotacao DATE NOT NULL,
    data_compra DATETIME(6) NOT NULL,

    CONSTRAINT pk_tb_compras PRIMARY KEY (id),
    CONSTRAINT fk_tb_compras_cliente FOREIGN KEY (cliente_id) REFERENCES tb_clientes(id),
    INDEX idx_compra_cliente_data (cliente_id, data_compra)
);
