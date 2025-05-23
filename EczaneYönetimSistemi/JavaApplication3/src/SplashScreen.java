import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SplashScreen extends JWindow {
    private JLabel logoLabel;
    private Timer timer;
    private float opacity = 0.0f;
    private int counter = 0;
    private static final int TOTAL_DURATION = 5000; // 5 saniye
    private static final int TIMER_DELAY = 50; // 50ms
    private static final int STEPS = TOTAL_DURATION / TIMER_DELAY;

    public SplashScreen() {
        // Pencere boyutunu ayarla
        setSize(300, 300);
        setLocationRelativeTo(null);

        // Logo için JLabel oluştur
        logoLabel = new JLabel();
        logoLabel.setHorizontalAlignment(JLabel.CENTER);
        logoLabel.setVerticalAlignment(JLabel.CENTER);
        logoLabel.setPreferredSize(new Dimension(300, 300));
        logoLabel.setOpaque(true);
        logoLabel.setBackground(new Color(173, 216, 230));
        
        // GIF animasyonunu yükle
        try {
            java.net.URL imageUrl = getClass().getResource("/resources/logo.gif");
            if (imageUrl == null) {
                System.out.println("Logo dosyası bulunamadı!");
            } else {
                System.out.println("Logo dosyası bulundu: " + imageUrl.getPath());
            }
            ImageIcon logoIcon = new ImageIcon(imageUrl);
            System.out.println("Logo boyutu: " + logoIcon.getIconWidth() + "x" + logoIcon.getIconHeight());
            logoLabel.setIcon(logoIcon); // Bu şekilde animasyon da çalışır, görünür olur

            System.out.println("Logo yüklendi ve JLabel'e eklendi");
        } catch (Exception e) {
            System.out.println("Hata oluştu: " + e.getMessage());
            e.printStackTrace();
        }

        // Pencere içeriğini ayarla
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());  // GridBagLayout kullanıyoruz
        panel.setBackground(new Color(173, 216, 230));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.CENTER;
        
        panel.add(logoLabel, gbc);
        add(panel);

        // Animasyon için timer oluştur
        timer = new Timer(TIMER_DELAY, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (counter < STEPS / 3) {
                    // Fade-in animasyonu (ilk 1.67 saniye)
                    opacity = Math.min(1.0f, opacity + (1.0f / (STEPS / 3)));
                    setOpacity(opacity);
                } else if (counter < (STEPS * 2) / 3) {
                    // Logo görünür kalır (1.67 saniye)
                } else if (counter < STEPS) {
                    // Fade-out animasyonu (son 1.67 saniye)
                    opacity = Math.max(0.0f, opacity - (1.0f / (STEPS / 3)));
                    setOpacity(opacity);
                } else {
                    // Animasyon bittiğinde timer'ı durdur ve SecimFormu'na geç
                    timer.stop();
                    dispose();
                    SwingUtilities.invokeLater(() -> {
                        SecimFormu form = new SecimFormu();
                        form.setVisible(true);
                    });
                }
                counter++;
            }
        });
    }

    public void showSplash() {
        setVisible(true);
        timer.start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SplashScreen splash = new SplashScreen();
            splash.showSplash();
        });
    }
} 