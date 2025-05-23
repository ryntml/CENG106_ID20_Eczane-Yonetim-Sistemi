import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.text.ParseException;

public class MusteriYonetim extends JPanel {
    private JTextField tcField, adField, soyadField, telField, cinsiyetField, adresField, receteNoField, aramaField;
    private JTextField dogumTarihiField;
    private JTable musteriTable;
    private DefaultTableModel tableModel;

    public MusteriYonetim() {
        setLayout(new BorderLayout());
        setBackground(new Color(173, 216, 230));

        // Ana panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(173, 216, 230));

        // Üst Panel - Tablo
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] columnNames = {"TC No", "Ad", "Soyad", "Telefon", "Cinsiyet", "Adres", "Reçete No", "Doğum Tarihi"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        musteriTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(musteriTable);
        topPanel.add(scrollPane, BorderLayout.CENTER);

        // Alt Panel - Form alanları
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(new Color(173, 216, 230));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Sol Panel - Form alanları
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(new Color(173, 216, 230));

        formPanel.add(createFormLabel("TC No:"));
        tcField = createTextField();
        formPanel.add(tcField);

        formPanel.add(createFormLabel("Ad:"));
        adField = createTextField();
        formPanel.add(adField);

        formPanel.add(createFormLabel("Soyad:"));
        soyadField = createTextField();
        formPanel.add(soyadField);

        formPanel.add(createFormLabel("Telefon:"));
        telField = createTextField();
        formPanel.add(telField);

        formPanel.add(createFormLabel("Cinsiyet:"));
        cinsiyetField = createTextField();
        formPanel.add(cinsiyetField);

        formPanel.add(createFormLabel("Adres:"));
        adresField = createTextField();
        formPanel.add(adresField);

        formPanel.add(createFormLabel("Reçete No:"));
        receteNoField = createTextField();
        formPanel.add(receteNoField);

        formPanel.add(createFormLabel("Doğum Tarihi:"));
        dogumTarihiField = createTextField();
        formPanel.add(dogumTarihiField);

        formPanel.add(Box.createVerticalStrut(10));

        // Buton Paneli
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBackground(new Color(173, 216, 230));

        JButton kaydetBtn = createButton("Kaydet", e -> kaydet());
        JButton silBtn = createButton("Sil", e -> sil());
        JButton guncelleBtn = createButton("Güncelle", e -> guncelle());
        JButton araBtn = createButton("Ara", e -> ara());
        JButton temizleBtn = createButton("Temizle", e -> temizle());

        buttonPanel.add(kaydetBtn);
        buttonPanel.add(silBtn);
        buttonPanel.add(guncelleBtn);
        buttonPanel.add(araBtn);
        buttonPanel.add(temizleBtn);

        formPanel.add(buttonPanel);

        // Alt paneli düzenle
        bottomPanel.add(formPanel, BorderLayout.CENTER);

        // Ana panele ekle
        mainPanel.add(topPanel, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        add(mainPanel);

        // İlk yükleme
        musterileriListele();
    }

    private JLabel createFormLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Times New Roman", Font.PLAIN, 16));
        return label;
    }

    private JTextField createTextField() {
        JTextField field = new JTextField();
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        return field;
    }

    private JButton createButton(String text, ActionListener action) {
        JButton button = new JButton(text);
        button.setBackground(new Color(240, 255, 255));
        button.setFocusPainted(false);
        button.addActionListener(action);
        return button;
    }

    private void musterileriListele() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            tableModel.setRowCount(0);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM musterilistesi");
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getString("tcno"), rs.getString("ad"), rs.getString("soyad"),
                    rs.getString("tlfno"), rs.getString("cinsiyeti"), rs.getString("adresi"),
                    rs.getString("receteno"), rs.getString("dogumtarih")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Veritabanı hatası: " + e.getMessage());
        }
    }

    private void kaydet() {
        if (!validateFields()) return;

        String tcno = tcField.getText();
        String ad = adField.getText();
        String soyad = soyadField.getText();
        String tlfno = telField.getText();
        String cinsiyet = cinsiyetField.getText();
        String adresi = adresField.getText();
        String receteno = receteNoField.getText();
        String dogumtarihStr = dogumTarihiField.getText();

        String dogumtarihDB = null;
        if (!dogumtarihStr.isEmpty()) {
            try {
                SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy");
                SimpleDateFormat dbFormat = new SimpleDateFormat("yyyy-MM-dd");
                java.util.Date date = inputFormat.parse(dogumtarihStr);
                dogumtarihDB = dbFormat.format(date);
            } catch (ParseException e) {
                JOptionPane.showMessageDialog(this, "Doğum tarihi formatı hatalı! Lütfen GG/AA/YYYY formatında giriniz.");
                return;
            }
        }

        String sql = "INSERT INTO musterilistesi (tcno, ad, soyad, tlfno, cinsiyeti, adresi, receteno, dogumtarih) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, tcno);
            pstmt.setString(2, ad);
            pstmt.setString(3, soyad);
            pstmt.setString(4, tlfno);
            pstmt.setString(5, cinsiyet);
            pstmt.setString(6, adresi);
            pstmt.setString(7, receteno);
            pstmt.setString(8, dogumtarihDB);

            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Müşteri başarıyla eklendi!");
            musterileriListele();
            temizle();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Veritabanı hatası: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void guncelle() {
        String tcno = tcField.getText().trim();
        if (tcno.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Lütfen TC No giriniz!");
            return;
        }

        // Önce müşterinin var olup olmadığını kontrol et
        try (Connection conn = DatabaseConnection.getConnection()) {
            String checkSql = "SELECT COUNT(*) FROM musterilistesi WHERE tcno=?";
            PreparedStatement checkStmt = conn.prepareStatement(checkSql);
            checkStmt.setString(1, tcno);
            ResultSet rs = checkStmt.executeQuery();
            rs.next();
            if (rs.getInt(1) == 0) {
                JOptionPane.showMessageDialog(this, "Güncellenecek müşteri bulunamadı!");
                return;
            }

            // Form alanlarından değerleri al
            String ad = adField.getText().trim();
            String soyad = soyadField.getText().trim();
            String tlfno = telField.getText().trim();
            String cinsiyet = cinsiyetField.getText().trim();
            String adresi = adresField.getText().trim();
            String receteno = receteNoField.getText().trim();
            String dogumtarihStr = dogumTarihiField.getText().trim();

            // Doğum tarihi formatını kontrol et
            String dogumtarihDB = null;
            if (!dogumtarihStr.isEmpty()) {
                try {
                    SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy");
                    SimpleDateFormat dbFormat = new SimpleDateFormat("yyyy-MM-dd");
                    java.util.Date date = inputFormat.parse(dogumtarihStr);
                    dogumtarihDB = dbFormat.format(date);
                } catch (ParseException e) {
                    JOptionPane.showMessageDialog(this, "Doğum tarihi formatı hatalı! Lütfen GG/AA/YYYY formatında giriniz.");
                    return;
                }
            }

            // Güncelleme sorgusunu hazırla ve çalıştır
            String updateSql = "UPDATE musterilistesi SET ad=?, soyad=?, tlfno=?, cinsiyeti=?, adresi=?, receteno=?, dogumtarih=? WHERE tcno=?";
            PreparedStatement updateStmt = conn.prepareStatement(updateSql);
            updateStmt.setString(1, ad);
            updateStmt.setString(2, soyad);
            updateStmt.setString(3, tlfno);
            updateStmt.setString(4, cinsiyet);
            updateStmt.setString(5, adresi);
            updateStmt.setString(6, receteno);
            updateStmt.setString(7, dogumtarihDB);
            updateStmt.setString(8, tcno);

            int affected = updateStmt.executeUpdate();
            if (affected > 0) {
                JOptionPane.showMessageDialog(this, "Müşteri başarıyla güncellendi!");
                musterileriListele();
                temizle();
            } else {
                JOptionPane.showMessageDialog(this, "Güncelleme yapılamadı!");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Güncelleme hatası: " + e.getMessage());
        }
    }

    private void sil() {
        if (tcField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Lütfen TC Kimlik Numarası giriniz.");
            return;
        }
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "DELETE FROM musterilistesi WHERE tcno=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, tcField.getText());
            int affected = stmt.executeUpdate();
            musterileriListele();
            temizle();
            if (affected > 0)
                JOptionPane.showMessageDialog(this, "Müşteri başarıyla silindi.");
            else
                JOptionPane.showMessageDialog(this, "Müşteri bulunamadı.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Silme hatası: " + e.getMessage());
        }
    }

    private void ara() {
        String tc = tcField.getText().trim();
        if (tc.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Lütfen TC No giriniz!");
            return;
        }
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT * FROM musterilistesi WHERE tcno=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, tc);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                // Önce tüm müşterileri listele
                musterileriListele();
                
                // Form alanlarını doldur
                tcField.setText(rs.getString("tcno"));
                adField.setText(rs.getString("ad"));
                soyadField.setText(rs.getString("soyad"));
                telField.setText(rs.getString("tlfno"));
                cinsiyetField.setText(rs.getString("cinsiyeti"));
                adresField.setText(rs.getString("adresi"));
                receteNoField.setText(rs.getString("receteno"));
                
                // Doğum tarihini formatla
                String dogumTarihi = rs.getString("dogumtarih");
                if (dogumTarihi != null && !dogumTarihi.isEmpty()) {
                    try {
                        SimpleDateFormat dbFormat = new SimpleDateFormat("yyyy-MM-dd");
                        SimpleDateFormat displayFormat = new SimpleDateFormat("dd/MM/yyyy");
                        java.util.Date date = dbFormat.parse(dogumTarihi);
                        dogumTarihiField.setText(displayFormat.format(date));
                    } catch (ParseException e) {
                        dogumTarihiField.setText(dogumTarihi);
                    }
                } else {
                    dogumTarihiField.setText("");
                }

                // İlgili satırı bul ve seç
                for (int i = 0; i < tableModel.getRowCount(); i++) {
                    if (tableModel.getValueAt(i, 0).toString().equals(tc)) {
                        musteriTable.setRowSelectionInterval(i, i);
                        musteriTable.scrollRectToVisible(musteriTable.getCellRect(i, 0, true));
                        break;
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Müşteri bulunamadı.");
                temizle();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Arama hatası: " + e.getMessage());
        }
    }

    private void tabloSecim() {
        int row = musteriTable.getSelectedRow();
        if (row >= 0) {
            tcField.setText(tableModel.getValueAt(row, 0).toString());
            adField.setText(tableModel.getValueAt(row, 1).toString());
            soyadField.setText(tableModel.getValueAt(row, 2).toString());
            telField.setText(tableModel.getValueAt(row, 3).toString());
            cinsiyetField.setText(tableModel.getValueAt(row, 4).toString());
            adresField.setText(tableModel.getValueAt(row, 5).toString());
            receteNoField.setText(tableModel.getValueAt(row, 6).toString());
            
            try {
                Object dateValue = tableModel.getValueAt(row, 7);
                if (dateValue != null) {
                    String dogumTarihiStr = dateValue.toString();
                    if (!dogumTarihiStr.isEmpty()) {
                        SimpleDateFormat dbFormat = new SimpleDateFormat("yyyy-MM-dd");
                        SimpleDateFormat displayFormat = new SimpleDateFormat("dd/MM/yyyy");
                        java.util.Date date = dbFormat.parse(dogumTarihiStr);
                        dogumTarihiField.setText(displayFormat.format(date));
                    } else {
                        dogumTarihiField.setText("");
                    }
                } else {
                    dogumTarihiField.setText("");
                }
            } catch (ParseException e) {
                System.out.println("Tablodan tarih okunurken format hatası: " + e.getMessage());
                dogumTarihiField.setText("Format Hatası");
            } catch (Exception e) {
                System.out.println("Tarih yüklenirken hata: " + e.getMessage());
                dogumTarihiField.setText("");
            }
        }
    }

    private void temizle() {
        tcField.setText("");
        adField.setText("");
        soyadField.setText("");
        telField.setText("");
        cinsiyetField.setText("");
        adresField.setText("");
        receteNoField.setText("");
        dogumTarihiField.setText("");
        aramaField.setText("");
        musteriTable.clearSelection();
    }

    private boolean validateFields() {
        if (tcField.getText().isEmpty() || adField.getText().isEmpty() || soyadField.getText().isEmpty() ||
            telField.getText().isEmpty() || cinsiyetField.getText().isEmpty() || adresField.getText().isEmpty() ||
            receteNoField.getText().isEmpty() || dogumTarihiField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Lütfen tüm alanları doldurunuz!");
            return false;
        }
        
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy");
            inputFormat.setLenient(false);
            inputFormat.parse(dogumTarihiField.getText());
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(this, "Doğum tarihi formatı hatalı! Lütfen GG/AA/YYYY formatında giriniz.");
            return false;
        }

        return true;
    }
}
