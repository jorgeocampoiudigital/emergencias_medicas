package ui;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import dominio.Ambulancia;
import gestion.GestorRecursos;

public class PanelAmbulancias extends JPanel {
    private final GestorRecursos gestorRecursos;
    private final DefaultTableModel modelo;

    public PanelAmbulancias(GestorRecursos gestorRecursos) {
        this.gestorRecursos = gestorRecursos;
        this.modelo = new DefaultTableModel(new Object[]{"ID", "Tipo", "Estado"}, 0);
        
        setLayout(new BorderLayout());
        JTable tabla = new JTable(modelo);
        add(new JScrollPane(tabla), BorderLayout.CENTER);
        setBorder(BorderFactory.createTitledBorder("Ambulancias"));
    }

    public void actualizarDatos() {
        modelo.setRowCount(0);
        List<Ambulancia> ambulancias = gestorRecursos.getAmbulancias();
        ambulancias.forEach(ambulancia -> {
            modelo.addRow(new Object[]{
                ambulancia.getId(),
                ambulancia.getTipo(),
                ambulancia.isDisponible() ? "Disponible" : "En uso"
            });
        });
    }
}