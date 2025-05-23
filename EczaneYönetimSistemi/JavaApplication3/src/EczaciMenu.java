import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EczaciMenu extends JFrame {
    private JPanel panel1;
    private JPanel panel2;
    private JLabel labelSaatTarih;
    private Timer timer;
    private CardLayout cardLayout;
    private Eczaci eczaci;

    public EczaciMenu(Eczaci eczaci) {
        this.eczaci = eczaci;
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Eczacı Paneli");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 850);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());

        // Sol Menü Paneli
        panel1 = new JPanel();
        panel1.setBackground(new Color(173, 216, 230));
        panel1.setPreferredSize(new Dimension(300, 800));
        panel1.setLayout(null);

        // ECZACI görseli
        JLabel iconLabel;
        if (eczaci != null && eczaci.getFotoPath() != null && !eczaci.getFotoPath().isEmpty()) {
            ImageIcon icon = new ImageIcon(eczaci.getFotoPath());
            Image scaledImage = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            iconLabel = new JLabel(new ImageIcon(scaledImage));
        } else {
            java.net.URL imgURL = getClass().getResource("/images/boss2.png");
            if (imgURL != null) {
                ImageIcon eczaciIcon = new ImageIcon(imgURL);
                Image scaledImage = eczaciIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                iconLabel = new JLabel(new ImageIcon(scaledImage));
            } else {
                iconLabel = new JLabel("Görsel Yok");
            }
        }
        iconLabel.setBounds(100, 30, 100, 100);
        panel1.add(iconLabel);

        // Kullanıcı adı
        if (eczaci != null) {
            JLabel userName = new JLabel(eczaci.getAd() + " " + eczaci.getSoyad());
            userName.setForeground(Color.WHITE);
            userName.setFont(new Font("Times New Roman", Font.BOLD, 18));
            userName.setBounds(75, 140, 150, 30);
            userName.setHorizontalAlignment(SwingConstants.CENTER);
            panel1.add(userName);
        }

        // ECZACI yazısı
        JLabel labelEczaci = new JLabel("ECZACI");
        labelEczaci.setForeground(Color.WHITE);
        labelEczaci.setFont(new Font("Times New Roman", Font.BOLD, 24));
        labelEczaci.setBounds(75, 170, 150, 30);
        labelEczaci.setHorizontalAlignment(SwingConstants.CENTER);
        panel1.add(labelEczaci);

        // Butonlar
        JButton btnStokTakip = createMenuButton("Stok Takibi", 220);
        JButton btnSatisYap = createMenuButton("Satış Yap", 270);
        JButton btnMusteri = createMenuButton("Müşteri", 320);
        JButton btnCikis = createMenuButton("Çıkış", 370);

        panel1.add(btnStokTakip);
        panel1.add(btnSatisYap);
        panel1.add(btnMusteri);
        panel1.add(btnCikis);

        // Saat/Tarih Label
        labelSaatTarih = new JLabel();
        labelSaatTarih.setForeground(Color.WHITE);
        labelSaatTarih.setFont(new Font("Times New Roman", Font.PLAIN, 14));
        labelSaatTarih.setBounds(75, 750, 200, 20);
        labelSaatTarih.setHorizontalAlignment(SwingConstants.CENTER);
        panel1.add(labelSaatTarih);

        // Sağ İçerik Paneli
        panel2 = new JPanel();
        panel2.setBackground(Color.WHITE);
        cardLayout = new CardLayout();
        panel2.setLayout(cardLayout);

        // Gerçek panelleri ekle
        JPanel stokPanel = new IlacYonetim();
        JPanel satisPanel = new SatisYonetim();
        JPanel musteriPanel = new MusteriYonetim();
        panel2.add(stokPanel, "STOK_TAKIBI");
        panel2.add(satisPanel, "SATIS_YAP");
        panel2.add(musteriPanel, "MUSTERI");

        btnStokTakip.addActionListener(e -> cardLayout.show(panel2, "STOK_TAKIBI"));
        btnSatisYap.addActionListener(e -> cardLayout.show(panel2, "SATIS_YAP"));
        btnMusteri.addActionListener(e -> cardLayout.show(panel2, "MUSTERI"));
        btnCikis.addActionListener(e -> {
            new EczaciGiris().setVisible(true);
            this.dispose();
        });

        add(panel1, BorderLayout.WEST);
        add(panel2, BorderLayout.CENTER);

        timer = new Timer(1000, e -> updateDateTime());
        timer.start();

        // İlk açılışta Stok Takibi panelini göster
        cardLayout.show(panel2, "STOK_TAKIBI");
    }

    private JButton createMenuButton(String text, int y) {
        JButton button = new JButton(text);
        button.setBounds(50, y, 200, 40);
        button.setBackground(Color.WHITE);
        button.setFont(new Font("Times New Roman", Font.PLAIN, 16));
        button.setFocusPainted(false);
        button.setHorizontalAlignment(SwingConstants.CENTER);
        return button;
    }

    private void updateDateTime() {
        labelSaatTarih.setText(new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date()));
    }

    // Test için ana metod
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new EczaciMenu(null).setVisible(true));
    }
}
