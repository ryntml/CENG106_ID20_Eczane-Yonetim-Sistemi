import javax.swing.*;
import java.awt.*;

public class SecimFormu extends JFrame {
    private JButton yoneticiGirisBtn;
    private JButton calisanGirisBtn;
    private JButton cikisBtn;

    public SecimFormu() {
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Eczane Yönetim Sistemi");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(448, 631);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(new Color(173, 216, 230)); // LightBlue

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(173, 216, 230));

        yoneticiGirisBtn = new JButton("YÖNETİCİ GİRİŞİ");
        yoneticiGirisBtn.setBounds(121, 214, 173, 78);
        yoneticiGirisBtn.addActionListener(e -> {
            YoneticiGiris yg = new YoneticiGiris();
            yg.setVisible(true);
            this.setVisible(false);
        });

        calisanGirisBtn = new JButton("ÇALIŞAN GİRİŞİ");
        calisanGirisBtn.setBounds(121, 317, 173, 78);
        calisanGirisBtn.addActionListener(e -> {
            EczaciGiris eg = new EczaciGiris();
            eg.setVisible(true);
            this.setVisible(false);
        });

        cikisBtn = new JButton("ÇIKIŞ");
        cikisBtn.setBounds(145, 539, 126, 33);
        cikisBtn.addActionListener(e -> System.exit(0));

        panel.add(yoneticiGirisBtn);
        panel.add(calisanGirisBtn);
        panel.add(cikisBtn);

        add(panel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SplashScreen splash = new SplashScreen();
            splash.showSplash();
        });
    }
} 