package ui;

import java.awt.BorderLayout;
import java.time.format.DateTimeFormatter;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import gestion.GestorRecursos;

public class PanelEmergenciasAtendidas extends JPanel {
    private final GestorRecursos gestorRecursos;
    private final JTable tabla;
    private final DefaultTableModel modelo;
    
    public PanelEmergenciasAtendidas(GestorRecursos gestorRecursos) {
        this.gestorRecursos = gestorRecursos;
        setBorder(BorderFactory.createTitledBorder("Emergencias Atendidas"));
        setLayout(new BorderLayout());
        
        // Configurar tabla
        modelo = new DefaultTableModel(
            new Object[]{"ID", "Gravedad", "Ubicación", "Hora Resolución", "Tiempo Atención"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tabla = new JTable(modelo);
        tabla.setAutoCreateRowSorter(true);
        
        add(new JScrollPane(tabla), BorderLayout.CENTER);
    }
    
    public void actualizarDatos() {
        modelo.setRowCount(0);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        gestorRecursos.getEmergenciasAtendidas().forEach(e -> {
            modelo.addRow(new Object[]{
                e.getId(),
                e.getGravedad(),
                e.getUbicacion(),
                e.getHoraResolucion().format(formatter),
                e.getTiempoAtencion()+ " seg"
            });
        });
    }
}