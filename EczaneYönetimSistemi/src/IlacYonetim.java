import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class IlacYonetim extends JPanel {
    private JTextField ilacAdiField;
    private JTextField barkodField;
    private JTextField atcKodField;
    private JTextField firmaField;
    private JTextField satisFiyatiField;
    private JTextField miktarField;
    private JComboBox<String> kategoriCombo;
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

        // Sol panel - Form alanları
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(new Color(173, 216, 230));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Form alanları
        ilacAdiField = createTextField("İlaç Adı");
        barkodField = createTextField("Barkod No");
        atcKodField = createTextField("ATC Kodu");
        firmaField = createTextField("Firma Adı");
        satisFiyatiField = createTextField("Satış Fiyatı");
        miktarField = createTextField("Miktar");
        
        kategoriCombo = new JComboBox<>(KATEGORILER);
        kategoriCombo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        // Butonlar
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBackground(new Color(173, 216, 230));
        
        JButton kaydetBtn = createButton("Kaydet", this::kaydetAction);
        JButton silBtn = createButton("Sil", this::silAction);
        JButton fiyatGuncelleBtn = createButton("Fiyat Güncelle", this::fiyatGuncelleAction);
        JButton araBtn = createButton("Ara", this::araAction);

        buttonPanel.add(kaydetBtn);
        buttonPanel.add(silBtn);
        buttonPanel.add(fiyatGuncelleBtn);
        buttonPanel.add(araBtn);

        // Form alanlarını panele ekle
        formPanel.add(ilacAdiField);
        formPanel.add(Box.createVerticalStrut(5));
        formPanel.add(barkodField);
        formPanel.add(Box.createVerticalStrut(5));
        formPanel.add(atcKodField);
        formPanel.add(Box.createVerticalStrut(5));
        formPanel.add(firmaField);
        formPanel.add(Box.createVerticalStrut(5));
        formPanel.add(satisFiyatiField);
        formPanel.add(Box.createVerticalStrut(5));
        formPanel.add(miktarField);
        formPanel.add(Box.createVerticalStrut(5));
        formPanel.add(kategoriCombo);
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(buttonPanel);

        // Sağ panel - Tablo
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        
        String[] columnNames = {"İlaç Adı", "Barkod No", "ATC Kodu", "Firma", "Satış Fiyatı", "Kategori", "Miktar"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        ilaclarTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(ilaclarTable);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        // Ana panele ekle
        add(formPanel, BorderLayout.WEST);
        add(tablePanel, BorderLayout.CENTER);
    }

    private JTextField createTextField(String label) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(173, 216, 230));
        
        JLabel jLabel = new JLabel(label);
        JTextField textField = new JTextField();
        textField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        
        panel.add(jLabel, BorderLayout.NORTH);
        panel.add(textField, BorderLayout.CENTER);
        
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
                String sql = "INSERT INTO ilaclar (ilacad, barkodno, atcKod, firma, satisfiyati, kategori, miktari) VALUES (?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement stmt = conn.prepareStatement(sql);
                
                stmt.setString(1, ilacAdiField.getText());
                stmt.setString(2, barkodField.getText());
                stmt.setString(3, atcKodField.getText());
                stmt.setString(4, firmaField.getText());
                stmt.setDouble(5, Double.parseDouble(satisFiyatiField.getText()));
                stmt.setString(6, (String) kategoriCombo.getSelectedItem());
                stmt.setInt(7, Integer.parseInt(miktarField.getText()));
                
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
        if (validateFields()) {
            try (Connection conn = DatabaseConnection.getConnection()) {
                String sql = "DELETE FROM ilaclar WHERE barkodno = ? AND atcKod = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                
                stmt.setString(1, barkodField.getText());
                stmt.setString(2, atcKodField.getText());
                
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
        }
    }

    private void fiyatGuncelleAction() {
        if (!barkodField.getText().isEmpty() && !satisFiyatiField.getText().isEmpty()) {
            try (Connection conn = DatabaseConnection.getConnection()) {
                String sql = "UPDATE ilaclar SET satisfiyati = ? WHERE barkodno = ?";
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
            JOptionPane.showMessageDialog(this, "Lütfen barkod numarası ve yeni fiyatı giriniz!");
        }
    }

    private void araAction() {
        if (!barkodField.getText().isEmpty()) {
            try (Connection conn = DatabaseConnection.getConnection()) {
                String sql = "SELECT * FROM ilaclar WHERE barkodno = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, barkodField.getText());
                
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    ilacAdiField.setText(rs.getString("ilacad"));
                    atcKodField.setText(rs.getString("atcKod"));
                    firmaField.setText(rs.getString("firma"));
                    satisFiyatiField.setText(String.valueOf(rs.getDouble("satisfiyati")));
                    miktarField.setText(String.valueOf(rs.getInt("miktari")));
                    kategoriCombo.setSelectedItem(rs.getString("kategori"));
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

    private void loadIlaclar() {
        tableModel.setRowCount(0);
        try (Connection conn = DatabaseConnection.getConnection()) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM ilaclar");
            
            while (rs.next()) {
                Object[] row = {
                    rs.getString("ilacad"),
                    rs.getString("barkodno"),
                    rs.getString("atcKod"),
                    rs.getString("firma"),
                    rs.getDouble("satisfiyati"),
                    rs.getString("kategori"),
                    rs.getInt("miktari")
                };
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Hata: " + e.getMessage());
        }
    }

    private boolean validateFields() {
        if (ilacAdiField.getText().isEmpty() || barkodField.getText().isEmpty() ||
            atcKodField.getText().isEmpty() || firmaField.getText().isEmpty() ||
            satisFiyatiField.getText().isEmpty() || miktarField.getText().isEmpty() ||
            kategoriCombo.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Lütfen tüm alanları doldurunuz!");
            return false;
        }
        return true;
    }

    private void clearFields() {
        ilacAdiField.setText("");
        barkodField.setText("");
        atcKodField.setText("");
        firmaField.setText("");
        satisFiyatiField.setText("");
        miktarField.setText("");
        kategoriCombo.setSelectedIndex(0);
    }
} 