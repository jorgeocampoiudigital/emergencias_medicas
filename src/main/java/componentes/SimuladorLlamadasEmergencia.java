package componentes;

import java.util.Random;
import java.util.concurrent.BlockingQueue;

import dominio.Emergencia;
import dominio.enumeraciones.Gravedad;

public class SimuladorLlamadasEmergencia implements Runnable {
    private final BlockingQueue<Emergencia> colaEmergencias;
    private final Random random = new Random();
    private int idLlamada = 1;

    public SimuladorLlamadasEmergencia(BlockingQueue<Emergencia> colaEmergencias) {
        this.colaEmergencias = colaEmergencias;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(1000 + random.nextInt(2000));
                Emergencia emergencia = new Emergencia(
                    "E-" + idLlamada++,
                    Gravedad.values()[random.nextInt(Gravedad.values().length)],
                    new String[]{"Norte", "Sur", "Este", "Oeste"}[random.nextInt(4)]
                );
                colaEmergencias.put(emergencia);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}