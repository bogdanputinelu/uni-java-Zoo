package DBServices;

import DBConnection.DBConnection;
import Enclosure.BirdEnclosure;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BirdEnclosureService {
    private static BirdEnclosureService instance = null;
    private static Connection con = DBConnection.getDbConnection();
    private BirdEnclosureService(){}

    public static BirdEnclosureService getInstance()
    {
        if(instance == null)
        {
            instance = new BirdEnclosureService();
        }
        return instance;
    }

    public void allBirdEnc(){
        try {
            PreparedStatement pr = con.prepareStatement("select * from birdenc");
            ResultSet rs = pr.executeQuery();
            while (rs.next()) {
                System.out.println("id: " + rs.getInt("id") + " " + rs.getString("name")
                        + " " + rs.getInt("surface") + " " + rs.getInt("maxCapacity")
                        + " " + rs.getInt("nrOfNests") + " " + rs.getInt("height"));
            }
        }
        catch (SQLException e){
            System.out.println("sql error");
        }
    }
    public void addBirdEnc(BirdEnclosure be){
        try {
            PreparedStatement pr = con.prepareStatement("insert into birdenc(id, surface, maxCapacity, name, nrOfNests, height) values (?,?,?,?,?,?)");
            pr.setInt(1, be.getEnclosureId());
            pr.setInt(2, be.getSurface());
            pr.setInt(3, be.getMaxCapacity());
            pr.setString(4, be.getName());
            pr.setInt(5, be.getNrOfNests());
            pr.setInt(6, be.getHeight());
            pr.execute();
        }
        catch (SQLException e){
            System.out.println("sql error");
        }
    }
    public void deleteAllBirdEnc(){
        try{
            PreparedStatement pr = con.prepareStatement("delete from birdenc where id > 0");
            pr.execute();
        }
        catch (SQLException e){
            System.out.println("sql error");
        }
    }
    public void deleteBirdEnc(int id){
        try{
            PreparedStatement pr = con.prepareStatement("delete from birdenc where id = ?");
            pr.setInt(1, id);
            pr.execute();
        }
        catch (SQLException e){
            System.out.println("sql error");
        }
    }
    public void updateNrOfNests(int id, int nrOfNests){
        try{
            PreparedStatement pr = con.prepareStatement("update birdenc set nrOfNests = ? where id = ?");
            pr.setInt(2, id);
            pr.setInt(1, nrOfNests);
            pr.execute();
        }
        catch (SQLException e){
            System.out.println("sql error");
        }
    }
}
