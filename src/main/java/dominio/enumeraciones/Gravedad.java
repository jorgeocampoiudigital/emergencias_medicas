package dominio.enumeraciones;

public enum Gravedad {
    CRITICA, GRAVE, MODERADA, LEVE;
    
    @Override
    public String toString() {
        return name().charAt(0) + name().substring(1).toLowerCase();
    }
}