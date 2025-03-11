package OpticYou;

import javax.swing.*;
import java.awt.*;


class MainScreen {
    private static final String APP_NAME = "OpticYou";
    public MainScreen(String userType) {
        JFrame frame = new JFrame(APP_NAME + " Menú Principal"+ userType);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);
        frame.setLayout(new BorderLayout());

        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new GridLayout(4, 1, 10, 10));

        if (userType.equals("client")) {
            JButton dadesButton = new JButton("Consulta les teves dades");
            JButton gestióDeCitesButton = new JButton("Gestió de Cites");
            JButton recomanacionsButton = new JButton("Recomanacions");
            dadesButton.addActionListener(e -> new DadesUserScreen());
            recomanacionsButton.addActionListener(e -> new RecomenacionsScreen());
            gestióDeCitesButton.addActionListener(e -> new CitesScreen());
            menuPanel.add(gestióDeCitesButton);
            menuPanel.add(recomanacionsButton);
            menuPanel.add(dadesButton);

        } else if (userType.equals("negoci")) {
            JButton manageClientsButton = new JButton("Gestió de Clients");
            JButton adminPanelButton = new JButton("Panell d'Administració");
            manageClientsButton.addActionListener(e -> new ManageClientsScreen());
            adminPanelButton.addActionListener(e -> new AdminPanelScreen());
            menuPanel.add(manageClientsButton);
            menuPanel.add(adminPanelButton);
        }


        frame.add(menuPanel, BorderLayout.CENTER);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
