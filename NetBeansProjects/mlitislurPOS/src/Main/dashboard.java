/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Main;

/**
 *
 * @author mac
 */

import javax.swing.*;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.table.DefaultTableModel;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class dashboard extends javax.swing.JFrame {

    /**
     * Creates new form dashboard
     */
    
    // deklarasi
    Connection con;
    Statement stat;
    ResultSet rs;
    String sql;
    
    private String selectedId; // Variabel untuk menyimpan ID dari tabel
    
    public dashboard() {
        initComponents();
        //pemanggilan fungsi koneksi database yang sudah kita buat pada class koneksi.java
        conn DB = new conn();
        DB.config();
        con = DB.con;
        stat = DB.stm;
        
        // Ambil nama admin dari session dan tampilkan
        sessionNama.setText(Session.getNamaAdmin());
        
        tampilAdmin();
        
        showTipeLayanan();
        tampilLayanan();
        
        showTipePelanggan();
        tampilPelanggan();
        
        tampilInvoice();
        
        // Order
        adminOrderField.setText(Session.getNamaAdmin());
        loadComboBoxOrder();
    }
    
    private void showTipeLayanan(){
        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/praktikumjava", "praktikumjava", "Praktikumj4va!");
            Statement stm = con.createStatement();
            ResultSet rs = stm.executeQuery("SELECT jenis_kendaraan from tipe");
                    
            while(rs.next()) {
                String jenis_kendaraan = rs.getString("jenis_kendaraan");
                cmbBoxTipeLayanan.addItem(jenis_kendaraan);
            }
                    
            con.close();
            }catch(Exception e){
                    
        }
    }
    
    private void showTipePelanggan(){
        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/praktikumjava", "praktikumjava", "Praktikumj4va!");
            Statement stm = con.createStatement();
            ResultSet rs = stm.executeQuery("SELECT jenis_kendaraan from tipe");
                    
            while(rs.next()) {
                String jenis_kendaraan = rs.getString("jenis_kendaraan");
                cmbBoxTipePelanggan.addItem(jenis_kendaraan);
            }
                    
            con.close();
            }catch(Exception e){
                    
        }
    }
    
    public void tampilAdmin(){
        DefaultTableModel tabel = new DefaultTableModel();
        tabel.addColumn("no");
        tabel.addColumn("Nama Admin");
        tabel.addColumn("No Whatsapp");
        try {
            java.sql.Connection conn = (java.sql.Connection)con;
            String sql = "select nama_admin, no_whatsapp from admin";
            java.sql.PreparedStatement pst = conn.prepareStatement(sql);
            java.sql.ResultSet rs = pst.executeQuery(sql);
            int no = 1;
            while(rs.next()){
                tabel.addRow(new Object[] {
                    no++,
                    rs.getString(1),
                    rs.getString(2),
                });
            } tableAdmin.setModel(tabel);
        } catch (Exception e) {
            
        }
    }
    
    public void tampilLayanan(){
        DefaultTableModel tabel = new DefaultTableModel();
        tabel.addColumn("No");
        tabel.addColumn("Tipe");
        tabel.addColumn("Nama Layanan");
        tabel.addColumn("Harga");
        try {
            java.sql.Connection conn = (java.sql.Connection)con;
            // Query dengan JOIN untuk menampilkan jenis kendaraan
            String sql = "SELECT t.jenis_kendaraan, l.nama_layanan, l.harga " + "FROM layanan l JOIN tipe t ON l.id_tipe = t.id";
            java.sql.PreparedStatement pst = con.prepareStatement(sql);
            java.sql.ResultSet rs = pst.executeQuery();
            int no = 1;
            // Tambahkan data ke tabel
            while(rs.next()){
                tabel.addRow(new Object[] {
                    no++,
                    rs.getString(1), // jenis kendaraan
                    rs.getString(2), // tipe layanan
                    rs.getString(3), // Harga
                });
            }
            tableLayanan.setModel(tabel);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }

    public void tampilPelanggan(){
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("No");
        model.addColumn("Jenis");
        model.addColumn("No Kendaraan");
        model.addColumn("Nama");
        model.addColumn("No Whatsapp");
        model.addColumn("Alamat");

        try {
            String sql = "SELECT tipe.jenis_kendaraan, kendaraan.no_kendaraan, pelanggan.nama_pelanggan, pelanggan.no_whatsapp, pelanggan.alamat " +
                         "FROM pelanggan " +
                         "JOIN kendaraan ON pelanggan.id_kendaraan = kendaraan.id " +
                         "JOIN tipe ON kendaraan.id_tipe = tipe.id";
            java.sql.PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            int no = 1;
            while (rs.next()) {
                model.addRow(new Object[]{
                    no++,
                    rs.getString("jenis_kendaraan"),
                    rs.getString("no_kendaraan"),
                    rs.getString("nama_pelanggan"),
                    rs.getString("no_whatsapp"),
                    rs.getString("alamat")
                });
            }
            tablePelanggan.setModel(model);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
    
    public void tampilInvoice(){
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("No");
        model.addColumn("Admin");
        model.addColumn("Pelanggan");
        model.addColumn("Layanan");
        model.addColumn("No Plat");
        model.addColumn("Harga");
        model.addColumn("Waktu");

        try {
            // Query untuk mengambil data invoice
            String sql = """
                SELECT 
                    i.id AS id_invoice,
                    a.nama_admin,
                    p.nama_pelanggan,
                    p.no_whatsapp,
                    l.nama_layanan,
                    k.no_kendaraan,
                    l.harga,
                    i.metode_pembayaran,
                    i.created_at
                FROM 
                    invoice i
                JOIN 
                    admin a ON i.id_admin = a.id
                JOIN 
                    pelanggan p ON i.id_pelanggan = p.id
                JOIN 
                    kendaraan k ON p.id_kendaraan = k.id
                JOIN 
                    layanan l ON i.id_layanan = l.id
                ORDER BY 
                    i.created_at DESC
            """;
            java.sql.PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            int no = 1;
            while (rs.next()) {
                model.addRow(new Object[]{
                    no++,
                    rs.getString("nama_admin"),
                    rs.getString("nama_pelanggan") + "(" + rs.getString("no_whatsapp") + ")",
                    rs.getString("nama_layanan"),
                    rs.getString("no_kendaraan"),
                    "Rp " + rs.getString("harga") + "(" + rs.getString("metode_pembayaran") + ")",
                    rs.getString("created_at"),
                });
            }
            tableInvoice.setModel(model);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPopupMenu1 = new javax.swing.JPopupMenu();
        jPanel1 = new javax.swing.JPanel();
        tabbedPaneCustom1 = new tabbed.TabbedPaneCustom();
        jPanel5 = new javax.swing.JPanel();
        tabbedPaneCustom2 = new tabbed.TabbedPaneCustom();
        jPanel6 = new javax.swing.JPanel();
        addAdminButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableAdmin = new javax.swing.JTable();
        editAdminButton = new javax.swing.JButton();
        deleteAdminButton = new javax.swing.JButton();
        resetAdminButton = new javax.swing.JButton();
        Adminnama_adminField = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        Adminno_whatsappField = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        namaLayananField = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        hargaLayananField = new javax.swing.JTextField();
        deleteLayananButton = new javax.swing.JButton();
        editLayananButton = new javax.swing.JButton();
        addLayananButton = new javax.swing.JButton();
        resetLayananButton = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tableLayanan = new javax.swing.JTable();
        jLabel8 = new javax.swing.JLabel();
        cmbBoxTipeLayanan = new javax.swing.JComboBox<>();
        jPanel8 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        namaPelangganField = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        noPelangganField = new javax.swing.JTextField();
        deletePelangganButton = new javax.swing.JButton();
        editPelangganButton = new javax.swing.JButton();
        addPelangganButton = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        tablePelanggan = new javax.swing.JTable();
        jLabel12 = new javax.swing.JLabel();
        cmbBoxTipePelanggan = new javax.swing.JComboBox<>();
        jLabel13 = new javax.swing.JLabel();
        noKendaraanPelangganField = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        alamatPelangganField = new javax.swing.JTextField();
        searchPelangganField = new javax.swing.JTextField();
        searchPelangganButton = new javax.swing.JButton();
        resetPelangganButton = new javax.swing.JButton();
        jLabel16 = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tableInvoice = new javax.swing.JTable();
        searchPelangganButton1 = new javax.swing.JButton();
        searchPelangganField1 = new javax.swing.JTextField();
        jLabel26 = new javax.swing.JLabel();
        resetPelangganButton1 = new javax.swing.JButton();
        addPelangganButton1 = new javax.swing.JButton();
        editPelangganButton1 = new javax.swing.JButton();
        jLabel17 = new javax.swing.JLabel();
        deleteInvoiceButton = new javax.swing.JButton();
        searchInvoiceButton = new javax.swing.JButton();
        searchInvoiceField = new javax.swing.JTextField();
        jLabel31 = new javax.swing.JLabel();
        resetInvoiceButton = new javax.swing.JButton();
        jPanel11 = new javax.swing.JPanel();
        jPanel13 = new javax.swing.JPanel();
        jPanel14 = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        cmbBoxLayananOrder = new javax.swing.JComboBox<>();
        jLabel15 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        adminOrderField = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        cmbBoxPelangganOrder = new javax.swing.JComboBox<>();
        jLabel23 = new javax.swing.JLabel();
        cmbBoxMetodeOrder = new javax.swing.JComboBox<>();
        resetOrderButton = new javax.swing.JButton();
        submitOrderButton = new javax.swing.JButton();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        lblJenisDetailOrder = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        lblNoKendaraanDetailOrder = new javax.swing.JLabel();
        lblNoAlamatDetailOrder = new javax.swing.JLabel();
        lblAdminDetailOrder = new javax.swing.JLabel();
        lblNamaDetailOrder = new javax.swing.JLabel();
        lblLayananDetailOrder = new javax.swing.JLabel();
        lblHargaDetailOrder = new javax.swing.JLabel();
        lblTotalDetailOrder = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        sessionNama = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        logoutButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tabbedPaneCustom2.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tabbedPaneCustom2FocusGained(evt);
            }
        });
        tabbedPaneCustom2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabbedPaneCustom2MouseClicked(evt);
            }
        });

        addAdminButton.setText("Tambah");
        addAdminButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addAdminButtonActionPerformed(evt);
            }
        });

        tableAdmin.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "No", "Nama Admin", "No Whatsapp"
            }
        ));
        tableAdmin.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableAdminMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tableAdmin);

        editAdminButton.setText("Edit");
        editAdminButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editAdminButtonActionPerformed(evt);
            }
        });

        deleteAdminButton.setText("Hapus");
        deleteAdminButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteAdminButtonActionPerformed(evt);
            }
        });

        resetAdminButton.setText("Reset");
        resetAdminButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetAdminButtonActionPerformed(evt);
            }
        });

        jLabel2.setText("Nama Admin");

        jLabel3.setText("No Whatsapp");

        jLabel4.setFont(new java.awt.Font("Helvetica Neue", 1, 36)); // NOI18N
        jLabel4.setText("Data Admin");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 303, Short.MAX_VALUE)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(Adminnama_adminField, javax.swing.GroupLayout.DEFAULT_SIZE, 153, Short.MAX_VALUE)
                            .addComponent(Adminno_whatsappField))))
                .addGap(253, 253, 253)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(addAdminButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(resetAdminButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(editAdminButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(deleteAdminButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(49, 49, 49))
            .addComponent(jScrollPane1)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(resetAdminButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(addAdminButton)
                        .addGap(12, 12, 12)
                        .addComponent(editAdminButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(deleteAdminButton)
                        .addGap(30, 30, 30))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(Adminnama_adminField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(Adminno_whatsappField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 416, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        tabbedPaneCustom2.addTab("Admin", jPanel6);

        jLabel5.setFont(new java.awt.Font("Helvetica Neue", 1, 36)); // NOI18N
        jLabel5.setText("Data Layanan");

        jLabel6.setText("Nama Layanan");

        jLabel7.setText("Harga");

        deleteLayananButton.setText("Hapus");
        deleteLayananButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteLayananButtonActionPerformed(evt);
            }
        });

        editLayananButton.setText("Edit");
        editLayananButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editLayananButtonActionPerformed(evt);
            }
        });

        addLayananButton.setText("Tambah");
        addLayananButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addLayananButtonActionPerformed(evt);
            }
        });

        resetLayananButton.setText("Reset");
        resetLayananButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetLayananButtonActionPerformed(evt);
            }
        });

        tableLayanan.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "No", "Tipe", "Nama Layanan", "Harga"
            }
        ));
        tableLayanan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableLayananMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tableLayanan);

        jLabel8.setText("Tipe");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbBoxTipeLayanan, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addComponent(jLabel7))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(namaLayananField)
                            .addComponent(hargaLayananField, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(46, 46, 46))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(addLayananButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(resetLayananButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(editLayananButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(deleteLayananButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(49, 49, 49))
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 639, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(resetLayananButton)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(addLayananButton)
                        .addGap(12, 12, 12)
                        .addComponent(editLayananButton))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(namaLayananField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6)
                            .addComponent(jLabel8)
                            .addComponent(cmbBoxTipeLayanan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(hargaLayananField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(deleteLayananButton)
                .addGap(30, 30, 30)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 401, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        tabbedPaneCustom2.addTab("Layanan", jPanel7);

        jLabel9.setFont(new java.awt.Font("Helvetica Neue", 1, 36)); // NOI18N
        jLabel9.setText("Data Pelanggan");

        jLabel10.setText("Nama Pelanggan");

        jLabel11.setText("No Whatsapp");

        deletePelangganButton.setText("Hapus");
        deletePelangganButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deletePelangganButtonActionPerformed(evt);
            }
        });

        editPelangganButton.setText("Edit");
        editPelangganButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editPelangganButtonActionPerformed(evt);
            }
        });

        addPelangganButton.setText("Tambah");
        addPelangganButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addPelangganButtonActionPerformed(evt);
            }
        });

        tablePelanggan.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "No", "Jenis", "No Kendaraan", "Nama", "No Whatsapp", "Alamat"
            }
        ));
        tablePelanggan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablePelangganMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tablePelanggan);

        jLabel12.setText("Tipe");

        jLabel13.setText("No Kendaraan");

        jLabel14.setText("Alamat");

        searchPelangganField.setToolTipText("Contoh : R3902AB");

        searchPelangganButton.setText("Cari");
        searchPelangganButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchPelangganButtonActionPerformed(evt);
            }
        });

        resetPelangganButton.setText("Reset");
        resetPelangganButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetPelangganButtonActionPerformed(evt);
            }
        });

        jLabel16.setFont(new java.awt.Font("Helvetica Neue", 0, 10)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(255, 0, 51));
        jLabel16.setText("Contoh : R3902AB");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(132, 132, 132))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel9Layout.createSequentialGroup()
                                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(cmbBoxTipePelanggan, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(jPanel9Layout.createSequentialGroup()
                                .addComponent(jLabel13)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(noKendaraanPelangganField, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(26, 26, 26)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel9Layout.createSequentialGroup()
                                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED))
                            .addGroup(jPanel9Layout.createSequentialGroup()
                                .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(4, 4, 4)))
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(noPelangganField)
                            .addComponent(namaPelangganField)
                            .addComponent(alamatPelangganField, javax.swing.GroupLayout.DEFAULT_SIZE, 149, Short.MAX_VALUE))
                        .addGap(33, 33, 33)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(addPelangganButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(editPelangganButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(deletePelangganButton, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(resetPelangganButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(107, 107, 107))))
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 640, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel16)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(searchPelangganField, javax.swing.GroupLayout.PREFERRED_SIZE, 328, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(searchPelangganButton, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(217, 217, 217))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel9)
                .addGap(12, 12, 12)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel9Layout.createSequentialGroup()
                                .addGap(35, 35, 35)
                                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(noPelangganField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel11)))
                            .addGroup(jPanel9Layout.createSequentialGroup()
                                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel12)
                                    .addComponent(cmbBoxTipePelanggan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(noKendaraanPelangganField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel13)))
                            .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(namaPelangganField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel10)
                                .addComponent(resetPelangganButton)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(alamatPelangganField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel14))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 39, Short.MAX_VALUE)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(searchPelangganField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(searchPelangganButton))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel16)
                        .addGap(23, 23, 23))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addComponent(addPelangganButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(editPelangganButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(deletePelangganButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 323, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        searchPelangganField.getAccessibleContext().setAccessibleName("Contoh : R3902AB");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 688, Short.MAX_VALUE)
            .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel8Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, 653, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 606, Short.MAX_VALUE)
            .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel8Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        tabbedPaneCustom2.addTab("Pelanggan", jPanel8);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabbedPaneCustom2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabbedPaneCustom2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        tabbedPaneCustom1.addTab("Data", jPanel5);

        tableInvoice.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "No", "Admin", "Layanan", "Nama", "Metode Pembayaran", "Status"
            }
        ));
        tableInvoice.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableInvoiceMouseClicked(evt);
            }
        });
        jScrollPane5.setViewportView(tableInvoice);

        searchPelangganButton1.setText("Cari");
        searchPelangganButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchPelangganButton1ActionPerformed(evt);
            }
        });

        searchPelangganField1.setToolTipText("Contoh : R3902AB");

        jLabel26.setFont(new java.awt.Font("Helvetica Neue", 0, 10)); // NOI18N
        jLabel26.setForeground(new java.awt.Color(255, 0, 51));
        jLabel26.setText("Contoh : R3902AB");

        resetPelangganButton1.setText("Reset");
        resetPelangganButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetPelangganButton1ActionPerformed(evt);
            }
        });

        addPelangganButton1.setText("Tambah");
        addPelangganButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addPelangganButton1ActionPerformed(evt);
            }
        });

        editPelangganButton1.setText("Edit");
        editPelangganButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editPelangganButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 641, Short.MAX_VALUE)
            .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel12Layout.createSequentialGroup()
                    .addGap(54, 54, 54)
                    .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel12Layout.createSequentialGroup()
                            .addGap(456, 456, 456)
                            .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(addPelangganButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(editPelangganButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(resetPelangganButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                            .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel26)
                                .addGroup(jPanel12Layout.createSequentialGroup()
                                    .addComponent(searchPelangganField1, javax.swing.GroupLayout.PREFERRED_SIZE, 328, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(searchPelangganButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGap(110, 110, 110)))
                    .addContainerGap(54, Short.MAX_VALUE)))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel12Layout.createSequentialGroup()
                    .addGap(199, 199, 199)
                    .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel12Layout.createSequentialGroup()
                            .addComponent(resetPelangganButton1)
                            .addGap(68, 68, 68)
                            .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(searchPelangganField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(searchPelangganButton1))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jLabel26))
                        .addGroup(jPanel12Layout.createSequentialGroup()
                            .addGap(35, 35, 35)
                            .addComponent(addPelangganButton1)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(editPelangganButton1)))
                    .addContainerGap(107, Short.MAX_VALUE)))
        );

        jLabel17.setFont(new java.awt.Font("Helvetica Neue", 1, 36)); // NOI18N
        jLabel17.setText("Invoice");

        deleteInvoiceButton.setText("Hapus");
        deleteInvoiceButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteInvoiceButtonActionPerformed(evt);
            }
        });

        searchInvoiceButton.setText("Cari");
        searchInvoiceButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchInvoiceButtonActionPerformed(evt);
            }
        });

        jLabel31.setFont(new java.awt.Font("Helvetica Neue", 0, 10)); // NOI18N
        jLabel31.setForeground(new java.awt.Color(255, 0, 51));
        jLabel31.setText("Contoh : R0983AB");

        resetInvoiceButton.setText("Reset");
        resetInvoiceButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetInvoiceButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel17, javax.swing.GroupLayout.DEFAULT_SIZE, 256, Short.MAX_VALUE)
                .addGap(431, 431, 431))
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(131, 131, 131)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(jLabel31)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(searchInvoiceField)
                        .addGap(18, 18, 18)
                        .addComponent(searchInvoiceButton)
                        .addGap(94, 94, 94)
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(deleteInvoiceButton)
                            .addComponent(resetInvoiceButton))
                        .addGap(29, 29, 29))))
            .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                    .addContainerGap(26, Short.MAX_VALUE)
                    .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(26, Short.MAX_VALUE)))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(resetInvoiceButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(deleteInvoiceButton)
                        .addGap(35, 35, 35))
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(jLabel17)
                        .addGap(77, 77, 77)
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(searchInvoiceButton)
                            .addComponent(searchInvoiceField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel31)
                .addGap(0, 480, Short.MAX_VALUE))
            .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                    .addContainerGap(169, Short.MAX_VALUE)
                    .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(36, Short.MAX_VALUE)))
        );

        tabbedPaneCustom1.addTab("Invoice", jPanel10);

        jPanel13.setBackground(new java.awt.Color(255, 204, 102));

        jLabel18.setFont(new java.awt.Font("Helvetica Neue", 1, 36)); // NOI18N
        jLabel18.setText("Order");

        jLabel15.setText("Layanan");

        jLabel21.setText("Admin");

        adminOrderField.setEditable(false);

        jLabel22.setText("Pelanggan");

        jLabel23.setText("Metode Pembayaran");

        resetOrderButton.setText("Reset");
        resetOrderButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetOrderButtonActionPerformed(evt);
            }
        });

        submitOrderButton.setText("Submit");
        submitOrderButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                submitOrderButtonActionPerformed(evt);
            }
        });

        jLabel24.setFont(new java.awt.Font("Helvetica Neue", 1, 12)); // NOI18N
        jLabel24.setText("Detail Order");

        jLabel25.setFont(new java.awt.Font("Helvetica Neue", 1, 13)); // NOI18N
        jLabel25.setText("Admin :");

        jLabel27.setFont(new java.awt.Font("Helvetica Neue", 1, 13)); // NOI18N
        jLabel27.setText("Jenis");

        jLabel28.setFont(new java.awt.Font("Helvetica Neue", 1, 13)); // NOI18N
        jLabel28.setText("Nama Layanan");

        jLabel29.setFont(new java.awt.Font("Helvetica Neue", 1, 13)); // NOI18N
        jLabel29.setText("Harga");

        jLabel30.setFont(new java.awt.Font("Helvetica Neue", 1, 13)); // NOI18N
        jLabel30.setText("Total");

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel14Layout.createSequentialGroup()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addGap(273, 273, 273)
                        .addComponent(jLabel24))
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel14Layout.createSequentialGroup()
                                .addComponent(jLabel25)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblAdminDetailOrder, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(lblNoKendaraanDetailOrder, javax.swing.GroupLayout.PREFERRED_SIZE, 279, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblNamaDetailOrder, javax.swing.GroupLayout.PREFERRED_SIZE, 279, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel14Layout.createSequentialGroup()
                                .addComponent(jLabel27)
                                .addGap(203, 203, 203)
                                .addComponent(jLabel28))
                            .addGroup(jPanel14Layout.createSequentialGroup()
                                .addComponent(lblJenisDetailOrder, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(88, 88, 88)
                                .addComponent(lblLayananDetailOrder, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel14Layout.createSequentialGroup()
                        .addComponent(jLabel29)
                        .addGap(90, 90, 90))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel14Layout.createSequentialGroup()
                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel14Layout.createSequentialGroup()
                                .addComponent(jLabel30)
                                .addGap(47, 47, 47)
                                .addComponent(lblTotalDetailOrder, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(lblHargaDetailOrder, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(47, 47, 47))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel14Layout.createSequentialGroup()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel14Layout.createSequentialGroup()
                                .addComponent(jLabel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(87, 87, 87))
                            .addGroup(jPanel14Layout.createSequentialGroup()
                                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel14Layout.createSequentialGroup()
                                        .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(cmbBoxMetodeOrder, 0, 255, Short.MAX_VALUE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel14Layout.createSequentialGroup()
                                        .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(83, 83, 83)
                                        .addComponent(cmbBoxPelangganOrder, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addGroup(jPanel14Layout.createSequentialGroup()
                                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(83, 83, 83)
                                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(adminOrderField)
                                            .addComponent(cmbBoxLayananOrder, 0, 255, Short.MAX_VALUE))))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(resetOrderButton)
                                    .addComponent(submitOrderButton)))))
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(lblNoAlamatDetailOrder, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(76, 76, 76))
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel18)
                .addGap(31, 31, 31)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel21)
                    .addComponent(adminOrderField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbBoxLayananOrder, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15))
                .addGap(18, 18, 18)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbBoxPelangganOrder, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel22)
                    .addComponent(resetOrderButton))
                .addGap(18, 18, 18)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbBoxMetodeOrder, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel23)
                    .addComponent(submitOrderButton))
                .addGap(37, 37, 37)
                .addComponent(jLabel24)
                .addGap(15, 15, 15)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel25, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblAdminDetailOrder, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblNamaDetailOrder, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblNoAlamatDetailOrder))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel14Layout.createSequentialGroup()
                        .addComponent(lblNoKendaraanDetailOrder, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(32, 32, 32)
                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel27)
                            .addComponent(jLabel28)
                            .addComponent(jLabel29))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblJenisDetailOrder, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(lblHargaDetailOrder)
                                .addComponent(lblLayananDetailOrder, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(55, 55, 55)
                        .addComponent(lblTotalDetailOrder, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel30, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(416, 416, 416))
        );

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 687, Short.MAX_VALUE)
            .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel13Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, 675, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 610, Short.MAX_VALUE)
            .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
                    .addContainerGap(19, Short.MAX_VALUE)
                    .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, 572, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(19, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(28, Short.MAX_VALUE))
        );

        tabbedPaneCustom1.addTab("Order", jPanel11);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabbedPaneCustom1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabbedPaneCustom1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 6, -1, -1));

        jLabel19.setFont(new java.awt.Font("Helvetica Neue", 1, 18)); // NOI18N
        jLabel19.setText("Selamat Datang!, ");

        jLabel20.setFont(new java.awt.Font("Helvetica Neue", 1, 14)); // NOI18N
        jLabel20.setText("MlitisLur!");

        sessionNama.setEditable(false);
        sessionNama.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sessionNamaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel19)
                    .addComponent(jLabel20)
                    .addComponent(sessionNama, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(32, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jLabel19)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel20)
                .addGap(24, 24, 24)
                .addComponent(sessionNama, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(138, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(669, 6, 265, -1));

        logoutButton.setText("Logout");
        logoutButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logoutButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(logoutButton, javax.swing.GroupLayout.DEFAULT_SIZE, 218, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(291, Short.MAX_VALUE)
                .addComponent(logoutButton)
                .addGap(28, 28, 28))
        );

        getContentPane().add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(704, 312, 230, 342));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void logoutButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logoutButtonActionPerformed
        // TODO add your handling code here:
        // Logout session
            Session.logout();
            JOptionPane.showMessageDialog(this, "Logout berhasil!");
            
            new LoginForm().setVisible(true); // Kembali ke form login
            dispose(); // Tutup dashboard
    }//GEN-LAST:event_logoutButtonActionPerformed
    
    // Validasi Input
    public boolean validateInputPelanggan() {
        if (cmbBoxTipePelanggan.getSelectedItem() == null || namaPelangganField.getText().isEmpty() || noKendaraanPelangganField.getText().isEmpty() ||
            noPelangganField.getText().isEmpty() || alamatPelangganField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Semua field harus diisi!", "Validasi", JOptionPane.WARNING_MESSAGE);
            searchPelangganField.setText("");
            return false;
        }
        return true;
    }
    
    private void sessionNamaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sessionNamaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_sessionNamaActionPerformed

    private void submitOrderButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_submitOrderButtonActionPerformed
        // TODO add your handling code here:
        String admin = adminOrderField.getText();
        String layanan = cmbBoxLayananOrder.getSelectedItem().toString();
        String pelanggan = cmbBoxPelangganOrder.getSelectedItem().toString();
        String metodePembayaran = cmbBoxMetodeOrder.getSelectedItem().toString();

        try {
            // Ambil ID admin berdasarkan nama
            String getAdminIdQuery = "SELECT id FROM admin WHERE nama_admin = ?";
            java.sql.PreparedStatement psAdmin = con.prepareStatement(getAdminIdQuery);
            psAdmin.setString(1, admin);
            ResultSet rsAdmin = psAdmin.executeQuery();

            if (!rsAdmin.next()) {
                JOptionPane.showMessageDialog(null, "Admin dengan nama " + admin + " tidak ditemukan.");
                return;
            }
            int adminId = rsAdmin.getInt("id");

            // Ambil ID layanan berdasarkan nama layanan
            String getLayananIdQuery = "SELECT id FROM layanan WHERE nama_layanan = ?";
            java.sql.PreparedStatement psLayanan = con.prepareStatement(getLayananIdQuery);
            psLayanan.setString(1, layanan);
            ResultSet rsLayanan = psLayanan.executeQuery();

            if (!rsLayanan.next()) {
                JOptionPane.showMessageDialog(null, "Layanan dengan nama " + layanan + " tidak ditemukan.");
                return;
            }
            int layananId = rsLayanan.getInt("id");

            // Ambil ID pelanggan berdasarkan nama pelanggan
            String getPelangganIdQuery = "SELECT id FROM pelanggan WHERE nama_pelanggan = ?";
            java.sql.PreparedStatement psPelanggan = con.prepareStatement(getPelangganIdQuery);
            psPelanggan.setString(1, pelanggan);
            ResultSet rsPelanggan = psPelanggan.executeQuery();

            if (!rsPelanggan.next()) {
                JOptionPane.showMessageDialog(null, "Pelanggan dengan nama " + pelanggan + " tidak ditemukan.");
                return;
            }
            int pelangganId = rsPelanggan.getInt("id");

            // Insert ke tabel invoice
            String insertOrder = "INSERT INTO invoice (id_admin, id_layanan, id_pelanggan, metode_pembayaran, is_paid, created_at) VALUES (?, ?, ?, ?, ?, ?)";
            java.sql.PreparedStatement psOrder = con.prepareStatement(insertOrder);

            psOrder.setInt(1, adminId);
            psOrder.setInt(2, layananId);
            psOrder.setInt(3, pelangganId);
            psOrder.setString(4, metodePembayaran);
            psOrder.setInt(5, 1);
            // Mendapatkan waktu sekarang
            LocalDateTime now = LocalDateTime.now();
            Timestamp timestamp = Timestamp.valueOf(now);
            psOrder.setTimestamp(6, timestamp);
            psOrder.executeUpdate();

            String query = """
            SELECT
            i.id AS id_invoice,
            a.nama_admin,
            p.nama_pelanggan,
            p.alamat AS no_alamat,
            p.no_whatsapp AS no_whatsapp,
            k.no_kendaraan,
            t.jenis_kendaraan,
            l.nama_layanan,
            l.harga,
            i.metode_pembayaran,
            i.is_paid,
            i.created_at
            FROM
            invoice i
            JOIN
            admin a ON i.id_admin = a.id
            JOIN
            pelanggan p ON i.id_pelanggan = p.id
            JOIN
            kendaraan k ON p.id_kendaraan = k.id
            JOIN
            tipe t ON k.id_tipe = t.id
            JOIN
            layanan l ON i.id_layanan = l.id
            ORDER BY
            i.created_at DESC
            LIMIT 1;
            """;

            java.sql.PreparedStatement psQuery = con.prepareStatement(query);
            ResultSet rs = psQuery.executeQuery();

            if (rs.next()) {

                lblAdminDetailOrder.setText(rs.getString("nama_admin"));
                lblNamaDetailOrder.setText(rs.getString("nama_pelanggan"));
                lblNoAlamatDetailOrder.setText(rs.getString("no_whatsapp") + " / " + rs.getString("no_alamat"));
                lblNoKendaraanDetailOrder.setText(rs.getString("no_kendaraan"));
                lblJenisDetailOrder.setText(rs.getString("jenis_kendaraan"));
                lblLayananDetailOrder.setText(rs.getString("nama_layanan"));
                lblHargaDetailOrder.setText("Rp " + rs.getInt("harga") + "(" + rs.getString("metode_pembayaran") + ")");
                lblTotalDetailOrder.setText("Rp " + rs.getInt("harga"));
            } else {
                JOptionPane.showMessageDialog(null, "Tidak ada data order terbaru.");
            }

            JOptionPane.showMessageDialog(null, "Order berhasil disimpan!");
            selectedId = null;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error saat menyimpan order: " + e.getMessage());
        }
    }//GEN-LAST:event_submitOrderButtonActionPerformed

    private void resetOrderButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetOrderButtonActionPerformed
        // TODO add your handling code here:
        // Mengosongkan semua input
        cmbBoxLayananOrder.setSelectedIndex(0);
        cmbBoxPelangganOrder.setSelectedIndex(0);
        cmbBoxMetodeOrder.setSelectedIndex(0);

        lblAdminDetailOrder.setText(null);
        lblNamaDetailOrder.setText(null);
        lblNoAlamatDetailOrder.setText(null);
        lblNoKendaraanDetailOrder.setText(null);
        lblJenisDetailOrder.setText(null);
        lblLayananDetailOrder.setText(null);
        lblHargaDetailOrder.setText(null);
        lblTotalDetailOrder.setText(null);
        selectedId = null;
    }//GEN-LAST:event_resetOrderButtonActionPerformed

    private void resetInvoiceButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetInvoiceButtonActionPerformed
        // TODO add your handling code here:
        try {
            tampilInvoice(); // Kembalikan model asli
            searchInvoiceField.setText("");       // Kosongkan field pencarian
            JOptionPane.showMessageDialog(null, "Data telah direset!");
            selectedId = null;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }//GEN-LAST:event_resetInvoiceButtonActionPerformed

    private void searchInvoiceButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchInvoiceButtonActionPerformed
        DefaultTableModel originalModel = new DefaultTableModel(); // Untuk menyimpan semua data asli
        DefaultTableModel filteredModel = new DefaultTableModel(); // Untuk menyimpan data yang difilter
        filteredModel.addColumn("No");
        filteredModel.addColumn("Admin");
        filteredModel.addColumn("Layanan");
        filteredModel.addColumn("Nama");
        filteredModel.addColumn("No Kendaraan");
        filteredModel.addColumn("Metode Pembayaran");
        filteredModel.addColumn("Status");

        try {
            // Ambil semua data dari tabel invoice
            String sql = "SELECT i.id, a.nama_admin, l.nama_layanan, p.nama_pelanggan, p.no_whatsapp, k.no_kendaraan, l.harga, i.metode_pembayaran, i.created_at " +
            "FROM invoice i " +
            "JOIN admin a ON i.id_admin = a.id " +
            "JOIN pelanggan p ON i.id_pelanggan = p.id " +
            "JOIN kendaraan k ON p.id_kendaraan = k.id " +
            "JOIN layanan l ON i.id_layanan = l.id";
            java.sql.PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            int rowIndex = 0;
            boolean isFound = false;
            int no = 1;

            // Reset original model
            originalModel.setRowCount(0);

            while (rs.next()) {
                Object[] rowData = new Object[]{
                    no++,
                    rs.getString("nama_admin"),
                    rs.getString("nama_pelanggan") + "(" + rs.getString("no_whatsapp") + ")",
                    rs.getString("nama_layanan"),
                    rs.getString("no_kendaraan"),
                    "Rp " + rs.getString("harga") + "(" + rs.getString("metode_pembayaran") + ")",
                    rs.getString("created_at"),
                };

                originalModel.addRow(rowData); // Simpan ke model asli

                // Jika no_kendaraan cocok
                if (rs.getString("no_kendaraan").equalsIgnoreCase(searchInvoiceField.getText())) {
                    isFound = true;
                    filteredModel.addRow(rowData);
                }
                rowIndex++;
            }

            // Tampilkan filteredModel jika ditemukan, jika tidak tampilkan pesan
            if (isFound) {
                tableInvoice.setModel(filteredModel);
                JOptionPane.showMessageDialog(null, "Invoice ditemukan!");
            } else {
                tableInvoice.setModel(originalModel); // Tampilkan semua data jika tidak ada yang cocok
                JOptionPane.showMessageDialog(null, "No Kendaraan tidak ditemukan!");
            }

            searchInvoiceField.setText(""); // Kosongkan field pencarian
            selectedId = null;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }//GEN-LAST:event_searchInvoiceButtonActionPerformed

    private void deleteInvoiceButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteInvoiceButtonActionPerformed
        // TODO add your handling code here:
        try {
            String sql = "delete from invoice where id='" + selectedId + "';";
            java.sql.Connection conn = (java.sql.Connection)con;
            java.sql.PreparedStatement pst = conn.prepareStatement(sql);
            pst.execute();
            JOptionPane.showMessageDialog(null, "Berhasil dihapus");
            tampilInvoice();
            searchInvoiceField.setText("");       // Kosongkan field pencarian
            selectedId = null;
        } catch (Exception e){
            JOptionPane.showMessageDialog(null, "Gagal dihapus");
            System.out.println(e.getMessage());
        }
    }//GEN-LAST:event_deleteInvoiceButtonActionPerformed

    private void editPelangganButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editPelangganButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_editPelangganButton1ActionPerformed

    private void addPelangganButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addPelangganButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_addPelangganButton1ActionPerformed

    private void resetPelangganButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetPelangganButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_resetPelangganButton1ActionPerformed

    private void searchPelangganButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchPelangganButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_searchPelangganButton1ActionPerformed

    private void tableInvoiceMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableInvoiceMouseClicked
        // TODO add your handling code here:
        try {
            // Ambil koneksi database
            java.sql.Connection conn = (java.sql.Connection) con;

            // Ambil baris yang diklik
            int row = tableInvoice.getSelectedRow();
            String tabel_klik = tableInvoice.getModel().getValueAt(row, 0).toString(); // ID dari kolom pertama

            // Simpan ID ke variabel global
            selectedId = tabel_klik;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Gagal mengambil data");
            System.out.println(e.getMessage());
        }
    }//GEN-LAST:event_tableInvoiceMouseClicked

    private void tabbedPaneCustom2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabbedPaneCustom2MouseClicked
        // TODO add your handling code here:

    }//GEN-LAST:event_tabbedPaneCustom2MouseClicked

    private void tabbedPaneCustom2FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tabbedPaneCustom2FocusGained
        // TODO add your handling code here:

    }//GEN-LAST:event_tabbedPaneCustom2FocusGained

    private void resetPelangganButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetPelangganButtonActionPerformed
        // TODO add your handling code here:
        cmbBoxTipePelanggan.setSelectedIndex(0);
        namaPelangganField.setText("");
        noKendaraanPelangganField.setText("");
        noPelangganField.setText("");
        alamatPelangganField.setText("");
        searchPelangganField.setText("");
        selectedId = null;
    }//GEN-LAST:event_resetPelangganButtonActionPerformed

    private void searchPelangganButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchPelangganButtonActionPerformed
        // TODO add your handling code here:
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("No");
        model.addColumn("Jenis");
        model.addColumn("No Kendaraan");
        model.addColumn("Nama");
        model.addColumn("No Whatsapp");
        model.addColumn("Alamat");

        boolean isFound = false;
        int rowToFocus = -1;

        try {
            // Ambil semua data
            String sql = "SELECT pelanggan.id, tipe.jenis_kendaraan, kendaraan.no_kendaraan, pelanggan.nama_pelanggan, pelanggan.no_whatsapp, pelanggan.alamat " +
            "FROM pelanggan " +
            "JOIN kendaraan ON pelanggan.id_kendaraan = kendaraan.id " +
            "JOIN tipe ON kendaraan.id_tipe = tipe.id";
            java.sql.PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            int rowIndex = 0;

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("id"),
                    rs.getString("jenis_kendaraan"),
                    rs.getString("no_kendaraan"),
                    rs.getString("nama_pelanggan"),
                    rs.getString("no_whatsapp"),
                    rs.getString("alamat")
                });

                // Jika no_kendaraan cocok
                if (rs.getString("no_kendaraan").equalsIgnoreCase(searchPelangganField.getText())) {
                    isFound = true;
                    rowToFocus = rowIndex;
                    // Masukkan data ke TextField
                    selectedId = rs.getString("id");
                    cmbBoxTipePelanggan.setSelectedItem(rs.getString("jenis_kendaraan"));
                    noKendaraanPelangganField.setText(rs.getString("no_kendaraan"));
                    namaPelangganField.setText(rs.getString("nama_pelanggan"));
                    noPelangganField.setText(rs.getString("no_whatsapp"));
                    alamatPelangganField.setText(rs.getString("alamat"));
                }
                rowIndex++;
            }

            tablePelanggan.setModel(model);

            // Jika ditemukan, fokus ke baris yang cocok
            if (isFound && rowToFocus >= 0) {
                tablePelanggan.setRowSelectionInterval(rowToFocus, rowToFocus);
                JOptionPane.showMessageDialog(null, "No Kendaraan ditemukan dan ditampilkan!");
            } else {
                JOptionPane.showMessageDialog(null, "No Kendaraan tidak ditemukan!");
            }
            searchPelangganField.setText("");
            selectedId = null;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }//GEN-LAST:event_searchPelangganButtonActionPerformed

    private void tablePelangganMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablePelangganMouseClicked
        // TODO add your handling code here:
        int row = tablePelanggan.getSelectedRow();
        selectedId = tablePelanggan.getValueAt(row, 0).toString();
        cmbBoxTipePelanggan.setSelectedItem(tablePelanggan.getValueAt(row, 1).toString());
        noKendaraanPelangganField.setText(tablePelanggan.getValueAt(row, 2).toString());
        namaPelangganField.setText(tablePelanggan.getValueAt(row, 3).toString());
        noPelangganField.setText(tablePelanggan.getValueAt(row, 4).toString());
        alamatPelangganField.setText(tablePelanggan.getValueAt(row, 5).toString());
        searchPelangganField.setText("");
    }//GEN-LAST:event_tablePelangganMouseClicked

    private void addPelangganButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addPelangganButtonActionPerformed
        // TODO add your handling code here:
        if (validateInputPelanggan()) {
            try {
                // Ambil id_tipe berdasarkan jenis_kendaraan
                String sqlTipe = "SELECT id FROM tipe WHERE jenis_kendaraan = ?";
                java.sql.PreparedStatement pstTipe = con.prepareStatement(sqlTipe);
                pstTipe.setString(1, cmbBoxTipePelanggan.getSelectedItem().toString());
                ResultSet rs = pstTipe.executeQuery();
                if (rs.next()) {
                    int idTipe = rs.getInt("id");

                    // Insert kendaraan
                    String sqlKendaraan = "INSERT INTO kendaraan (id_tipe, no_kendaraan) VALUES (?, ?)";
                    java.sql.PreparedStatement pstKendaraan = con.prepareStatement(sqlKendaraan, Statement.RETURN_GENERATED_KEYS);
                    pstKendaraan.setInt(1, idTipe);
                    pstKendaraan.setString(2, noKendaraanPelangganField.getText());
                    pstKendaraan.executeUpdate();
                    ResultSet keys = pstKendaraan.getGeneratedKeys();

                    if (keys.next()) {
                        int idKendaraan = keys.getInt(1);

                        // Insert pelanggan
                        String sqlPelanggan = "INSERT INTO pelanggan (id_kendaraan, nama_pelanggan, no_whatsapp, alamat) VALUES (?, ?, ?, ?)";
                        java.sql.PreparedStatement pstPelanggan = con.prepareStatement(sqlPelanggan);
                        pstPelanggan.setInt(1, idKendaraan);
                        pstPelanggan.setString(2, namaPelangganField.getText());
                        pstPelanggan.setString(3, noPelangganField.getText());
                        pstPelanggan.setString(4, alamatPelangganField.getText());
                        pstPelanggan.executeUpdate();

                        JOptionPane.showMessageDialog(null, "Data berhasil ditambahkan");
                        tampilPelanggan();
                        cmbBoxTipePelanggan.setSelectedIndex(0);
                        namaPelangganField.setText("");
                        noKendaraanPelangganField.setText("");
                        noPelangganField.setText("");
                        alamatPelangganField.setText("");
                        searchPelangganField.setText("");
                    }
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Gagal: " + e.getMessage());
            }
        }
    }//GEN-LAST:event_addPelangganButtonActionPerformed

    private void editPelangganButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editPelangganButtonActionPerformed
        // TODO add your handling code here:
        if (validateInputPelanggan()) {
            try {
                // Ambil id_tipe berdasarkan jenis_kendaraan
                String sqlTipe = "SELECT id FROM tipe WHERE jenis_kendaraan = ?";
                java.sql.PreparedStatement pstTipe = con.prepareStatement(sqlTipe);
                pstTipe.setString(1, cmbBoxTipePelanggan.getSelectedItem().toString());
                ResultSet rsTipe = pstTipe.executeQuery();

                if (rsTipe.next()) {
                    int idTipe = rsTipe.getInt("id");

                    // Update kendaraan
                    String sqlKendaraan = "UPDATE kendaraan SET id_tipe = ?, no_kendaraan = ? WHERE id = (SELECT id_kendaraan FROM pelanggan WHERE id = ?)";
                    java.sql.PreparedStatement pstKendaraan = con.prepareStatement(sqlKendaraan);
                    pstKendaraan.setInt(1, idTipe);
                    pstKendaraan.setString(2, noKendaraanPelangganField.getText());
                    pstKendaraan.setString(3, selectedId);
                    pstKendaraan.executeUpdate();

                    // Update pelanggan
                    String sqlPelanggan = "UPDATE pelanggan SET nama_pelanggan = ?, no_whatsapp = ?, alamat = ? WHERE id = ?";
                    java.sql.PreparedStatement pstPelanggan = con.prepareStatement(sqlPelanggan);
                    pstPelanggan.setString(1, namaPelangganField.getText());
                    pstPelanggan.setString(2, noPelangganField.getText());
                    pstPelanggan.setString(3, alamatPelangganField.getText());
                    pstPelanggan.setString(4, selectedId);
                    pstPelanggan.executeUpdate();

                    JOptionPane.showMessageDialog(null, "Data berhasil diubah");
                    tampilPelanggan();
                    cmbBoxTipePelanggan.setSelectedIndex(0);
                    namaPelangganField.setText("");
                    noKendaraanPelangganField.setText("");
                    noPelangganField.setText("");
                    alamatPelangganField.setText("");
                    searchPelangganField.setText("");
                    selectedId = null;
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Gagal: " + e.getMessage());
            }
        }
    }//GEN-LAST:event_editPelangganButtonActionPerformed

    private void deletePelangganButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deletePelangganButtonActionPerformed
        // TODO add your handling code here:
        if (selectedId == null || selectedId.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Pilih data terlebih dahulu!");
            return;
        }

        try {
            // Ambil id_kendaraan dari tabel pelanggan
            String sqlGetKendaraanId = "SELECT id_kendaraan FROM pelanggan WHERE id = ?";
            java.sql.PreparedStatement pstGet = con.prepareStatement(sqlGetKendaraanId);
            pstGet.setString(1, selectedId);
            ResultSet rs = pstGet.executeQuery();

            if (rs.next()) {
                String idKendaraan = rs.getString("id_kendaraan");

                // Hapus data di tabel pelanggan
                String sqlDeletePelanggan = "DELETE FROM pelanggan WHERE id = ?";
                java.sql.PreparedStatement pstDeletePelanggan = con.prepareStatement(sqlDeletePelanggan);
                pstDeletePelanggan.setString(1, selectedId);
                pstDeletePelanggan.executeUpdate();

                // Hapus data di tabel kendaraan berdasarkan id_kendaraan
                String sqlDeleteKendaraan = "DELETE FROM kendaraan WHERE id = ?";
                java.sql.PreparedStatement pstDeleteKendaraan = con.prepareStatement(sqlDeleteKendaraan);
                pstDeleteKendaraan.setString(1, idKendaraan);
                pstDeleteKendaraan.executeUpdate();

                JOptionPane.showMessageDialog(null, "Data berhasil dihapus!");

                // Refresh tabel
                tampilPelanggan();
                cmbBoxTipePelanggan.setSelectedIndex(0);
                namaPelangganField.setText("");
                noKendaraanPelangganField.setText("");
                noPelangganField.setText("");
                alamatPelangganField.setText("");
                searchPelangganField.setText("");
                selectedId = null;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Gagal menghapus data: " + e.getMessage());
        }
    }//GEN-LAST:event_deletePelangganButtonActionPerformed

    private void tableLayananMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableLayananMouseClicked
        // TODO add your handling code here:
        try {
            // Ambil koneksi database
            java.sql.Connection conn = (java.sql.Connection) con;

            // Ambil baris yang diklik
            int row = tableLayanan.getSelectedRow();
            String tabel_klik = tableLayanan.getModel().getValueAt(row, 0).toString(); // ID dari kolom pertama

            // Simpan ID ke variabel global
            selectedId = tabel_klik;

            // Query SQL untuk mengambil data layanan dan tipe
            String sql = "SELECT l.nama_layanan, l.harga, t.jenis_kendaraan, t.id AS id_tipe " +
            "FROM layanan l JOIN tipe t ON l.id_tipe = t.id " +
            "WHERE l.id = ?";
            java.sql.PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, tabel_klik);

            java.sql.ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                // Isi input field dengan data layanan
                String nama_layanan = rs.getString("nama_layanan");
                namaLayananField.setText(nama_layanan);
                String harga = rs.getString("harga");
                hargaLayananField.setText(harga);

                // Ambil tipe kendaraan dan atur ComboBox
                String id_tipe = rs.getString("id_tipe");
                String jenis_kendaraan = rs.getString("jenis_kendaraan");
                cmbBoxTipeLayanan.setSelectedItem(jenis_kendaraan); // Set item berdasarkan jenis kendaraan
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Gagal mengambil data: " + e.getMessage());
            e.printStackTrace();
        }
    }//GEN-LAST:event_tableLayananMouseClicked

    private void resetLayananButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetLayananButtonActionPerformed
        // TODO add your handling code here:
        // Reset Input Field
        namaLayananField.setText("");
        hargaLayananField.setText("");
        cmbBoxTipeLayanan.setSelectedIndex(0); // Reset ComboBox
        selectedId = null;
    }//GEN-LAST:event_resetLayananButtonActionPerformed

    private void addLayananButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addLayananButtonActionPerformed
        // TODO add your handling code here:
        // Ambil Data Inputan
        String jenisKendaraan = (String) cmbBoxTipeLayanan.getSelectedItem(); // Pilihan ComboBox
        String namaLayanan = namaLayananField.getText().trim(); // Text Field
        String harga = hargaLayananField.getText().trim(); // Text Field

        // Validasi Input
        if (jenisKendaraan == null || jenisKendaraan.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Silakan pilih jenis kendaraan!", "Validasi Gagal", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (namaLayanan.isEmpty() || harga.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Nama layanan dan harga tidak boleh kosong!", "Validasi Gagal", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Periksa format nomor WhatsApp (hanya angka)
        if (!harga.matches("\\d+")) {
            JOptionPane.showMessageDialog(null, "Harga hanya boleh berisi angka!", "Validasi Gagal", JOptionPane.WARNING_MESSAGE);
            hargaLayananField.requestFocus();
            return;
        }

        try {
            String queryId = "SELECT id FROM tipe WHERE jenis_kendaraan = ?";
            java.sql.PreparedStatement pstId = con.prepareStatement(queryId);
            pstId.setString(1, jenisKendaraan); // Parameter index 1
            ResultSet rs = pstId.executeQuery();

            int idTipe = -1;
            if (rs.next()) {
                idTipe = rs.getInt("id");
            } else {
                JOptionPane.showMessageDialog(null, "Tipe kendaraan tidak ditemukan!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Query Insert ke Database
            String queryInsert = "INSERT INTO layanan (id_tipe, nama_layanan, harga) VALUES (?, ?, ?)";
            java.sql.PreparedStatement pstInsert = con.prepareStatement(queryInsert);
            pstInsert.setInt(1, idTipe);
            pstInsert.setString(2, namaLayanan);
            pstInsert.setString(3, harga);

            // Eksekusi Query
            pstInsert.executeUpdate();
            JOptionPane.showMessageDialog(null, "Data layanan berhasil ditambahkan!");

            // Reset Input Field
            namaLayananField.setText("");
            hargaLayananField.setText("");
            cmbBoxTipeLayanan.setSelectedIndex(0); // Reset ComboBox

            tampilLayanan();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Terjadi kesalahan: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }//GEN-LAST:event_addLayananButtonActionPerformed

    private void editLayananButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editLayananButtonActionPerformed
        // TODO add your handling code here:
        try {
            // Ambil koneksi database
            java.sql.Connection conn = (java.sql.Connection) con;

            // Ambil nilai input dari textfield dan combobox
            String namaLayanan = namaLayananField.getText();
            String harga = hargaLayananField.getText();
            String tipeKendaraan = cmbBoxTipeLayanan.getSelectedItem().toString(); // Dari ComboBox

            // Validasi Input
            if (tipeKendaraan == null || tipeKendaraan.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Silakan pilih jenis kendaraan!", "Validasi Gagal", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (namaLayanan.isEmpty() || harga.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Nama layanan dan harga tidak boleh kosong!", "Validasi Gagal", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Periksa format nomor WhatsApp (hanya angka)
            if (!harga.matches("\\d+")) {
                JOptionPane.showMessageDialog(null, "Harga hanya boleh berisi angka!", "Validasi Gagal", JOptionPane.WARNING_MESSAGE);
                hargaLayananField.requestFocus();
                return;
            }

            // Ambil ID tipe dari tabel tipe berdasarkan jenis kendaraan
            String sqlGetTipe = "SELECT id FROM tipe WHERE jenis_kendaraan = ?";
            java.sql.PreparedStatement pstTipe = conn.prepareStatement(sqlGetTipe);
            pstTipe.setString(1, tipeKendaraan);
            java.sql.ResultSet rs = pstTipe.executeQuery();

            if (rs.next()) {
                String idTipe = rs.getString("id"); // Ambil ID tipe

                // Query untuk Update layanan berdasarkan ID
                String sqlUpdate = "UPDATE layanan SET nama_layanan = ?, harga = ?, id_tipe = ? WHERE id = ?";
                java.sql.PreparedStatement pstUpdate = conn.prepareStatement(sqlUpdate);
                pstUpdate.setString(1, namaLayanan);
                pstUpdate.setString(2, harga);
                pstUpdate.setString(3, idTipe);
                pstUpdate.setString(4, selectedId); // ID layanan yang dipilih

                // Eksekusi query update
                int rowsUpdated = pstUpdate.executeUpdate();

                if (rowsUpdated > 0) {
                    JOptionPane.showMessageDialog(this, "Data layanan berhasil diperbarui!");
                    tampilLayanan(); // Refresh tabel
                    // Reset Input Field
                    namaLayananField.setText("");
                    hargaLayananField.setText("");
                    cmbBoxTipeLayanan.setSelectedIndex(0); // Reset ComboBox
                    selectedId = null;
                } else {
                    JOptionPane.showMessageDialog(this, "Data layanan gagal diperbarui!");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Tipe kendaraan tidak ditemukan!");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error saat memperbarui data: " + e.getMessage());
            e.printStackTrace();
        }
    }//GEN-LAST:event_editLayananButtonActionPerformed

    private void deleteLayananButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteLayananButtonActionPerformed
        // TODO add your handling code here:
        try {
            String sql = "delete from layanan where id='" + selectedId + "';";
            java.sql.Connection conn = (java.sql.Connection)con;
            java.sql.PreparedStatement pst = conn.prepareStatement(sql);
            pst.execute();
            JOptionPane.showMessageDialog(null, "Berhasil dihapus");
            tampilLayanan();
            // Reset Input Field
            namaLayananField.setText("");
            hargaLayananField.setText("");
            cmbBoxTipeLayanan.setSelectedIndex(0); // Reset ComboBox
            selectedId = null;
        } catch (Exception e){
            JOptionPane.showMessageDialog(null, "Gagal dihapus");
            System.out.println(e.getMessage());
        }
    }//GEN-LAST:event_deleteLayananButtonActionPerformed

    private void resetAdminButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetAdminButtonActionPerformed
        // TODO add your handling code here:
        Adminnama_adminField.setText(null);
        Adminno_whatsappField.setText(null);
        selectedId = null;
    }//GEN-LAST:event_resetAdminButtonActionPerformed

    private void deleteAdminButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteAdminButtonActionPerformed
        // TODO add your handling code here:
        try {
            String sql = "delete from admin where id='" + selectedId + "';";
            java.sql.Connection conn = (java.sql.Connection)con;
            java.sql.PreparedStatement pst = conn.prepareStatement(sql);
            pst.execute();
            JOptionPane.showMessageDialog(null, "Berhasil dihapus");
            tampilAdmin();
            Adminnama_adminField.setText(null);
            Adminno_whatsappField.setText(null);
            selectedId = null;
        } catch (Exception e){
            JOptionPane.showMessageDialog(null, "Gagal dihapus");
            System.out.println(e.getMessage());
        }
    }//GEN-LAST:event_deleteAdminButtonActionPerformed

    private void editAdminButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editAdminButtonActionPerformed
        // TODO add your handling code here:
        // Validasi Input Field
        String namaAdmin = Adminnama_adminField.getText().trim();
        String noWhatsapp = Adminno_whatsappField.getText().trim();

        // Periksa jika input kosong
        if (namaAdmin.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Nama Admin tidak boleh kosong!", "Validasi Gagal", JOptionPane.WARNING_MESSAGE);
            Adminnama_adminField.requestFocus();
            return;
        }

        if (noWhatsapp.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Nomor WhatsApp tidak boleh kosong!", "Validasi Gagal", JOptionPane.WARNING_MESSAGE);
            Adminno_whatsappField.requestFocus();
            return;
        }

        // Periksa format nomor WhatsApp (hanya angka)
        if (!noWhatsapp.matches("\\d+")) {
            JOptionPane.showMessageDialog(null, "Nomor WhatsApp hanya boleh berisi angka!", "Validasi Gagal", JOptionPane.WARNING_MESSAGE);
            Adminno_whatsappField.requestFocus();
            return;
        }

        try {
            java.sql.Connection conn = (java.sql.Connection)con;
            String sql = "update admin set nama_admin='" + Adminnama_adminField.getText() + "', no_whatsapp='" + Adminno_whatsappField.getText() + "' where id='" + selectedId + "';";
            java.sql.Statement stm = con.createStatement();
            stm.executeUpdate(sql);
            tampilAdmin();
            JOptionPane.showMessageDialog(null, "Berhasil diedit");
            Adminnama_adminField.setText(null);
            Adminno_whatsappField.setText(null);
            selectedId = null;
        } catch (Exception e){
            JOptionPane.showMessageDialog(null, "Gagal diedit");
            System.out.println(e.getMessage());
        }
    }//GEN-LAST:event_editAdminButtonActionPerformed

    private void tableAdminMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableAdminMouseClicked
        // TODO add your handling code here:
        try {
            // Ambil koneksi database
            java.sql.Connection conn = (java.sql.Connection) con;

            // Ambil baris yang diklik
            int row = tableAdmin.getSelectedRow();
            String tabel_klik = tableAdmin.getModel().getValueAt(row, 0).toString(); // ID dari kolom pertama

            // Simpan ID ke variabel global
            selectedId = tabel_klik;

            // Query SQL untuk mengambil data admin
            String sql = "SELECT * FROM admin WHERE id = ?";
            java.sql.PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, tabel_klik);

            java.sql.ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                // Isi input field dengan data admin
                String nama_admin = rs.getString("nama_admin");
                Adminnama_adminField.setText(nama_admin);
                String no_whatsapp = rs.getString("no_whatsapp");
                Adminno_whatsappField.setText(no_whatsapp);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Gagal mengambil data");
            System.out.println(e.getMessage());
        }
    }//GEN-LAST:event_tableAdminMouseClicked

    private void addAdminButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addAdminButtonActionPerformed
        // TODO add your handling code here:
        // Validasi Input Field
        String namaAdmin = Adminnama_adminField.getText().trim();
        String noWhatsapp = Adminno_whatsappField.getText().trim();

        // Periksa jika input kosong
        if (namaAdmin.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Nama Admin tidak boleh kosong!", "Validasi Gagal", JOptionPane.WARNING_MESSAGE);
            Adminnama_adminField.requestFocus();
            return;
        }

        if (noWhatsapp.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Nomor WhatsApp tidak boleh kosong!", "Validasi Gagal", JOptionPane.WARNING_MESSAGE);
            Adminno_whatsappField.requestFocus();
            return;
        }

        // Periksa format nomor WhatsApp (hanya angka)
        if (!noWhatsapp.matches("\\d+")) {
            JOptionPane.showMessageDialog(null, "Nomor WhatsApp hanya boleh berisi angka!", "Validasi Gagal", JOptionPane.WARNING_MESSAGE);
            Adminno_whatsappField.requestFocus();
            return;
        }

        try {
            String sql = "insert into admin (nama_admin, password, no_whatsapp) values ('" + Adminnama_adminField.getText() + "', '" + Adminnama_adminField.getText() + "', '" + Adminno_whatsappField.getText() + "')";
            java.sql.Connection conn = (java.sql.Connection)con;
            java.sql.PreparedStatement pst = conn.prepareStatement(sql);
            pst.execute();
            JOptionPane.showMessageDialog(null, "Berhasil disimpan");
            tampilAdmin();
            Adminnama_adminField.setText(null);
            Adminno_whatsappField.setText(null);

        } catch (Exception e){
            JOptionPane.showMessageDialog(null, "Gagal disimpan");
            System.out.println(e.getMessage());
        }
    }//GEN-LAST:event_addAdminButtonActionPerformed
    
    // Method untuk menampilkan data pada ComboBox
    private void loadComboBoxOrder() {
        try {
            // Untuk layanan
            String queryLayanan = "SELECT nama_layanan FROM layanan";
            java.sql.PreparedStatement psLayanan = con.prepareStatement(queryLayanan);
            ResultSet rsLayanan = psLayanan.executeQuery();
            while (rsLayanan.next()) {
                cmbBoxLayananOrder.addItem(rsLayanan.getString("nama_layanan"));
            }

            // Untuk pelanggan
            String queryPelanggan = "SELECT nama_pelanggan FROM pelanggan";
            java.sql.PreparedStatement psPelanggan = con.prepareStatement(queryPelanggan);
            ResultSet rsPelanggan = psPelanggan.executeQuery();
            while (rsPelanggan.next()) {
                cmbBoxPelangganOrder.addItem(rsPelanggan.getString("nama_pelanggan"));
            }

            // Untuk metode pembayaran manual
            cmbBoxMetodeOrder.addItem("Cash");
            cmbBoxMetodeOrder.addItem("QRIS");
            cmbBoxMetodeOrder.addItem("Transfer");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error loading data: " + e.getMessage());
        }
    }
    
    
    /**
     * @param args the command line arguments
     */
    
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new dashboard().setVisible(true);
                
                // Create a new JFrame (window)
                JFrame window = new JFrame("JTabbedPane Example");
                // Close operation when the window is closed
                window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Close operation when the window is closed
                // Set the initial size of the window
                window.setSize(400, 300);

                // Create a JTabbedPane, which will hold the tabs
                JTabbedPane tabPanel = new JTabbedPane();

                // Create the first tab (page1) and add a JLabel to it
                JPanel page1 = new JPanel();
                page1.add(new JLabel("This is Tab 1"));

                // Create the second tab (page2) and add a JLabel to it
                JPanel page2 = new JPanel();
                page2.add(new JLabel("This is Tab 2"));

                // Create the third tab (page3) and add a JLabel to it
                JPanel page3 = new JPanel();
                page3.add(new JLabel("This is Tab 3"));

                // Add the three tabs to the JTabbedPane
                tabPanel.addTab("Tab 1", page1);
                tabPanel.addTab("Tab 2", page2);
                tabPanel.addTab("Tab 3", page3);

                // Add the JTabbedPane to the JFrame's content
                window.add(tabPanel);

                // Make the JFrame visible
                window.setVisible(true);
                
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField Adminnama_adminField;
    private javax.swing.JTextField Adminno_whatsappField;
    private javax.swing.JButton addAdminButton;
    private javax.swing.JButton addLayananButton;
    private javax.swing.JButton addPelangganButton;
    private javax.swing.JButton addPelangganButton1;
    private javax.swing.JTextField adminOrderField;
    private javax.swing.JTextField alamatPelangganField;
    private javax.swing.JComboBox<String> cmbBoxLayananOrder;
    private javax.swing.JComboBox<String> cmbBoxMetodeOrder;
    private javax.swing.JComboBox<String> cmbBoxPelangganOrder;
    private javax.swing.JComboBox<String> cmbBoxTipeLayanan;
    private javax.swing.JComboBox<String> cmbBoxTipePelanggan;
    private javax.swing.JButton deleteAdminButton;
    private javax.swing.JButton deleteInvoiceButton;
    private javax.swing.JButton deleteLayananButton;
    private javax.swing.JButton deletePelangganButton;
    private javax.swing.JButton editAdminButton;
    private javax.swing.JButton editLayananButton;
    private javax.swing.JButton editPelangganButton;
    private javax.swing.JButton editPelangganButton1;
    private javax.swing.JTextField hargaLayananField;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JLabel lblAdminDetailOrder;
    private javax.swing.JLabel lblHargaDetailOrder;
    private javax.swing.JLabel lblJenisDetailOrder;
    private javax.swing.JLabel lblLayananDetailOrder;
    private javax.swing.JLabel lblNamaDetailOrder;
    private javax.swing.JLabel lblNoAlamatDetailOrder;
    private javax.swing.JLabel lblNoKendaraanDetailOrder;
    private javax.swing.JLabel lblTotalDetailOrder;
    private javax.swing.JButton logoutButton;
    private javax.swing.JTextField namaLayananField;
    private javax.swing.JTextField namaPelangganField;
    private javax.swing.JTextField noKendaraanPelangganField;
    private javax.swing.JTextField noPelangganField;
    private javax.swing.JButton resetAdminButton;
    private javax.swing.JButton resetInvoiceButton;
    private javax.swing.JButton resetLayananButton;
    private javax.swing.JButton resetOrderButton;
    private javax.swing.JButton resetPelangganButton;
    private javax.swing.JButton resetPelangganButton1;
    private javax.swing.JButton searchInvoiceButton;
    private javax.swing.JTextField searchInvoiceField;
    private javax.swing.JButton searchPelangganButton;
    private javax.swing.JButton searchPelangganButton1;
    private javax.swing.JTextField searchPelangganField;
    private javax.swing.JTextField searchPelangganField1;
    private javax.swing.JTextField sessionNama;
    private javax.swing.JButton submitOrderButton;
    private tabbed.TabbedPaneCustom tabbedPaneCustom1;
    private tabbed.TabbedPaneCustom tabbedPaneCustom2;
    private javax.swing.JTable tableAdmin;
    private javax.swing.JTable tableInvoice;
    private javax.swing.JTable tableLayanan;
    private javax.swing.JTable tablePelanggan;
    // End of variables declaration//GEN-END:variables
}
