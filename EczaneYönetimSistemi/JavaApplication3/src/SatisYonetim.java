import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class SatisYonetim extends JPanel {
    private JTextField tcField, adField, soyadField, barkodField, ilacAdField, kategoriField, fiyatField, miktarField, toplamFiyatField;
    private JTable sepetTable;
    private DefaultTableModel tableModel;
    private JLabel toplamLabel;

    public SatisYonetim() {
        setLayout(new BorderLayout());
        setBackground(new Color(173, 216, 230));

        // Ana panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(173, 216, 230));

        // Üst Panel - Tablo
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] columns = {"Barkod", "Kategori", "İlaç Adı", "Satış Fiyatı", "Miktar", "Toplam Fiyat", "TC No", "Ad", "Soyad"};
        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        sepetTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(sepetTable);
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
        tcField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    musteriBilgisiDoldur();
                }
            }
        });
        tcField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                if (!tcField.getText().isEmpty()) {
                    musteriBilgisiDoldur();
                }
            }
        });
        formPanel.add(tcField);

        formPanel.add(createFormLabel("Ad:"));
        adField = createTextField();
        formPanel.add(adField);

        formPanel.add(createFormLabel("Soyad:"));
        soyadField = createTextField();
        formPanel.add(soyadField);

        formPanel.add(createFormLabel("Barkod:"));
        barkodField = createTextField();
        barkodField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    ilacBilgisiDoldur();
                }
            }
        });
        formPanel.add(barkodField);

        formPanel.add(createFormLabel("İlaç Adı:"));
        ilacAdField = createTextField();
        formPanel.add(ilacAdField);

        formPanel.add(createFormLabel("Kategori:"));
        kategoriField = createTextField();
        formPanel.add(kategoriField);

        formPanel.add(createFormLabel("Satış Fiyatı:"));
        fiyatField = createTextField();
        formPanel.add(fiyatField);

        formPanel.add(createFormLabel("Miktar:"));
        miktarField = createTextField();
        formPanel.add(miktarField);

        formPanel.add(createFormLabel("Toplam Fiyat:"));
        toplamFiyatField = createTextField();
        formPanel.add(toplamFiyatField);

        formPanel.add(Box.createVerticalStrut(10));

        // Buton Paneli
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBackground(new Color(173, 216, 230));

        JButton ekleBtn = createButton("Ekle", e -> ekle());
        JButton silBtn = createButton("Sil", e -> sil());
        JButton satisYapBtn = createButton("Satış Yap", e -> satisYap());
        JButton satisIptalBtn = createButton("Satış İptal", e -> satisIptal());
        JButton satislariGorBtn = createButton("Satışları Görüntüle", e -> satislariGor());
        JButton temizleBtn = createButton("Temizle", e -> temizle());

        buttonPanel.add(ekleBtn);
        buttonPanel.add(silBtn);
        buttonPanel.add(satisYapBtn);
        buttonPanel.add(satisIptalBtn);
        buttonPanel.add(satislariGorBtn);
        buttonPanel.add(temizleBtn);

        formPanel.add(buttonPanel);

        // Sağ Panel - Toplam Fiyat
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(new Color(173, 216, 230));
        rightPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        toplamLabel = new JLabel("GENEL TOPLAM: 0 TL");
        toplamLabel.setHorizontalAlignment(JLabel.CENTER);
        toplamLabel.setFont(new Font("Times New Roman", Font.BOLD, 18));
        rightPanel.add(toplamLabel, BorderLayout.CENTER);

        // Alt paneli düzenle
        bottomPanel.add(formPanel, BorderLayout.WEST);
        bottomPanel.add(rightPanel, BorderLayout.CENTER);

        // Ana panele ekle
        mainPanel.add(topPanel, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        add(mainPanel);

        // İlk yükleme
        sepetListele();
        hesapla();

        // Dinamik hesaplamalar
        miktarField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                hesaplaToplamFiyat();
            }
        });
        fiyatField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                hesaplaToplamFiyat();
            }
        });
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

    private void sepetListele() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            tableModel.setRowCount(0);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT barkod, kategori, ilacad, satisfiyati, miktari, toplamfiyat, tcno, ad, soyad FROM sepet");
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getString("barkod"),
                    rs.getString("kategori"),
                    rs.getString("ilacad"),
                    rs.getDouble("satisfiyati"),
                    rs.getInt("miktari"),
                    rs.getDouble("toplamfiyat"),
                    rs.getString("tcno"),
                    rs.getString("ad"),
                    rs.getString("soyad")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Veritabanı hatası: " + e.getMessage());
        }
        hesapla();
    }

    private void hesapla() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT SUM(toplamfiyat) as toplam FROM sepet");
            if (rs.next()) {
                double toplam = rs.getDouble("toplam");
                toplamLabel.setText(String.format("GENEL TOPLAM: %.2f TL", toplam));
            } else {
                toplamLabel.setText("GENEL TOPLAM: 0.00 TL");
            }
        } catch (SQLException e) {
            toplamLabel.setText("GENEL TOPLAM: 0.00 TL");
        }
    }

    private void hesaplaToplamFiyat() {
        try {
            double fiyat = Double.parseDouble(fiyatField.getText());
            int miktar = Integer.parseInt(miktarField.getText());
            toplamFiyatField.setText(String.valueOf(fiyat * miktar));
        } catch (Exception e) {
            toplamFiyatField.setText("");
        }
    }

    private void ilacBilgisiDoldur() {
        String barkod = barkodField.getText();
        if (barkod.isEmpty()) return;
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT kategori, ad, fiyat, stok_miktari FROM ilaclar WHERE barkod=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, barkod);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                kategoriField.setText(rs.getString("kategori"));
                ilacAdField.setText(rs.getString("ad"));
                fiyatField.setText(String.valueOf(rs.getDouble("fiyat")));
                miktarField.setText("1"); // Varsayılan miktar 1
                hesaplaToplamFiyat();
            } else {
                JOptionPane.showMessageDialog(this, "İlaç bulunamadı!");
                barkodField.setText("");
                kategoriField.setText("");
                ilacAdField.setText("");
                fiyatField.setText("");
                miktarField.setText("");
                toplamFiyatField.setText("");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "İlaç bilgileri alınırken hata oluştu: " + e.getMessage());
        }
    }

    private void musteriBilgisiDoldur() {
        String tc = tcField.getText().trim();
        if (tc.isEmpty()) return;
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT * FROM musterilistesi WHERE tcno=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, tc);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                adField.setText(rs.getString("ad"));
                soyadField.setText(rs.getString("soyad"));
            } else {
                JOptionPane.showMessageDialog(this, "Müşteri bulunamadı!");
                tcField.setText("");
                adField.setText("");
                soyadField.setText("");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Müşteri bilgileri alınırken hata oluştu: " + e.getMessage());
        }
    }

    private void ekle() {
        if (barkodField.getText().isEmpty() || ilacAdField.getText().isEmpty() || 
            kategoriField.getText().isEmpty() || fiyatField.getText().isEmpty() || 
            miktarField.getText().isEmpty() || toplamFiyatField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Lütfen ilaç bilgilerini eksiksiz doldurunuz!");
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            // Önce stok kontrolü yap
            String stokKontrolSql = "SELECT stok_miktari FROM ilaclar WHERE barkod=?";
            PreparedStatement stokStmt = conn.prepareStatement(stokKontrolSql);
            stokStmt.setString(1, barkodField.getText());
            ResultSet stokRs = stokStmt.executeQuery();
            
            if (stokRs.next()) {
                int mevcutStok = stokRs.getInt("stok_miktari");
                int istenenMiktar = Integer.parseInt(miktarField.getText());
                
                if (mevcutStok < istenenMiktar) {
                    JOptionPane.showMessageDialog(this, "Yetersiz stok! Mevcut stok: " + mevcutStok);
                    return;
                }
                
                // Stok yeterliyse sepete ekle
                String sql = "INSERT INTO sepet (barkod, kategori, ilacad, satisfiyati, miktari, toplamfiyat, tcno, ad, soyad) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, barkodField.getText());
                stmt.setString(2, kategoriField.getText());
                stmt.setString(3, ilacAdField.getText());
                stmt.setDouble(4, Double.parseDouble(fiyatField.getText()));
                stmt.setInt(5, istenenMiktar);
                stmt.setDouble(6, Double.parseDouble(toplamFiyatField.getText()));
                stmt.setString(7, tcField.getText());
                stmt.setString(8, adField.getText());
                stmt.setString(9, soyadField.getText());
                stmt.executeUpdate();
                
                sepetListele();
                // Sadece ilaç bilgilerini temizle, müşteri bilgilerini koru
                barkodField.setText("");
                ilacAdField.setText("");
                kategoriField.setText("");
                fiyatField.setText("");
                miktarField.setText("");
                toplamFiyatField.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "İlaç bulunamadı!");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Ekleme hatası: " + e.getMessage());
        }
    }

    private void sil() {
        int row = sepetTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Lütfen silinecek satırı seçin.");
            return;
        }
        String barkod = tableModel.getValueAt(row, 0).toString();
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "DELETE FROM sepet WHERE barkod=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, barkod);
            stmt.executeUpdate();
            sepetListele();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Silme hatası: " + e.getMessage());
        }
    }

    private void satisYap() {
        // TC no kontrolü
        if (tcField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Lütfen müşteri TC numarasını giriniz!");
            return;
        }

        // Müşteri kontrolü
        try (Connection conn = DatabaseConnection.getConnection()) {
            String musteriKontrolSql = "SELECT * FROM musterilistesi WHERE tcno=?";
            PreparedStatement musteriStmt = conn.prepareStatement(musteriKontrolSql);
            musteriStmt.setString(1, tcField.getText());
            ResultSet musteriRs = musteriStmt.executeQuery();
            
            if (!musteriRs.next()) {
                JOptionPane.showMessageDialog(this, "Müşteri bulunamadı! Lütfen geçerli bir TC numarası giriniz.");
                return;
            }

            // Müşteri bilgilerini al
            String musteriAd = musteriRs.getString("ad");
            String musteriSoyad = musteriRs.getString("soyad");

            // Sepetteki tüm ürünleri satış tablosuna ekle
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT barkod, kategori, ilacad, satisfiyati, miktari, toplamfiyat, tcno, ad, soyad FROM sepet");
            
            while (rs.next()) {
                // Satış kaydı oluştur
                String satisSql = "INSERT INTO satis (barkod, kategori, ilacAd, satis_fiyati, miktar, toplam_fiyat, tc_no, ad, soyad) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement satisStmt = conn.prepareStatement(satisSql);
                satisStmt.setString(1, rs.getString("barkod"));
                satisStmt.setString(2, rs.getString("kategori"));
                satisStmt.setString(3, rs.getString("ilacad"));
                satisStmt.setDouble(4, rs.getDouble("satisfiyati"));
                satisStmt.setInt(5, rs.getInt("miktari"));
                satisStmt.setDouble(6, rs.getDouble("toplamfiyat"));
                satisStmt.setString(7, tcField.getText());
                satisStmt.setString(8, musteriAd);
                satisStmt.setString(9, musteriSoyad);
                satisStmt.executeUpdate();
                
                // Stok miktarını güncelle
                String stokGuncelleSql = "UPDATE ilaclar SET stok_miktari = stok_miktari - ? WHERE barkod = ?";
                PreparedStatement stokStmt = conn.prepareStatement(stokGuncelleSql);
                stokStmt.setInt(1, rs.getInt("miktari"));
                stokStmt.setString(2, rs.getString("barkod"));
                stokStmt.executeUpdate();
            }
            
            // Sepeti temizle
            stmt.executeUpdate("DELETE FROM sepet");
            sepetListele();
            JOptionPane.showMessageDialog(this, "Satış işlemi başarıyla gerçekleştirildi.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Satış hatası: " + e.getMessage());
        }
    }

    private void satisIptal() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("DELETE FROM sepet");
            sepetListele();
            JOptionPane.showMessageDialog(this, "Satış iptal edildi ve sepet temizlendi.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "İptal hatası: " + e.getMessage());
        }
    }

    private void satislariGor() {
        JFrame frame = new JFrame("Satışlar");
        frame.setSize(800, 400);
        frame.setLocationRelativeTo(this);

        DefaultTableModel satisModel = new DefaultTableModel(new String[]{"Barkod", "Kategori", "İlaç Adı", "Satış Fiyatı", "Miktar", "Toplam Fiyat", "TC No", "Ad", "Soyad"}, 0);
        JTable satisTable = new JTable(satisModel);

        try (Connection conn = DatabaseConnection.getConnection()) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT barkod, kategori, ilacAd, satis_fiyati, miktar, toplam_fiyat, tc_no, ad, soyad FROM satis");
            while (rs.next()) {
                satisModel.addRow(new Object[]{
                    rs.getString("barkod"),
                    rs.getString("kategori"),
                    rs.getString("ilacAd"),
                    rs.getDouble("satis_fiyati"),
                    rs.getInt("miktar"),
                    rs.getDouble("toplam_fiyat"),
                    rs.getString("tc_no"),
                    rs.getString("ad"),
                    rs.getString("soyad")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Satışları görüntüleme hatası: " + e.getMessage());
        }

        frame.add(new JScrollPane(satisTable));
        frame.setVisible(true);
    }

    private void temizle() {
        tcField.setText("");
        adField.setText("");
        soyadField.setText("");
        barkodField.setText("");
        ilacAdField.setText("");
        kategoriField.setText("");
        fiyatField.setText("");
        miktarField.setText("");
        toplamFiyatField.setText("");
        sepetTable.clearSelection();
    }

    private boolean validateFields() {
        if (barkodField.getText().isEmpty() || ilacAdField.getText().isEmpty() || 
            kategoriField.getText().isEmpty() || fiyatField.getText().isEmpty() || 
            miktarField.getText().isEmpty() || toplamFiyatField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Lütfen ilaç bilgilerini eksiksiz doldurunuz!");
            return false;
        }
        return true;
    }
}
