package componentes;

import dominio.Ambulancia;
import dominio.Emergencia;
import dominio.enumeraciones.EstadoEmergencia;
import gestion.GestorRecursos;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Despachador implements Runnable {
    private final BlockingQueue<Emergencia> colaEmergencias;
    private final GestorRecursos gestorRecursos;
    private final ExecutorService executor;
    private volatile boolean enEjecucion = true;
    
    public Despachador(BlockingQueue<Emergencia> colaEmergencias) {
        this.colaEmergencias = colaEmergencias;
        this.gestorRecursos = GestorRecursos.getInstancia();
        this.executor = Executors.newFixedThreadPool(8);
    }
    
    @Override
    public void run() {
        while (enEjecucion) {
            try {
                Emergencia emergencia = colaEmergencias.take();
                executor.execute(() -> {
                    try {
                        procesarEmergencia(emergencia);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                });
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                enEjecucion = false;
            }
        }
    }
    
    private void procesarEmergencia(Emergencia emergencia) throws InterruptedException {
        emergencia.setEstado(EstadoEmergencia.ASIGNADA);
        
        Ambulancia ambulancia = gestorRecursos.asignarAmbulancia(emergencia);
        if (ambulancia != null) {
            try {
                ambulancia.setEmergenciaAsignada(emergencia);
                
                int tiempoBase = switch(emergencia.getGravedad()) {
                    case CRITICA -> 1000;
                    case GRAVE -> 3000;
                    case MODERADA -> 5000;
                    case LEVE -> 7000;
                };
                
                Thread.sleep(tiempoBase + (int)(Math.random() * 5000));
                
                emergencia.marcarComoResuelta();
            } finally {
                gestorRecursos.liberarAmbulancia(ambulancia, emergencia);
            }
        }
    }
    
    public void detener() {
        enEjecucion = false;
        executor.shutdownNow();
    }
}