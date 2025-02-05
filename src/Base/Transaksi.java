package Base;

import javax.swing.*;  
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.swing.table.DefaultTableModel;

public class Transaksi extends javax.swing.JFrame {
    Connection con;
    ResultSet rs;
    PreparedStatement pst;

    
    public Transaksi() {
        initComponents();
        Koneksi DB = new Koneksi();
        DB.config();
        con = DB.con;
        
        AutoNoTransaksi();
        Tanggal();
        
        SaranProdukOtomatis();
        TampilListProduk();
        
        listProduk.setVisible(false);
        ubah_p.setEnabled(false);
        hapus_p.setEnabled(false);
        
    }

    private void AutoNoTransaksi() {    //fungsi generate no transaksi otomatis
        try {
            String sql = "SELECT MAX(id_transaksi) FROM transaksi";
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery(sql);
            
            if (rs.next()) {
                String maxNoTransaksi = rs.getString(1);
                if (maxNoTransaksi != null && maxNoTransaksi.startsWith("TRX")) {
                    int number = Integer.parseInt(maxNoTransaksi.substring(3)) + 1;
                    no_transaksi.setText("TRX" + String.format("%02d", number));
                } else {
                    no_transaksi.setText("TRX01");
                }
                no_transaksi.setEditable(false);
            }
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error : " + e.getMessage());
        }
    }
    
    private void Tanggal() { //menampilkan tanggal transaksi sesuai kapan transaksi itu dibuat
        LocalDate tanggal = LocalDate.now();
        DateTimeFormatter formatTanggal = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        tgl.setText(tanggal.format(formatTanggal));
        tgl.setEditable(false);
    } 
    
    private int counter;
    private void AutoIdDetail() { // fungsi generate id detail otomatis
          try {
            String sql = "SELECT MAX(id_detail) FROM detail_transaksi";
            rs = pst.executeQuery(sql);
            if (rs.next() && rs.getString(1) != null) {
                String lastId = rs.getString(1);
                int lastCounter = Integer.parseInt(lastId.replaceAll("\\D+", "")); 
                counter = lastCounter + 1; 
            } else {
                counter = 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            counter = 1; 
        }
    }
    
    private String FormatIdDetail() { //untuk menampilkan id detail secara otomatis dengan format DTL
        String idDetail = "DTL" + String.format("%02d", counter);
        counter++;
        return idDetail;
    }
    
    private void SaranProdukOtomatis() {
        nama_p.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                TampilSaranProduk();
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                TampilSaranProduk();
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                TampilSaranProduk();
            }
        });
    }

    private void TampilSaranProduk() {
        listProduk.setVisible(true);
        String Search = nama_p.getText().trim();
        
        if (Search.isEmpty() || Search.matches("\\d+")) {
            listProduk.setVisible(false);
            return;
        }

        try (PreparedStatement pst = con.prepareStatement("SELECT nama_produk FROM produk WHERE nama_produk LIKE ?")) {
            pst.setString(1, "%" + Search + "%");

            try (ResultSet rs = pst.executeQuery()) {
                DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
                while (rs.next()) {
                    model.addElement(rs.getString("nama_produk"));
                }
                listProduk.setModel(model);
                listProduk.setVisible(model.getSize() > 0);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error : " + e.getMessage());
        }
    }
    
    private void TampilListProduk() {
        listProduk.addActionListener(e -> {
            String PilihProduk = (String) listProduk.getSelectedItem();
            if (PilihProduk != null) {
                nama_p.setText(PilihProduk);
                GetHargaJual(PilihProduk);
                listProduk.setVisible(false);
            }
        });
    }

    private int GetStok(String namaProduk) { //fungsi untuk mendapatkan stok produk berdasarkan nama produk
        int stok = 0;

        if (namaProduk == null || namaProduk.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Nama produk tidak boleh kosong.");
            return stok;
        }

        String sqlStok = "SELECT stok FROM produk WHERE nama_produk = ?";
        try (PreparedStatement pst = con.prepareStatement(sqlStok)) {
            pst.setString(1, namaProduk);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    stok = rs.getInt("stok");
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal mengambil stok untuk produk: " + namaProduk + ". Error: " + e.getMessage());
        }

        return stok;
    }

    private int hargaJual = 0;
    private String kodeProduk = "";
    private void GetHargaJual(String namaProduk) { //fungsi untuk menampilkan harga jual sesuai dengan nama produknya
        harga_j.setEditable(false);
        if (namaProduk == null || namaProduk.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Nama produk tidak boleh kosong.");
            return;
        }

        try {
            String sql = "SELECT kode_produk, harga_jual FROM produk WHERE nama_produk = ?";
            pst = con.prepareStatement(sql);
            pst.setString(1, namaProduk);
            rs = pst.executeQuery();
            if (rs.next()) {
                kodeProduk = rs.getString("kode_produk");
                hargaJual = rs.getInt("harga_jual");

                String hargaFormatted = "Rp " + String.format("%,d", hargaJual);
                harga_j.setText(hargaFormatted);
            } else {
                    JOptionPane.showMessageDialog(null, "Produk tidak ditemukan: " + namaProduk);
                }
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error : " + e.getMessage());
        }      
    }

    private void TotalHarga() { //fungsi menghitung total harga dari produk yang di tambahkan di gui tabel transaksi
        DefaultTableModel model = (DefaultTableModel) tabel_transaksi.getModel();

        int total = 0;
        for (int i = 0; i < model.getRowCount(); i++) {
            Object subtotalObj = model.getValueAt(i, 4);

            if (subtotalObj != null) {  
            String subtotalStr = subtotalObj.toString().replace("Rp ", "").replace(",", "").replace(".", "").trim();
            try {
                int subtotal = Integer.parseInt(subtotalStr); 
                total += subtotal; 
            } catch (NumberFormatException e) {
                System.out.println("Error saat mengonversi subtotal: " + subtotalStr);
                continue;
            }
        }
    }
        total_P1.setText("Rp " + String.format("%,d", total)); 
        total_P2.setText("Rp " + String.format("%,d", total)); 
    }
    

     
    private void CekDanHitungKembalian() { //fungsi untuk mengecek dan menghitung kembalian
        try {
            String bayarText = bayar_transaksi.getText().replace("Rp ", "").replace(",", "").replaceAll("[^\\d]", "");
            String totalText = total_P2.getText().replace("Rp ", "").replace(",", "").replaceAll("[^\\d]", "");

            if (bayarText.isEmpty() || totalText.isEmpty() || bayarText.equals("0")) {
                kembalian_p.setText("Rp 0");
                return;
            }

            int bayar = Integer.parseInt(bayarText);
            int totalHarga = Integer.parseInt(totalText);

            if (bayar < totalHarga) {
                kembalian_p.setText("Rp 0"); 
                return;
            }

            Kembalian();
        } catch (NumberFormatException e) {
            kembalian_p.setText("Rp 0");
        }
    }

    private void Kembalian() {
        try {
            String totalText = total_P2.getText().replace("Rp ", "").replace(",", "").replaceAll("[^\\d]", "");
            String bayarText = bayar_transaksi.getText().replace("Rp ", "").replace(",", "").replaceAll("[^\\d]", "");

            if (totalText.isEmpty() || bayarText.isEmpty()) {
                kembalian_p.setText("Rp 0");
                return;
            }

            int totalHarga = Integer.parseInt(totalText);
            int bayar = Integer.parseInt(bayarText);

            int kembalian = bayar - totalHarga;

            if (kembalian < 0) {
                JOptionPane.showMessageDialog(null, "Jumlah bayar kurang!");
                kembalian_p.setText("Rp 0");
                return;
            }

            kembalian_p.setText("Rp " + String.format("%,d", kembalian));
        } catch (NumberFormatException e) {
            kembalian_p.setText("Rp 0");
            JOptionPane.showMessageDialog(null, "Tolong masukkan angka yang valid.");
        }
    }
    

    private void resetTransaksi() { // fungsi untuk mereset semua form di transaksi
        no_transaksi.setText("");

        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        tgl.setText(today.format(formatter));

        total_P1.setText("Rp " + String.format("%,d", 0));
        total_P2.setText("Rp " + String.format("%,d", 0));
        bayar_transaksi.setText("Rp " + String.format("%,d", 0));
        kembalian_p.setText("Rp " + String.format("%,d", 0));

        DefaultTableModel model = (DefaultTableModel) tabel_transaksi.getModel();
        model.setRowCount(0);

        AutoNoTransaksi();
    }

    
    private void clear() { // fungsi untuk merest form inputan di transaksi
        no_transaksi.setText("");
        listProduk.setSelectedIndex(-1);
        nama_p.setText("");
        harga_j.setText("");
        qty_p.setText("");
        kodeProduk = "";
        hargaJual = 0;
        
        listProduk.setVisible(false);
        
        nama_p.setEditable(true);
        harga_j.setEditable(true);
        
        AutoNoTransaksi();
        
    }



    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        no_transaksi = new javax.swing.JTextField();
        harga_j = new javax.swing.JTextField();
        nama_p = new javax.swing.JTextField();
        qty_p = new javax.swing.JTextField();
        kembalian_p = new javax.swing.JTextField();
        total_P1 = new javax.swing.JTextField();
        bayar_transaksi = new javax.swing.JTextField();
        tgl = new javax.swing.JFormattedTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabel_transaksi = new javax.swing.JTable();
        total_P2 = new javax.swing.JTextField();
        tambah_p = new javax.swing.JButton();
        clear_p = new javax.swing.JButton();
        ubah_p = new javax.swing.JButton();
        hapus_p = new javax.swing.JButton();
        bayar_p = new javax.swing.JButton();
        kembali = new javax.swing.JButton();
        listProduk = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        no_transaksi.setFont(new java.awt.Font("Segoe UI Black", 0, 14)); // NOI18N
        no_transaksi.setBorder(null);
        no_transaksi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                no_transaksiActionPerformed(evt);
            }
        });
        getContentPane().add(no_transaksi, new org.netbeans.lib.awtextra.AbsoluteConstraints(55, 140, 160, 30));

        harga_j.setFont(new java.awt.Font("Segoe UI Black", 0, 14)); // NOI18N
        harga_j.setToolTipText("");
        harga_j.setBorder(null);
        getContentPane().add(harga_j, new org.netbeans.lib.awtextra.AbsoluteConstraints(433, 248, 140, 31));

        nama_p.setFont(new java.awt.Font("Segoe UI Black", 0, 14)); // NOI18N
        nama_p.setBorder(null);
        nama_p.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nama_pActionPerformed(evt);
            }
        });
        getContentPane().add(nama_p, new org.netbeans.lib.awtextra.AbsoluteConstraints(55, 248, 160, 30));

        qty_p.setFont(new java.awt.Font("Segoe UI Black", 0, 14)); // NOI18N
        qty_p.setBorder(null);
        getContentPane().add(qty_p, new org.netbeans.lib.awtextra.AbsoluteConstraints(608, 248, 70, 30));

        kembalian_p.setFont(new java.awt.Font("Segoe UI Black", 0, 18)); // NOI18N
        kembalian_p.setBorder(null);
        getContentPane().add(kembalian_p, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 588, 200, 52));

        total_P1.setBackground(new java.awt.Color(0, 0, 0));
        total_P1.setFont(new java.awt.Font("Arial Black", 0, 36)); // NOI18N
        total_P1.setForeground(new java.awt.Color(0, 153, 0));
        total_P1.setBorder(null);
        total_P1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                total_P1ActionPerformed(evt);
            }
        });
        getContentPane().add(total_P1, new org.netbeans.lib.awtextra.AbsoluteConstraints(960, 114, 340, 70));

        bayar_transaksi.setFont(new java.awt.Font("Segoe UI Black", 0, 14)); // NOI18N
        bayar_transaksi.setBorder(null);
        bayar_transaksi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bayar_transaksiActionPerformed(evt);
            }
        });
        bayar_transaksi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                bayar_transaksiKeyReleased(evt);
            }
        });
        getContentPane().add(bayar_transaksi, new org.netbeans.lib.awtextra.AbsoluteConstraints(1075, 620, 236, 33));

        tgl.setBorder(null);
        tgl.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter()));
        tgl.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        tgl.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tglActionPerformed(evt);
            }
        });
        getContentPane().add(tgl, new org.netbeans.lib.awtextra.AbsoluteConstraints(252, 140, 140, 31));

        tabel_transaksi.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Kode Produk", "Nama Produk", "Harga", "Qty", "Sub Total"
            }
        ));
        tabel_transaksi.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabel_transaksiMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tabel_transaksi);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(49, 300, 1270, 223));

        total_P2.setFont(new java.awt.Font("Segoe UI Black", 0, 14)); // NOI18N
        total_P2.setBorder(null);
        getContentPane().add(total_P2, new org.netbeans.lib.awtextra.AbsoluteConstraints(1075, 571, 235, 33));

        tambah_p.setContentAreaFilled(false);
        tambah_p.setBorderPainted(false);
        tambah_p.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tambah_pActionPerformed(evt);
            }
        });
        getContentPane().add(tambah_p, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 244, 110, 40));

        clear_p.setContentAreaFilled(false);
        clear_p.setBorderPainted(false);
        clear_p.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clear_pActionPerformed(evt);
            }
        });
        getContentPane().add(clear_p, new org.netbeans.lib.awtextra.AbsoluteConstraints(840, 244, 90, 40));

        ubah_p.setContentAreaFilled(false);
        ubah_p.setBorderPainted(false);
        ubah_p.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ubah_pActionPerformed(evt);
            }
        });
        getContentPane().add(ubah_p, new org.netbeans.lib.awtextra.AbsoluteConstraints(949, 244, 88, 40));

        hapus_p.setContentAreaFilled(false);
        hapus_p.setBorderPainted(false);
        hapus_p.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hapus_pActionPerformed(evt);
            }
        });
        getContentPane().add(hapus_p, new org.netbeans.lib.awtextra.AbsoluteConstraints(1057, 244, 95, 40));

        bayar_p.setContentAreaFilled(false);
        bayar_p.setBorderPainted(false);
        bayar_p.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bayar_pActionPerformed(evt);
            }
        });
        getContentPane().add(bayar_p, new org.netbeans.lib.awtextra.AbsoluteConstraints(1200, 668, 120, 40));

        kembali.setContentAreaFilled(false);
        kembali.setBorderPainted(false);
        kembali.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                kembaliActionPerformed(evt);
            }
        });
        getContentPane().add(kembali, new org.netbeans.lib.awtextra.AbsoluteConstraints(1190, 40, 110, 40));

        listProduk.setFont(new java.awt.Font("Segoe UI Black", 0, 14)); // NOI18N
        listProduk.setToolTipText("");
        listProduk.setBorder(null);
        listProduk.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                listProdukMouseEntered(evt);
            }
        });
        listProduk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                listProdukActionPerformed(evt);
            }
        });
        getContentPane().add(listProduk, new org.netbeans.lib.awtextra.AbsoluteConstraints(244, 240, 153, 42));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Asset/HalamanTransaksi.png"))); // NOI18N
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void no_transaksiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_no_transaksiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_no_transaksiActionPerformed

    private void total_P1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_total_P1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_total_P1ActionPerformed

    private void clear_pActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clear_pActionPerformed
        // TODO add your handling code here:
        clear();
        tambah_p.setEnabled(true);
    }//GEN-LAST:event_clear_pActionPerformed

    private void tambah_pActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tambah_pActionPerformed
        try {
            String namaProduk = nama_p.getText().trim();
            String qty = qty_p.getText().trim();

            if (kodeProduk == null || kodeProduk.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Harap pilih produk dari daftar.");
                return;
            }

            if (namaProduk.isEmpty() || qty.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Isi semua data!");
                return;
            }

            int qtyInt;
            try {
                qtyInt = Integer.parseInt(qty);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Jumlah harus berupa angka.");
                return;
            }

            if (qtyInt <= 0) {
                JOptionPane.showMessageDialog(null, "Jumlah harus lebih dari nol.");
                return;
            }

            int stokProduk = GetStok(namaProduk); 
            if (qtyInt > stokProduk) {
                JOptionPane.showMessageDialog(null, "Stok tidak mencukupi! Stok yang tersedia: " + stokProduk);
                return;
            }

            int subtotal = hargaJual * qtyInt;

            DefaultTableModel model = (DefaultTableModel) tabel_transaksi.getModel();
            boolean produkDitemukan = false;

            for (int i = 0; i < model.getRowCount(); i++) {
                String namaProdukTabel = model.getValueAt(i, 1).toString(); 

                if (namaProdukTabel.equals(namaProduk)) {
                    int qtyTabel = Integer.parseInt(model.getValueAt(i, 3).toString());
                    int qtyBaru = qtyTabel + qtyInt;
                    int subtotalBaru = hargaJual * qtyBaru;

                    model.setValueAt(qtyBaru, i, 3);
                    model.setValueAt("Rp " + String.format("%,d", subtotalBaru), i, 4);
                    produkDitemukan = true;
                    break;
                }
            }

            if (!produkDitemukan) {
                model.addRow(new Object[]{
                    kodeProduk, 
                    namaProduk,
                    "Rp " + String.format("%,d", hargaJual),
                    qtyInt,
                    "Rp " + String.format("%,d", subtotal)
                });
            }
            
            TotalHarga();
            
            JOptionPane.showMessageDialog(null, "Produk berhasil ditambah!");
            
            clear(); 

        }  catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(null, "Format input salah!");
    }

    }//GEN-LAST:event_tambah_pActionPerformed

    private void bayar_transaksiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bayar_transaksiActionPerformed
       
    }//GEN-LAST:event_bayar_transaksiActionPerformed

    private void bayar_transaksiKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_bayar_transaksiKeyReleased
        String input = bayar_transaksi.getText().replace("Rp ", "").replace(",", "").replaceAll("[^\\d]", "");

        try {
            if (!input.isEmpty()) {
                int angka = Integer.parseInt(input); 
                bayar_transaksi.setText("Rp " + String.format("%,d", angka));
            } else {
                bayar_transaksi.setText("Rp 0");
            }
        } catch (NumberFormatException e) {
            bayar_transaksi.setText("Rp 0");
        }
        
        CekDanHitungKembalian();
    }//GEN-LAST:event_bayar_transaksiKeyReleased

    private void bayar_pActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bayar_pActionPerformed
        try {
            String idTransaksi = no_transaksi.getText(); 
            String tglTransaksi = tgl.getText(); 
            String totalHargaText = total_P2.getText().replace("Rp ", "").replace(",", "").replaceAll("[^\\d]", "");
            String dibayarText = bayar_transaksi.getText().replace("Rp ", "").replace(",", "").replaceAll("[^\\d]", "");
            String kembalianText = kembalian_p.getText().replace("Rp ", "").replace(",", "").replaceAll("[^\\d]", "");

            int totalHarga = Integer.parseInt(totalHargaText);
            int dibayar = Integer.parseInt(dibayarText);
            int kembalian = Integer.parseInt(kembalianText);

            String idUser = Login.activeUserId;

        String sqlTransaksi = "INSERT INTO transaksi (id_transaksi, tgl_transaksi, total_harga, dibayar, kembalian, id_user) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pst = con.prepareStatement(sqlTransaksi)) {
            pst.setString(1, idTransaksi); 
            pst.setString(2, tglTransaksi);
            pst.setInt(3, totalHarga);
            pst.setInt(4, dibayar);
            pst.setInt(5, kembalian);
            pst.setString(6, idUser);
            pst.executeUpdate();
        }

        AutoIdDetail();
        DefaultTableModel model = (DefaultTableModel) tabel_transaksi.getModel();
        String sqlDetail = "INSERT INTO detail_transaksi (id_detail, id_transaksi, kode_produk, harga_jual, qty, total_harga) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstDetail = con.prepareStatement(sqlDetail)) {
            for (int i = 0; i < model.getRowCount(); i++) {
                String idDetail = FormatIdDetail(); 
                String kodeProduk = model.getValueAt(i, 0).toString();
                String hargaText = model.getValueAt(i, 2).toString().replace("Rp ", "").replace(",", "").replaceAll("[^\\d]", "");
                String qtyText = model.getValueAt(i, 3).toString();
                String subtotalText = model.getValueAt(i, 4).toString().replace("Rp ", "").replace(",", "").replaceAll("[^\\d]", "");

                int harga_jual = Integer.parseInt(hargaText);
                int qty = Integer.parseInt(qtyText);
                int subtotal = Integer.parseInt(subtotalText);

                pstDetail.setString(1, idDetail); 
                pstDetail.setString(2, idTransaksi); 
                pstDetail.setString(3, kodeProduk);
                pstDetail.setInt(4, harga_jual);
                pstDetail.setInt(5, qty);
                pstDetail.setInt(6, subtotal);
                pstDetail.addBatch(); 
            }

            pstDetail.executeBatch(); 
        }

        JOptionPane.showMessageDialog(null, "Transaksi berhasil disimpan!");
        resetTransaksi();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Terjadi kesalahan saat menyimpan transaksi: " + e.getMessage());
        }
    }//GEN-LAST:event_bayar_pActionPerformed

    private void ubah_pActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ubah_pActionPerformed
        try {
            String qtyText = qty_p.getText();
            if (qtyText.isEmpty() || !qtyText.matches("\\d+")) {
                JOptionPane.showMessageDialog(null, "Qty harus berupa angka.");
            return;
            }
            int qtyBaru = Integer.parseInt(qtyText);
            
            String hargaText = harga_j.getText().replace("Rp ", "").replace(".", "");
            if (hargaText.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Harga tidak valid.");
                return;
            }
            int harga = Integer.parseInt(hargaText);

            int totalHargaBaru = harga * qtyBaru;
            String idTransaksi = no_transaksi.getText(); 

            int selectedRow = tabel_transaksi.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(null, "Pilih produk yang akan diubah!");
                return; 
            }

            String kodeProduk = tabel_transaksi.getValueAt(selectedRow, 0).toString(); 
            
            String sqlGetStok = "SELECT stok FROM produk WHERE kode_produk = ?";
            int stokTersedia = 0;
            try (PreparedStatement pstStok = con.prepareStatement(sqlGetStok)) {
                pstStok.setString(1, kodeProduk);
                try (ResultSet rs = pstStok.executeQuery()) {
                    if (rs.next()) {
                        stokTersedia = rs.getInt("stok");
                    }
                }
            }

            if (qtyBaru > stokTersedia) {
                JOptionPane.showMessageDialog(null, "Stok tidak mencukupi! Stok yang tersedia : " + stokTersedia);
                return;
            }

            String sqlUpdate = "UPDATE detail_transaksi SET qty = ?, total_harga = ? WHERE id_transaksi = ? AND kode_produk = ?";
            try (PreparedStatement pst = con.prepareStatement(sqlUpdate)) {
                pst.setInt(1, qtyBaru);
                pst.setInt(2, totalHargaBaru);
                pst.setString(3, idTransaksi);
                pst.setString(4, kodeProduk);
                pst.executeUpdate();
            }

            DefaultTableModel model = (DefaultTableModel) tabel_transaksi.getModel();
            String formattedTotalHarga = "Rp " + String.format("%,d", totalHargaBaru).replace(',', '.');
            model.setValueAt(qtyBaru, selectedRow, 3);
            model.setValueAt(formattedTotalHarga, selectedRow, 4);

            int totalBaru = 0;
            for (int i = 0; i < model.getRowCount(); i++) {
                String subTotal = model.getValueAt(i, 4).toString();
                subTotal = subTotal.replace("Rp ", "").replace(".", "");
                totalBaru += Integer.parseInt(subTotal);
            }
            
            total_P1.setText("Rp " + String.format("%,d", totalBaru).replace(',', '.'));
            total_P2.setText("Rp " + String.format("%,d", totalBaru).replace(',', '.'));

            clear();
            
            tambah_p.setEnabled(true);
            ubah_p.setEnabled(false);
            hapus_p.setEnabled(false);
            
            JOptionPane.showMessageDialog(null, "Produk berhasil diubah!");

        
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Input qty atau harga tidak valid: " + e.getMessage());
            e.printStackTrace();
        }catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Terjadi kesalahan saat memperbarui qty: " + e.getMessage());
        }
    }//GEN-LAST:event_ubah_pActionPerformed

    private void hapus_pActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hapus_pActionPerformed
    try {
        int selectedRow = tabel_transaksi.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Silakan pilih baris yang ingin dihapus.");
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(null, 
            "Apakah Anda yakin ingin menghapus produk ini?", 
            "Konfirmasi Hapus", 
            JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        DefaultTableModel model = (DefaultTableModel) tabel_transaksi.getModel();
        model.removeRow(selectedRow);

        int totalBaru = 0;
        for (int i = 0; i < model.getRowCount(); i++) {
            String subTotal = model.getValueAt(i, 4).toString();
            subTotal = subTotal.replace("Rp ", "").replace(".", "");
            totalBaru += Integer.parseInt(subTotal);
        }

        total_P2.setText("Rp " + String.format("%,d", totalBaru).replace(',', '.'));
        total_P1.setText("Rp " + String.format("%,d", totalBaru).replace(',', '.'));

        clear();

        tambah_p.setEnabled(true);
        ubah_p.setEnabled(false);
        hapus_p.setEnabled(false);
        JOptionPane.showMessageDialog(null, "Produk berhasil dihapus!");
        
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Terjadi kesalahan: " + e.getMessage());
        e.printStackTrace();
    }

    }//GEN-LAST:event_hapus_pActionPerformed

    private void tabel_transaksiMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabel_transaksiMouseClicked
    int selectedRow = tabel_transaksi.getSelectedRow();

    if (selectedRow != -1) {
        String namaProduk = tabel_transaksi.getValueAt(selectedRow, 1).toString();
        String harga_jual = tabel_transaksi.getValueAt(selectedRow, 2).toString();
        String qty = tabel_transaksi.getValueAt(selectedRow, 3).toString();

        nama_p.setText(namaProduk);
        harga_j.setText(harga_jual); 
        qty_p.setText(qty);
        
        nama_p.setEditable(false);
        harga_j.setEditable(false);
        
        tambah_p.setEnabled(false); 
        ubah_p.setEnabled(true);   
        hapus_p.setEnabled(true);   
    }
    }//GEN-LAST:event_tabel_transaksiMouseClicked

    private void kembaliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_kembaliActionPerformed
        // TODO add your handling code here:
        DashKasir pindah = new DashKasir();
        pindah.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_kembaliActionPerformed

    private void tglActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tglActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tglActionPerformed

    private void nama_pActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nama_pActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_nama_pActionPerformed

    private void listProdukActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_listProdukActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_listProdukActionPerformed

    private void listProdukMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_listProdukMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_listProdukMouseEntered

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
            java.util.logging.Logger.getLogger(Transaksi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Transaksi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Transaksi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Transaksi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Transaksi().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bayar_p;
    private javax.swing.JTextField bayar_transaksi;
    private javax.swing.JButton clear_p;
    private javax.swing.JButton hapus_p;
    private javax.swing.JTextField harga_j;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton kembali;
    private javax.swing.JTextField kembalian_p;
    private javax.swing.JComboBox<String> listProduk;
    private javax.swing.JTextField nama_p;
    private javax.swing.JTextField no_transaksi;
    private javax.swing.JTextField qty_p;
    private javax.swing.JTable tabel_transaksi;
    private javax.swing.JButton tambah_p;
    private javax.swing.JFormattedTextField tgl;
    private javax.swing.JTextField total_P1;
    private javax.swing.JTextField total_P2;
    private javax.swing.JButton ubah_p;
    // End of variables declaration//GEN-END:variables
}
