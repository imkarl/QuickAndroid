package cn.jeesoft.qa.sample.adapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据实体类
 */
public class Entrys {

    public static List<Entrys> SIMPLE_DATA = new ArrayList<Entrys>();
    static {
        for (int i=0; i<100; i++) {
            Entrys entrys = new Entrys(i, "name-"+i, "message");
            SIMPLE_DATA.add(entrys);
        }
    }




    private int id;
    private String name;
    private String message;


    public Entrys() {
        super();
    }
    public Entrys(int id, String name, String message) {
        super();
        this.id = id;
        this.name = name;
        this.message = message;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Entrys other = (Entrys) obj;
        if (id != other.id)
            return false;
        if (message == null) {
            if (other.message != null)
                return false;
        } else if (!message.equals(other.message))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }


    /*
     * getter\setter
     */
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
}
