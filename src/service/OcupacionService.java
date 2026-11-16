package service;

import model.Habitacion;
import model.Ocupacion;
import model.Reserva;
import model.ReservaHabitacion;

import java.util.List;

public class OcupacionService {

    public boolean validarOcupacion(
            Habitacion habitacion,
            ReservaHabitacion reservaHabitacion,
            Reserva reserva,
            List<Ocupacion> ocupaciones) {

        if (habitacion == null || reservaHabitacion == null
                || reserva == null || ocupaciones == null) {
            return false;
        }

        if (habitacion.getCapacidadMaxima() == null
                || reserva.getIdTitular() == null
                || reservaHabitacion.getIdReservaHabitacion() == null) {
            return false;
        }

        int cantidadPersonas = 0;
        int cantidadTitulares = 0;

        for (Ocupacion ocupacion : ocupaciones) {

            if (ocupacion == null
                    || !reservaHabitacion.getIdReservaHabitacion()
                    .equals(ocupacion.getIdReservaHabitacion())) {
                continue;
            }

            if (!ocupacion.esValido()) {
                return false;
            }

            cantidadPersonas++;

            if (ocupacion.esTitular()) {
                cantidadTitulares++;

                if (!reserva.getIdTitular().equals(ocupacion.getIdHuesped())) {
                    return false;
                }
            }
        }

        return cantidadPersonas > 0
                && cantidadPersonas <= habitacion.getCapacidadMaxima()
                && cantidadTitulares == 1;
    }
}