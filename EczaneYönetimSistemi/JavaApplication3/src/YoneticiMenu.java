import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class YoneticiMenu extends JFrame {
    private JPanel panel1;
    private JPanel panel2;
    private CardLayout cardLayout;
    private JLabel labelSaatTarih;
    private Timer timer;

    public YoneticiMenu() {
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Yönetici Paneli");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 850);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());

        panel1 = new JPanel();
        panel1.setBackground(new Color(173, 216, 230));
        panel1.setPreferredSize(new Dimension(300, 800));
        panel1.setLayout(null);

        JLabel iconLabel;
        java.net.URL imgURL = getClass().getResource("/images/boss2.png");
        if (imgURL != null) {
            ImageIcon yoneticiIcon = new ImageIcon(imgURL);
            Image scaledImage = yoneticiIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            iconLabel = new JLabel(new ImageIcon(scaledImage));
        } else {
            iconLabel = new JLabel("Görsel Yok");
        }
        iconLabel.setBounds(100, 30, 100, 100);
        panel1.add(iconLabel);

        JLabel labelYonetici = new JLabel("YÖNETİCİ");
        labelYonetici.setForeground(Color.WHITE);
        labelYonetici.setFont(new Font("Times New Roman", Font.BOLD, 24));
        labelYonetici.setBounds(75, 140, 150, 30);
        labelYonetici.setHorizontalAlignment(SwingConstants.CENTER);
        panel1.add(labelYonetici);

        JButton ilacYonetimiBtn = createMenuButton("İlaç Yönetimi", 220);
        JButton eczaciYonetimiBtn = createMenuButton("Eczacı Yönetimi", 270);
        JButton musteriYonetimiBtn = createMenuButton("Müşteri Yönetimi", 320);
        JButton satisYonetimiBtn = createMenuButton("Satış Yönetimi", 370);
        JButton cikisBtn = createMenuButton("Çıkış", 420);

        panel1.add(ilacYonetimiBtn);
        panel1.add(eczaciYonetimiBtn);
        panel1.add(musteriYonetimiBtn);
        panel1.add(satisYonetimiBtn);
        panel1.add(cikisBtn);

        labelSaatTarih = new JLabel();
        labelSaatTarih.setForeground(Color.WHITE);
        labelSaatTarih.setFont(new Font("Times New Roman", Font.PLAIN, 14));
        labelSaatTarih.setBounds(75, 750, 200, 20);
        labelSaatTarih.setHorizontalAlignment(SwingConstants.CENTER);
        panel1.add(labelSaatTarih);

        panel2 = new JPanel();
        panel2.setBackground(Color.WHITE);
        cardLayout = new CardLayout();
        panel2.setLayout(cardLayout);

        panel2.add(new IlacYonetim(), "ILAC_YONETIMI");
        panel2.add(new EczaciYonetim(), "ECZACI_YONETIMI");
        panel2.add(new MusteriYonetim(), "MUSTERI_YONETIMI");
        panel2.add(new SatisYonetim(), "SATIS_YONETIMI");

        ilacYonetimiBtn.addActionListener(e -> cardLayout.show(panel2, "ILAC_YONETIMI"));
        eczaciYonetimiBtn.addActionListener(e -> cardLayout.show(panel2, "ECZACI_YONETIMI"));
        musteriYonetimiBtn.addActionListener(e -> cardLayout.show(panel2, "MUSTERI_YONETIMI"));
        satisYonetimiBtn.addActionListener(e -> cardLayout.show(panel2, "SATIS_YONETIMI"));
        cikisBtn.addActionListener(e -> {
            dispose();
            new SecimFormu().setVisible(true);
        });

        add(panel1, BorderLayout.WEST);
        add(panel2, BorderLayout.CENTER);

        timer = new Timer(1000, e -> updateDateTime());
        timer.start();
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new YoneticiMenu().setVisible(true));
    }
}
