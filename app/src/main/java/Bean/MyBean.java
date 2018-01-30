package Bean;

/**
 * Created by Administrator on 2017/12/21 0021.
 */

public class MyBean {

    private int pictures;
    private String titles;

    public MyBean(int pictures, String titles) {
        this.pictures = pictures;
        this.titles = titles;
    }

    public int getPictures() {
        return pictures;
    }

    public void setPictures(int pictures) {
        this.pictures = pictures;
    }

    public String getTitles() {
        return titles;
    }

    public void setTitles(String titles) {
        this.titles = titles;
    }
}
