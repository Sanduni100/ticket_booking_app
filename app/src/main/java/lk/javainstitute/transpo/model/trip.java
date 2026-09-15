package lk.javainstitute.transpo.model;

public class trip {
    String rTime;
    String tTime;
    String pick;
    String drop;
    String price;
    String date;

    public trip() {
    }

    public trip(String rTime, String tTime, String pick, String drop, String price, String date) {
        this.rTime = rTime;
        this.tTime = tTime;
        this.pick = pick;
        this.drop = drop;
        this.price = price;
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getrTime() {
        return rTime;
    }

    public void setrTime(String rTime) {
        this.rTime = rTime;
    }

    public String gettTime() {
        return tTime;
    }

    public void settTime(String tTime) {
        this.tTime = tTime;
    }

    public String getPick() {
        return pick;
    }

    public void setPick(String pick) {
        this.pick = pick;
    }

    public String getDrop() {
        return drop;
    }

    public void setDrop(String drop) {
        this.drop = drop;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
