package javaassignment;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;

public class LecturerInteractTextFile {
    
    public boolean saveTimeSlots(JCheckBox[] CBSelections, String [] otherSlotInfo) throws IOException {
        boolean status = false;
        String lecturerID = otherSlotInfo[0];
        String year = otherSlotInfo[1];
        String month = otherSlotInfo[2];
        String day = otherSlotInfo[3];
        File f = new File("timeslot.txt");
        if (!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "An error occured. Try again later.", "Database Failure", 0);
            }
        }
        
        try (BufferedReader timeSlotBR = new BufferedReader(new FileReader("timeslot.txt"))) {
            System.out.println("hello1");
            
            int counter = 0;
            String eachLine;
            while ((eachLine = timeSlotBR.readLine()) != null) {
                if (counter == 0) {
                    try (BufferedWriter timeSlotBW = new BufferedWriter(new FileWriter("timeslot.txt"))) {
                        timeSlotBW.write("");
                    }
                }
                String [] slotDetails = eachLine.split(",");
                if (slotDetails[0].equals(lecturerID) && slotDetails[1].equals(year) && slotDetails[2].equals(month) && slotDetails[3].equals(day)) {
                    System.out.println("Old data to be replaced");
                } else {
                    try (BufferedWriter timeSlotBW = new BufferedWriter(new FileWriter("timeslot.txt", true))) {
                        timeSlotBW.write(String.join(",", eachLine)+"\n");
                    }
                }
                System.out.println(eachLine);
                counter++;
            }
        }
        try (BufferedWriter timeSlotBW = new BufferedWriter(new FileWriter("timeslot.txt", true))) {
            System.out.println("hello2");
            for (JCheckBox checkBox : CBSelections) {
                if (checkBox.isSelected()) {
                    String slot = checkBox.getText().replace(":", "");
                    String startTime = slot.substring(0, 4);
                    String endTime = slot.substring(7);

                    String [] completeRecord = new String[6];
                    for (int i = 0; i < otherSlotInfo.length; i++) {
                        completeRecord[i] = otherSlotInfo[i];
                    }
                    completeRecord[4] = startTime;
                    completeRecord[5] = endTime;

                    timeSlotBW.write(String.join(",", completeRecord)+","+"unbooked"+"\n");
                    status = true;
                }
            }
            JOptionPane.showMessageDialog(null, "Time slot has been successfully saved.");
        }
        return status;
    }
    
    public List<String[]> readAppointments(String lecturer, String appointmentType) {
        LocalDateTime currentDate = LocalDateTime.now();
        List<String[]> appointRecords = new ArrayList<>();
        
        try (BufferedReader appointBR = new BufferedReader(new FileReader("appointment.txt"))) {
            int counter = 0;
            String eachLine;
            while ((eachLine = appointBR.readLine()) != null) {
                String [] appointDetails = eachLine.split(",");
                
                String studentID = appointDetails[1];
                //retrieve from user file later
                String studentName = "";
                String studentEmail = "";
                String lecturerID = appointDetails[2];
                
                String status = appointDetails[8];
                
                //form a full date
                String year = appointDetails[3];
                int intYear = Integer.parseInt(year);
                String month = appointDetails[4];
                int intMonth = Integer.parseInt(month);
                String day = appointDetails[5];
                int intDay = Integer.parseInt(day);
                int hours = Integer.parseInt(appointDetails[6].substring(0, 2)); //first 2 characters are hours
                int minutes = Integer.parseInt(appointDetails[6].substring(2, 4));
                
                //use int YY-MM-DD to form date object for comparison later
                LocalDateTime lineDate = LocalDateTime.of(intYear, intMonth, intDay, 0, 0);
                lineDate = lineDate.withHour(hours).withMinute(minutes);
                String date = year+"-"+month+"-"+day;
                String start = appointDetails[6].substring(0, 2)+":"+appointDetails[6].substring(2);
                String end = appointDetails[7].substring(0, 2)+":"+appointDetails[7].substring(2);
                try (BufferedReader userBR = new BufferedReader(new FileReader("user.txt"))) {
                    int userCounter = 0;
                    String eachUser;
                    while ((eachUser = userBR.readLine()) != null) {
                        String [] userDetails = eachUser.split(",");
                        if (studentID.equals(userDetails[0])) {
                            studentName = userDetails[1];
                            studentEmail = userDetails[3];
                            break;
                        }
                        userCounter++;
                    }

                }
                if (lecturerID.equals(lecturer) && (status.equals("null") || status.equals("accept"))) {
                    if (appointmentType.equals("all") || (appointmentType.equals("past") && lineDate.isBefore(currentDate)) || (appointmentType.equals("upcoming") && lineDate.isAfter(currentDate))) {
                        //appointment ID, student ID, stu name, date, time slot, stu feedback, lecturer feedback
                        String [] helo = {appointDetails[0], appointDetails[1], studentName, date, start+" - "+end, appointDetails[9], appointDetails[10], studentEmail};
                        appointRecords.add(helo);
                    }
                }
                counter++;
            }
        } catch (FileNotFoundException ex) {
            try {
                new File("appointment.txt").createNewFile();
            } catch (IOException ex1) {
            } finally {
                JOptionPane.showMessageDialog(null, "An error occured. Try again later.", "Database Failure", 0);
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "An error occured. Try again later.", "Database Failure", 0);
        }
        
        return appointRecords;
    }
}
