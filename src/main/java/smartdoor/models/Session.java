package smartdoor.models;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class Session {

    private int id;
    private String date_in;
    private String filename;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate_in() {
        return date_in;
    }

    public void setDate_in(String date_in) {
        this.date_in = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename() {
        this.filename = UUID.randomUUID().toString() + ".jpg";
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    @Override
    public String toString() {
        return "Session{" +
                "id=" + id +
                ", date_in='" + date_in + '\'' +
                ", file='" + filename + '\'' +
                '}';
    }
}
