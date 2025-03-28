/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Funciones;

/**
 *
 * @author danilos
 */
public class HashTable {
    Entry head;  

    public HashTable() {
        head = null;
    }

    void add(String username, long pos) {
        Entry newEntry = new Entry(username, pos);
        if (head == null) {
            head = newEntry;
            return;
        }
        Entry current = head;
        while (current.next != null) {
        current = current.next;
        }
        current.next = newEntry;
    }

    void remove(String username) {
        if (head == null) return;
        
        if (head.username.equals(username)) {
            head = head.next;
            return;
        }
        Entry current = head;
        Entry previous = null;
        while (current != null) {
            if (current.username.equals(username)) {
                previous.next = current.next;
                return;
            }
            previous = current;
            current = current.next;
        }
    }

    long search(String username) {
        Entry current = head;
        while (current != null) {
            if (current.username.equals(username)) {
                return current.pos;
            }
            current = current.next;
        }
        return -1;
    }
}