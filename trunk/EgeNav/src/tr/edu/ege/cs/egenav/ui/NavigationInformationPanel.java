/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * NavigationInformationPanel.java
 *
 * Created on 22.May.2014, 15:18:43
 */
package tr.edu.ege.cs.egenav.ui;

/**
 *
 * @author Özgün Yılmaz
 */
public class NavigationInformationPanel extends javax.swing.JPanel implements NavigationInformation{

    /** Creates new form NavigationInformationPanel */
    public NavigationInformationPanel() {
        initComponents();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

    @Override
    public void setLatitude(double lat) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setLongitude(double lon) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setHeading(double heading) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setSpeed(double speed) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setTotalDistance(double dist) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setTimeElapsed(double timeElapsed) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setAverageSpeed(double averagSpeed) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setInstructions(String ins) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    
}