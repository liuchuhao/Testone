package life.lch.testone;

import org.litepal.crud.DataSupport;

public class Chat extends DataSupport {
    private int id;
    private int type;
    private String message;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getString() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
