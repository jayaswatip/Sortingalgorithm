import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        // Set modern look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("🎯 Algorithm Visualizer & Analyzer");
            frame.setSize(1200, 700);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLocationRelativeTo(null); // Center on screen
            
            // Set custom icon and colors
            frame.getContentPane().setBackground(new Color(240, 242, 245));
            
            VisualPanel panel = new VisualPanel();
            frame.add(panel);
            
            frame.setVisible(true);
        });
    }
}