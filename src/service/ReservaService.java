package service;

import model.Reserva;
import model.ReservaHabitacion;

import java.time.LocalDateTime;
import java.util.List;

public class ReservaService {

    public boolean confirmar(
            Reserva reserva,
            List<ReservaHabitacion> habitaciones,
            LocalDateTime fechaActual) {

        if (reserva == null || habitaciones == null || fechaActual == null) {
            return false;
        }

        boolean tieneHabitaciones = false;

        for (ReservaHabitacion habitacion : habitaciones) {
            if (habitacion != null
                    && reserva.getIdReserva().equals(habitacion.getIdReserva())) {
                tieneHabitaciones = true;
                break;
            }
        }

        if (!tieneHabitaciones) {
            return false;
        }

        return reserva.confirmar(fechaActual);
    }

    public boolean cancelar(Reserva reserva) {
        if (reserva == null) {
            return false;
        }

        return reserva.cancelar();
    }

    public int cancelarVencidas(
            List<Reserva> reservas,
            LocalDateTime fechaActual) {

        if (reservas == null || fechaActual == null) {
            return 0;
        }

        int cantidadCanceladas = 0;

        for (Reserva reserva : reservas) {
            if (reserva != null
                    && reserva.cancelarSiEstaVencida(fechaActual)) {
                cantidadCanceladas++;
            }
        }

        return cantidadCanceladas;
    }
}