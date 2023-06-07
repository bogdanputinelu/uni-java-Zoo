package DBServices;

import DBConnection.DBConnection;
import Enclosure.MammalEnclosure;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MammalEnclosureService {
    private static MammalEnclosureService instance = null;
    private static Connection con = DBConnection.getDbConnection();
    private MammalEnclosureService(){}

    public static MammalEnclosureService getInstance()
    {
        if(instance == null)
        {
            instance = new MammalEnclosureService();
        }
        return instance;
    }

    public void allMammalEnc(){
        try {
            PreparedStatement pr = con.prepareStatement("select * from mammalenc");
            ResultSet rs = pr.executeQuery();
            while (rs.next()) {
                System.out.println("id: " + rs.getInt("id") + " " + rs.getString("name")
                        + " " + rs.getInt("surface") + " " + rs.getInt("maxCapacity")
                        + " " + rs.getInt("shadedSurface"));
            }
        }
        catch (SQLException e){
            System.out.println("sql error");
        }
    }
    public void addMammalEnc(MammalEnclosure be){
        try {
            PreparedStatement pr = con.prepareStatement("insert into mammalenc(id, surface, maxCapacity, name, shadedSurface) values (?,?,?,?,?)");
            pr.setInt(1, be.getEnclosureId());
            pr.setInt(2, be.getSurface());
            pr.setInt(3, be.getMaxCapacity());
            pr.setString(4, be.getName());
            pr.setInt(5, be.getShadedSurface());
            pr.execute();
        }
        catch (SQLException e){
            System.out.println("sql error");
        }
    }
    public void deleteAllMammalEnc(){
        try{
            PreparedStatement pr = con.prepareStatement("delete from mammalenc where id > 0");
            pr.execute();
        }
        catch (SQLException e){
            System.out.println("sql error");
        }
    }
    public void deleteMammalEnc(int id){
        try{
            PreparedStatement pr = con.prepareStatement("delete from mammalenc where id = ?");
            pr.setInt(1, id);
            pr.execute();
        }
        catch (SQLException e){
            System.out.println("sql error");
        }
    }
    public void updateShadedSurface(int id, int shadedSurface){
        try{
            PreparedStatement pr = con.prepareStatement("update mammalenc set shadedSurface = ? where id = ?");
            pr.setInt(2, id);
            pr.setInt(1, shadedSurface);
            pr.execute();
        }
        catch (SQLException e){
            System.out.println("sql error");
        }
    }
}
