package OpticYou;

import javax.swing.*;

class CitesScreen {
    private static final String APP_NAME = "OpticYou";
    public CitesScreen() {
        JFrame frame = new JFrame(APP_NAME +" Gestión de Citas");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.add(new JLabel("Aquí pots gestionar les cites", SwingConstants.CENTER));
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

