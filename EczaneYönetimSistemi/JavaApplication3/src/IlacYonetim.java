import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class IlacYonetim extends JPanel {
    private JTextField ilacAdiField;
    private JTextField barkodField;
    private JTextField firmaField;
    private JTextField satisFiyatiField;
    private JTextField miktarField;
    private JComboBox<String> kategoriCombo;
    private JTextField sonKullanmaTarihiField;
    private JTextField receteTipiField;
    private JTable ilaclarTable;
    private DefaultTableModel tableModel;

    private final String[] KATEGORILER = {
        "Antiviral", "Antibiyotik", "Analjezik", "Diüretik",
        "Antidepresan", "Antipsikotik", "Antienflamatuar",
        "Antikoagülan", "Antihistaminik"
    };

    public IlacYonetim() {
        initializeUI();
        loadIlaclar();
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

        String[] columnNames = {
            "Barkod", "Ad", "Üretici", "Kategori", "Fiyat",
            "Stok Miktarı", "Son Kullanma Tarihi", "Reçete Tipi"
        };

        tableModel = new DefaultTableModel(columnNames, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        ilaclarTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(ilaclarTable);
        topPanel.add(scrollPane, BorderLayout.CENTER);

        // Alt Panel - Form alanları
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(new Color(173, 216, 230));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Sol Panel - Form alanları
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(new Color(173, 216, 230));

        formPanel.add(new JLabel("Barkod No:"));
        barkodField = createTextField();
        formPanel.add(barkodField);

        formPanel.add(new JLabel("Ad:"));
        ilacAdiField = createTextField();
        formPanel.add(ilacAdiField);

        formPanel.add(new JLabel("Üretici:"));
        firmaField = createTextField();
        formPanel.add(firmaField);

        formPanel.add(new JLabel("Kategori:"));
        kategoriCombo = new JComboBox<>(KATEGORILER);
        kategoriCombo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        formPanel.add(kategoriCombo);

        formPanel.add(new JLabel("Fiyat:"));
        satisFiyatiField = createTextField();
        formPanel.add(satisFiyatiField);

        formPanel.add(new JLabel("Stok Miktarı:"));
        miktarField = createTextField();
        formPanel.add(miktarField);

        formPanel.add(new JLabel("Son Kullanma Tarihi (YYYY-MM-DD):"));
        sonKullanmaTarihiField = createTextField();
        formPanel.add(sonKullanmaTarihiField);

        formPanel.add(new JLabel("Reçete Tipi:"));
        receteTipiField = createTextField();
        formPanel.add(receteTipiField);

        formPanel.add(Box.createVerticalStrut(10));

        // Buton Paneli
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBackground(new Color(173, 216, 230));

        buttonPanel.add(createButton("Kaydet", this::kaydetAction));
        buttonPanel.add(createButton("Sil", this::silAction));
        buttonPanel.add(createButton("Fiyat Güncelle", this::fiyatGuncelleAction));
        buttonPanel.add(createButton("Ara", this::araAction));

        formPanel.add(buttonPanel);

        // Alt paneli düzenle
        bottomPanel.add(formPanel, BorderLayout.CENTER);

        // Ana panele ekle
        mainPanel.add(topPanel, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        add(mainPanel);

        // İlk yükleme
        loadIlaclar();
    }

    private JTextField createTextField() {
        JTextField textField = new JTextField();
        textField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        return textField;
    }

    private JButton createButton(String text, Runnable action) {
        JButton button = new JButton(text);
        button.setBackground(new Color(240, 255, 255));
        button.setFocusPainted(false);
        button.addActionListener(e -> action.run());
        return button;
    }

    private void kaydetAction() {
        if (validateFields()) {
            try (Connection conn = DatabaseConnection.getConnection()) {
                String sql = "INSERT INTO ilaclar (barkod, ad, uretici, kategori, fiyat, stok_miktari, son_kullanma_tarihi, reçete_tipi) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, barkodField.getText());
                stmt.setString(2, ilacAdiField.getText());
                stmt.setString(3, firmaField.getText());
                stmt.setString(4, (String) kategoriCombo.getSelectedItem());
                stmt.setDouble(5, Double.parseDouble(satisFiyatiField.getText()));
                stmt.setInt(6, Integer.parseInt(miktarField.getText()));
                stmt.setString(7, sonKullanmaTarihiField.getText());
                stmt.setString(8, receteTipiField.getText());
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "İlaç başarıyla eklendi!");
                clearFields();
                loadIlaclar();
            } catch (SQLException | NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Hata: " + e.getMessage());
            }
        }
    }

    private void silAction() {
        if (!barkodField.getText().isEmpty()) {
            try (Connection conn = DatabaseConnection.getConnection()) {
                String sql = "DELETE FROM ilaclar WHERE barkod = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, barkodField.getText());
                int affectedRows = stmt.executeUpdate();
                if (affectedRows > 0) {
                    JOptionPane.showMessageDialog(this, "İlaç başarıyla silindi!");
                    clearFields();
                    loadIlaclar();
                } else {
                    JOptionPane.showMessageDialog(this, "İlaç bulunamadı!");
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Hata: " + e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(this, "Lütfen barkod numarası giriniz!");
        }
    }

    private void fiyatGuncelleAction() {
        if (!barkodField.getText().isEmpty() && !satisFiyatiField.getText().isEmpty()) {
            try (Connection conn = DatabaseConnection.getConnection()) {
                String sql = "UPDATE ilaclar SET fiyat = ? WHERE barkod = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setDouble(1, Double.parseDouble(satisFiyatiField.getText()));
                stmt.setString(2, barkodField.getText());
                int affectedRows = stmt.executeUpdate();
                if (affectedRows > 0) {
                    JOptionPane.showMessageDialog(this, "Fiyat başarıyla güncellendi!");
                    loadIlaclar();
                } else {
                    JOptionPane.showMessageDialog(this, "İlaç bulunamadı!");
                }
            } catch (SQLException | NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Hata: " + e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(this, "Lütfen barkod ve yeni fiyatı giriniz!");
        }
    }

    private void araAction() {
        if (!barkodField.getText().isEmpty()) {
            try (Connection conn = DatabaseConnection.getConnection()) {
                String sql = "SELECT * FROM ilaclar WHERE barkod = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, barkodField.getText());
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    ilacAdiField.setText(rs.getString("ad"));
                    firmaField.setText(rs.getString("uretici"));
                    kategoriCombo.setSelectedItem(rs.getString("kategori"));
                    satisFiyatiField.setText(String.valueOf(rs.getDouble("fiyat")));
                    miktarField.setText(String.valueOf(rs.getInt("stok_miktari")));
                    sonKullanmaTarihiField.setText(rs.getString("son_kullanma_tarihi"));
                    receteTipiField.setText(rs.getString("reçete_tipi"));
                } else {
                    JOptionPane.showMessageDialog(this, "İlaç bulunamadı!");
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Hata: " + e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(this, "Lütfen barkod giriniz!");
        }
    }

    private void loadIlaclar() {
        tableModel.setRowCount(0);
        try (Connection conn = DatabaseConnection.getConnection()) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM ilaclar");
            while (rs.next()) {
                Object[] row = {
                    rs.getString("barkod"),
                    rs.getString("ad"),
                    rs.getString("uretici"),
                    rs.getString("kategori"),
                    rs.getDouble("fiyat"),
                    rs.getInt("stok_miktari"),
                    rs.getString("son_kullanma_tarihi"),
                    rs.getString("reçete_tipi")
                };
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Hata: " + e.getMessage());
        }
    }

    private void clearFields() {
        barkodField.setText("");
        ilacAdiField.setText("");
        firmaField.setText("");
        satisFiyatiField.setText("");
        miktarField.setText("");
        sonKullanmaTarihiField.setText("");
        receteTipiField.setText("");
        kategoriCombo.setSelectedIndex(0);
    }

    private boolean validateFields() {
        return !barkodField.getText().isEmpty() &&
               !ilacAdiField.getText().isEmpty() &&
               !firmaField.getText().isEmpty() &&
               !satisFiyatiField.getText().isEmpty() &&
               !miktarField.getText().isEmpty() &&
               !sonKullanmaTarihiField.getText().isEmpty() &&
               !receteTipiField.getText().isEmpty() &&
               kategoriCombo.getSelectedItem() != null;
    }
}
