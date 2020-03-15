package proje1;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Database {
    private PreparedStatement preparedStatement = null;
    private String user_name = "root";
    private String parola ="";
    private String db_name = "fisbilgileri";
    private String host = "localhost";
    private int port = 3306;
    private Connection con = null;
    private Statement statement = null;
    
    
    public ArrayList<FisBilgileri> showInfo(){
        
         ArrayList<FisBilgileri> cikti = new ArrayList<FisBilgileri>();
        
        try {
            statement =  con.createStatement();
            String sorgu =  "Select * From fisbilgileri ORDER BY tarih DESC ";
            
            ResultSet rs =  statement.executeQuery(sorgu);
            
            while(rs.next()) {
          
                int id = rs.getInt("id"); 
                String isletmeAdi = rs.getString("IsletmeAdi");
                String tarih = rs.getString("tarih");
                String fisNo  = rs.getString("fisNo");
                String urun= rs.getString("urunler");
                /*ArrayList<String> urunler = new ArrayList<>();
                urunler.add(urun);*/
                System.out.println("Urun:"+urun);
                String toplamFiyat = rs.getString("toplamFiyat");
                // System.out.println("99999"+urunler);
                cikti.add(new FisBilgileri(isletmeAdi, tarih, fisNo, urun,toplamFiyat));      
            }
            return cikti;            
        } catch (SQLException ex) {
            return null;
        }
    }
    public ArrayList<String> isletmeAd(){
        ArrayList<String> cikti = new ArrayList<String>();
        
        try {
            statement =  con.createStatement();
            String sorgu =  "Select IsletmeAdi From fisbilgileri  ORDER BY tarih DESC";
            
            ResultSet rs =  statement.executeQuery(sorgu);
            
            while(rs.next())  {
                String isletmeAdi = rs.getString("IsletmeAdi");
                if(!cikti.contains(isletmeAdi))
                    cikti.add(isletmeAdi);      
            }
            return cikti;
            
        } catch (SQLException ex) {  
            return null;
        }
    }
    public void bilgileriEkle(String isletmeAdi,String tarih,String fisNo ,String urunler,String toplamFiyat){
        
        String sorgu = "Insert Into fisbilgileri (IsletmeAdi,tarih,fisNo,urunler,toplamFiyat) VALUES(?,?,?,?,?)";
        try {
            preparedStatement = con.prepareStatement(sorgu);
            preparedStatement.setString(1, isletmeAdi);
            preparedStatement.setString(2, tarih);
            preparedStatement.setString(3, fisNo);
            preparedStatement.setString(4, urunler);
            preparedStatement.setString(5, toplamFiyat);
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Hata!");
        }
    }
    public ArrayList<FisBilgileri> search(String date,String isletmeAdi) throws SQLException{
       String sorgu;
       ArrayList<FisBilgileri>cikti=new ArrayList<FisBilgileri>();
   
       if(!date.isEmpty() && !isletmeAdi.isEmpty()){
           sorgu =  "Select * From fisbilgileri where tarih = ? and IsletmeAdi = ?  ORDER BY tarih DESC";
           try {
               preparedStatement = con.prepareStatement(sorgu);
           } catch (SQLException ex) {
               Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
           }
            preparedStatement.setString(1,date);
            preparedStatement.setString(2,isletmeAdi);
       }
       else if(!date.isEmpty()){
           sorgu =  "Select * From fisbilgileri where tarih = ?  ORDER BY tarih DESC";
           preparedStatement = con.prepareStatement(sorgu);
           preparedStatement.setString(1,date);
       }
       else if(!isletmeAdi.isEmpty()){
           sorgu =  "Select * From fisbilgileri where IsletmeAdi = ?  ORDER BY tarih DESC";
           preparedStatement = con.prepareStatement(sorgu);
           preparedStatement.setString(1,isletmeAdi);
        } 
       else{
           System.out.println("Arama yapmak için bir şeyler giriniz...");
       }
       ResultSet rs = preparedStatement.executeQuery();
       while(rs.next()){
            String isletme_adi = rs.getString("IsletmeAdi");
            String tarih = rs.getString("tarih");
            String fisNo  = rs.getString("fisNo");
            String urun= rs.getString("urunler");
            ArrayList<String> urunler = new ArrayList<>();
            String toplamFiyat = rs.getString("toplamFiyat");
            cikti.add(new FisBilgileri(isletme_adi, tarih, fisNo, urun,toplamFiyat));      
            }
        return cikti;
    }
    
    public Database(){
        String url = "jdbc:mysql://" + host+":" + port + "/" + db_name;
        
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            System.out.println("Driver bulumamadı.");
        }
        try {
            con = DriverManager.getConnection(url, user_name, parola);
            System.out.println("Bağlantı başarılı");
        } catch (SQLException ex) {
            System.out.println("Bağlantı başarısız!");
        }
    } 
}
