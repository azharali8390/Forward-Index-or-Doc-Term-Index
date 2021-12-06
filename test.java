/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Assignment;

import java.io.IOException;


/**
 *
 * @author Dell
 */
public class test {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException 
    {
        final String path_variable = "D:\\Semester 7\\Information Retirievel\\corpus\\corpus";
        reading_files obj=new reading_files();
        obj.read(path_variable);
    }
    
}
