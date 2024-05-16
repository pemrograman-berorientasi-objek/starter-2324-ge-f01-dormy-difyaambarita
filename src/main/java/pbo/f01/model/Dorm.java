package pbo.f01.model;
/*
 * 12S22046 - Difya Laurensya Ambarita
 * 12S22052 - Rosari Simanjuntak
 */
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

