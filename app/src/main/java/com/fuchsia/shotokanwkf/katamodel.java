package com.fuchsia.shotokanwkf;

public class katamodel {

    String Name,Pic,URL;
    katamodel()
    {

    }
    public katamodel(String Name, String Pic, String URL) {
        this.Name = Name;
        this.Pic= Pic;
        this.URL = URL;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getPic() {
        return Pic;
    }

    public void setPic(String Pic) {
        this.Pic = Pic;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }
}
