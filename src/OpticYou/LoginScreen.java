package OpticYou;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginScreen {
    private static final String APP_NAME = "OpticYou";

    public static void main(String[] args) {

        SwingUtilities.invokeLater(LoginScreen::new);
    }

    public LoginScreen() {


        JFrame frame = new JFrame(APP_NAME + " - Login");
        //Panel superior
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());
        JLabel logoLabel = new JLabel(new ImageIcon(getClass().getResource("/recursos/logo.jpg")));
        topPanel.add(logoLabel, BorderLayout.CENTER);


        //Part login
        frame.setLayout(new BorderLayout());
        frame.add(topPanel, BorderLayout.NORTH);

        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new GridLayout(3, 2,10,10));




        JLabel userLabel = new JLabel("USUARI:");
        JTextField userText = new JTextField();
        JLabel passLabel = new JLabel("CONTRASENYA:");
        JPasswordField passText = new JPasswordField();
        JButton loginButton = new JButton("Iniciar sessió");


        loginPanel.add(userLabel);
        loginPanel.add(userText);
        loginPanel.add(passLabel);
        loginPanel.add(passText);
        loginPanel.add(new JLabel()); //espai buit
        loginPanel.add(loginButton);

        //afegir panel login al mig del JFrame
        frame.add(loginPanel, BorderLayout.CENTER);

        //configuració JFrame
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 500);
        frame.setLocationRelativeTo(null);  // Centrar a la finestra
        frame.setVisible(true);


        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String user = userText.getText();
                String pass = new String(passText.getPassword());

                if (user.equals("admin") && pass.equals("1234")) {
                    frame.dispose();
                    new MainScreen("negoci");
                } else if (user.equals("client") && pass.equals("1234")) {
                    frame.dispose();
                    new MainScreen("client");
                } else {
                    JOptionPane.showMessageDialog(frame, "Usuari o contrasenya incorrectes", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}






