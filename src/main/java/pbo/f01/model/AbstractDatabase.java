package pbo.f01.model;

import java.sql.*; 

public abstract class AbstractDatabase { 
    protected String url = null; 
    protected Connection connect = null; 
    public AbstractDatabase(String url) throws SQLException{ 
        this.url = url; 
        this.prepareTables(); 
    }

    protected Connection getConnection() throws SQLException{ // Metode protected getConnection() untuk mendapatkan koneksi database
        if (this.connect == null) { 
            this.connect = DriverManager.getConnection(this.url); 
        }
        
        return this.connect; 
    }

    public void shutdown() throws SQLException { 
        if(this.connect != null){ 
            this.connect.close(); 
        }
    }

    protected void prepareTables() throws SQLException { 
        this.createTables(); 
        this.seedTables();
    }

    protected void createTables() throws SQLException { 
    }

    protected void seedTables() throws SQLException{ 
    }

}
