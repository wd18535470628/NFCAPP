package Bean;

import org.litepal.crud.DataSupport;

/**
 * Created by Administrator on 2018/1/18 0018.
 */

public class DescPoint extends DataSupport{

    private int id;
    private String descpoint;
    private String number;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescpoint() {
        return descpoint;
    }

    public void setDescpoint(String descpoint) {
        this.descpoint = descpoint;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
