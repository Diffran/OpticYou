package OpticYou;

import javax.swing.*;

class RecomenacionsScreen {
    private static final String APP_NAME = "OpticYou";
    public RecomenacionsScreen() {
        JFrame frame = new JFrame(APP_NAME + " Recomenacions");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.add(new JLabel("Aquí podràs veure les recomanacions", SwingConstants.CENTER));
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

