package service;

import model.Bloqueo;
import model.Habitacion;
import model.Reserva;
import model.ReservaHabitacion;

import java.time.LocalDate;
import java.util.List;

public class DisponibilidadService {

    public boolean estaDisponible(
            Habitacion habitacion,
            LocalDate fechaEntrada,
            LocalDate fechaSalida,
            List<ReservaHabitacion> reservasHabitacion,
            List<Bloqueo> bloqueos,
            List<Reserva> reservas) {

        if (habitacion == null
                || fechaEntrada == null
                || fechaSalida == null
                || !fechaSalida.isAfter(fechaEntrada)) {
            return false;
        }

        if (!habitacion.estaDisponible()) {
            return false;
        }

        if (hayBloqueo(habitacion, fechaEntrada, fechaSalida, bloqueos)) {
            return false;
        }

        return !hayReserva(habitacion, fechaEntrada, fechaSalida,
                reservasHabitacion, reservas);
    }

    private boolean hayBloqueo(
            Habitacion habitacion,
            LocalDate entrada,
            LocalDate salida,
            List<Bloqueo> bloqueos) {

        if (bloqueos == null) {
            return false;
        }

        for (Bloqueo bloqueo : bloqueos) {
            if (bloqueo.getIdHabitacion().equals(habitacion.getIdHabitacion())
                    && entrada.atStartOfDay().isBefore(bloqueo.getFechaFin())
                    && salida.atStartOfDay().isAfter(bloqueo.getFechaInicio())) {
                return true;
            }
        }

        return false;
    }

    private boolean hayReserva(
            Habitacion habitacion,
            LocalDate entrada,
            LocalDate salida,
            List<ReservaHabitacion> reservasHabitacion,
            List<Reserva> reservas) {

        if (reservasHabitacion == null || reservas == null) {
            return false;
        }

        for (ReservaHabitacion reservaHabitacion : reservasHabitacion) {

            if (!reservaHabitacion.getIdHabitacion()
                    .equals(habitacion.getIdHabitacion())) {
                continue;
            }

            Reserva reserva = buscarReserva(
                    reservaHabitacion.getIdReserva(), reservas);

            if (reserva == null
                    || "CANCELADA".equals(reserva.getEstado())
                    || "NO_SHOW".equals(reserva.getEstado())) {
                continue;
            }

            boolean seCruzan = entrada.isBefore(
                    reservaHabitacion.getFechaSalidaReservada())
                    && salida.isAfter(
                    reservaHabitacion.getFechaEntradaReservada());

            if (seCruzan) {
                return true;
            }
        }

        return false;
    }

    private Reserva buscarReserva(Long idReserva, List<Reserva> reservas) {

        for (Reserva reserva : reservas) {
            if (reserva.getIdReserva().equals(idReserva)) {
                return reserva;
            }
        }

        return null;
    }
}