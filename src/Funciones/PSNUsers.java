package Funciones;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PSNUsers {
    private RandomAccessFile psnFile;
    private HashTable users;

    public PSNUsers(String filename) throws IOException {
        this.psnFile = new RandomAccessFile(filename, "rw");
        this.users = new HashTable();
        reloadHashTable();
    }

    private void reloadHashTable() throws IOException {
        psnFile.seek(0);
        while (psnFile.getFilePointer() < psnFile.length()) {
            long pos = psnFile.getFilePointer();
            String line = psnFile.readLine();
            if (line == null) break;
            
            if (!line.startsWith("*")) {
                String[] parts = line.split(",");
                if (parts.length > 0) {
                    String username = parts[0];
                    users.add(username, pos);
                }
            }
        }
    }

    public void addUser(String username) throws IOException {
        if (users.search(username) != -1) {
            throw new IllegalArgumentException("El usuario ya existe");
        }
        
        long pos = psnFile.length();
        psnFile.seek(pos);
        String record = username + ",0,0\n";
        psnFile.writeBytes(record);
        
        users.add(username, pos);
    }

   public void deactivateUser(String username) throws IOException {
    long pos = users.search(username);
    if (pos == -1) {
        throw new IllegalArgumentException("Usuario no encontrado");
    }
    
    psnFile.seek(pos);
    psnFile.writeByte('*');
    
    users.remove(username);
    
    psnFile.getFD().sync();
}

    public void addTrophieTo(String username, String trophyGame, String trophyName, Trophy type) throws IOException {
    long pos = users.search(username);
    if (pos == -1) {
        throw new IllegalArgumentException("Usuario no encontrado");
    }
    
    psnFile.seek(pos);
    String userRecord = psnFile.readLine();
    String[] parts = userRecord.split(",");
    int currentPoints = Integer.parseInt(parts[1]);
    int currentTrophies = Integer.parseInt(parts[2]);
    
    currentPoints += type.getPoints();
    currentTrophies++;
    
    String updatedRecord = username + "," + currentPoints + "," + currentTrophies + "\n";
    psnFile.seek(pos);
    psnFile.writeBytes(updatedRecord);
    
    long trophyPos = psnFile.length();
    psnFile.seek(trophyPos);
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    String date = sdf.format(new Date());
    String trophyRecord = username + "," + type.name() + "," + trophyGame + "," + trophyName + "," + date + "\n";
    psnFile.writeBytes(trophyRecord);
}

    public String playerInfo(String username) throws IOException {
    // Primero verificar si el usuario existe en la tabla hash (usuarios activos)
    if (users.search(username) == -1) {
        throw new IllegalArgumentException("Usuario no encontrado o cuenta desactivada");
    }
    
    long pos = users.search(username);
    StringBuilder info = new StringBuilder();
    
    // Leer datos del usuario
    psnFile.seek(pos);
    String userRecord = psnFile.readLine();
    String[] parts = userRecord.split(",");
    
    info.append("Usuario: ").append(parts[0]).append("\n");
    info.append("Puntos: ").append(parts[1]).append("\n");
    info.append("Trofeos: ").append(parts[2]).append("\n");
    
    // Solo mostrar trofeos si tiene alguno
    if (Integer.parseInt(parts[2]) > 0) {
        info.append("\nTrofeos obtenidos:\n");
        psnFile.seek(0);
        while (psnFile.getFilePointer() < psnFile.length()) {
            long trophyPos = psnFile.getFilePointer();
            String line = psnFile.readLine();
            if (line == null) break;
            
            if (!line.startsWith("*") && line.startsWith(username + ",") && line.split(",").length > 3) {
                String[] trophyParts = line.split(",");
                if (trophyParts.length >= 5) {
                    info.append(trophyParts[4]).append(" - ").append(trophyParts[1]).append(" - ")
                       .append(trophyParts[2]).append(" - ").append(trophyParts[3]).append("\n");
                }
            }
        }
    }
    
    return info.toString();
}
 
    public void close() throws IOException {
        psnFile.close();
    }
}