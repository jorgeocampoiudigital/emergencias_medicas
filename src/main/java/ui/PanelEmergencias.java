package ui;

import gestion.GestorRecursos;
import java.awt.BorderLayout;
import java.time.format.DateTimeFormatter;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class PanelEmergencias extends JPanel {
    private final GestorRecursos gestorRecursos;
    private final JTable tabla;
    private final DefaultTableModel modelo;

    public PanelEmergencias(GestorRecursos gestorRecursos) {
      this.gestorRecursos = gestorRecursos;  
      setBorder(BorderFactory.createTitledBorder("Emergencias En Proceso"));
      setLayout(new BorderLayout());
        
      // Configurar tabla
      modelo = new DefaultTableModel(
          new Object[]{"ID", "Gravedad", "Ubicación", "Hora Recepción", "Estado"}, 0) {
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
        gestorRecursos.getEmergenciasEnProceso().forEach(e -> {
            modelo.addRow(new Object[]{
                e.getId(),
                e.getGravedad(),
                e.getUbicacion(),
                e.getHoraRecepcion().format(formatter),
                e.getEstado()
            });
        });
    }
}