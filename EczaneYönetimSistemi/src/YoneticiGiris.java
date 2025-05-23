import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class YoneticiGiris extends JFrame {
    private JTextField kullaniciAdiField;
    private JPasswordField sifreField;
    private JButton girisBtn;
    private JButton geriBtn;

    public YoneticiGiris() {
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Yönetici Girişi");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(new Color(173, 216, 230));

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(173, 216, 230));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        kullaniciAdiField = new JTextField();
        sifreField = new JPasswordField();
        girisBtn = new JButton("Giriş Yap");
        geriBtn = new JButton("Geri");

        JPanel kullaniciAdiPanel = createFieldPanel("Kullanıcı Adı:", kullaniciAdiField);
        JPanel sifrePanel = createFieldPanel("Şifre:", sifreField);

        panel.add(kullaniciAdiPanel);
        panel.add(Box.createVerticalStrut(10));
        panel.add(sifrePanel);
        panel.add(Box.createVerticalStrut(20));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(new Color(173, 216, 230));
        buttonPanel.add(girisBtn);
        buttonPanel.add(geriBtn);

        panel.add(buttonPanel);

        girisBtn.addActionListener(e -> girisYap());
        geriBtn.addActionListener(e -> {
            new SecimFormu().setVisible(true);
            dispose();
        });

        add(panel);
    }

    private JPanel createFieldPanel(String label, JComponent field) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(173, 216, 230));
        
        JLabel jLabel = new JLabel(label);
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        
        panel.add(jLabel, BorderLayout.NORTH);
        panel.add(field, BorderLayout.CENTER);
        
        return panel;
    }

    private void girisYap() {
        String kullaniciAdi = kullaniciAdiField.getText();
        String sifre = new String(sifreField.getPassword());

        if (kullaniciAdi.isEmpty() || sifre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Lütfen tüm alanları doldurunuz!");
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT * FROM yoneticiler WHERE kullanici_adi = ? AND sifre = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, kullaniciAdi);
            stmt.setString(2, sifre);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                JOptionPane.showMessageDialog(this, "Giriş başarılı!");
                YoneticiMenu yoneticiMenu = new YoneticiMenu();
                yoneticiMenu.setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Kullanıcı adı veya şifre hatalı!");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Veritabanı hatası: " + e.getMessage());
        }
    }
} 