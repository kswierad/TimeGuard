package pl.agh.cs.io.counter;

import javax.swing.JLabel;
import javax.swing.JWindow;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class TimeCounter {

    private double xOffset = 0, yOffset = 0;
    private JLabel label;
    JWindow jWindow;


    public void start() {
        label = new JLabel();
        label.setFont(new Font(label.getName(), Font.PLAIN, 20));
        jWindow = new JWindow();
        jWindow.add(label);
        jWindow.setVisible(true);
        jWindow.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                xOffset = e.getX();
                yOffset = e.getY();
            }
        });

        jWindow.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                jWindow.setLocation(e.getXOnScreen() - (int) xOffset, e.getYOnScreen() - (int) yOffset);
            }
        });
    }

    public void setAlwaysOnTop() {
        jWindow.setAlwaysOnTop(true);
    }

    public void setText(String text) {
        text = String.format("<html>%s</html>", text.replace("\n", "<br>"));
        label.setText(text);
        jWindow.pack();
    }

    public void setWindowPosition(int x, int y) {
        jWindow.setLocation(x, y - 50);
    }

}
