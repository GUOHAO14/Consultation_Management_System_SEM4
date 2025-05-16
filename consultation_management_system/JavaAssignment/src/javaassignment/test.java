/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javaassignment;

import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.io.*;
import java.util.Scanner;
/**
 *
 * @author Khoo Guo Hao
 */
public class test {
    public static void main (String [] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        Date d1 = new Date(2024, 5, 20);
        Date d2 = new Date();
        Calendar c = new GregorianCalendar();
        File f = new File("testFile.txt");
        
        System.out.println(f.exists());
    }
}
