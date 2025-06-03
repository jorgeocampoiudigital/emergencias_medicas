package dominio;

public class Ambulancia {
    private final String id;
    private final String tipo;
    private boolean disponible;
    private String ubicacionActual;
    private Emergencia emergenciaAsignada;
    
    public Ambulancia(String id, String tipo, String ubicacionInicial) {
        this.id = id;
        this.tipo = tipo;
        this.disponible = true;
        this.ubicacionActual = ubicacionInicial;
    }
    
    // Getters y setters
    public String getId() { return id; }
    public String getTipo() { return tipo; }
    public boolean isDisponible() { return disponible; }
    public String getUbicacionActual() { return ubicacionActual; }
    
    public void setDisponible(boolean disponible) { this.disponible = disponible; }
    public void setUbicacionActual(String ubicacion) { this.ubicacionActual = ubicacion; }

    public Emergencia getEmergenciaAsignada() {
        return emergenciaAsignada;
    }

    public void setEmergenciaAsignada(Emergencia emergencia) {
        this.emergenciaAsignada = emergencia;
    }
    
    @Override
    public String toString() {
        return String.format("%s (%s) - %s", id, tipo, 
            disponible ? "Disponible" : "En uso");
    }
}