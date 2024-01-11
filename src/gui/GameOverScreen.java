package gui;
import java.awt.*;
import javax.swing.*;

public class GameOverScreen extends JFrame {
    private Font font;
    private Color textColor;
    private String message;
    private JLabel gifLabel;

    public GameOverScreen(String message) {
        this.message = message;
        this.font = new Font("Arial", Font.BOLD, 26);
        this.textColor = Color.RED;
        initializeUI();
    }

    private void initializeUI() {
        setTitle("");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 500);
        setLocationRelativeTo(null);

        gifLabel = new JLabel();
        ImageIcon gifIcon = new ImageIcon("resources/img/giphy.gif");
        gifLabel.setIcon(gifIcon);
        // icon GAME OVER

        // Thêm nhãn hiển thị thông báo
        JLabel messageLabel = new JLabel(message);
        messageLabel.setForeground(Color.RED);
        messageLabel.setFont(new Font("Arial", Font.BOLD, 80));
        messageLabel.setHorizontalAlignment(JLabel.CENTER);
        // "YOU LOSE"

        // Tạo panel chứa cả hình ảnh và nhãn thông báo
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.add(gifLabel, BorderLayout.CENTER);
        contentPanel.add(messageLabel, BorderLayout.SOUTH);



        add(contentPanel);

        setVisible(true);
    }

    private void render(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setFont(font);
        g2d.setColor(textColor);

        FontMetrics fontMetrics = g2d.getFontMetrics();
        int messageWidth = fontMetrics.stringWidth(message);
        int messageHeight = fontMetrics.getHeight();

        int x = (getWidth() - messageWidth) / 2;
        int y = (getHeight() - messageHeight) / 2;

        g2d.drawString(message, x, y);
        g2d.dispose();
    }
    public void disposeFrame(){
        dispose();
    } // dispose the frame
}