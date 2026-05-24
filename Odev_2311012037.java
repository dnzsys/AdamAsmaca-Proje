package com.mycompany.mavenproject2;

import java.awt.HeadlessException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import javax.swing.table.DefaultTableModel;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */

/**
 *
 * @author HP
 */
public class Odev_2311012037 extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Odev_2311012037.class.getName());
    
    String TXT_YOLU = "C:\\P2Oyun\\TXTDosyalar\\";
    String RESIM_YOLU = "C:\\P2Oyun\\Resimler\\";
    
    String SIFRE_DOSYASI = TXT_YOLU + "sifre.txt";
    String KELIME_DOSYASI = TXT_YOLU + "kelimeler.txt";
    String LOG_DOSYASI = TXT_YOLU + "log.txt";
    String OYUNLAR_DOSYASI = TXT_YOLU + "oyunlar.txt";

    String secilenKelime = "";
    int yanlisTahminSayisi = 0;
    int gecenSaniye = 0;
    javax.swing.Timer zamanlayici;

    /**
     * Creates new form Odev_2311012037
     * @throws java.io.IOException
     */
    public Odev_2311012037() throws IOException {
        initComponents();
        
        txtHarf.setEnabled(false);
        txtKelime.setEnabled(false);
        btnHarfTahmin.setEnabled(false);
        btnKelimeTahmin.setEnabled(false);
        
        sifreKontrolEt();
        
        tabloyaYukle(OYUNLAR_DOSYASI, tblSkorlar);
        tabloyaYukle(LOG_DOSYASI, tblLoglar);
        
        zamanlayici = new javax.swing.Timer(1000, (java.awt.event.ActionEvent e) -> {
            gecenSaniye++;
            lblSure.setText("Süre: " + gecenSaniye + " sn");
        });
    }
    
    private void sifreKontrolEt() throws IOException {
        File dosya = new File(SIFRE_DOSYASI);
    
        if (!dosya.exists() || dosya.length() == 0) {
            String yeniSifre = javax.swing.JOptionPane.showInputDialog("İlk girişiniz. Yeni şifre belirleyin:");
            if (yeniSifre == null || yeniSifre.trim().isEmpty()) {
                System.exit(0);
            }
            try (FileWriter yazar = new FileWriter(dosya)) {
                logKaydet("Şifre oluşturuldu.");
                yazar.write(yeniSifre);
            }
        } else {
            String gercekSifre;
            try (Scanner okuyucu = new Scanner(dosya)) {
                gercekSifre = okuyucu.nextLine();
            }
            
            int hak = 3; 
            while(hak > 0) {
                String girilen = javax.swing.JOptionPane.showInputDialog("Şifrenizi girin:");
                if(girilen.equals(gercekSifre)) {
                    logKaydet("Başarılı giriş yapıldı.");
                    break;
                } else {
                    hak--;
                }
            }
            if(hak == 0) {
                System.exit(0);
            }
        }
    }
    
    private void logKaydet(String islem) {
        File dosya = new File(LOG_DOSYASI);
        
        try {
            FileWriter yazar = new FileWriter(dosya, true);
            try (BufferedWriter bw = new BufferedWriter(yazar)) {
                java.time.LocalDateTime simdi = java.time.LocalDateTime.now();
                java.time.format.DateTimeFormatter format = java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
                String tarihSaat = simdi.format(format);
                
                bw.write("[" + tarihSaat + "] - İşlem: " + islem);
                bw.newLine();
            }
            tabloyaYukle(LOG_DOSYASI, tblLoglar);
        } catch (IOException e) {
            System.out.println("Log kaydedilirken hata oluştu: " + e.getMessage());
        }
    }
    
    private void oyunSonucuKaydet(boolean kazanildiMi) {
        File dosya = new File(OYUNLAR_DOSYASI);
        
        try {
            FileWriter yazar = new FileWriter(dosya, true);
            try (BufferedWriter bw = new BufferedWriter(yazar)) {
                java.time.LocalDateTime simdi = java.time.LocalDateTime.now();
                java.time.format.DateTimeFormatter format = java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
                String tarihSaat = simdi.format(format);
                
                String durum = kazanildiMi ? "Kazandı" : "Kaybetti";
                
                bw.write(tarihSaat + " | Süre: " + gecenSaniye + " saniye | Sonuç: " + durum);
                bw.newLine();
            }
            tabloyaYukle(OYUNLAR_DOSYASI, tblSkorlar);
        } catch (IOException e) {
            System.out.println("Oyun sonucu kaydedilirken hata: " + e.getMessage());
        }
    }
    
    private void oyunBitir(boolean kazanildi) {
        zamanlayici.stop();
        txtHarf.setEnabled(false);
        txtKelime.setEnabled(false);
        
        String mesaj = kazanildi ? "Tebrikler Kazandınız!" : "Kaybettiniz! Kelime: " + secilenKelime;
        javax.swing.JOptionPane.showMessageDialog(this, mesaj);
        
        oyunSonucuKaydet(kazanildi);
    }
    
    private void resmiGuncelle(int adim) {
        if(adim > 11) adim = 11;
        javax.swing.ImageIcon ikon = new javax.swing.ImageIcon(RESIM_YOLU + adim + ".jpg");
        java.awt.Image image = ikon.getImage().getScaledInstance(lblResim.getWidth(), lblResim.getHeight(), java.awt.Image.SCALE_SMOOTH);
        lblResim.setIcon(new javax.swing.ImageIcon(image));
    }
    
    private void tabloyaYukle(String dosyaYolu, javax.swing.JTable tablo) {
        DefaultTableModel model = (DefaultTableModel) tablo.getModel();
        model.setRowCount(0);
        try (BufferedReader br = new BufferedReader(new FileReader(dosyaYolu))) {
            String satir;
            while ((satir = br.readLine()) != null) {
                String[] sutunlar = satir.split("\\|");
                model.addRow(sutunlar);
            }
        } catch (Exception e) {}
    }
    
    private void dosyaTemizle(String dosyaYolu, javax.swing.JTable tablo) {
        try {
            String gercekSifre;
            try (Scanner okuyucu = new Scanner(new java.io.File(SIFRE_DOSYASI))) {
                gercekSifre = okuyucu.nextLine();
            }
            
            String girilen = javax.swing.JOptionPane.showInputDialog("Temizlemek için şifrenizi girin:");
            if(girilen != null && girilen.equals(gercekSifre)) {
                new FileWriter(dosyaYolu).close();
                tabloyaYukle(dosyaYolu, tablo);
                javax.swing.JOptionPane.showMessageDialog(this, "Temizleme başarılı!");
            } else {
                javax.swing.JOptionPane.showMessageDialog(this, "Hatalı şifre!");
            }
        } catch (HeadlessException | IOException e) {}
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        lblResim = new javax.swing.JLabel();
        txtKelime = new javax.swing.JTextField();
        btnKelimeTahmin = new javax.swing.JButton();
        lblSure = new javax.swing.JLabel();
        txtHarf = new javax.swing.JTextField();
        btnHarfTahmin = new javax.swing.JButton();
        pnlKelime = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblSkorlar = new javax.swing.JTable();
        btnSkorTemizle = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblLoglar = new javax.swing.JTable();
        btnLogTemizle = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        btnOyunaBasla = new javax.swing.JMenuItem();
        btnYenidenBaslat = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        btnKelimeTahmin.setText("Kelime Tahmin Et");
        btnKelimeTahmin.addActionListener(this::btnKelimeTahminActionPerformed);

        lblSure.setText("Oyun Başlamadı");

        btnHarfTahmin.setText("Harf Tahmin Et");
        btnHarfTahmin.addActionListener(this::btnHarfTahminActionPerformed);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(lblSure, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(29, 29, 29))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(txtKelime, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnKelimeTahmin))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(txtHarf, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnHarfTahmin))
                            .addComponent(pnlKelime, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 106, Short.MAX_VALUE)))
                .addComponent(lblResim, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblResim, javax.swing.GroupLayout.PREFERRED_SIZE, 326, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(lblSure, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(pnlKelime, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtKelime, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnKelimeTahmin, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtHarf, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnHarfTahmin, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(59, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Oyun Oynama", jPanel1);

        tblSkorlar.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Tarih / Saat", "Oyun Süresi", "Sonuç"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tblSkorlar);

        btnSkorTemizle.setText("Temizle");
        btnSkorTemizle.addActionListener(this::btnSkorTemizleActionPerformed);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 654, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnSkorTemizle, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 356, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnSkorTemizle, javax.swing.GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE)
                .addGap(16, 16, 16))
        );

        jTabbedPane1.addTab("Eski Skorları Görüntüleme", jPanel2);

        tblLoglar.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Tarih / Saat", "İşlem"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(tblLoglar);

        btnLogTemizle.setText("Temizle");
        btnLogTemizle.addActionListener(this::btnLogTemizleActionPerformed);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 654, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnLogTemizle, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 356, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnLogTemizle, javax.swing.GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE)
                .addGap(16, 16, 16))
        );

        jTabbedPane1.addTab("Logları Görüntüleme", jPanel3);

        jMenu1.setText("Oyun");

        btnOyunaBasla.setText("Oyuna Başla");
        btnOyunaBasla.addActionListener(this::btnOyunaBaslaActionPerformed);
        jMenu1.add(btnOyunaBasla);

        btnYenidenBaslat.setText("Oyunu Yeniden Başlat");
        btnYenidenBaslat.addActionListener(this::btnYenidenBaslatActionPerformed);
        jMenu1.add(btnYenidenBaslat);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnYenidenBaslatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnYenidenBaslatActionPerformed
        // TODO add your handling code here:
        
        List<String> kelimeler = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new java.io.FileReader(KELIME_DOSYASI))) {
            String satir;
            while ((satir = br.readLine()) != null) {
                if (satir.trim().length() >= 6) kelimeler.add(satir.trim().toUpperCase());
            }
        } catch (Exception ex) {}
        
        if(kelimeler.isEmpty()) secilenKelime = "PROGRAMLAMA";
        else secilenKelime = kelimeler.get(new Random().nextInt(kelimeler.size()));

        yanlisTahminSayisi = 0;
        gecenSaniye = 0;
        lblSure.setText("Süre: 0 sn");
        resmiGuncelle(1);

        pnlKelime.removeAll();
        for(int i = 0; i < secilenKelime.length(); i++) {
            javax.swing.JLabel lblHarf = new javax.swing.JLabel("*");
            lblHarf.setFont(new java.awt.Font("Tahoma", 1, 24));
            lblHarf.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 5, 0, 5));
            pnlKelime.add(lblHarf);
        }
        pnlKelime.revalidate();
        pnlKelime.repaint();
        
        txtHarf.setEnabled(true);
        txtKelime.setEnabled(true);
        btnHarfTahmin.setEnabled(true);
        btnKelimeTahmin.setEnabled(true);
        zamanlayici.start();
    }//GEN-LAST:event_btnYenidenBaslatActionPerformed

    private void btnHarfTahminActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHarfTahminActionPerformed
        // TODO add your handling code here:
        
        String tahmin = txtHarf.getText().toUpperCase();
        txtHarf.setText("");
        if(tahmin.length() != 1) return;

        char harf = tahmin.charAt(0);
        boolean bulundu = false;
        
        java.awt.Component[] labellar = pnlKelime.getComponents();

        for (int i = 0; i < secilenKelime.length(); i++) {
            if (secilenKelime.charAt(i) == harf) {
                ((javax.swing.JLabel) labellar[i]).setText(String.valueOf(harf));
                bulundu = true;
            }
        }

        if(!bulundu) {
            yanlisTahminSayisi++;
            resmiGuncelle(yanlisTahminSayisi + 1);
            
            if(yanlisTahminSayisi >= 11) {
                oyunBitir(false);
            }
        } else {
            boolean bittiMi = true;
            for (java.awt.Component c : labellar) {
                if (((javax.swing.JLabel) c).getText().equals("*")) {
                    bittiMi = false;
                    break;
                }
            }
            if (bittiMi) {
                oyunBitir(true);
            }
        }
    }//GEN-LAST:event_btnHarfTahminActionPerformed

    private void btnKelimeTahminActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnKelimeTahminActionPerformed
        // TODO add your handling code here:
        
        String tahmin = txtKelime.getText().toUpperCase();
        txtKelime.setText("");
        
        if(tahmin.equals(secilenKelime)) {
            java.awt.Component[] labellar = pnlKelime.getComponents();
            for (int i = 0; i < secilenKelime.length(); i++) {
                ((javax.swing.JLabel) labellar[i]).setText(String.valueOf(secilenKelime.charAt(i)));
            }
            oyunBitir(true);
        } else {
            yanlisTahminSayisi++;
            resmiGuncelle(yanlisTahminSayisi + 1);
            if(yanlisTahminSayisi >= 11) {
                oyunBitir(false);
            }
        }
    }//GEN-LAST:event_btnKelimeTahminActionPerformed

    private void btnSkorTemizleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSkorTemizleActionPerformed
        // TODO add your handling code here:
        
        dosyaTemizle(OYUNLAR_DOSYASI, tblSkorlar);
    }//GEN-LAST:event_btnSkorTemizleActionPerformed

    private void btnLogTemizleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLogTemizleActionPerformed
        // TODO add your handling code here:
        
        dosyaTemizle(LOG_DOSYASI, tblLoglar);
    }//GEN-LAST:event_btnLogTemizleActionPerformed

    private void btnOyunaBaslaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOyunaBaslaActionPerformed
        // TODO add your handling code here:
        
        btnYenidenBaslatActionPerformed(evt);
    }//GEN-LAST:event_btnOyunaBaslaActionPerformed

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
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            try {
                new Odev_2311012037().setVisible(true);
            } catch (IOException ex) {
                System.getLogger(Odev_2311012037.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnHarfTahmin;
    private javax.swing.JButton btnKelimeTahmin;
    private javax.swing.JButton btnLogTemizle;
    private javax.swing.JMenuItem btnOyunaBasla;
    private javax.swing.JButton btnSkorTemizle;
    private javax.swing.JMenuItem btnYenidenBaslat;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel lblResim;
    private javax.swing.JLabel lblSure;
    private javax.swing.JPanel pnlKelime;
    private javax.swing.JTable tblLoglar;
    private javax.swing.JTable tblSkorlar;
    private javax.swing.JTextField txtHarf;
    private javax.swing.JTextField txtKelime;
    // End of variables declaration//GEN-END:variables
}
