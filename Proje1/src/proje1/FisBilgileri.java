package proje1;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;


public class FisBilgileri {
    private String isletmeAdi;
    private String tarih;
    private String fisNo;
    private String urunler;
    private String toplamFiyat;

    public FisBilgileri(String isletmeAdi, String tarih, String fisNo, String urunler, String toplamFiyat) {
       
        this.isletmeAdi = isletmeAdi;
        this.tarih = tarih;
        this.fisNo = fisNo;
        this.urunler = urunler;
        this.toplamFiyat = toplamFiyat;
        isNull();
    }
    public  void isNull(){
        LocalDateTime myDateObj = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String formattedDate = myDateObj.format(myFormatObj);
               
        if(tarih.isEmpty())
            tarih = formattedDate;
        if(fisNo.isEmpty())
            fisNo = "0001";
        if(toplamFiyat.isEmpty())
            toplamFiyat = "0.0";
        
    }

    public String getIsletmeAdi() {
        return isletmeAdi;
    }

    public void setIsletmeAdi(String isletmeAdi) {
        this.isletmeAdi = isletmeAdi;
    }

    public String getFisNo() {
        return fisNo;
    }

    public void setFisNo(String fisNo) {
        this.fisNo = fisNo;
    }
    

    public String getTarih() {
        return tarih;
    }

    public void setTarih(String tarih) {
        this.tarih = tarih;
    }

    public String getUrunler() {
        //System.out.println(urunler.size());
        return urunler;
    }

    public void setUrunler(String urunler) {
        this.urunler = urunler;
    }

    public String getToplamFiyat() {
        return toplamFiyat;
    }

    public void setToplamFiyat(String toplamFiyat) {
        this.toplamFiyat = toplamFiyat;
    }
  
}
