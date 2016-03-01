/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package folderorganizer;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author Alp Sayin
 */
public class FolderOrganizer
{

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        try
        {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (ClassNotFoundException ex)
        {
            Logger.getLogger(FolderOrganizer.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (InstantiationException ex)
        {
            Logger.getLogger(FolderOrganizer.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IllegalAccessException ex)
        {
            Logger.getLogger(FolderOrganizer.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (UnsupportedLookAndFeelException ex)
        {
            Logger.getLogger(FolderOrganizer.class.getName()).log(Level.SEVERE, null, ex);
        }
        JFileChooser chooser = new JFileChooser(File.listRoots()[0]);
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setMultiSelectionEnabled(true);
        int retVal = chooser.showOpenDialog(null);
        if(retVal == JFileChooser.APPROVE_OPTION)
        {
            File[] dirs = chooser.getSelectedFiles();
            for(File dir : dirs)
            {
                System.out.println(dir.getAbsolutePath());
                organizeDirectory(dir);
            }
        }
    }
    private static void organizeDirectory(File dir)
    {
        File[] files = dir.listFiles(new FileFilter() {
            @Override public boolean accept(File pathname)
            {
                return pathname.isFile();
            }
        });
        for(int i=0; i<files.length; i++)
        {
            File f1 = files[i];
            for(int j=i+1; j<files.length; j++)
            {
                if(j < files.length)
                {   
                    File f2 = files[j];
                    if(compareFiles(f1, f2))
                    {
                        int retVal = showConfirmationDialog();
                        System.out.println(f1.getName()+" = "+f2.getName());
                        //while(f1.delete());
                        f1.deleteOnExit();
                    }
                }
            }
        }
    }
    private static boolean compareFiles(File f1, File f2)
    {
        FileInputStream fis1 = null;
        FileInputStream fis2 = null;
        if(f1.length() != f2.length())
            return false;
        try
        {
            fis1 = new FileInputStream(f1);
            fis2 = new FileInputStream(f2);
            BufferedInputStream bis1 = new BufferedInputStream(fis1);
            BufferedInputStream bis2 = new BufferedInputStream(fis2);
            int nextByte1 = bis1.read();
            int nextByte2 = bis2.read();
            long counter = 0;
            while(nextByte1!=-1 && nextByte2!=-1 && nextByte1==nextByte2)
            {
                nextByte1 = bis1.read();
                nextByte2 = bis2.read();
                counter++;
            }
            System.out.println(counter+" ?= "+f1.length());
            fis1.close();
            fis2.close();
            if(counter == f1.length())
                return true;
        }
        catch (IOException ex)
        {
            Logger.getLogger(FolderOrganizer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    private static int showConfirmationDialog()
    {
        return 1;
    }
}
