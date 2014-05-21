package tr.edu.ege.cs.egenav;

import java.awt.Point;

/**
 * @author Özgün Yılmaz
 * Created on 04.Nis.2014, 14:35:45
 */
public class GSMMapURL extends MapURL{
    
    //marker,styled maps, paths
    //visible parametresini ekle.
    
    private boolean sensor=false;
    private String apiKey,signature,language,region,mapType,format;
    private int scale=-1;
    private GSMPath path;
    
    public static final int MAX_ZOOM=21;
    
    
    public GSMMapURL(){
        setMiddleURL("maps.googleapis.com/maps/api/staticmap?");
        setSeparator("&");
    }
    
    public GSMMapURL(String apiKey){
        this();
        this.apiKey=apiKey;
    }
    
    public GSMMapURL(String clientID,String signature){
        this();
        setClientID(clientID);
        this.signature=signature;
    }
    
    @Override
    public boolean incrementZoom(){
        if (getZoom()<MAX_ZOOM){
            incrementZoom();
            return true;
        }
        return false;
    }
    
    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }
    
    @Override
    public GSMLocation getLocation(){
        return (GSMLocation)super.getLocation();
    }
    
    public GSMLocation getCenter() {
        return getLocation();
    }

    public void setCenter(GSMLocation center) {
        setLocation(center);
    }
    
    public int getScale() {
        return scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public boolean isSensor() {
        return sensor;
    }

    public void setSensor(boolean sensor) {
        this.sensor = sensor;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getMapType() {
        return mapType;
    }

    public void setMapType(String mapType) {
        this.mapType = mapType;
    }

    @Override
    public String getAbsoluteURLString() {
        
        String url;
        
        if (isSecure()){
            url="https://";
        }else{
            url="http://";
        }
        
        url=url+getMiddleURL();
        
        if (getCenter()!=null){
            url=url+"center="+getCenter().toString()+getSeparator();
        }
        
        if (getZoom()!=-1){
            url=url+"zoom="+getZoom()+getSeparator();
        }
        
        url=url+"size="+getMapSize().getHorizontal()+"x"+getMapSize().getVertical()+getSeparator()+
                "sensor="+isSensor()+getSeparator();
        
        if (getScale()!=-1){
            url=url+"scale="+getScale()+getSeparator();
        }
        
        if (getFormat()!=null && !getFormat().isEmpty()){
            
            url=url+"format="+getFormat()+getSeparator();
        }
        
        if (getMapType()!=null && !getMapType().isEmpty()){
            
            url=url+"maptype="+getMapType()+getSeparator();
        }
        
        if (getLanguage()!=null && !getLanguage().isEmpty()){
            url=url+"language="+getLanguage()+getSeparator();
        }
                
        if (getRegion()!=null && !getRegion().isEmpty()){
            url=url+"region="+getRegion()+getSeparator();
        }
        
        if (getPath()!=null){
            url=url+getPath().toString()+getSeparator();
        }
        
        for (int i=0;i<parameters.size();i++){
            url=url+parameters.get(i).toString()+getSeparator();
        }
        
        if (getApiKey()!=null && !getApiKey().isEmpty()){
            
            url=url+"key="+getApiKey()+getSeparator();
        }
        
        if (getClientID()!=null && !getClientID().isEmpty()){
            
            url=url+"client="+getClientID()+getSeparator();
        }
        
        if (getSignature()!=null && !getSignature().isEmpty()){
            
            url=url+"signature="+getSignature()+getSeparator();
        }
        
        while (url.endsWith(getSeparator())){
            url=url.substring(0, url.lastIndexOf(getSeparator()));
        }
        
        return url;
    }

    @Override
    public GSMMapURL clone() {
        
        GSMMapURL gsm=new GSMMapURL();
        gsm.setApiKey(getApiKey());
        gsm.setCenter(getCenter().clone());
        gsm.setClientID(getClientID());
        gsm.setFormat(getFormat());
        gsm.setLanguage(getLanguage());
        gsm.setMapSize(getMapSize().clone());
        gsm.setMapType(getMapType());
        gsm.setMiddleURL(getMiddleURL());
        gsm.setRegion(getRegion());
        gsm.setScale(getScale());
        gsm.setSecure(isSecure());
        gsm.setSensor(isSensor());
        gsm.setSeparator(getSeparator());
        gsm.setSignature(getSignature());
        gsm.setZoom(getZoom());
        
        return gsm;
    }

    @Override
    public Point getPixelOnMap(double lat, double lon) {
        
        Point p=new Point(getMapSize().getVertical()/2+MercatorProjection.getDeltaByLats(this.getLocation().getLatitude(), lat, getZoom()),
                getMapSize().getHorizontal()/2+MercatorProjection.getDeltaByLons(this.getLocation().getLongitude(), lon, getZoom()));
        return p;
    }

    @Override
    public GeoPoint getCordinatesOnMap(Point p) {
        
        GeoPoint gp=new GeoPoint(
                MercatorProjection.getLatByPixels(getCenter().getLatitude(),getMapSize().getVertical()/2-(int)p.getY(),getZoom()),
                MercatorProjection.getLonByPixels(getCenter().getLongitude(),getMapSize().getHorizontal()/2-(int)p.getX(),getZoom()));
        
        return gp;
    }

    @Override
    public MapURL getNeighborTile(int verticalDirection, int horizontalDirection) {
        
        GSMMapURL gsm=clone();
        GeoPoint gp=gsm.getCenter().getCoordinate();
        double lat=gp.getLatitude();
        double lon=gp.getLongitude();
        
        if (verticalDirection!=Direction.CONSTANT){
            double newLat=MercatorProjection.getLatByPixels(lat,verticalDirection*getMapSize().getVertical(),getZoom());
            gp.setLatitude(newLat);
        }
        
        if (horizontalDirection!=Direction.CONSTANT){
            double newLon=MercatorProjection.getLonByPixels(lon,horizontalDirection*getMapSize().getHorizontal(),getZoom());
            gp.setLongitude(newLon);
        }
        
        return gsm;
        
    }

    public GSMPath getPath() {
        return path;
    }

    public void setPath(GSMPath path) {
        this.path = path;
    }
    
}