package kw.artpuzzle.csv;

public class LevelBean {
    private int id;
    private String resourceName;
    private int type;
    private String hintline;
    private int direct;
    private int sort;
    private int input = 0;

    public void setInput(int input) {
        this.input = input;
    }

    public int getInput() {
        return input;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public int getSort() {
        return sort;
    }

    public int getDirect() {
        return direct;
    }

    public void setDirect(int direct) {
        this.direct = direct;
    }

    public String getHintline() {
        return hintline;
    }

    public void setHintline(String hintline) {
        this.hintline = hintline;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
