package javaassignment;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.io.*;
import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;

/**
 *
 * @author Khoo Guo Hao
 */
public class SetConsultationTimePage extends FrameFormat {
    private String sessionUserID;
    private boolean updatableSign = true;
    private int selectedYear;
    private int selectedIntMonth;
    private int selectedDay;
    String monthNames [] = {"","Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    
    Calendar currentDate = new GregorianCalendar();

    int currentYear = currentDate.get(Calendar.YEAR);
    int currentIntMonth = currentDate.get(Calendar.MONTH)+1;
    int currentDay = currentDate.get(Calendar.DATE);
    
    JCheckBox[] timeSlots;
    //cannot edit past appointment dates
    //need do
    
    /** Creates new form test */
    public SetConsultationTimePage(String sessionUserID) {
        initComponents();
        this.sessionUserID = sessionUserID;
        super.formatWindow("Set Consultation Time Slot Page");
        timeSlots = new JCheckBox[]{slot0830, slot0900, slot0930, slot1000, slot1030, slot1100, slot1130, slot0130, slot0200, slot0230, slot0300, slot0330, slot0400, slot0430};
        
        //collect info about current date and selected date
        Date selectedDate = dateSelectionCalendar.getDate();
        
        Calendar c = new GregorianCalendar();
        c.setTime(selectedDate);
        selectedYear = c.get(Calendar.YEAR);
        selectedIntMonth = c.get(Calendar.MONTH)+1;
        String selectedMonth = monthNames[selectedIntMonth];
        selectedDay = c.get(Calendar.DATE);
        
        String consultDate = selectedDay+"-"+selectedMonth+"-"+selectedYear; //for display
        selectedDateLabel.setText("Time slots for: "+consultDate);
        timeSelectionNote.setText("Tick for the time slots that you are available.");
        checkTimeSlots();
        verifyDateChosen();
        
    }
    
    public void checkTimeSlots() {
        try (BufferedReader timeSlotBR = new BufferedReader(new FileReader("timeslot.txt"))) {
            int counter = 0;
            String eachLine;
            while ((eachLine = timeSlotBR.readLine()) != null) {
                String [] slotDetails = eachLine.split(",");
                //find time slots of the lecturer with the correct date
                if (slotDetails[0].equals(sessionUserID) && slotDetails[1].equals(String.valueOf(selectedYear)) && slotDetails[2].equals(String.valueOf(selectedIntMonth)) && slotDetails[3].equals(String.valueOf(selectedDay))) {
                    //find the corresponding time slot
                    for (JCheckBox checkBox : timeSlots) {
                        String guiStartTime = checkBox.getText().replace(":", "").substring(0, 4);
                        String fileStartTime = slotDetails[4];
                        if (!checkBox.isSelected()) {
                            if (guiStartTime.equals(fileStartTime)) {
                                checkBox.setSelected(true); //select the time slot
                            } else {
                                checkBox.setSelected(false);
                            }
                        }
                        checkBox.setEnabled(false);
                        submitTimeSlot.setEnabled(false);
                        timeSelectionNote.setText("Your time slots on this date is already set.");
                    }
                }
                System.out.println(eachLine);
                counter++;
            }
        } catch (FileNotFoundException ex) {
            try {
                new File("timeslot.txt").createNewFile();
            } catch (IOException ex1) {
            } finally {
                JOptionPane.showMessageDialog(null, "An error occured. Try again later.", "Database Failure", 0);
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "An error occured. Try again later.", "Database Failure", 0);
        }

    }
    
    private void verifyDateChosen() {
        dateSelectionCalendar.addPropertyChangeListener(new PropertyChangeListener() {
        
            public void propertyChange(PropertyChangeEvent event) {
                if ("calendar".equals(event.getPropertyName())) {
                    for (JCheckBox checkBox : timeSlots) {
                        checkBox.setSelected(false); //unselect when new date is selected
                    }
                    
                    //get selected date info
                    Date selectedDate = dateSelectionCalendar.getDate();

                    Calendar c = new GregorianCalendar();
                    c.setTime(selectedDate);
                    selectedYear = c.get(Calendar.YEAR);
                    selectedIntMonth = c.get(Calendar.MONTH)+1;
                    String selectedMonth = monthNames[selectedIntMonth];
                    selectedDay = c.get(Calendar.DATE);
                    
                    String consultDate = selectedDay+"-"+selectedMonth+"-"+selectedYear; //for display later
                    System.out.println(consultDate);
                    
                    //if selected month is more than 6 months away from current month
                    //if date is in the past
                    //disable time slot selection
                    if (currentYear + 1 == selectedYear) {
                        if (currentIntMonth + 6 - 12 >= selectedIntMonth) {
                            updatableSign = true;
                        }
                        else {
                            updatableSign = false;
                        }
                    } else if (currentYear == selectedYear) {
                        if (selectedIntMonth - currentIntMonth <= 6 && selectedIntMonth - currentIntMonth >= 0) {
                            updatableSign = true;
                        } else {
                            updatableSign = false;
                        }
                        if (selectedIntMonth == currentIntMonth && currentDay > selectedDay) {
                            updatableSign = false;
                        }
                    } else {
                        updatableSign = false;
                    }
                    
                    //see selected slot from text file
                    
                    //check updatableSign -> enable or disable checkbox ticking
                    if (updatableSign) {
                        selectedDateLabel.setText("Time slots for: "+consultDate);
                        submitTimeSlot.setEnabled(true);
                        timeSelectionNote.setText("Tick for the time slots that you are available.");
                        for (JCheckBox checkBox : timeSlots) {
                            checkBox.setEnabled(true);
                        }
                    }
                    else {
                        selectedDateLabel.setText(consultDate+" (Cannot set time slots)");
                        submitTimeSlot.setEnabled(false);
                        timeSelectionNote.setText("Time slots cannot be ticked.");
                        for (JCheckBox checkBox : timeSlots) {
                            checkBox.setEnabled(false);
                        }
                    }
                    
                    checkTimeSlots();
                }
            }
        });
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton1 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        timeSelectionScreen = new javax.swing.JPanel();
        selectedDateLabel = new javax.swing.JLabel();
        timeSelectionPanel = new javax.swing.JPanel();
        slot0830 = new javax.swing.JCheckBox();
        slot0900 = new javax.swing.JCheckBox();
        slot0930 = new javax.swing.JCheckBox();
        slot1000 = new javax.swing.JCheckBox();
        slot1030 = new javax.swing.JCheckBox();
        slot1100 = new javax.swing.JCheckBox();
        slot1130 = new javax.swing.JCheckBox();
        slot0130 = new javax.swing.JCheckBox();
        slot0200 = new javax.swing.JCheckBox();
        slot0230 = new javax.swing.JCheckBox();
        slot0300 = new javax.swing.JCheckBox();
        slot0330 = new javax.swing.JCheckBox();
        slot0400 = new javax.swing.JCheckBox();
        slot0430 = new javax.swing.JCheckBox();
        submitTimeSlot = new javax.swing.JButton();
        timeSelectionNote = new javax.swing.JLabel();
        dateSelectionCalendar = new com.toedter.calendar.JCalendar();
        jButton2 = new javax.swing.JButton();

        jButton1.setText("jButton1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 26)); // NOI18N
        jLabel1.setText("Set Consultation Schedule");

        timeSelectionScreen.setBackground(new java.awt.Color(204, 255, 255));

        selectedDateLabel.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        selectedDateLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        selectedDateLabel.setText("Pick a day");

        timeSelectionPanel.setOpaque(false);

        slot0830.setText("08:30 - 09:00");

        slot0900.setText("09:00 - 09:30");
        slot0900.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                slot0900ActionPerformed(evt);
            }
        });

        slot0930.setText("09:30 - 10:00");

        slot1000.setText("10:00 - 10:30");

        slot1030.setText("10:30 - 11:00");

        slot1100.setText("11:00 - 11:30");

        slot1130.setText("11:30 - 12:00");

        slot0130.setText("13:30 - 14:00");

        slot0200.setText("14:00 - 14:30");

        slot0230.setText("14:30 - 15:00");

        slot0300.setText("15:00 - 15:30");

        slot0330.setText("15:30 - 16:00");

        slot0400.setText("16:00 - 16:30");
        slot0400.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                slot0400ActionPerformed(evt);
            }
        });

        slot0430.setText("16:30 - 17:00");
        slot0430.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                slot0430ActionPerformed(evt);
            }
        });

        submitTimeSlot.setText("Submit");
        submitTimeSlot.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                submitTimeSlotActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout timeSelectionPanelLayout = new javax.swing.GroupLayout(timeSelectionPanel);
        timeSelectionPanel.setLayout(timeSelectionPanelLayout);
        timeSelectionPanelLayout.setHorizontalGroup(
            timeSelectionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(timeSelectionPanelLayout.createSequentialGroup()
                .addContainerGap(16, Short.MAX_VALUE)
                .addGroup(timeSelectionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(timeSelectionPanelLayout.createSequentialGroup()
                        .addComponent(slot0900, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(slot0200, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(timeSelectionPanelLayout.createSequentialGroup()
                        .addComponent(slot0830, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(slot0130, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(timeSelectionPanelLayout.createSequentialGroup()
                        .addGroup(timeSelectionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(slot0930, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(slot1000, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(slot1030, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(slot1100, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(slot1130, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(timeSelectionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(slot0230, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(slot0300, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(slot0330, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(slot0400, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(slot0430, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
            .addGroup(timeSelectionPanelLayout.createSequentialGroup()
                .addGap(148, 148, 148)
                .addComponent(submitTimeSlot, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        timeSelectionPanelLayout.setVerticalGroup(
            timeSelectionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(timeSelectionPanelLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(timeSelectionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(slot0830)
                    .addComponent(slot0130))
                .addGap(18, 18, 18)
                .addGroup(timeSelectionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(slot0900)
                    .addComponent(slot0200))
                .addGap(18, 18, 18)
                .addGroup(timeSelectionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(timeSelectionPanelLayout.createSequentialGroup()
                        .addComponent(slot0230)
                        .addGap(18, 18, 18)
                        .addComponent(slot0300)
                        .addGap(18, 18, 18)
                        .addComponent(slot0330)
                        .addGap(18, 18, 18)
                        .addComponent(slot0400)
                        .addGap(18, 18, 18)
                        .addComponent(slot0430))
                    .addGroup(timeSelectionPanelLayout.createSequentialGroup()
                        .addComponent(slot0930)
                        .addGap(18, 18, 18)
                        .addComponent(slot1000)
                        .addGap(18, 18, 18)
                        .addComponent(slot1030)
                        .addGap(18, 18, 18)
                        .addComponent(slot1100)
                        .addGap(18, 18, 18)
                        .addComponent(slot1130)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 20, Short.MAX_VALUE)
                .addComponent(submitTimeSlot)
                .addContainerGap())
        );

        timeSelectionNote.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        timeSelectionNote.setText("Tick for the time slot that you are available.");

        javax.swing.GroupLayout timeSelectionScreenLayout = new javax.swing.GroupLayout(timeSelectionScreen);
        timeSelectionScreen.setLayout(timeSelectionScreenLayout);
        timeSelectionScreenLayout.setHorizontalGroup(
            timeSelectionScreenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(timeSelectionScreenLayout.createSequentialGroup()
                .addGroup(timeSelectionScreenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(timeSelectionScreenLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(timeSelectionPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(timeSelectionScreenLayout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addGroup(timeSelectionScreenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(selectedDateLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 345, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(timeSelectionNote, javax.swing.GroupLayout.PREFERRED_SIZE, 335, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        timeSelectionScreenLayout.setVerticalGroup(
            timeSelectionScreenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(timeSelectionScreenLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(selectedDateLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, Short.MAX_VALUE)
                .addComponent(timeSelectionNote)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(timeSelectionPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jButton2.setText("BACK");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(20, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(dateSelectionCalendar, javax.swing.GroupLayout.PREFERRED_SIZE, 613, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 445, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(53, 53, 53)))
                .addComponent(timeSelectionScreen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(timeSelectionScreen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton2))
                        .addGap(37, 37, 37)
                        .addComponent(dateSelectionCalendar, javax.swing.GroupLayout.PREFERRED_SIZE, 318, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 33, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void submitTimeSlotActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_submitTimeSlotActionPerformed
        // TODO add your handling code here:
        JCheckBox[] timeSlots = {slot0830, slot0900, slot0930, slot1000, slot1030, slot1100, slot1130, slot0130, slot0200, slot0230, slot0300, slot0330, slot0400, slot0430};
        
        //needed to save with time
        String [] otherSlotInfo = new String[4];
        otherSlotInfo[0] = sessionUserID;
        otherSlotInfo[1] = String.valueOf(selectedYear);
        otherSlotInfo[2] = String.valueOf(selectedIntMonth);
        otherSlotInfo[3] = String.valueOf(selectedDay);
        System.out.println(selectedIntMonth);
        
        int confirmation = JOptionPane.showConfirmDialog(
            null, 
            "<html>Are you sure to save chosen time slots as available?<br>You cannot change them once saved and they will be displayed to students.</html>", 
            "Confirm Action", 
            JOptionPane.YES_NO_OPTION, 
            JOptionPane.QUESTION_MESSAGE
        );

        switch (confirmation) {
            case JOptionPane.YES_OPTION -> {
                boolean decision = false;
                for (JCheckBox slot : timeSlots) {
                    if (slot.isSelected()) {
                        decision = true;
                    }
                }

                if (!decision) {
                    JOptionPane.showMessageDialog(null, "<html>Since no time slots are selected, <br>you are still free to set consultations for this date in case you change your mind.</html>", "No Time Slot Selected", 1);
                }
                
                //decision = true means user did not misclick when all time slots are left empty
                if (decision) {
                    //write into file
                    try {
                        //save time slots with the date and lecturer ID
                        LecturerInteractTextFile setConsultationTime = new LecturerInteractTextFile();
                        if (setConsultationTime.saveTimeSlots(timeSlots, otherSlotInfo)) {
                            new SetConsultationTimePage(sessionUserID).setVisible(true);
                            this.dispose();
                        }
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(null, "Time slots failed to be saved. Try again later.", "Database Failure", 0);
                    }
                }
            }
                
            default -> {}
        }
        
        //nothing button for other buttons
    }//GEN-LAST:event_submitTimeSlotActionPerformed

    private void slot0400ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_slot0400ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_slot0400ActionPerformed

    private void slot0430ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_slot0430ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_slot0430ActionPerformed

    private void slot0900ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_slot0900ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_slot0900ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        new LecturerHomePage(sessionUserID).setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButton2ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(SetConsultationTimePage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SetConsultationTimePage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SetConsultationTimePage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SetConsultationTimePage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new SetConsultationTimePage("").setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.toedter.calendar.JCalendar dateSelectionCalendar;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel selectedDateLabel;
    private javax.swing.JCheckBox slot0130;
    private javax.swing.JCheckBox slot0200;
    private javax.swing.JCheckBox slot0230;
    private javax.swing.JCheckBox slot0300;
    private javax.swing.JCheckBox slot0330;
    private javax.swing.JCheckBox slot0400;
    private javax.swing.JCheckBox slot0430;
    private javax.swing.JCheckBox slot0830;
    private javax.swing.JCheckBox slot0900;
    private javax.swing.JCheckBox slot0930;
    private javax.swing.JCheckBox slot1000;
    private javax.swing.JCheckBox slot1030;
    private javax.swing.JCheckBox slot1100;
    private javax.swing.JCheckBox slot1130;
    private javax.swing.JButton submitTimeSlot;
    private javax.swing.JLabel timeSelectionNote;
    private javax.swing.JPanel timeSelectionPanel;
    private javax.swing.JPanel timeSelectionScreen;
    // End of variables declaration//GEN-END:variables

}
