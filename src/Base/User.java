
package Base;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class User extends javax.swing.JFrame {
Connection con;
ResultSet rs;
PreparedStatement pst;
private String selectID;

    public User() {
        initComponents();
        Koneksi DB = new Koneksi();
        DB.config();
        con = DB.con;
        
        load_table();
        
        ubah_u.setEnabled(false);
        hapus_u.setEnabled(false);
    }
    
    private void reset() {
        tambah_u.setEnabled(true);
        ubah_u.setEnabled(false);
        hapus_u.setEnabled(false);
        
        nama_u.setText("");
        username_u.setText("");
        password_u.setText("");
        alamat_u.setText("");
        notlp_u.setText("");
        level_u.setText("");
    }
    
    private void load_table() {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Id User");
        model.addColumn("Nama");
        model.addColumn("Username");
        model.addColumn("Password");
        model.addColumn("No telp");
        model.addColumn("Alamat");
        model.addColumn("Level");

        try {
            String sql = "select * from user";
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery(sql);

            while (rs.next()) {
                model.addRow(new Object[]{ rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7)});
            }
            tabel_u.setModel(model);
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }
        
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        kembali_u = new javax.swing.JButton();
        nama_u = new javax.swing.JTextField();
        username_u = new javax.swing.JTextField();
        password_u = new javax.swing.JTextField();
        notlp_u = new javax.swing.JTextField();
        level_u = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabel_u = new javax.swing.JTable();
        tambah_u = new javax.swing.JButton();
        ubah_u = new javax.swing.JButton();
        hapus_u = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        alamat_u = new javax.swing.JTextArea();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        kembali_u.setContentAreaFilled(false);
        kembali_u.setBorderPainted(false);
        kembali_u.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                kembali_uActionPerformed(evt);
            }
        });
        getContentPane().add(kembali_u, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 660, 110, 50));

        nama_u.setBorder(null);
        nama_u.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nama_uActionPerformed(evt);
            }
        });
        getContentPane().add(nama_u, new org.netbeans.lib.awtextra.AbsoluteConstraints(495, 144, 212, 28));

        username_u.setBorder(null);
        username_u.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                username_uActionPerformed(evt);
            }
        });
        getContentPane().add(username_u, new org.netbeans.lib.awtextra.AbsoluteConstraints(495, 194, 212, 29));

        password_u.setBorder(null);
        password_u.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                password_uActionPerformed(evt);
            }
        });
        getContentPane().add(password_u, new org.netbeans.lib.awtextra.AbsoluteConstraints(495, 244, 212, 29));

        notlp_u.setBorder(null);
        notlp_u.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                notlp_uActionPerformed(evt);
            }
        });
        getContentPane().add(notlp_u, new org.netbeans.lib.awtextra.AbsoluteConstraints(495, 292, 212, 28));

        level_u.setBorder(null);
        getContentPane().add(level_u, new org.netbeans.lib.awtextra.AbsoluteConstraints(895, 296, 222, 29));

        tabel_u.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Id_user", "Nama", "Username", "Password", "Alamat", "No Telp", "Level"
            }
        ));
        tabel_u.setShowGrid(true);
        tabel_u.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabel_uMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tabel_u);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(298, 370, 1003, 346));

        tambah_u.setContentAreaFilled(false);
        tambah_u.setBorderPainted(false);
        tambah_u.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tambah_uActionPerformed(evt);
            }
        });
        getContentPane().add(tambah_u, new org.netbeans.lib.awtextra.AbsoluteConstraints(1180, 158, 110, 40));

        ubah_u.setContentAreaFilled(false);
        ubah_u.setBorderPainted(false);
        ubah_u.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ubah_uActionPerformed(evt);
            }
        });
        getContentPane().add(ubah_u, new org.netbeans.lib.awtextra.AbsoluteConstraints(1180, 215, 110, 40));

        hapus_u.setContentAreaFilled(false);
        hapus_u.setBorderPainted(false);
        hapus_u.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hapus_uActionPerformed(evt);
            }
        });
        getContentPane().add(hapus_u, new org.netbeans.lib.awtextra.AbsoluteConstraints(1180, 274, 110, 40));

        alamat_u.setColumns(20);
        alamat_u.setRows(5);
        alamat_u.setBorder(null);
        jScrollPane2.setViewportView(alamat_u);

        getContentPane().add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(889, 141, 230, 140));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Asset/HalamanUser.png"))); // NOI18N
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1360, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void nama_uActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nama_uActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_nama_uActionPerformed

    private void username_uActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_username_uActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_username_uActionPerformed

    private void password_uActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_password_uActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_password_uActionPerformed

    private void notlp_uActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_notlp_uActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_notlp_uActionPerformed

    private void tambah_uActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tambah_uActionPerformed
    try {
        String sqlCheck = "SELECT COUNT(*) AS count FROM user WHERE username = ? AND level = ?";
        pst = con.prepareStatement(sqlCheck);
        pst.setString(1, username_u.getText());
        pst.setString(2, level_u.getText());
        rs = pst.executeQuery();

        if (rs.next() && rs.getInt("count") > 0) {
            JOptionPane.showMessageDialog(null, "Username sudah digunakan untuk level ini!");
        } else {
            String sqlID = "SELECT id_user FROM user ORDER BY id_user DESC LIMIT 1";
            pst = con.prepareStatement(sqlID);
            rs = pst.executeQuery();

            String userID = "U01";
            if (rs.next()) {
                String IDterakhir = rs.getString("id_user");
                int angka = Integer.parseInt(IDterakhir.substring(1)) + 1;
                userID = String.format("U%02d", angka);
            }

            rs.close();
            pst.close();

            // Insert data baru
            String sql = "INSERT INTO user (id_user, nama, username, password, alamat, no_telp, level) VALUES (?, ?, ?, ?, ?, ?, ?)";
            pst = con.prepareStatement(sql);

            pst.setString(1, userID);
            pst.setString(2, nama_u.getText());
            pst.setString(3, username_u.getText());
            pst.setString(4, password_u.getText());
            pst.setString(5, notlp_u.getText());
            pst.setString(6, alamat_u.getText());
            pst.setString(7, level_u.getText());

            if (pst.executeUpdate() > 0) {
                JOptionPane.showMessageDialog(null, "Berhasil menambah data!");
                reset(); 
                load_table();
            }
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
    }


    }//GEN-LAST:event_tambah_uActionPerformed

    private void ubah_uActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ubah_uActionPerformed
    try {
        String sqlCheck = "SELECT COUNT(*) AS count FROM user WHERE username = ? AND level = ? AND id_user != ?";
        pst = con.prepareStatement(sqlCheck);
        pst.setString(1, username_u.getText());
        pst.setString(2, level_u.getText());
        pst.setString(3, selectID);
        rs = pst.executeQuery();

        if (rs.next() && rs.getInt("count") > 0) {
            JOptionPane.showMessageDialog(null, "Username sudah digunakan oleh user lain!");
        } else {
            String sql = "UPDATE user SET nama=?, username=?, password=?, no_telp=?, alamat=?, level=? WHERE id_user=?";
            pst = con.prepareStatement(sql);

            pst.setString(1, nama_u.getText());
            pst.setString(2, username_u.getText());
            pst.setString(3, password_u.getText());
            pst.setString(4, alamat_u.getText());
            pst.setString(5, notlp_u.getText());
            pst.setString(6, level_u.getText());
            pst.setString(7, selectID);

            if (pst.executeUpdate() > 0) {
                JOptionPane.showMessageDialog(null, "Berhasil mengubah data!");
                reset();      
                load_table();  
            }
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
    } 
    }//GEN-LAST:event_ubah_uActionPerformed

    private void kembali_uActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_kembali_uActionPerformed
        DashAdmin pindah = new DashAdmin();
        pindah.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_kembali_uActionPerformed

    private void tabel_uMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabel_uMouseClicked
        tambah_u.setEnabled(false);
        ubah_u.setEnabled(true);
        hapus_u.setEnabled(true);

        int baris = tabel_u.rowAtPoint(evt.getPoint());

        selectID = tabel_u.getValueAt(baris, 0).toString();

        String nama = tabel_u.getValueAt(baris,1).toString(); 
        nama_u.setText(nama);

        String username = tabel_u.getValueAt(baris, 2).toString(); 
        username_u.setText(username);

        String password = tabel_u.getValueAt(baris, 3).toString(); 
        password_u.setText(password);

        String no_telp = tabel_u.getValueAt(baris, 4).toString();
        notlp_u.setText(no_telp);
        
        String alamat = tabel_u.getValueAt(baris, 5).toString();
        alamat_u.setText(alamat);
        
        String level = tabel_u.getValueAt(baris, 6).toString();
        level_u.setText(level);
    }//GEN-LAST:event_tabel_uMouseClicked

    private void hapus_uActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hapus_uActionPerformed
        int ok = JOptionPane.showConfirmDialog(null, "Ingin menghapus data ini?","Confirmation",JOptionPane.YES_NO_OPTION);
        if(ok==0){
            try{
                String sql="DELETE FROM user WHERE id_user='"+selectID+"'";
                pst = con.prepareStatement(sql);
                
                if (pst.executeUpdate() > 0) JOptionPane.showMessageDialog(null, "Berhasil menghapus data!");
                
                reset();
                load_table();
                
            } catch (SQLException e){
                  JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
                
            }
        }
    }//GEN-LAST:event_hapus_uActionPerformed

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
            java.util.logging.Logger.getLogger(User.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(User.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(User.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(User.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new User().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea alamat_u;
    private javax.swing.JButton hapus_u;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JButton kembali_u;
    private javax.swing.JTextField level_u;
    private javax.swing.JTextField nama_u;
    private javax.swing.JTextField notlp_u;
    private javax.swing.JTextField password_u;
    private javax.swing.JTable tabel_u;
    private javax.swing.JButton tambah_u;
    private javax.swing.JButton ubah_u;
    private javax.swing.JTextField username_u;
    // End of variables declaration//GEN-END:variables
}
