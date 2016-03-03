package com.pranvera.root.pranvera.model;
/**
 * Created by root on 06/01/15.
 */
public class Patient {

    private String name;
    private String image;
    private String tel;





    public String getName() {
        return this.name;
    }

    public void setName(String name){
        this.name=name;
    }
    public void setImage(String image)
    {
        this.image=image;
    }
    public String getImagePatient(){
        return this.image;
    }

    public void setTel(String tel){
        this.tel=tel;
    }
    public String getTel() {
        return this.tel;
    }


}
