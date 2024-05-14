package app;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import javax.swing.JFrame;

public abstract class AppKeyListener {
    private static int keyCode;

    public static String getKeyPressed() {
        keyCode = waitForKeyPress();

        return KeyEvent.getKeyText(keyCode).contains("NumPad-") ? KeyEvent.getKeyText(keyCode).replace("NumPad-", "") : KeyEvent.getKeyText(keyCode);
    }

    public static int waitForKeyPress() {
        final JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(240, 0);
        frame.setFocusable(true);
        frame.requestFocus();
        frame.setTitle("Navigator");
        frame.addWindowFocusListener(new WindowFocusListener() {
            @Override
            public void windowGainedFocus(WindowEvent e) {}

            @Override
            public void windowLostFocus(WindowEvent e) {
                synchronized (frame) {
                    frame.notify();
                }
                frame.dispose();
            }
        });

        frame.addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent e) {
                keyCode = e.getKeyCode();
                synchronized (frame) {
                    frame.notify();
                }
                frame.dispose();
            }

            @Override
            public void keyTyped(KeyEvent e) {}

            @Override
            public void keyReleased(KeyEvent e) {}
        });

        frame.setVisible(true);

        synchronized (frame) {
            try {
                frame.wait();
            } catch (InterruptedException ignored) {
            }
        }

        return keyCode;
    }
}
