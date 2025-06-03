package ui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.concurrent.PriorityBlockingQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.UIManager;

import componentes.Despachador;
import componentes.SimuladorLlamadasEmergencia;
import dominio.Emergencia;
import gestion.GestorRecursos;

public class MainFrame extends JFrame {
    private final PriorityBlockingQueue<Emergencia> colaEmergencias;
    private final GestorRecursos gestorRecursos;
    private final SimuladorLlamadasEmergencia simulador;

    public MainFrame() {
        super("Sistema de Gestión de Emergencias");
        this.gestorRecursos = GestorRecursos.getInstancia();
        this.colaEmergencias = new PriorityBlockingQueue<>();
        this.simulador = new SimuladorLlamadasEmergencia(colaEmergencias);

        configurarInterfaz();
        iniciarSistema();
    }

    private void configurarInterfaz() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setLayout(new GridLayout(1, 2));

        JPanel panelSuperior = new JPanel(new GridLayout(1, 2));
        panelSuperior.add(new PanelEmergencias(gestorRecursos));
        panelSuperior.add(new PanelAmbulancias(gestorRecursos));
        
        JPanel panelInferior = new JPanel(new GridLayout(1, 1));
        panelInferior.add(new PanelEmergenciasAtendidas(gestorRecursos));

        setLayout(new BorderLayout());
        add(panelSuperior, BorderLayout.CENTER);
        add(panelInferior, BorderLayout.SOUTH);

        Timer timer = new Timer(1000, e -> {
            ((PanelEmergencias)panelSuperior.getComponent(0)).actualizarDatos();
            ((PanelAmbulancias)panelSuperior.getComponent(1)).actualizarDatos();
            ((PanelEmergenciasAtendidas)panelInferior.getComponent(0)).actualizarDatos();
        });
        timer.start();
    }

    private void iniciarSistema() {
        new Thread(simulador).start();
        new Thread(new Despachador(colaEmergencias)).start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                new MainFrame().setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}