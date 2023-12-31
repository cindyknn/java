/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

/**
 *
 * @author cindyknn
 */
public class OpenPanel extends javax.swing.JPanel {

    MainFrame data;
    private final int type;
    public static final int OPEN_FILE = 0;
    public static final int IMPORT = 1;
    public static final int SAVE = 2;

    /**
     * Creates new form OpenPanel
     *
     * @param data
     * @param type
     */
    public OpenPanel(MainFrame data, int type) {
        this.type = type;
        this.data = data;

        initComponents();

        String dir = System.getProperty("user.dir");
        fileChooser.setCurrentDirectory(new File(dir));
    }
    
    public void setDirectory(String defaultDir){
        String dir = System.getProperty("user.dir") + "/Data/";
        String defaultPath = dir + "default.txt";
        defaultDir = defaultDir.replace(dir, "");
        defaultDir = defaultDir.replace("/data.txt", "");
        
        try (PrintWriter writer = new PrintWriter(new File(defaultPath))) {
            writer.println(defaultDir);
        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(this, "Error trying to load file: " + ex,
                    "Load Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        fileChooser = new javax.swing.JFileChooser();

        fileChooser.setCurrentDirectory(new File("%USERPROFILE%/Desktop"));
        fileChooser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fileChooserActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(fileChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(fileChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void fileChooserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fileChooserActionPerformed
        if (evt.getActionCommand().equals("CancelSelection")) {
            ((JDialog) this.getTopLevelAncestor()).dispose();
        } else if (fileChooser.getSelectedFile().getAbsolutePath() == null) {
        } else {
            switch (type) {
                case OPEN_FILE:
                    String dir = fileChooser.getSelectedFile().getAbsolutePath();
                    setDirectory(dir);
                    break;
                case IMPORT:
                    String path = fileChooser.getSelectedFile().getAbsolutePath();
                    data.importPlayers(path);
                    break;
                default:
                    break;
            }
            ((JDialog) this.getTopLevelAncestor()).dispose();
        }
    }//GEN-LAST:event_fileChooserActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JFileChooser fileChooser;
    // End of variables declaration//GEN-END:variables
}
