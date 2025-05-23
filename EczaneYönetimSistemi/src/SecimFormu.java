import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SecimFormu extends JFrame {
    private JButton yoneticiBtn;
    private JButton eczaciBtn;

    public SecimFormu() {
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Eczane Yönetim Sistemi");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(new Color(173, 216, 230));

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 1, 10, 10));
        panel.setBackground(new Color(173, 216, 230));

        yoneticiBtn = new JButton("Yönetici Girişi");
        eczaciBtn = new JButton("Eczacı Girişi");

        yoneticiBtn.addActionListener(e -> {
            YoneticiGiris yoneticiGiris = new YoneticiGiris();
            yoneticiGiris.setVisible(true);
            dispose();
        });

        eczaciBtn.addActionListener(e -> {
            EczaciGiris eczaciGiris = new EczaciGiris();
            eczaciGiris.setVisible(true);
            dispose();
        });

        panel.add(yoneticiBtn);
        panel.add(eczaciBtn);

        add(panel);
    }
} 