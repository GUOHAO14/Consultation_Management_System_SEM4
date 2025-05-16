package javaassignment;
import javax.swing.*;
import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.awt.event.*;


public class stu_lecture extends control {
    private String ID;
    
    private String lec_id;
    private String operation;
    private String app_id;
    private String stu_id;

    private int[] year;
    private int[] month;
    private int[] day;
    private int[] at_start;
    private int[] at_end;
    
    private int c_year;
    private int c_month;
    private int c_day;
    
    private int t_year;
    private int t_month;
    private int t_day;
    
    public stu_lecture(){
        
    }

    public stu_lecture(String lec_id, String operation, String app_id, String ID) throws IOException{
        initComponents();
        formatWindow("Lecture Page");
        this.ID = ID;
        this.stu_id = ID;
        this.lec_id = lec_id;
        this.operation = operation;
        this.app_id = app_id;
        
        if(operation.equals("reschedule")){
            back.setText("EXIT");
            jButton1.setText("Reschedule");
        }else if(operation.equals("book")){
            back.setText("BACK");
            jButton1.setText("Make Appointment");
        }
        
        String [] id_list = read_lec("lec_id");
        int index_lec = 0;
        for(int i = 0; i < id_list.length; i++){
            if(id_list[i].equals(lec_id)){
                index_lec = i;
            }
        }
        String [] name_list = read_lec("username");
        String name = name_list[index_lec];
        jLabel2.setText(name);

        String[] lec_id_ts = read_AT("lec_id");
        String[] status = read_AT("status");
        ArrayList<Integer> index_AT = new ArrayList<>();
        for(int i = 0; i < lec_id_ts.length; i++){
            if(lec_id_ts[i].equals(lec_id) && status[i].equals("unbooked")){
                index_AT.add(i);
            }
        }

        String[] y1 = new String[index_AT.size()];
        String[] m1 = new String[index_AT.size()];
        String[] d1 = new String[index_AT.size()];
        String[] at_s = new String[index_AT.size()];
        String[] at_e = new String[index_AT.size()];
        for(int i = 0; i < index_AT.size(); i++){
            y1[i] = read_AT("year")[index_AT.get(i)];
            m1[i] = read_AT("month")[index_AT.get(i)];
            d1[i] = read_AT("day")[index_AT.get(i)];
            at_s[i] = read_AT("at_start")[index_AT.get(i)];
            at_e[i] = read_AT("at_end")[index_AT.get(i)];
        }
        year = to_int(y1);
        month = to_int(m1);
        day = to_int(d1);
        at_start = to_int(at_s);
        at_end = to_int(at_e);

        
        Calendar cl = Calendar.getInstance();
        int t_time = Integer.parseInt(String.valueOf(cl.get(Calendar.HOUR_OF_DAY)) + String.valueOf(cl.get(Calendar.MINUTE)));
        t_year = cl.get(Calendar.YEAR);
        t_month = cl.get(Calendar.MONTH) + 1;
        t_day = cl.get(Calendar.DAY_OF_MONTH);
        
        jComboBox1.removeAllItems();
        for(int i = t_year; i <= 2030; i++){
            jComboBox1.addItem(String.valueOf(i));
        }
        
        jComboBox2.removeAllItems();
        if(Integer.parseInt((String) jComboBox1.getSelectedItem()) == t_year){
            for(int i = t_month; i <= 12; i++){
                jComboBox2.addItem(String.valueOf(i));
            }
        }else{
            for(int i = 1; i <= 12; i++){
                jComboBox2.addItem(String.valueOf(i));
            }
        }
        
        jComboBox1.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    jComboBox2.removeAllItems();
                    if (Integer.parseInt((String) jComboBox1.getSelectedItem()) == t_year) {
                        for (int i = t_month; i <= 12; i++) {
                            jComboBox2.addItem(String.valueOf(i));
                        }
                    } else {
                        for (int i = 1; i <= 12; i++) {
                            jComboBox2.addItem(String.valueOf(i));
                        }
                    }

                    if (jComboBox2.getItemCount() > 0) {
                        jComboBox2.setSelectedIndex(0);
                    }

                    changetime("year");
                    changetime("month");
                }
            }
        });
        
        jComboBox2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (jComboBox2.getSelectedItem() != null) {
                    changetime("month");
                }
            }
        });
        
        jComboBox3.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    changetime("day");
                }
            }
        });

        c_year = t_year;
        c_month = t_month;
        c_day = t_day;
        
        jComboBox1.setSelectedItem(String.valueOf(t_year));
        jComboBox2.setSelectedItem(String.valueOf(t_month));
        jComboBox3.setSelectedItem(String.valueOf(t_day));
        
        //Review
        String[] app_lec = read_app("lec_id");
        ArrayList<Integer> index_app = new ArrayList<>();
        for(int i = 0; i < app_lec.length; i++){
            if(app_lec[i].equals(lec_id)){
                index_app.add(i);
            }
        }
        
        String[] review = new String[index_app.size()];
        for(int i = 0; i < index_app.size(); i++){
            review[i] = read_app("review")[index_app.get(i)];
        }
        
        if(review.length == 0){
            jTextArea1.setText("No Review");
        }else{
            for(int i = 0; i < review.length;i++){
                if(!review[i].equals("null")){
                    jTextArea1.append(review[i] + "\n");
                }
            }
        }
    }
    
    public void changetime(String selection){
        if(!selection.equals("make app")){
            switch (selection) {
                case "year" -> {c_year = Integer.parseInt((String) jComboBox1.getSelectedItem());}
                case "month" -> c_month = Integer.parseInt((String) jComboBox2.getSelectedItem());
                case "day" -> c_day = Integer.parseInt((String) jComboBox3.getSelectedItem());
            }
        }
        
        if (selection.equals("month")) {
            String c_month = (String) jComboBox2.getSelectedItem();
            jComboBox3.removeAllItems();
            int days = switch (c_month) {
                case "2" -> 28;
                case "4", "6", "9", "11" -> 30;
                default -> 31;
            };

            if (c_year == t_year && Integer.parseInt(c_month) == t_month) {
                for (int i = t_day; i <= days; i++) {
                    jComboBox3.addItem(String.valueOf(i));
                }
            } else {
                for (int i = 1; i <= days; i++) {
                    jComboBox3.addItem(String.valueOf(i));
                }
            }

            if (jComboBox3.getItemCount() > 0) {
                jComboBox3.setSelectedIndex(0);
            }
        }

        ArrayList<Integer> c_at_start = new ArrayList<>();
        ArrayList<Integer> c_at_end = new ArrayList<>();
        for(int i =0; i < year.length; i++){
            if(c_year == year[i] && c_month == month[i] && c_day == day[i]){
                c_at_start.add(at_start[i]);
                c_at_end.add(at_end[i]);
            }
        }

        Collections.sort(c_at_start);
        Collections.sort(c_at_end);
        
        jTextArea2.setText("");
        for(int i = 0; i < c_at_start.size(); i++){
            jTextArea2.append("| " + c_at_start.get(i) + " - " + c_at_end.get(i) + " |");
        }
        
        jComboBox5.removeAllItems();
        for(int i = 0; i < c_at_start.size(); i++){
            jComboBox5.addItem(c_at_start.get(i) + " - " + c_at_end.get(i));
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

        back = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea2 = new javax.swing.JTextArea();
        jComboBox2 = new javax.swing.JComboBox<>();
        jComboBox3 = new javax.swing.JComboBox<>();
        jComboBox1 = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jComboBox5 = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        back.setText("BACK");
        back.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 26)); // NOI18N
        jLabel2.setText("Lecture Name");

        jTextArea1.setEditable(false);
        jTextArea1.setBackground(new java.awt.Color(204, 204, 204));
        jTextArea1.setColumns(20);
        jTextArea1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel4.setText("Reviews");

        jTextArea2.setEditable(false);
        jTextArea2.setBackground(new java.awt.Color(204, 204, 204));
        jTextArea2.setColumns(20);
        jTextArea2.setFont(new java.awt.Font("Segoe UI Semibold", 1, 14)); // NOI18N
        jTextArea2.setRows(1);
        jTextArea2.setTabSize(5);
        jScrollPane2.setViewportView(jTextArea2);

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox2ActionPerformed(evt);
            }
        });

        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox3.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox3ItemStateChanged(evt);
            }
        });
        jComboBox3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox3ActionPerformed(evt);
            }
        });

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox1ItemStateChanged(evt);
            }
        });
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        jButton1.setText("Make Appointment");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel1.setText("Choose Time:");

        jComboBox5.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(back)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel2)
                        .addGap(129, 129, 129))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(9, 9, 9)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jComboBox5, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(34, 34, 34)
                                .addComponent(jButton1))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 358, Short.MAX_VALUE)
                            .addComponent(jLabel4)
                            .addComponent(jScrollPane1))
                        .addContainerGap(35, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(back))
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jLabel1)
                    .addComponent(jComboBox5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(19, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void backActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backActionPerformed
        try {
            if(operation.equals("reschedule")){
                stu_upcoming_app gui = new stu_upcoming_app(app_id, "", ID);
                gui.setVisible(true);
                this.dispose();
            }else if(operation.equals("book")){
                view_lecture gui = new view_lecture(ID);
                gui.setVisible(true);
                this.dispose();
            }
        } catch (IOException ex) {
            Logger.getLogger(stu_lecture.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_backActionPerformed

    private void jComboBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox2ActionPerformed

    }//GEN-LAST:event_jComboBox2ActionPerformed

    private void jComboBox3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox3ActionPerformed
    }//GEN-LAST:event_jComboBox3ActionPerformed

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jComboBox3ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox3ItemStateChanged
        
    }//GEN-LAST:event_jComboBox3ItemStateChanged

    private void jComboBox1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox1ItemStateChanged
        
    }//GEN-LAST:event_jComboBox1ItemStateChanged

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
    if(operation.equals("book")){
        try {
            String c_t = (String) jComboBox5.getSelectedItem();
            if(c_t == null){
                JOptionPane.showMessageDialog(null, "No available time for that day. Please choose again!", "No available time", JOptionPane.INFORMATION_MESSAGE);
            }else{
                String[] c_time = c_t.split(" - ");
                int c_start = Integer.parseInt(c_time[0]);
                int c_end = Integer.parseInt(c_time[1]);

                c_year = Integer.parseInt((String) jComboBox1.getSelectedItem());
                c_month = Integer.parseInt((String) jComboBox2.getSelectedItem());
                c_day = Integer.parseInt((String) jComboBox3.getSelectedItem());

                int result = JOptionPane.showConfirmDialog(null, "Do you sure you want to make this appointment?", "Confirm Appointment Booking",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

                if (result == JOptionPane.YES_OPTION) {
                    insert_app("", stu_id, lec_id, c_year, c_month, c_day, c_start, c_end, "book");
                    JOptionPane.showMessageDialog(null, "Appointment made successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    this.dispose();
                    stu_lecture gui = new stu_lecture(lec_id, "book", null, ID);
                    gui.setVisible(true);
                } else if (result == JOptionPane.NO_OPTION) {

                }
            }
        } catch (IOException ex) {
            Logger.getLogger(stu_lecture.class.getName()).log(Level.SEVERE, null, ex);
        }
    }else if(operation.equals("reschedule")){
        try {
            String c_t = (String) jComboBox5.getSelectedItem();
            if(c_t == null){
                JOptionPane.showMessageDialog(null, "No available time for that day. Please choose again!", "No available time", JOptionPane.INFORMATION_MESSAGE);
            }else{
                String[] c_time = c_t.split(" - ");
                int c_start = Integer.parseInt(c_time[0]);
                int c_end = Integer.parseInt(c_time[1]);

                c_year = Integer.parseInt((String) jComboBox1.getSelectedItem());
                c_month = Integer.parseInt((String) jComboBox2.getSelectedItem());
                c_day = Integer.parseInt((String) jComboBox3.getSelectedItem());

                int result = JOptionPane.showConfirmDialog(null, "Do you sure you want to reschedule this appointment?", "Confirm Appointment Reschedule",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

                if (result == JOptionPane.YES_OPTION) {
                    cancel_app(app_id);
                    insert_app(app_id, stu_id, lec_id, c_year, c_month, c_day, c_start, c_end, "reschedule");
                    JOptionPane.showMessageDialog(null, "Appointment reschedule successfully. Waiting for lecture to accept!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    this.dispose();
                    view_appointment gui = new view_appointment(ID);
                    gui.setVisible(true);
                } else if (result == JOptionPane.NO_OPTION) {

                }
            }
        } catch (IOException ex) {
            Logger.getLogger(stu_lecture.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
        
        
    }//GEN-LAST:event_jButton1ActionPerformed

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
            java.util.logging.Logger.getLogger(stu_lecture.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(stu_lecture.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(stu_lecture.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(stu_lecture.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new stu_lecture().setVisible(true);
            }
        });
        
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton back;
    private javax.swing.JButton jButton1;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JComboBox<String> jComboBox3;
    private javax.swing.JComboBox<String> jComboBox5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextArea jTextArea2;
    // End of variables declaration//GEN-END:variables
}
