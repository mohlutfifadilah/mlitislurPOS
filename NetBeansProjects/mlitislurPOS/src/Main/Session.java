/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Main;

/**
 *
 * @author mac
 */
public class Session {
    // Variabel statis untuk menyimpan data session
    private static String nama_admin;
    private static boolean isLoggedIn = false;

    // Getter dan Setter untuk nama_admin
    public static String getNamaAdmin() {
        return nama_admin;
    }

    public static void setNamaAdmin(String nama_admin) {
        Session.nama_admin = nama_admin;
    }
    
    // Getter dan Setter untuk status login
    public static boolean isLoggedIn() {
        return isLoggedIn;
    }

    public static void setLoggedIn(boolean isLoggedIn) {
        Session.isLoggedIn = isLoggedIn;
    }

    // Metode untuk menghapus session saat logout
    public static void logout() {
        Session.nama_admin = null;
        Session.isLoggedIn = false;
    }
}