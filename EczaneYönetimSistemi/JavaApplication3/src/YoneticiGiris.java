import javax.swing.*;
import java.awt.*;

public class YoneticiGiris extends JFrame {
    private JTextField kullaniciAdiField;
    private JPasswordField sifreField;
    private JButton girisBtn;
    private JButton geriBtn;
    private YoneticiService yoneticiService = new YoneticiService();

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
        panel.setLayout(null);
        panel.setBackground(new Color(173, 216, 230));

        // Kullanıcı Adı
        JLabel kullaniciAdiLabel = new JLabel("Kullanıcı Adı:");
        kullaniciAdiLabel.setBounds(50, 50, 100, 25);
        kullaniciAdiField = new JTextField();
        kullaniciAdiField.setBounds(150, 50, 200, 25);

        // Şifre
        JLabel sifreLabel = new JLabel("Şifre:");
        sifreLabel.setBounds(50, 100, 100, 25);
        sifreField = new JPasswordField();
        sifreField.setBounds(150, 100, 200, 25);

        // Giriş Butonu
        girisBtn = new JButton("GİRİŞ");
        girisBtn.setBounds(150, 150, 100, 30);
        girisBtn.addActionListener(e -> {
            String kullaniciAdi = kullaniciAdiField.getText();
            String sifre = new String(sifreField.getPassword());
            if (yoneticiService.girisYap(kullaniciAdi, sifre)) {
                JOptionPane.showMessageDialog(this, "Giriş başarılı!");
                YoneticiMenu yoneticiMenu = new YoneticiMenu();
                yoneticiMenu.setVisible(true);
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Hatalı kullanıcı adı veya şifre!");
            }
        });

        // Geri Butonu
        geriBtn = new JButton("GERİ");
        geriBtn.setBounds(150, 200, 100, 30);
        geriBtn.addActionListener(e -> {
            SecimFormu secimFormu = new SecimFormu();
            secimFormu.setVisible(true);
            this.dispose();
        });

        panel.add(kullaniciAdiLabel);
        panel.add(kullaniciAdiField);
        panel.add(sifreLabel);
        panel.add(sifreField);
        panel.add(girisBtn);
        panel.add(geriBtn);

        add(panel);
    }
} 
