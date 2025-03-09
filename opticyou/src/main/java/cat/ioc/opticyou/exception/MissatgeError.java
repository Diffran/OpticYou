package cat.ioc.opticyou.exception;

import java.util.Date;

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
