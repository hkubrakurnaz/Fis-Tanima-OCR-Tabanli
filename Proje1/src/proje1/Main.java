
package proje1;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import static javax.swing.text.StyleConstants.Size;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;


public class Main {
    static String isletmeAd = "";
    static String fisNo = " ";
    static String tarih = "";
    static String toplam = "";
    static ArrayList<String> urunler = new ArrayList<String>();
	
    
    public static void main(String[] args) {   
        String path = new Main().dosyaSecimi();
        if(path==null){
            System.out.println("Dosya seçilemedi...");
        }
        else{
            Database db = new Database();
            path = path.replace('\\' , '/');
            System.out.println(path);
            new Main().ocrIslemi(path);
            String res = String.join("\n", urunler); //ArrayList Stringe çevirme
           
            FisBilgileri fisBilgileri = new FisBilgileri(isletmeAd,tarih,fisNo,res,toplam);
            // System.out.println("Fis No:"+fisNo);
            db.bilgileriEkle(isletmeAd,tarih,fisNo,res,toplam); //add database
            Ekran ekran = new Ekran( fisBilgileri,path);
            ekran.setVisible(true);
        
        }
    }
    public void ocrIslemi(String path){
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        Mat image = new Mat();
        image = Imgcodecs.imread(path);
       
        Mat gray = new Mat();
        Imgproc.cvtColor(image, gray, Imgproc.COLOR_BGR2GRAY, 0);
        System.out.println("size:"+image.size()+" "+image.height()+" "+image.width());
        
        Mat resize = new Mat();
        Imgproc.resize(gray, resize, new Size(1000, 1000));
        
        Mat blur = new Mat();
        Imgproc.medianBlur(resize, blur, 3);
        
        Mat th = new Mat();
        Imgproc.threshold(gray, th, 0,255,Imgproc.THRESH_OTSU);
        

        String new_path = "C:/Users/hatic/Desktop/aresim/fis_deneme.jpg";
        MatOfInt parametre = new MatOfInt(Imgcodecs.CV_IMWRITE_PNG_COMPRESSION);
        //File ocr = new File("ocrImage.png");
        Imgcodecs.imwrite(new_path,th,parametre);
        
        ITesseract tess = new Tesseract();  

        String tessdata_path = "C:/Users/hatic/OneDrive/Belgeler/NetBeansProjects/JavaApplication6/tessdata";
        tess.setDatapath(tessdata_path);
        tess.setLanguage("tur");
        
        File file = new File(new_path);
          try {
            String result = tess.doOCR(file);
            System.out.println(result);
            new Main().splitString(result);
        } catch (TesseractException e) {
            System.err.println(e.getMessage());
        }
    }
    public String dosyaSecimi(){
        JFileChooser fileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
	fileChooser.setDialogTitle("Image");
	fileChooser.setAcceptAllFileFilterUsed(false);
	FileNameExtensionFilter filter = new FileNameExtensionFilter("PNG and JPG images", "png", "jpg");
	fileChooser.addChoosableFileFilter(filter);

	int value = fileChooser.showOpenDialog(null);
	if (value == JFileChooser.APPROVE_OPTION) {
        	System.out.println(fileChooser.getSelectedFile().getPath());
                return fileChooser.getSelectedFile().getPath();
	}
        return null;
    }
    void splitString(String result) {
        String[] tarihler = null;
        String[] fields = result.split("\n");
	for(int i = 0;i<fields.length;i++) {
            isletmeAd = fields[0];
            
            if(fields[i].toLowerCase().trim().contains("fiş no") ||fields[i].toLowerCase().trim().contains("fis no") ||
                    fields[i].toLowerCase().trim().contains("fişno") ||fields[i].toLowerCase().trim().contains("fıs no")){
               // System.out.println("FIS");
                String[] fis = new String[5];
               
                if(fields[i].toLowerCase().trim().contains("fiş no"))
                     fis = fields[i].toLowerCase().trim().split("fiş no");
                
                else if(fields[i].toLowerCase().trim().contains("fis no"))
                     fis = fields[i].toLowerCase().trim().split("fis no");
                
                else if(fields[i].toLowerCase().trim().contains("fıs no"))
                     fis = fields[i].toLowerCase().trim().split("fıs no");
     
                else if(fields[i].toLowerCase().trim().contains("fişno"))
                     fis = fields[i].toLowerCase().trim().split("fişno");
             
                fisNo = fis[1];
                if( fisNo!=null)
                    fisNo = fisNo.replace(":", "");
            }
            if(fields[i].toLowerCase().trim().contains("tarih")||fields[i].toLowerCase().trim().contains("tarıh")){
                if(fields[i].toLowerCase().trim().contains("tarih"))
                    tarihler = fields[i].toLowerCase().trim().split("tarih");
                else if(fields[i].toLowerCase().trim().contains("tarıh"))
                    tarihler = fields[i].toLowerCase().trim().split("tarıh :");
      
                tarih = tarihler[1].replace(":","");
                tarihParcala();
               // System.out.println("Tarih:"+tarih);
            }
            if((fields[i].toLowerCase().trim().contains("fiş no :") ||fields[i].toLowerCase().trim().contains("fıs no:")||
                    fields[i].toLowerCase().trim().contains("fiş no:") ||
                    fields[i].toLowerCase().trim().contains("fişno:")) && (tarihler == null)){
                tarihler = fields[i].toLowerCase().trim().split(" ",2);
                tarih = tarihler[0]; 
                tarihParcala();
            }
          
            if(fields[i].toLowerCase().trim().contains("%08") || fields[i].toLowerCase().trim().contains("%8"))
                 urunler.add(fields[i]);
            else  if(fields[i].toLowerCase().trim().contains("%") && (fields[i].toLowerCase().trim().contains("*")|| (fields[i].toLowerCase().trim().contains("x")))){
                urunler.add(fields[i]);
            }
            if(fields[i].toLowerCase().trim().contains("toplam") ){
                String total[] = fields[i].toLowerCase().trim().split("toplam");

                toplam = total[1];
                toplam = toplam.replace("*","");
                break;
            }
            LocalDateTime myDateObj = LocalDateTime.now();
            DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String formattedDate = myDateObj.format(myFormatObj);
               
            if(tarih.isEmpty())
                tarih = formattedDate;
            
        }	
    }
    void tarihParcala(){
        if(tarih.toLowerCase().trim().contains("saat")){
            int index = tarih.indexOf('s');
            System.out.println("index:"+index);
            tarih = tarih.substring(0,index);
        }
    }
}
