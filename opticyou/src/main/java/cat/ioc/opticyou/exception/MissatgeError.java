package cat.ioc.opticyou.exception;

import java.util.Date;

/**
 * Missatge d'error personalitzat.
 * Mostra l'estat, el temps, el missatge d'error i la descripció.
 */
public class MissatgeError {
    private int statusCode;
    private Date timestamp;
    private String missatge;
    private String descripcio;

    public MissatgeError(int statusCode, Date timestamp, String missatge, String descripcio) {
        this.statusCode = statusCode;
        this.timestamp = timestamp;
        this.missatge = missatge;
        this.descripcio = descripcio;
    }
}
