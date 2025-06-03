package dominio;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import dominio.enumeraciones.EstadoEmergencia;
import dominio.enumeraciones.Gravedad;

public class Emergencia implements Comparable<Emergencia> {
    private final String id;
    private final Gravedad gravedad;
    private final LocalDateTime horaRecepcion;
    private final String ubicacion;
    private EstadoEmergencia estado;
    private LocalDateTime horaResolucion;
    
    public Emergencia(String id, Gravedad gravedad, String ubicacion) {
        this.id = id;
        this.gravedad = gravedad;
        this.ubicacion = ubicacion;
        this.horaRecepcion = LocalDateTime.now();
        this.estado = EstadoEmergencia.PENDIENTE;
    }
    
    // Getters
    public String getId() { return id; }
    public Gravedad getGravedad() { return gravedad; }
    public LocalDateTime getHoraRecepcion() { return horaRecepcion; }
    public LocalDateTime getHoraResolucion() { return horaResolucion; }
    public String getUbicacion() { return ubicacion; }
    public EstadoEmergencia getEstado() { return estado; }
    
    public void setEstado(EstadoEmergencia estado) {
        this.estado = estado;
    }

    public void marcarComoResuelta() {
        this.estado = EstadoEmergencia.RESUELTA;
        this.horaResolucion = LocalDateTime.now();
    }
    
    public long getTiempoAtencion() {
        if (horaResolucion == null) return 0;
        return Duration.between(horaRecepcion, horaResolucion).getSeconds();
    }
    
    @Override
    public int compareTo(Emergencia otra) {
        int comparacionGravedad = otra.gravedad.compareTo(this.gravedad);
        if (comparacionGravedad != 0) return comparacionGravedad;
        
        return this.horaRecepcion.compareTo(otra.horaRecepcion);
    }
    
    @Override
    public String toString() {
        return String.format("[%s] %s - %s (%s)", 
            id, gravedad, ubicacion, 
            horaRecepcion.format(DateTimeFormatter.ofPattern("HH:mm:ss")));
    }
}