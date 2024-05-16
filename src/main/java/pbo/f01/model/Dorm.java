package pbo.f01.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Dorm {
    @Id
    private String dormname;
    private int capacity;
    private String gender;

    public String getname(){
        return dormname;
    }
    public  void setName(String dormname){
        this.dormname = dormname;
    }

    public int getCapacity(){
        return capacity;
    }
    public  void setCapacity(int capacity){
        this.capacity = capacity;
    }

    public String getgender(){
        return gender;
    }
    public  void setGender(String gender){
        this.gender = gender;
    }
 
 
}

