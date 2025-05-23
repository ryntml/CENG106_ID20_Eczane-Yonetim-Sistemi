import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class JDateChooser extends JPanel {
    private JTextField dateField;
    private JButton chooseButton;
    private Date selectedDate;
    private String dateFormatString = "dd/MM/yyyy";
    private SimpleDateFormat dateFormat;

    public JDateChooser() {
        setLayout(new BorderLayout());
        dateFormat = new SimpleDateFormat(dateFormatString);

        dateField = new JTextField(10);
        dateField.setEditable(false);

        chooseButton = new JButton("...");
        chooseButton.addActionListener(e -> showDateDialog());

        add(dateField, BorderLayout.CENTER);
        add(chooseButton, BorderLayout.EAST);
    }

    private void showDateDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Tarih Seç", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(300, 200);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new BorderLayout());
        JSpinner spinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor editor = new JSpinner.DateEditor(spinner, dateFormatString);
        spinner.setEditor(editor);
        panel.add(spinner, BorderLayout.CENTER);

        JButton okButton = new JButton("Tamam");
        okButton.addActionListener(e -> {
            selectedDate = (Date) spinner.getValue();
            dateField.setText(dateFormat.format(selectedDate));
            dialog.dispose();
        });

        JButton cancelButton = new JButton("İptal");
        cancelButton.addActionListener(e -> dialog.dispose());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);

        dialog.add(panel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    public void setDateFormatString(String format) {
        this.dateFormatString = format;
        this.dateFormat = new SimpleDateFormat(format);
        if (selectedDate != null) {
            dateField.setText(dateFormat.format(selectedDate));
        }
    }

    public Date getDate() {
        return selectedDate;
    }

    public void setDate(Date date) {
        this.selectedDate = date;
        if (date != null) {
            dateField.setText(dateFormat.format(date));
        } else {
            dateField.setText("");
        }
    }
}