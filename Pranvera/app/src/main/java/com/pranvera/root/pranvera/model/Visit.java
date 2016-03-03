package com.pranvera.root.pranvera.model;

/**
 * Created by root on 11/01/15.
 */
public class Visit {

    private String content;
    private String image;

    //private Date data;

    public void setContent(String content){
        this.content=content;
    }

   // public void setData(Date data){
     //   this.data=data;
   // }

    public String getContent(){
        return this.content;
    }

   // public Date getData(){
     //   return this.data;
   // }


    public void setImage(String image)
    {
        this.image=image;
    }
    public String getImageVisit(){
        return this.image;
    }
}
