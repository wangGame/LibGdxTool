package kw.artpuzzle.history.bean;

public class HistoryNum {
    private int  currentCount;
    private int allCount;

    public int getCurrentCount() {
        return currentCount;
    }

    public void setCurrentCount(int currentCount) {
        this.currentCount = currentCount;
    }

    public int getAllCount() {
        return allCount;
    }

    public void setAllCount(int allCount) {
        this.allCount = allCount;
    }

    public int getPerent(){
        if (allCount == 0){
            return Integer.MIN_VALUE;
        }
        float v = currentCount * 1.0f / allCount;
        int endV = (int) (v * 100);
        return endV;
    }
}
