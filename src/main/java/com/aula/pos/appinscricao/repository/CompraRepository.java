package com.aula.pos.appinscricao.repository;

import com.aula.pos.appinscricao.model.Compra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface CompraRepository extends JpaRepository<Compra, UUID> {
    List<Compra> findByClienteIdOrderByDataCompraDesc(UUID clienteId);

    @Query("""
            select sum(c.valorFinalBRL)
            from Compra c
            where c.cliente.id = :clienteId
              and c.dataCompra >= :inicio
              and c.dataCompra < :fim
            """)
    BigDecimal somarValorFinalNoPeriodo(
            @Param("clienteId") UUID clienteId,
            @Param("inicio") LocalDateTime inicio,
            @Param("fim") LocalDateTime fim
    );
}
