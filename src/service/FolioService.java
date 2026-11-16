package service;

import model.Folio;
import model.Pago;
import model.Reserva;
import model.Sancion;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class FolioService {

    public BigDecimal calcularSaldo(
            Reserva reserva,
            List<Pago> pagos,
            List<Sancion> sanciones) {

        if (reserva == null || reserva.getPrecioTotal() == null) {
            return null;
        }

        BigDecimal total = reserva.getPrecioTotal();

        if (sanciones != null) {
            for (Sancion sancion : sanciones) {
                if (sancion != null
                        && reserva.getIdReserva().equals(sancion.getIdReserva())) {
                    total = total.add(sancion.getValor());
                }
            }
        }

        if (pagos != null) {
            for (Pago pago : pagos) {
                if (pago != null
                        && reserva.getIdReserva().equals(pago.getIdReserva())) {
                    total = total.subtract(pago.getMonto());
                }
            }
        }

        return total;
    }

    public boolean puedeRegistrarPago(
            Folio folio,
            Reserva reserva,
            Pago pago,
            List<Pago> pagos,
            List<Sancion> sanciones) {

        if (folio == null || reserva == null || pago == null
                || !"ABIERTO".equals(folio.getEstado())
                || !pago.esValido()
                || !reserva.getIdReserva().equals(folio.getIdReserva())
                || !reserva.getIdReserva().equals(pago.getIdReserva())) {
            return false;
        }

        BigDecimal saldo = calcularSaldo(reserva, pagos, sanciones);

        return saldo != null
                && saldo.compareTo(BigDecimal.ZERO) > 0
                && pago.getMonto().compareTo(saldo) <= 0;
    }

    public boolean cerrarFolio(
            Folio folio,
            BigDecimal saldoPendiente,
            boolean esAdministrador,
            LocalDateTime fechaCierre) {

        if (folio == null || saldoPendiente == null
                || fechaCierre == null
                || !"ABIERTO".equals(folio.getEstado())) {
            return false;
        }

        if (saldoPendiente.compareTo(BigDecimal.ZERO) > 0
                && !esAdministrador) {
            return false;
        }

        return folio.cerrar(fechaCierre);
    }
}