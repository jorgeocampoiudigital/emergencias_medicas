package gestion;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import dominio.Ambulancia;
import dominio.Emergencia;
import dominio.enumeraciones.EstadoEmergencia;

public class GestorRecursos {
    private static GestorRecursos instancia;
    private final Map<String, Ambulancia> ambulancias;
    private final List<Emergencia> emergenciasCerradas;
    private final Lock bloqueoRecursos;
    private final Condition ambulanciaDisponible;
    
    private GestorRecursos() {
        this.ambulancias = new ConcurrentHashMap<>();
        this.emergenciasCerradas = new CopyOnWriteArrayList<>();
        this.bloqueoRecursos = new ReentrantLock();
        this.ambulanciaDisponible = bloqueoRecursos.newCondition();
        inicializarRecursos();
    }
    
    public static synchronized GestorRecursos getInstancia() {
        if (instancia == null) {
            instancia = new GestorRecursos();
        }
        return instancia;
    }
    
    private void inicializarRecursos() {
        ambulancias.put("AMB-001", new Ambulancia("AMB-001", "Básica", "Central"));
        ambulancias.put("AMB-002", new Ambulancia("AMB-002", "Avanzada", "Norte"));
        ambulancias.put("AMB-003", new Ambulancia("AMB-003", "Básica", "Sur"));
        ambulancias.put("AMB-004", new Ambulancia("AMB-004", "Avanzada", "Este"));
        ambulancias.put("AMB-005", new Ambulancia("AMB-005", "Básica", "Oeste"));
    }
    
    public Ambulancia asignarAmbulancia(Emergencia emergencia) throws InterruptedException {
        bloqueoRecursos.lock();
        try {
            while (getAmbulanciasDisponibles().isEmpty()) {
                ambulanciaDisponible.await(5, TimeUnit.SECONDS);
                if (getAmbulanciasDisponibles().isEmpty()) {
                    throw new InterruptedException("No hay ambulancias disponibles");
                }
            }
            
            Ambulancia ambulancia = getAmbulanciasDisponibles().get(0);
            ambulancia.setDisponible(false);
            return ambulancia;
        } finally {
            bloqueoRecursos.unlock();
        }
    }
    
    public void liberarAmbulancia(Ambulancia ambulancia, Emergencia emergencia) {
        bloqueoRecursos.lock();
        try {
            ambulancia.setDisponible(true);
            ambulancia.setUbicacionActual("Base");
            
            if (emergencia != null) {
                emergencia.setEstado(EstadoEmergencia.RESUELTA);
                emergenciasCerradas.add(emergencia);
            }
            
            ambulanciaDisponible.signalAll();
        } finally {
            bloqueoRecursos.unlock();
        }
    }
    
    public List<Ambulancia> getAmbulanciasDisponibles() {
        return ambulancias.values().stream()
                .filter(Ambulancia::isDisponible)
                .toList();
    }
    
    public List<Ambulancia> getAmbulancias() {
        return new ArrayList<>(ambulancias.values());
    }
    
    public List<Emergencia> getEmergenciasCerradas() {
        return new ArrayList<>(emergenciasCerradas);
    }
    
    public int contarAmbulanciasDisponibles() {
        bloqueoRecursos.lock();
        try {
            return (int) ambulancias.values().stream()
                    .filter(Ambulancia::isDisponible)
                    .count();
        } finally {
            bloqueoRecursos.unlock();
        }
    }

    public List<Emergencia> getEmergenciasAtendidas() {
        return new ArrayList<>(emergenciasCerradas);
    }

    public List<Emergencia> getEmergenciasEnProceso() {
        return ambulancias.values().stream()
            .filter(a -> !a.isDisponible())
            .map(a -> a.getEmergenciaAsignada())
            .filter(Objects::nonNull)
            .toList();
    }
}