package javaassignment;
import javax.swing.*;
import java.io.*;
import java.util.*;

public class control extends FrameFormat {
    
    private int index;
    
    public control(){
        index = 0;
    }
        
    public void cancel_app(String app_id) throws IOException{
        File file = new File ("appointment.txt");
        BufferedReader br = new BufferedReader(new FileReader(file));
        List<String> lines = new ArrayList<>();
        String line;
        String lec_id = "";
        String fyear = ""; 
        String fmonth = "";
        String fday = "";
        String f_st = "";
        String f_et = "";
        while ((line = br.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) {
                continue;
            }
            
            if (line.startsWith(app_id)) {
                String[] l = line.split(",");
                lec_id = l[2];
                fyear = l[3];
                fmonth = l[4];
                fday = l[5];
                f_st = l[6];
                f_et = l[7];
            }else{
                lines.add(line);
            }
        }
        br.close();
        
        BufferedWriter bw = new BufferedWriter(new FileWriter(file, false));
        for (String value : lines) {
            bw.write(value);
            bw.newLine();
        }
        bw.close();
        
        File file1 = new File ("timeslot.txt");
        FileReader fr1 = new FileReader(file1);
        BufferedReader br1 = new BufferedReader(fr1);
        List<String> lines1 = new ArrayList<>();
        String line1;
        while((line1 = br1.readLine()) != null){
            line1 = line1.trim();
            if (line1.isEmpty()) {
                continue;
            }
            
            String[] L = line1.split(",");
            if(L[0].equals(lec_id) && L[1].equals(fyear) && L[2].equals(fmonth) && L[3].equals(fday)
            && L[4].equals(f_st) && L[5].equals(f_et)){
                lines1.add(L[0] + "," + L[1] + "," + L[2] + "," + L[3] + "," + L[4] + "," + L[5] + ",unbooked");
            }else{
                lines1.add(line1);
            }
        }
        br1.close();
        fr1.close();
        
        BufferedWriter bw1 = new BufferedWriter(new FileWriter(file1, false));
        for (String value : lines1) {
            bw1.write(value);
            bw1.newLine();
        }
        bw1.close();
    }
    
    public void update(String app_id, String new_data, String selection) throws IOException{
        File file = new File ("appointment.txt");
        BufferedReader br = new BufferedReader(new FileReader(file));
        List<String> lines = new ArrayList<>();
        String line;
        while ((line = br.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) {
                continue;
            }
            
            if (line.startsWith(app_id)) {
                String[] l = line.split(",");
                if(selection.equals("review")){
                    lines.add(l[0] + "," + l[1] + "," + l[2] + "," + l[3] + "," + l[4] 
                    + "," + l[5] + "," + l[6] + "," + l[7] + "," + l[8] + "," + new_data + "," + l[10]);
                }else if(selection.equals("acceptance")){
                    lines.add(l[0] + "," + l[1] + "," + l[2] + "," + l[3] + "," + l[4] 
                    + "," + l[5] + "," + l[6] + "," + l[7] + "," + new_data + "," + l[9] + "," + l[10]);
                }else if(selection.equals("lecturerFeedback")){
                    lines.add(l[0] + "," + l[1] + "," + l[2] + "," + l[3] + "," + l[4] 
                    + "," + l[5] + "," + l[6] + "," + l[7] + "," + l[8] + "," + l[9] + "," + new_data);
                }
            }else{
                lines.add(line);
            }
        }
        br.close();
        
        BufferedWriter bw = new BufferedWriter(new FileWriter(file, false));
        for (String value : lines) {
            bw.write(value);
            bw.newLine();
        }
        bw.close();
    }
    
    public void insert_app(String old_id, String stu_id, String lec_id, int year, int month, int day, int start_time, int end_time, String selection) throws IOException{
        File file = new File ("appointment.txt");
        String[] app_ida1 = read_app("app_id");
        ArrayList<Integer> app_ida = new ArrayList<>();
        for (String id : app_ida1) {
            app_ida.add(Integer.valueOf(id));
        }
        int app_id = (Collections.max(app_ida) + 1);
        
        BufferedWriter bw = new BufferedWriter(new FileWriter(file, true));
        String fyear = String.format("%04d", year);
        String fmonth = String.format("%02d", month);
        String fday = String.format("%02d", day);
        String f_st = String.format("%04d", start_time);
        String f_et = String.format("%04d", end_time);
        if(selection.equals("book")){
            bw.write(app_id + "," + stu_id + "," + lec_id + "," + fyear + "," + fmonth + "," + fday + "," + f_st + "," + f_et + ",null,null,null");
        }else if(selection.equals("reschedule")){
            bw.write(old_id + "," + stu_id + "," + lec_id + "," + fyear + "," + fmonth + "," + fday + "," + f_st + "," + f_et + ",waiting,null,null");
        }
        
        bw.newLine();
        bw.close();
        
        File file1 = new File ("timeslot.txt");
        FileReader fr1 = new FileReader(file1);
        BufferedReader br1 = new BufferedReader(fr1);
        List<String> lines = new ArrayList<>();
        String line;
        while((line = br1.readLine()) != null){
            line = line.trim();
            if (line.isEmpty()) {
                continue;
            }
            
            String l[] = line.split(",");
            if(l[0].equals(lec_id) && l[1].equals(fyear) && l[2].equals(fmonth) && l[3].equals(fday)
            && l[4].equals(f_st) && l[5].equals(f_et)){
                lines.add(l[0] + "," + l[1] + "," + l[2] + "," + l[3] + "," + l[4] + "," + l[5] + ",booked");
            }else{
                lines.add(line);
            }
        }
        br1.close();
        fr1.close();
        
        BufferedWriter bw1 = new BufferedWriter(new FileWriter(file1, false));
        for (String value : lines) {
            bw1.write(value);
            bw1.newLine();
        }
        bw1.close();
    }
    
    public String[] read_lec(String element) throws IOException{
        File file = new File ("user.txt");
        FileReader fr = new FileReader(file);
        BufferedReader lec_file = new BufferedReader(fr);
        ArrayList<String> column = new ArrayList<>();
        String line;
        while((line = lec_file.readLine()) != null){
            line = line.trim();
            if (line.isEmpty()) {
                continue;
            }
            
            String value[] = line.split(",");
            if(value[0].startsWith("LC")){
                switch (element) {
                    case "lec_id" -> column.add(value[0]);
                    case "username" -> column.add(value[1]);
                    case "password" -> column.add(value[2]);
                }
            }
        }
        lec_file.close();
        fr.close();
        
        String[] array;
        array = column.toArray(String[]::new);
        return array;
    }
    
    public String[] read_AT(String element) throws IOException{
        File file = new File ("timeslot.txt");
        FileReader fr = new FileReader(file);
        BufferedReader lec_file = new BufferedReader(fr);
        ArrayList<String> column = new ArrayList<>();
        String line;
        while((line = lec_file.readLine()) != null){
            line = line.trim();
            if (line.isEmpty()) {
                continue;
            }
            
            String value[] = line.split(",");
            switch (element) {
                case "lec_id" -> column.add(value[0]);
                case "year" -> column.add(value[1]);
                case "month" -> column.add(value[2]);
                case "day" -> column.add(value[3]);
                case "at_start" -> column.add(value[4]);
                case "at_end" -> column.add(value[5]);
                case "status" -> column.add(value[6]);
            }
        }
        lec_file.close();
        fr.close();
        
        String[] array;
        array = column.toArray(String[]::new);
        return array;
    }
    
    public String[] read_app(String element) throws IOException{
        File file = new File ("appointment.txt");
        FileReader fr = new FileReader(file);
        BufferedReader app_file = new BufferedReader(fr);
        ArrayList<String> column = new ArrayList<>();
        String line;
        while((line = app_file.readLine()) != null){
            line = line.trim();
            if (line.isEmpty()) {
                continue;
            }
            
            String value[] = line.split(",");
            switch (element) {
                case "app_id" -> column.add(value[0]);
                case "stu_id" -> column.add(value[1]);
                case "lec_id" -> column.add(value[2]);
                case "year" -> column.add(value[3]);
                case "month" -> column.add(value[4]);
                case "day" -> column.add(value[5]);
                case "start_time" -> column.add(value[6]);
                case "end_time" -> column.add(value[7]);
                case "acceptance" -> column.add(value[8]);
                case "review" -> column.add(value[9]);
                case "lec_feedback" -> column.add(value[10]);
            }
        }
        app_file.close();
        fr.close();
        
        String[] array;
        array = column.toArray(String[]::new);
        return array;
    }
    
    public String[] read_stu(String element) throws IOException{
        File file = new File ("user.txt");
        FileReader fr = new FileReader(file);
        BufferedReader stu_file = new BufferedReader(fr);
        ArrayList<String> column = new ArrayList<>();
        String line;
        while((line = stu_file.readLine()) != null){
            line = line.trim();
            if (line.isEmpty()) {
                continue;
            }
            
            String value[] = line.split(",");
            if(value[0].startsWith("TP")){
                switch (element) {
                    case "stu_id" -> column.add(value[0]);
                    case "username" -> column.add(value[1]);
                    case "password" -> column.add(value[2]);
                }
            }
        }
        stu_file.close();
        fr.close();
        
        String[] array;
        array = column.toArray(String[]::new);
        return array;
    }
            
    public void settext(String[] value, JButton[] btn){
        int i = index;
        
        for(int j = 0; j < btn.length; j++){
            btn[j].setText(value[i]);
            i++;
        }
    }
    
    public void settext2(String[] lec, String[] date, int[] ST, int[] ET, JButton[] btn){
        int i = index;
        
        for(int j = 0; j < btn.length; j++){
            btn[j].setText("<html>" + date[i] + "<br>" + ST[i] + " >> " + ET[i] + "<br>" + lec[i] + "</html>");
            i++;
        }
    }
    
    public int checkbtn(String[] value, JButton[] btn){
        int num = 0;
        for(int i = 0; i < btn.length; i++){
            try{
                String ans = value[i];
                num++;
            }catch(ArrayIndexOutOfBoundsException e){
                remove(btn[i]);    
            }   
        }
        return num;
    }
    
    public void initial(String[] value, JButton[] btn, JButton left, JButton right){
        left.setVisible(false);
        if(value.length == btn.length){
            right.setVisible(false);
        }else{
            right.setVisible(true);
        }
    }
    
    public void rightclick(String[] value, JButton[] btn, JButton left, JButton right){
        left.setVisible(true);
        index += 1;
        
        int i = (index + btn.length);
        try{
            String ans = value[i];
        }catch(ArrayIndexOutOfBoundsException e){
            right.setVisible(false);
        }
    }
    
    public void leftclick(String[] value, JButton[] btn, JButton left, JButton right){
        right.setVisible(true);
        index -= 1;
        
        if(index == 0){
            left.setVisible(false);
        }
    }
    
    public int id(){
        return index;
    }
    
    public int[] to_int(String[] array){
        int[] intarray = new int[array.length];
        for (int i = 0; i < array.length; i++) {
            intarray[i] = Integer.parseInt(array[i]);
        }
        return intarray;
    }
    
    public String[] to_str(int[] array){
        String[] strarray = new String[array.length];
        for (int i = 0; i < array.length; i++) {
            strarray[i] = String.valueOf(array[i]);
        }
        return strarray;
    }
    
    
    
}
