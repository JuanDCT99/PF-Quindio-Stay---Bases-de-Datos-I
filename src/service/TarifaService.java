package service;


import model.Tarifa;
import model.Temporada;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class TarifaService {

    public BigDecimal calcularTotal(
            Long idTipoHabitacion,
            LocalDate fechaEntrada,
            LocalDate fechaSalida,
            List<Tarifa> tarifas,
            List<Temporada> temporadas) {

        if (idTipoHabitacion == null
                || fechaEntrada == null
                || fechaSalida == null
                || !fechaSalida.isAfter(fechaEntrada)
                || tarifas == null
                || temporadas == null) {
            return null;
        }

        BigDecimal total = BigDecimal.ZERO;

        for (LocalDate fecha = fechaEntrada;
             fecha.isBefore(fechaSalida);
             fecha = fecha.plusDays(1)) {

            Temporada temporada = buscarTemporada(fecha, temporadas);

            if (temporada == null) {
                return null;
            }

            Tarifa tarifa = buscarTarifa(
                    idTipoHabitacion,
                    temporada.getIdTemporada(),
                    tarifas
            );

            if (tarifa == null) {
                return null;
            }

            total = total.add(tarifa.getPrecioNoche());
        }

        return total;
    }

    private Temporada buscarTemporada(
            LocalDate fecha,
            List<Temporada> temporadas) {

        for (Temporada temporada : temporadas) {
            if (fecha.isEqual(temporada.getFechaInicio())
                    || fecha.isAfter(temporada.getFechaInicio())) {

                if (fecha.isEqual(temporada.getFechaFin())
                        || fecha.isBefore(temporada.getFechaFin())) {
                    return temporada;
                }
            }
        }

        return null;
    }

    private Tarifa buscarTarifa(
            Long idTipoHabitacion,
            Long idTemporada,
            List<Tarifa> tarifas) {

        for (Tarifa tarifa : tarifas) {
            if (tarifa.getIdTipoHabitacion().equals(idTipoHabitacion)
                    && tarifa.getIdTemporada().equals(idTemporada)) {
                return tarifa;
            }
        }

        return null;
    }
}