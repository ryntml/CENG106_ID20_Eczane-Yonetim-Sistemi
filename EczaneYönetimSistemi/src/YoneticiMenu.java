import javax.swing.*;
import java.awt.*;

public class YoneticiMenu extends JFrame {
    private JButton ilacYonetimiBtn;
    private JButton eczaciYonetimiBtn;
    private JButton musteriYonetimiBtn;
    private JButton satisYonetimiBtn;
    private JButton cikisBtn;
    private CardLayout cardLayout;

    public YoneticiMenu() {
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Yönetici Paneli");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(new Color(240, 248, 255));

        // Ana panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(240, 248, 255));

        // Sol panel (menü butonları)
        JPanel leftPanel = new JPanel();
        leftPanel.setPreferredSize(new Dimension(200, 800));
        leftPanel.setBackground(new Color(70, 130, 180));
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));

        // Butonlar
        ilacYonetimiBtn = createMenuButton("İlaç Yönetimi");
        eczaciYonetimiBtn = createMenuButton("Eczacı Yönetimi");
        musteriYonetimiBtn = createMenuButton("Müşteri Yönetimi");
        satisYonetimiBtn = createMenuButton("Satış Yönetimi");
        cikisBtn = createMenuButton("Çıkış");

        // Butonları panele ekle
        leftPanel.add(Box.createVerticalStrut(50));
        leftPanel.add(ilacYonetimiBtn);
        leftPanel.add(Box.createVerticalStrut(20));
        leftPanel.add(eczaciYonetimiBtn);
        leftPanel.add(Box.createVerticalStrut(20));
        leftPanel.add(musteriYonetimiBtn);
        leftPanel.add(Box.createVerticalStrut(20));
        leftPanel.add(satisYonetimiBtn);
        leftPanel.add(Box.createVerticalStrut(20));
        leftPanel.add(cikisBtn);

        // Sağ panel (içerik alanı)
        JPanel rightPanel = new JPanel();
        cardLayout = new CardLayout();
        rightPanel.setLayout(cardLayout);
        rightPanel.setBackground(Color.WHITE);

        // Panelleri oluştur
        IlacYonetim ilacYonetimPanel = new IlacYonetim();
        EczaciYonetim eczaciYonetimPanel = new EczaciYonetim();
        MusteriYonetim musteriYonetimPanel = new MusteriYonetim();
        SatisYonetim satisYonetimPanel = new SatisYonetim();
        rightPanel.add(ilacYonetimPanel, "ILAC_YONETIMI");
        rightPanel.add(eczaciYonetimPanel, "ECZACI_YONETIMI");
        rightPanel.add(musteriYonetimPanel, "MUSTERI_YONETIMI");
        rightPanel.add(satisYonetimPanel, "SATIS_YONETIMI");

        // Panelleri ana panele ekle
        mainPanel.add(leftPanel, BorderLayout.WEST);
        mainPanel.add(rightPanel, BorderLayout.CENTER);

        // Action listener'ları ekle
        ilacYonetimiBtn.addActionListener(e -> cardLayout.show(rightPanel, "ILAC_YONETIMI"));
        eczaciYonetimiBtn.addActionListener(e -> cardLayout.show(rightPanel, "ECZACI_YONETIMI"));
        musteriYonetimiBtn.addActionListener(e -> cardLayout.show(rightPanel, "MUSTERI_YONETIMI"));
        satisYonetimiBtn.addActionListener(e -> cardLayout.show(rightPanel, "SATIS_YONETIMI"));
        cikisBtn.addActionListener(e -> {
            dispose();
            new SecimFormu().setVisible(true);
        });

        add(mainPanel);
    }

    private JButton createMenuButton(String text) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(180, 40));
        button.setBackground(new Color(240, 248, 255));
        button.setForeground(new Color(70, 130, 180));
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        return button;
    }
} 