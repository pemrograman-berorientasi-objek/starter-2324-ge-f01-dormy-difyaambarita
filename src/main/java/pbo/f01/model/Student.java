package pbo.f01.model;


import javax.persistence.*;


@Entity
@Table(name = "student")

public class Student {
 
    @Id
    private String id;
    private String name;
    private String gender;
    private int angkatan;

    public Student() {}

    public Student(String id, String name, String gender, int angkatan) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.angkatan = angkatan;
    }

    public String getId(){
        return id;
    }
    public  void setId(String id){
        this.id = id;
    }

    public String getname(){
        return id;
    }
    public  void setName(String name){
        this.name = name;
    }

    public String getgender(){
        return gender;
    }
    public  void setGender(String gender){
        this.gender = gender;
    }

    public int getAngkatan(){
        return angkatan;
    }
    public  void setangkatan(int angkatan){
        this.angkatan = angkatan;
    }

    public void setDorm(Dorm dorm) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setDorm'");
    }

}