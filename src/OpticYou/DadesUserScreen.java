package OpticYou;
import javax.swing.*;

public class DadesUserScreen {
    private static final String APP_NAME = "OpticYou";
    public DadesUserScreen() {
        JFrame frame = new JFrame(APP_NAME + " Modificar dades usuari");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.add(new JLabel("Aquí pots modificar les teves dades", SwingConstants.CENTER));
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
