import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.File;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class EczaciYonetim extends JPanel {
    private JTextField tcField;
    private JTextField kullaniciAdiField;
    private JTextField sifreField;
    private JTextField adField;
    private JTextField soyadField;
    private JTextField emailField;
    private JTextField telefonField;
    private JTextField maasField;
    private JTextField adresField;
    private JTextField gorevField;
    private JButton kaydetBtn;
    private JButton guncelleBtn;
    private JButton silBtn;
    private JButton temizleBtn;
    private JButton araBtn;
    private JButton fotoSecBtn;
    private JTable eczaciTable;
    private DefaultTableModel tableModel;
    private JDateChooser dogumTarihiChooser;
    private JDateChooser girisTarihiChooser;
    private Connection conn;
    private String secilenFotoPath;
    private JLabel fotoLabel;

    public EczaciYonetim() {
        try {
            conn = DatabaseConnection.getConnection();
            initializeUI();
            eczacilariGoster();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Veritabanı bağlantı hatası: " + e.getMessage());
        }
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        setBackground(new Color(173, 216, 230));

        // Ana panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(173, 216, 230));

        // Üst Panel - Tablo
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] columnNames = {"ID", "TC No", "Kullanıcı Adı", "Ad", "Soyad", "Email", "Telefon", "Maaş", "Adres", "Görev", "Giriş Tarihi", "Doğum Tarihi"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        eczaciTable = new JTable(tableModel);
        eczaciTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(eczaciTable);
        topPanel.add(scrollPane, BorderLayout.CENTER);

        // Alt Panel - Form alanları
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(new Color(173, 216, 230));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Sol Panel - Fotoğraf ve butonlar
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(new Color(173, 216, 230));

        // Fotoğraf alanı
        JPanel fotoPanel = new JPanel();
        fotoPanel.setBackground(new Color(173, 216, 230));
        fotoLabel = new JLabel();
        fotoLabel.setPreferredSize(new Dimension(150, 150));
        fotoLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        fotoPanel.add(fotoLabel);
        leftPanel.add(fotoPanel);
        leftPanel.add(Box.createVerticalStrut(10));

        // Butonlar
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        buttonPanel.setBackground(new Color(173, 216, 230));
        kaydetBtn = createButton("KAYDET", new Color(0, 191, 255));
        guncelleBtn = createButton("GÜNCELLE", new Color(0, 191, 255));
        silBtn = createButton("SİL", new Color(0, 191, 255));
        temizleBtn = createButton("TEMİZLE", new Color(0, 191, 255));
        araBtn = createButton("ARA", new Color(112, 128, 144));
        fotoSecBtn = createButton("FOTO SEÇ", new Color(112, 128, 144));

        buttonPanel.add(kaydetBtn);
        buttonPanel.add(guncelleBtn);
        buttonPanel.add(silBtn);
        buttonPanel.add(temizleBtn);
        buttonPanel.add(araBtn);
        buttonPanel.add(fotoSecBtn);
        leftPanel.add(buttonPanel);

        // Sağ Panel - Form alanları
        JPanel rightPanel = new JPanel(new GridLayout(7, 2, 5, 5));
        rightPanel.setBackground(new Color(173, 216, 230));

        tcField = createTextField("TC No:");
        kullaniciAdiField = createTextField("Kullanıcı Adı:");
        sifreField = createTextField("Şifre:");
        adField = createTextField("Ad:");
        soyadField = createTextField("Soyad:");
        emailField = createTextField("Email:");
        telefonField = createTextField("Telefon:");
        maasField = createTextField("Maaş:");
        adresField = createTextField("Adres:");
        gorevField = createTextField("Görev:");
        dogumTarihiChooser = new JDateChooser();
        girisTarihiChooser = new JDateChooser();
        dogumTarihiChooser.setDateFormatString("dd/MM/yyyy");
        girisTarihiChooser.setDateFormatString("dd/MM/yyyy");
        girisTarihiChooser.setDate(new Date());

        rightPanel.add(createLabeledPanel("TC No:", tcField));
        rightPanel.add(createLabeledPanel("Kullanıcı Adı:", kullaniciAdiField));
        rightPanel.add(createLabeledPanel("Şifre:", sifreField));
        rightPanel.add(createLabeledPanel("Ad:", adField));
        rightPanel.add(createLabeledPanel("Soyad:", soyadField));
        rightPanel.add(createLabeledPanel("Email:", emailField));
        rightPanel.add(createLabeledPanel("Telefon:", telefonField));
        rightPanel.add(createLabeledPanel("Maaş:", maasField));
        rightPanel.add(createLabeledPanel("Adres:", adresField));
        rightPanel.add(createLabeledPanel("Görev:", gorevField));
        rightPanel.add(createLabeledPanel("Doğum Tarihi:", dogumTarihiChooser));
        rightPanel.add(createLabeledPanel("Giriş Tarihi:", girisTarihiChooser));

        // Alt paneli düzenle
        bottomPanel.add(leftPanel, BorderLayout.WEST);
        bottomPanel.add(rightPanel, BorderLayout.CENTER);

        // Ana panele ekle
        mainPanel.add(topPanel, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        add(mainPanel);

        // Event listeners
        kaydetBtn.addActionListener(e -> kaydet());
        guncelleBtn.addActionListener(e -> guncelle());
        silBtn.addActionListener(e -> sil());
        temizleBtn.addActionListener(e -> temizle());
        araBtn.addActionListener(e -> ara());
        fotoSecBtn.addActionListener(e -> fotoSec());
    }

    private void fotoSec() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
            public boolean accept(File f) {
                return f.isDirectory() || f.getName().toLowerCase().endsWith(".jpg") || 
                       f.getName().toLowerCase().endsWith(".jpeg") || 
                       f.getName().toLowerCase().endsWith(".png");
            }
            public String getDescription() {
                return "Resim Dosyaları (*.jpg, *.jpeg, *.png)";
            }
        });

        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            if (selectedFile != null && selectedFile.exists()) {
                try {
                    BufferedImage originalImage = ImageIO.read(selectedFile);
                    if (originalImage != null) {
                        secilenFotoPath = selectedFile.getAbsolutePath();
                        Image scaledImage = originalImage.getScaledInstance(150, 150, Image.SCALE_SMOOTH);
                        fotoLabel.setIcon(new ImageIcon(scaledImage));
                    } else {
                        JOptionPane.showMessageDialog(this, "Seçilen dosya bir resim dosyası değil!");
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Fotoğraf yüklenirken hata oluştu: " + e.getMessage());
                }
            } else {
                JOptionPane.showMessageDialog(this, "Lütfen geçerli bir dosya seçin!");
            }
        }
    }

    private void kaydet() {
        if (validateFields()) {
            try {
                // TC No kontrolü
                String checkTcSql = "SELECT COUNT(*) FROM eczacilar WHERE tc_no = ?";
                PreparedStatement checkTcStmt = conn.prepareStatement(checkTcSql);
                checkTcStmt.setString(1, tcField.getText());
                ResultSet tcRs = checkTcStmt.executeQuery();
                tcRs.next();
                if (tcRs.getInt(1) > 0) {
                    JOptionPane.showMessageDialog(this, "Bu TC No ile kayıtlı bir eczacı zaten mevcut!");
                    return;
                }

                // Kullanıcı adı kontrolü
                String checkUsernameSql = "SELECT COUNT(*) FROM eczacilar WHERE kullanici_adi = ?";
                PreparedStatement checkUsernameStmt = conn.prepareStatement(checkUsernameSql);
                checkUsernameStmt.setString(1, kullaniciAdiField.getText());
                ResultSet usernameRs = checkUsernameStmt.executeQuery();
                usernameRs.next();
                if (usernameRs.getInt(1) > 0) {
                    JOptionPane.showMessageDialog(this, "Bu kullanıcı adı zaten kullanılıyor!");
                    return;
                }

                // Email format kontrolü
                String email = emailField.getText();
                if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                    JOptionPane.showMessageDialog(this, "Geçersiz email formatı!");
                    return;
                }

                // Telefon format kontrolü
                String telefon = telefonField.getText();
                if (!telefon.matches("^[0-9]{10}$")) {
                    JOptionPane.showMessageDialog(this, "Telefon numarası 10 haneli olmalıdır!");
                    return;
                }

                String sql = "INSERT INTO eczacilar (tc_no, kullanici_adi, sifre, ad, soyad, email, telefon, maas, adres, gorev, giris_tarihi, dogum_tarih, foto_path) " +
                           "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, tcField.getText());
                stmt.setString(2, kullaniciAdiField.getText());
                stmt.setString(3, sifreField.getText());
                stmt.setString(4, adField.getText());
                stmt.setString(5, soyadField.getText());
                stmt.setString(6, email);
                stmt.setString(7, telefon);
                stmt.setDouble(8, Double.parseDouble(maasField.getText()));
                stmt.setString(9, adresField.getText());
                stmt.setString(10, gorevField.getText());
                
                // Tarih formatlarını düzelt
                SimpleDateFormat dbFormat = new SimpleDateFormat("yyyy-MM-dd");
                stmt.setString(11, dbFormat.format(girisTarihiChooser.getDate()));
                stmt.setString(12, dbFormat.format(dogumTarihiChooser.getDate()));

                // Fotoğrafı kaydet
                if (secilenFotoPath != null && !secilenFotoPath.isEmpty()) {
                    String fotoKlasoru = "fotograflar";
                    File klasor = new File(fotoKlasoru);
                    if (!klasor.exists()) {
                        klasor.mkdir();
                    }

                    String yeniFotoAdi = tcField.getText() + "_" + System.currentTimeMillis() + ".jpg";
                    String hedefYol = fotoKlasoru + File.separator + yeniFotoAdi;

                    try {
                        // Fotoğrafı kopyala
                        Files.copy(Paths.get(secilenFotoPath), Paths.get(hedefYol));
                        stmt.setString(13, hedefYol);
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(this, "Fotoğraf kopyalanırken hata oluştu: " + e.getMessage());
                        stmt.setString(13, null);
                    }
                } else {
                    stmt.setString(13, null);
                }

                stmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Eczacı başarıyla kaydedildi!");
                eczacilariGoster();
                temizle();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Veritabanı hatası: " + e.getMessage());
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "İşlem sırasında hata oluştu: " + e.getMessage());
            }
        }
    }

    private void eczacilariGoster() {
        try {
            tableModel.setRowCount(0);
            String sql = "SELECT * FROM eczacilar";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Object[] row = {
                    rs.getInt("id"),
                    rs.getString("tc_no"),
                    rs.getString("kullanici_adi"),
                    rs.getString("ad"),
                    rs.getString("soyad"),
                    rs.getString("email"),
                    rs.getString("telefon"),
                    rs.getDouble("maas"),
                    rs.getString("adres"),
                    rs.getString("gorev"),
                    rs.getString("giris_tarihi"),
                    rs.getString("dogum_tarih")
                };
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Veritabanı hatası: " + e.getMessage());
        }
    }

    private boolean validateFields() {
        if (tcField.getText().isEmpty() || kullaniciAdiField.getText().isEmpty() || 
            sifreField.getText().isEmpty() || adField.getText().isEmpty() || 
            soyadField.getText().isEmpty() || emailField.getText().isEmpty() || 
            telefonField.getText().isEmpty() || maasField.getText().isEmpty() || 
            adresField.getText().isEmpty() || gorevField.getText().isEmpty() || 
            dogumTarihiChooser.getDate() == null) {
            JOptionPane.showMessageDialog(this, "Lütfen tüm alanları doldurunuz!");
            return false;
        }

        try {
            Double.parseDouble(maasField.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Maaş alanı sayısal bir değer olmalıdır!");
            return false;
        }

        return true;
    }

    private void temizle() {
        tcField.setText("");
        kullaniciAdiField.setText("");
        sifreField.setText("");
        adField.setText("");
        soyadField.setText("");
        emailField.setText("");
        telefonField.setText("");
        maasField.setText("");
        adresField.setText("");
        gorevField.setText("");
        dogumTarihiChooser.setDate(null);
        girisTarihiChooser.setDate(new Date());
        fotoLabel.setIcon(null);
        secilenFotoPath = null;
    }

    private JButton createButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        return button;
    }

    private JTextField createTextField(String label) {
        JTextField field = new JTextField();
        field.setPreferredSize(new Dimension(200, 25));
        return field;
    }

    private JPanel createLabeledPanel(String label, JComponent component) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JLabel(label), BorderLayout.WEST);
        panel.add(component, BorderLayout.CENTER);
        return panel;
    }

    private void guncelle() {
        try {
            // Sadece fotoğraf güncellemesi için kontrol
            if (secilenFotoPath != null && !secilenFotoPath.isEmpty()) {
                String fotoKlasoru = "fotograflar";
                File klasor = new File(fotoKlasoru);
                if (!klasor.exists()) {
                    klasor.mkdir();
                }

                String yeniFotoAdi = tcField.getText() + "_" + System.currentTimeMillis() + ".jpg";
                String hedefYol = fotoKlasoru + File.separator + yeniFotoAdi;

                try {
                    // Eski fotoğrafı sil
                    String eskiFotoSql = "SELECT foto_path FROM eczacilar WHERE tc_no = ?";
                    PreparedStatement eskiFotoStmt = conn.prepareStatement(eskiFotoSql);
                    eskiFotoStmt.setString(1, tcField.getText());
                    ResultSet rs = eskiFotoStmt.executeQuery();
                    if (rs.next()) {
                        String eskiFotoPath = rs.getString("foto_path");
                        if (eskiFotoPath != null) {
                            File eskiFoto = new File(eskiFotoPath);
                            if (eskiFoto.exists()) {
                                eskiFoto.delete();
                            }
                        }
                    }

                    // Yeni fotoğrafı kopyala
                    Files.copy(Paths.get(secilenFotoPath), Paths.get(hedefYol));
                    
                    // Sadece fotoğrafı güncelle
                    String updateFotoSql = "UPDATE eczacilar SET foto_path = ? WHERE tc_no = ?";
                    PreparedStatement updateFotoStmt = conn.prepareStatement(updateFotoSql);
                    updateFotoStmt.setString(1, hedefYol);
                    updateFotoStmt.setString(2, tcField.getText());
                    
                    int affectedRows = updateFotoStmt.executeUpdate();
                    if (affectedRows > 0) {
                        JOptionPane.showMessageDialog(this, "Fotoğraf başarıyla güncellendi!");
                        eczacilariGoster();
                        temizle();
                    } else {
                        JOptionPane.showMessageDialog(this, "Fotoğraf güncellenirken hata oluştu!");
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Fotoğraf güncellenirken hata oluştu: " + e.getMessage());
                }
                return;
            }

            // Normal güncelleme işlemi için validasyon
            if (validateFields()) {
                String sql = "UPDATE eczacilar SET kullanici_adi=?, sifre=?, ad=?, soyad=?, email=?, telefon=?, gorev=?, adres=?, maas=?, dogum_tarih=?, foto_path=? " +
                           "WHERE tc_no=?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, kullaniciAdiField.getText());
                stmt.setString(2, sifreField.getText());
                stmt.setString(3, adField.getText());
                stmt.setString(4, soyadField.getText());
                stmt.setString(5, emailField.getText().isEmpty() ? null : emailField.getText());
                stmt.setString(6, telefonField.getText());
                stmt.setString(7, gorevField.getText());
                stmt.setString(8, adresField.getText().isEmpty() ? null : adresField.getText());
                stmt.setDouble(9, Double.parseDouble(maasField.getText()));
                
                // Tarih formatını düzelt
                SimpleDateFormat dbFormat = new SimpleDateFormat("yyyy-MM-dd");
                stmt.setString(10, dbFormat.format(dogumTarihiChooser.getDate()));

                // Mevcut fotoğrafı koru
                String mevcutFotoSql = "SELECT foto_path FROM eczacilar WHERE tc_no = ?";
                PreparedStatement mevcutFotoStmt = conn.prepareStatement(mevcutFotoSql);
                mevcutFotoStmt.setString(1, tcField.getText());
                ResultSet rs = mevcutFotoStmt.executeQuery();
                if (rs.next()) {
                    stmt.setString(11, rs.getString("foto_path"));
                } else {
                    stmt.setString(11, null);
                }

                stmt.setString(12, tcField.getText());

                int affectedRows = stmt.executeUpdate();
                if (affectedRows > 0) {
                    JOptionPane.showMessageDialog(this, "Eczacı başarıyla güncellendi!");
                    eczacilariGoster();
                    temizle();
                } else {
                    JOptionPane.showMessageDialog(this, "Güncellenecek eczacı bulunamadı!");
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Veritabanı hatası: " + e.getMessage());
        }
    }

    private void sil() {
        String tcNo = tcField.getText();
        if (tcNo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Lütfen TC No giriniz!");
            return;
        }

        try {
            String sql = "DELETE FROM eczacilar WHERE tc_no=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, tcNo);

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                JOptionPane.showMessageDialog(this, "Eczacı başarıyla silindi!");
                eczacilariGoster();
                temizle();
            } else {
                JOptionPane.showMessageDialog(this, "Silinecek eczacı bulunamadı!");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Veritabanı hatası: " + e.getMessage());
        }
    }

    private void ara() {
        String tcNo = tcField.getText();
        if (tcNo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Lütfen TC No giriniz!");
            return;
        }

        try {
            String sql = "SELECT * FROM eczacilar WHERE tc_no=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, tcNo);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                kullaniciAdiField.setText(rs.getString("kullanici_adi"));
                sifreField.setText(rs.getString("sifre"));
                adField.setText(rs.getString("ad"));
                soyadField.setText(rs.getString("soyad"));
                emailField.setText(rs.getString("email"));
                telefonField.setText(rs.getString("telefon"));
                gorevField.setText(rs.getString("gorev"));
                adresField.setText(rs.getString("adres"));
                maasField.setText(String.valueOf(rs.getDouble("maas")));
                
                // Tarih formatını düzelt
                SimpleDateFormat dbFormat = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    Date dogumTarihi = dbFormat.parse(rs.getString("dogum_tarih"));
                    dogumTarihiChooser.setDate(dogumTarihi);
                    
                    Date girisTarihi = dbFormat.parse(rs.getString("giris_tarihi"));
                    girisTarihiChooser.setDate(girisTarihi);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Tarih dönüştürme hatası: " + e.getMessage());
                }

                // Fotoğrafı göster
                String fotoPath = rs.getString("foto_path");
                if (fotoPath != null) {
                    try {
                        File fotoFile = new File(fotoPath);
                        if (fotoFile.exists()) {
                            BufferedImage originalImage = ImageIO.read(fotoFile);
                            if (originalImage != null) {
                                Image scaledImage = originalImage.getScaledInstance(150, 150, Image.SCALE_SMOOTH);
                                fotoLabel.setIcon(new ImageIcon(scaledImage));
                            }
                        }
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(this, "Fotoğraf yüklenirken hata oluştu: " + e.getMessage());
                    }
                } else {
                    fotoLabel.setIcon(null);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Eczacı bulunamadı!");
                temizle();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Veritabanı hatası: " + e.getMessage());
        }
    }

    // Eczacı fotoğrafını yükle
    public void eczaciFotografiniYukle(String tcNo) {
        try {
            String sql = "SELECT foto_path FROM eczacilar WHERE tc_no = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, tcNo);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String fotoPath = rs.getString("foto_path");
                if (fotoPath != null) {
                    try {
                        File fotoFile = new File(fotoPath);
                        if (fotoFile.exists()) {
                            BufferedImage originalImage = ImageIO.read(fotoFile);
                            if (originalImage != null) {
                                Image scaledImage = originalImage.getScaledInstance(150, 150, Image.SCALE_SMOOTH);
                                fotoLabel.setIcon(new ImageIcon(scaledImage));
                            }
                        }
                    } catch (Exception e) {
                        System.out.println("Fotoğraf yüklenirken hata oluştu: " + e.getMessage());
                    }
                } else {
                    fotoLabel.setIcon(null);
                }
            }
        } catch (SQLException e) {
            System.out.println("Veritabanı hatası: " + e.getMessage());
        }
    }
}