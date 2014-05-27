package tr.edu.ege.cs.egenav.mapcache;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import tr.edu.ege.cs.egenav.MapDownloader;
import tr.edu.ege.cs.egenav.MapURL;

/**
 * @author Özgün Yılmaz
 * Created on 26.May.2014, 14:40:20
 */
public class MemoryMapCache extends MapCache{
    //todo private metotlar (deleteExceededMaps, vs.) public olabilir mi?
    
    ArrayList<MapInfo> maps;

    public MemoryMapCache() {
        
        this(System.getProperty("user.dir"));
        
    }

    public MemoryMapCache(String filePath) {
        super(filePath);
        
        File f=new File(getCacheFileAbsoluteName());
        //System.out.println(f.exists());
        if (f.exists()){
            FileInputStream fin = null;
            try {
                fin = new FileInputStream(getCacheFileAbsoluteName());
                ObjectInputStream ois = new ObjectInputStream(fin);
                maps = (ArrayList<MapInfo>) ois.readObject();
                deleteDateDueMaps();
                ois.close();
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(MemoryMapCache.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(MemoryMapCache.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    fin.close();
                } catch (IOException ex) {
                    Logger.getLogger(MemoryMapCache.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }else{
            maps=new ArrayList<MapInfo>();
        }
    }
    
    private void deleteDateDueMaps(){
        
        if (getTimeLimit()<0){
            return;
        }
        for (int i=0;i<maps.size();i++){
            
            MapInfo m=maps.get(i);
            long now=Calendar.getInstance().getTimeInMillis();
            long past=m.getDownloadDate().getTime();
            int day=(int)((now-past)/((24 * 60 * 60 * 1000)));
            
            
            if (day>getTimeLimit()){
                maps.remove(i);
                i--;
                m.deleteImageFile();
            }
        }
        
    }
    
    @Override
    public boolean isLimitReached(){
        if (getLimit()>0){
            return getLimit()<=maps.size();
        }else{
            return false;
        }
        
    }
    
    @Override
    public BufferedImage getMap(MapURL mapurl) {
        
        String mstr=mapurl.getAbsoluteURLString();
        MapInfo m=find(mstr);
        if (m==null){
            BufferedImage bim=MapDownloader.downloadMap(mstr);
            
            String fs;
            if (mapurl.getFormat()==null || mapurl.getFormat().equals("")){
                fs="png";
            }else{
                fs=mapurl.getFormat();
            }
            
            MapInfo minfo=new MapInfo(mstr,fs);    //Eğer limitse çıkarılmalı
            
            File outputfile = new File(getImagePath() + minfo.getImageFileName());
            try {
                
                ImageIO.write(bim, fs, outputfile);
            } catch (IOException ex) {
                Logger.getLogger(MemoryMapCache.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            if (isLimitReached()){
                removeLeastUsed();  
                //en az sayıda kullanılanı ve en eski olanı çıkar ve image dosyasını sil.
            }
            
            maps.add(minfo);
            
            return bim;
        }else{
            m.incrementUsageCount();
            String fname=m.getImageFileName();
            try {
                return ImageIO.read(new File(getImagePath()+fname));
            } catch (IOException ex) {
                Logger.getLogger(MemoryMapCache.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
            
        }
        
    }
    
    private void removeLeastUsed(){
        
        int index=-1,eks=1000;
        for (int i=0;i<maps.size();i++){
            
            MapInfo m=maps.get(i);
            if (m.getUsageCount()<eks){
                
                index=i;
                
            }
        }
        MapInfo m=maps.get(index);
        m.deleteImageFile();
        
    }
    
    public MapInfo find(String mapurl){
        
        for (int i=0;i<maps.size();i++){
            if (maps.get(i).getMapurl().equals(mapurl)){
                return maps.get(i);
            }
        }
        return null;
    }

    @Override
    public void close() {
        FileOutputStream fout = null;
        try {
            fout = new FileOutputStream(getCacheFileAbsoluteName());
            ObjectOutputStream oos = new ObjectOutputStream(fout);
            oos.writeObject(maps);
            oos.close();
            System.out.println("Done");
        } catch (Exception ex) {
            Logger.getLogger(MemoryMapCache.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fout.close();
            } catch (IOException ex) {
                Logger.getLogger(MemoryMapCache.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public static void main(String args[]){
        MemoryMapCache m=new MemoryMapCache("\\fdgfdg\\");
        MemoryMapCache m2=new MemoryMapCache("\\fdgfdg");
        
    }

    @Override
    public final String getCacheFileAbsoluteName() {
        return getPath()+"mapdata";
    }
    
}
