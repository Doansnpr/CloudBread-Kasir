
package Base;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;


public class Produk extends javax.swing.JFrame {
Connection con;
ResultSet rs;
PreparedStatement pst;
private String selectKode;
    
    public Produk() {
        initComponents();
        Koneksi DB = new Koneksi();
        DB.config();
        con = DB.con;
        
        load_table();
        
        ubah_p.setEnabled(false);
        hapus_p.setEnabled(false);
    }
    
    private void reset() {
        tambah_p.setEnabled(true);
        ubah_p.setEnabled(false);
        hapus_p.setEnabled(false);

        nama_p.setText("");
        harga_a.setText("");
        harga_j.setText("");
        stok_p.setText("");
    }
    
    private void load_table() {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Kode Produk");
        model.addColumn("Nama Produk");
        model.addColumn("Harga Awal");
        model.addColumn("Harga Jual");
        model.addColumn("Stok");

        try {
            String sql = "select * from produk";
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery(sql);

            while (rs.next()) {
                model.addRow(new Object[]{ rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5)});
            }
            
            tabel_produk.setModel(model);
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }  
    

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        nama_p = new javax.swing.JTextField();
        harga_a = new javax.swing.JTextField();
        harga_j = new javax.swing.JTextField();
        stok_p = new javax.swing.JTextField();
        tambah_p = new javax.swing.JButton();
        ubah_p = new javax.swing.JButton();
        hapus_p = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabel_produk = new javax.swing.JTable();
        kembali = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        nama_p.setBorder(null);
        nama_p.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nama_pActionPerformed(evt);
            }
        });
        getContentPane().add(nama_p, new org.netbeans.lib.awtextra.AbsoluteConstraints(504, 206, 180, 24));

        harga_a.setBorder(null);
        harga_a.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                harga_aActionPerformed(evt);
            }
        });
        getContentPane().add(harga_a, new org.netbeans.lib.awtextra.AbsoluteConstraints(504, 246, 180, 23));

        harga_j.setBorder(null);
        harga_j.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                harga_jActionPerformed(evt);
            }
        });
        getContentPane().add(harga_j, new org.netbeans.lib.awtextra.AbsoluteConstraints(916, 204, 180, 23));

        stok_p.setBorder(null);
        getContentPane().add(stok_p, new org.netbeans.lib.awtextra.AbsoluteConstraints(916, 246, 180, 23));

        tambah_p.setContentAreaFilled(false);
        tambah_p.setBorderPainted(false);
        tambah_p.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tambah_pActionPerformed(evt);
            }
        });
        getContentPane().add(tambah_p, new org.netbeans.lib.awtextra.AbsoluteConstraints(1170, 164, 110, 40));

        ubah_p.setContentAreaFilled(false);
        ubah_p.setBorderPainted(false);
        ubah_p.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ubah_pActionPerformed(evt);
            }
        });
        getContentPane().add(ubah_p, new org.netbeans.lib.awtextra.AbsoluteConstraints(1170, 220, 110, 41));

        hapus_p.setContentAreaFilled(false);
        hapus_p.setBorderPainted(false);
        hapus_p.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hapus_pActionPerformed(evt);
            }
        });
        getContentPane().add(hapus_p, new org.netbeans.lib.awtextra.AbsoluteConstraints(1170, 280, 110, 41));

        tabel_produk.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Kode Produk", "Nama Produk ", "Harga Awal", "Harga Jual", "Stok"
            }
        ));
        tabel_produk.setShowGrid(true);
        tabel_produk.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabel_produkMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tabel_produk);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(296, 370, 1006, 330));

        kembali.setContentAreaFilled(false);
        kembali.setBorderPainted(false);
        kembali.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                kembaliActionPerformed(evt);
            }
        });
        getContentPane().add(kembali, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 670, 110, 40));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Asset/HalamanProduk.png"))); // NOI18N
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tambah_pActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tambah_pActionPerformed
    try {
        String sqlKode = "SELECT kode_produk FROM produk ORDER BY kode_produk DESC LIMIT 1";
        pst = con.prepareStatement(sqlKode);
        rs = pst.executeQuery();

        String newKode = "P01"; 
        if (rs.next()) {
            String kodeTerakhir = rs.getString("kode_produk");
            int angka = Integer.parseInt(kodeTerakhir.substring(1)) + 1;
            newKode = String.format("P%02d", angka);
        }

        rs.close();
        pst.close();
 
        String sql = "INSERT INTO produk (kode_produk, nama_produk, harga_awal, harga_jual, stok) VALUES (?, ?, ?, ?, ?)";
        pst = con.prepareStatement(sql);
        
        pst.setString(1, newKode);
        pst.setString(2, nama_p.getText());
        pst.setString(3, harga_a.getText());
        pst.setString(4, harga_j.getText());
        pst.setString(5, stok_p.getText());
        
        if (pst.executeUpdate() > 0) JOptionPane.showMessageDialog(null, "Berhasil menambah data!");
        
        reset();
        load_table();
        
    } catch (SQLException e) {
         JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
    } 
    }//GEN-LAST:event_tambah_pActionPerformed

    private void hapus_pActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hapus_pActionPerformed
    int ok = JOptionPane.showConfirmDialog(null, "Ingin menghapus data ini?","Confirmation",JOptionPane.YES_NO_OPTION);
        if(ok==0){
            try{
                String sql="DELETE FROM produk WHERE kode_produk='"+selectKode+"'";
                pst = con.prepareStatement(sql);

                if (pst.executeUpdate() > 0) JOptionPane.showMessageDialog(null, "Berhasil menghapus data!");

                reset();
                load_table();
                
            } catch (SQLException e){
                JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());

            }
        }
    }//GEN-LAST:event_hapus_pActionPerformed

    private void ubah_pActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ubah_pActionPerformed
        try {
            String sql = "UPDATE produk SET nama_produk = ?, harga_awal = ?, harga_jual = ?, stok = ? WHERE kode_produk = ?";
            pst = con.prepareStatement(sql);

            pst.setString(1, nama_p.getText());  
            pst.setString(2, harga_a.getText());  
            pst.setString(3, harga_j.getText());  
            pst.setString(4, stok_p.getText());   
            pst.setString(5, selectKode); 

            if (pst.executeUpdate() > 0) JOptionPane.showMessageDialog(null, "Berhasil mengubah data!");
            
            reset();
            load_table();
            
        } catch (SQLException e) {
             JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
        
    }//GEN-LAST:event_ubah_pActionPerformed

    private void tabel_produkMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabel_produkMouseClicked
        tambah_p.setEnabled(false);
        ubah_p.setEnabled(true);
        hapus_p.setEnabled(true);

        int baris = tabel_produk.rowAtPoint(evt.getPoint());

        selectKode = tabel_produk.getValueAt(baris, 0).toString();

        String nama_produk = tabel_produk.getValueAt(baris,1).toString(); 
        nama_p.setText(nama_produk);

        String harga_awal = tabel_produk.getValueAt(baris, 2).toString(); 
        harga_a.setText(harga_awal);

        String harga_jual = tabel_produk.getValueAt(baris, 3).toString(); 
        harga_j.setText(harga_jual);

        String stok = tabel_produk.getValueAt(baris, 4).toString();
        stok_p.setText(stok);
        
    }//GEN-LAST:event_tabel_produkMouseClicked

    private void kembaliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_kembaliActionPerformed
        DashAdmin pindah = new DashAdmin();
        pindah.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_kembaliActionPerformed

    private void nama_pActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nama_pActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_nama_pActionPerformed

    private void harga_aActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_harga_aActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_harga_aActionPerformed

    private void harga_jActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_harga_jActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_harga_jActionPerformed

    
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
            java.util.logging.Logger.getLogger(Produk.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Produk.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Produk.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Produk.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Produk().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton hapus_p;
    private javax.swing.JTextField harga_a;
    private javax.swing.JTextField harga_j;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton kembali;
    private javax.swing.JTextField nama_p;
    private javax.swing.JTextField stok_p;
    private javax.swing.JTable tabel_produk;
    private javax.swing.JButton tambah_p;
    private javax.swing.JButton ubah_p;
    // End of variables declaration//GEN-END:variables
}
