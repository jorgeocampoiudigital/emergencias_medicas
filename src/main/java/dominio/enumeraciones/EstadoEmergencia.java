package dominio.enumeraciones;

public enum EstadoEmergencia {
    PENDIENTE, ASIGNADA, EN_PROGRESO, RESUELTA;
    
    @Override
    public String toString() {
        return name().charAt(0) + name().substring(1).toLowerCase();
    }
}