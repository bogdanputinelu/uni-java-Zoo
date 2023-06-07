package DBServices;

import DBConnection.DBConnection;
import Enclosure.ReptileEnclosure;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ReptileEnclosureService {
    private static ReptileEnclosureService instance = null;
    private static Connection con = DBConnection.getDbConnection();
    private ReptileEnclosureService(){}

    public static ReptileEnclosureService getInstance()
    {
        if(instance == null)
        {
            instance = new ReptileEnclosureService();
        }
        return instance;
    }

    public void allReptileEnc(){
        try {
            PreparedStatement pr = con.prepareStatement("select * from reptileenc");
            ResultSet rs = pr.executeQuery();
            while (rs.next()) {
                System.out.println("id: " + rs.getInt("id") + " " + rs.getString("name")
                        + " " + rs.getInt("surface") + " " + rs.getInt("maxCapacity")
                        + " " + rs.getInt("temperature"));
            }
        }
        catch (SQLException e){
            System.out.println("sql error");
        }
    }
    public void addReptileEnc(ReptileEnclosure be){
        try {
            PreparedStatement pr = con.prepareStatement("insert into reptileenc(id, surface, maxCapacity, name, temperature) values (?,?,?,?,?)");
            pr.setInt(1, be.getEnclosureId());
            pr.setInt(2, be.getSurface());
            pr.setInt(3, be.getMaxCapacity());
            pr.setString(4, be.getName());
            pr.setInt(5, be.getTemperature());
            pr.execute();
        }
        catch (SQLException e){
            System.out.println("sql error");
        }
    }
    public void deleteAllReptileEnc(){
        try{
            PreparedStatement pr = con.prepareStatement("delete from reptileenc where id > 0");
            pr.execute();
        }
        catch (SQLException e){
            System.out.println("sql error");
        }
    }
    public void deleteReptileEnc(int id){
        try{
            PreparedStatement pr = con.prepareStatement("delete from reptileenc where id = ?");
            pr.setInt(1, id);
            pr.execute();
        }
        catch (SQLException e){
            System.out.println("sql error");
        }
    }
    public void updateTemperature(int id, int temperature){
        try{
            PreparedStatement pr = con.prepareStatement("update reptileenc set temperature = ? where id = ?");
            pr.setInt(2, id);
            pr.setInt(1, temperature);
            pr.execute();
        }
        catch (SQLException e){
            System.out.println("sql error");
        }
    }
}
