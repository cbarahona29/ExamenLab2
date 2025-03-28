package Funciones;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            PSNUsers psn = new PSNUsers("psn.dat");
            
            System.out.println("=== PRUEBA DE TODAS LAS FUNCIONES ===");
            
            System.out.println("\n1. Agregando usuarios...");
            psn.addUser("player1");
            psn.addUser("player2");
            psn.addUser("player3");
            System.out.println("Usuarios agregados correctamente.");
            
            System.out.println("\n2. Agregando trofeos...");
            psn.addTrophieTo("player1", "God of War", "Matar a Thor", Trophy.PLATINO);
            System.out.println("Trofeos agregados correctamente.");
            
            System.out.println("\n3. Informacion de jugador:");
            psn.playerInfo("player1");
            
            System.out.println("\n4. Desactivando usuario player3...");
            psn.deactivateUser("player3");
            System.out.println("Usuario desactivado correctamente.");
            
            System.out.println("\n5. Intentando agregar trofeo a usuario desactivado...");
            try {
                psn.addTrophieTo("player3", "Uncharted", "Encontrar tesoro", Trophy.BRONCE);
            } catch (IllegalArgumentException e) {
                System.out.println("Error esperado: " + e.getMessage());
            }
            
            System.out.println("\n6. Informacion de otro jugador:");
            psn.playerInfo("player2");
            
            psn.close();
            System.out.println("\n=== TODAS LAS PRUEBAS COMPLETADAS CON ÉXITO ===");
            
        } catch (IOException e) {
            System.err.println("Error de E/S: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}