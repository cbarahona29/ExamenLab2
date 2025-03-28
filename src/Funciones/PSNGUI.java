package Funciones;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class PSNGUI extends JFrame {
    private PSNUsers psn;
    private JTextArea outputArea;
    
    public PSNGUI() {
        super("PSN Users Manager");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 500);
        setLayout(new BorderLayout());
        
        try {
            psn = new PSNUsers("psn.dat");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al inicializar PSN: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
        
        JPanel inputPanel = new JPanel(new GridLayout(6, 2, 5, 5));
        
        JLabel userLabel = new JLabel("Usuario:");
        JTextField userField = new JTextField();
        
        JLabel gameLabel = new JLabel("Juego (para trofeo):");
        JTextField gameField = new JTextField();
        
        JLabel trophyLabel = new JLabel("Nombre trofeo:");
        JTextField trophyField = new JTextField();
        
        JLabel typeLabel = new JLabel("Tipo de trofeo:");
        JComboBox<Trophy> typeCombo = new JComboBox<>(Trophy.values());
        
        JButton addUserBtn = new JButton("Agregar Usuario");
        JButton deactivateBtn = new JButton("Desactivar Usuario");
        JButton addTrophyBtn = new JButton("Agregar Trofeo");
        JButton infoBtn = new JButton("Información Jugador");
        
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);
        
        inputPanel.add(userLabel);
        inputPanel.add(userField);
        inputPanel.add(gameLabel);
        inputPanel.add(gameField);
        inputPanel.add(trophyLabel);
        inputPanel.add(trophyField);
        inputPanel.add(typeLabel);
        inputPanel.add(typeCombo);
        inputPanel.add(addUserBtn);
        inputPanel.add(deactivateBtn);
        inputPanel.add(addTrophyBtn);
        inputPanel.add(infoBtn);
        
        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        
       addUserBtn.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        String username = userField.getText().trim();
        if (username.isEmpty()) {
            showError("Ingrese un nombre de usuario");
            return;
        }
        
        try {
            psn.addUser(username);
            outputArea.append("Usuario '" + username + "' agregado exitosamente.\n");
        } catch (IOException ex) {
            showError("Error al agregar usuario: " + ex.getMessage());
        } catch (IllegalArgumentException ex) {
            if (ex.getMessage().equals("El usuario ya existe")) {
                JOptionPane.showMessageDialog(PSNGUI.this, 
                    "El nombre de usuario '" + username + "' ya está registrado.",
                    "Usuario existente",
                    JOptionPane.WARNING_MESSAGE);
            } else {
                showError(ex.getMessage());
            }
        }
    }
});

    deactivateBtn.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        String username = userField.getText().trim();
        if (username.isEmpty()) {
            showError("Ingrese un nombre de usuario");
            return;
        }
        
        try {
            psn.deactivateUser(username);
            outputArea.append("Usuario '" + username + "' desactivado exitosamente.\n");
        } catch (IOException ex) {
            showError("Error al desactivar usuario: " + ex.getMessage());
        } catch (IllegalArgumentException ex) {
            showError(ex.getMessage());
        }
    }
});

    addTrophyBtn.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        String username = userField.getText().trim();
        String game = gameField.getText().trim();
        String trophy = trophyField.getText().trim();
        
        if (username.isEmpty() || game.isEmpty() || trophy.isEmpty()) {
            showError("Complete todos los campos para agregar un trofeo");
            return;
        }
        
        Trophy type = (Trophy) typeCombo.getSelectedItem();
        
        try {
            psn.addTrophieTo(username, game, trophy, type);
            outputArea.append("Trofeo '" + trophy + "' agregado a '" + username + "'.\n");
        } catch (IOException ex) {
            showError("Error al agregar trofeo: " + ex.getMessage());
        } catch (IllegalArgumentException ex) {
            showError(ex.getMessage());
        }
    }
});

    infoBtn.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        String username = userField.getText().trim();
        if (username.isEmpty()) {
            showError("Ingrese un nombre de usuario");
            return;
        }
        
        try {
            outputArea.append("\n=== INFORMACIÓN DE " + username.toUpperCase() + " ===\n");
            String playerInfo = psn.playerInfo(username);
            outputArea.append(playerInfo);
            outputArea.append("=== FIN DE INFORMACIÓN ===\n\n");
            
            outputArea.setCaretPosition(outputArea.getDocument().getLength());
        } catch (IOException ex) {
            showError("Error al obtener información: " + ex.getMessage());
        } catch (IllegalArgumentException ex) {
            showError(ex.getMessage());
        }
    }
});
    }
    
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                PSNGUI gui = new PSNGUI();
                gui.setVisible(true);
            }
        });
    }
}