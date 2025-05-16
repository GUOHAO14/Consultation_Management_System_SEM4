package javaassignment;
import javax.swing.*;
import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class view_appointment extends control {
    private String stu_id;
    private String ID;

    //All same student
    private String[] app_lec_name;
    private String[] app_id;
    private int[] app_st;
    private int[] app_et;
    private int[] year;
    private int[] month;
    private int[] day;
    private String[] acpt;
    
    //Upcoming
    private JButton[] btn;
    control upcoming = new control();
    private String[] u_app_id;
    private String[] u_app_lec_name;
    private int[] u_app_st;
    private int[] u_app_et;
    private String[] u_app_date;
    
    //Past Appointment
    private JButton[] btn1;
    control past = new control();
    private String[] p_app_id;
    private String[] p_app_lec_name;
    private int[] p_app_st;
    private int[] p_app_et;
    private String[] p_app_date;
    
    //Waiting
    private JButton[] btn2;
    control waiting = new control();
    private String[] w_app_id;
    private String[] w_app_lec_name;
    private int[] w_app_st;
    private int[] w_app_et;
    private String[] w_app_date;
    
    //Rejected
    private JButton[] btn3;
    control reject = new control();
    private String[] r_app_id;
    private String[] r_app_lec_name;
    private int[] r_app_st;
    private int[] r_app_et;
    private String[] r_app_date;
    
    private void passid(int index, String[] id, String time) throws IOException{
        if(time.equals("upcoming")){
            stu_upcoming_app gui = new stu_upcoming_app(id[index], "", ID);
            gui.setVisible(true);
            this.dispose();
        }else if(time.equals("past")){
            stu_past_app gui = new stu_past_app(id[index], ID);
            gui.setVisible(true);
            this.dispose();
        }else if(time.equals("reject")){
            stu_upcoming_app gui = new stu_upcoming_app(id[index], "reject", ID);
            gui.setVisible(true);
            this.dispose();
        }
        
    }
    public view_appointment(){
        
    }
    
    public view_appointment(String ID) throws IOException {
        initComponents();
        formatWindow("View Appointment Page");
        Calendar cl = Calendar.getInstance();
        this.ID = ID;
        stu_id = ID;
                
        //All student appointment
        ArrayList<Integer> index_app = new ArrayList<>();
        String[] app_stu_id = read_app("stu_id");
        for(int i = 0; i < app_stu_id.length; i++){
            if(app_stu_id[i].equals(stu_id)){
                index_app.add(i);
            }
        }
        app_id = new String[index_app.size()];
        String[] app_lec_id = new String[index_app.size()];
        String[] y1 = new String[index_app.size()];
        String[] m1 = new String[index_app.size()];
        String[] d1 = new String[index_app.size()];
        String[] app_s = new String[index_app.size()];
        String[] app_e = new String[index_app.size()];
        acpt = new String[index_app.size()];
        for(int i = 0; i < index_app.size(); i++){
            app_id[i] = read_app("app_id")[index_app.get(i)];
            app_lec_id[i] = read_app("lec_id")[index_app.get(i)];
            y1[i] = read_app("year")[index_app.get(i)];
            m1[i] = read_app("month")[index_app.get(i)];
            d1[i] = read_app("day")[index_app.get(i)];
            app_s[i] = read_app("start_time")[index_app.get(i)];
            app_e[i] = read_app("end_time")[index_app.get(i)];
            acpt[i] = read_app("acceptance")[index_app.get(i)];
        }
        
        year = to_int(y1);
        month = to_int(m1);
        day = to_int(d1);
        app_st = to_int(app_s);
        app_et = to_int(app_e);

        String[] lec_id = read_lec("lec_id");
        String[] lec_name = read_lec("username");
        
        app_lec_name = new String[app_lec_id.length];
        for(int i = 0; i < app_lec_id.length; i++){
            for(int j = 0; j < lec_id.length; j++){
                if(lec_id[j].equals(app_lec_id[i])){
                    app_lec_name[i] = lec_name[j];
                }
            }
        }
        
        int c_time = Integer.parseInt(String.valueOf(cl.get(Calendar.HOUR_OF_DAY)) + String.valueOf(cl.get(Calendar.MINUTE)));
        int c_date = cl.get(Calendar.YEAR) * 10000 + (cl.get(Calendar.MONTH) + 1) * 100 + cl.get(Calendar.DAY_OF_MONTH);
        long c_datetime = ((long) c_date * 10000) + c_time;
        
        ArrayList<Integer> u_index = new ArrayList<>();
        ArrayList<Integer> p_index = new ArrayList<>();
        ArrayList<Integer> w_index = new ArrayList<>();
        ArrayList<Integer> r_index = new ArrayList<>();
        for(int i = 0; i < app_id.length; i++){
            int date = year[i] * 10000 + month[i] * 100 + day[i];
            long datetimeS = ((long) date * 10000) + app_st[i];
            long datetimeE = ((long) date * 10000) + app_et[i];
            
            if(acpt[i].equals("accept") || acpt[i].equals("null")){
                //Upcoming
                if((c_datetime <= datetimeS && c_datetime <= datetimeE) || (c_datetime >= datetimeS && c_datetime <= datetimeE)){
                    u_index.add(i);
                //Past appointment
                }else if(c_datetime >= datetimeS && c_datetime >= datetimeE){
                    p_index.add(i);
                }
                if(acpt[i].equals("accept")){
                    JOptionPane.showMessageDialog(null, "Your appointment reschedule is accepted", "Reschedule Accepted", JOptionPane.INFORMATION_MESSAGE);
                    update(app_id[i], "null", "acceptance");
                }
                
            }else if(acpt[i].equals("waiting")){
                if((c_datetime <= datetimeS && c_datetime <= datetimeE) || (c_datetime >= datetimeS && c_datetime <= datetimeE)){
                    w_index.add(i);
                //If wait until overtime, auto delete
                }else if(c_datetime >= datetimeS && c_datetime >= datetimeE){
                    cancel_app(app_id[i]);
                }
            }else if(acpt[i].equals("reject")){
                if((c_datetime <= datetimeS && c_datetime <= datetimeE) || (c_datetime >= datetimeS && c_datetime <= datetimeE)){
                    r_index.add(i);
                //If overtime, auto delete
                }else if(c_datetime >= datetimeS && c_datetime >= datetimeE){
                    cancel_app(app_id[i]);
                }
            }
        }
        
        //Upcoming
        JButton[] buttons = new JButton[]{jButton1, jButton2, jButton3, jButton4};
        u_app_id = new String[u_index.size()];
        u_app_lec_name = new String[u_index.size()];
        u_app_st = new int[u_index.size()];
        u_app_et = new int[u_index.size()];
        u_app_date = new String[u_index.size()];
        for(int i = 0; i < u_index.size(); i++){
            u_app_id[i] = app_id[u_index.get(i)];
            u_app_lec_name[i] = app_lec_name[u_index.get(i)];
            u_app_st[i] = app_st[u_index.get(i)];
            u_app_et[i] = app_et[u_index.get(i)];
            u_app_date[i] = (year[u_index.get(i)] + "-" + month[u_index.get(i)] + "-" + day[u_index.get(i)]);
        }
        if(u_app_id.length == 0){
            jLabel3.setText("No Upcoming Appointment");
            for(JButton button : buttons){
                remove(button);
            }
            right.setVisible(false);
            left.setVisible(false);
        }else{
            int num = checkbtn(u_app_lec_name, buttons);
            btn = new JButton[num];
            for(int i = 0; i < num; i++){
                btn[i] = buttons[i];
            }

            initial(u_app_lec_name, btn, left, right);
            upcoming.settext2(u_app_lec_name,u_app_date, u_app_st, u_app_et, btn);
        }
        
        //Past Appointment
        JButton[] buttons1 = new JButton[]{jButton5, jButton6, jButton7, jButton8};
        p_app_id = new String[p_index.size()];
        p_app_lec_name = new String[p_index.size()];
        p_app_st = new int[p_index.size()];
        p_app_et = new int[p_index.size()];
        p_app_date = new String[p_index.size()];
        for(int i = 0; i < p_index.size(); i++){
            p_app_id[i] = app_id[p_index.get(i)];
            p_app_lec_name[i] = app_lec_name[p_index.get(i)];
            p_app_st[i] = app_st[p_index.get(i)];
            p_app_et[i] = app_et[p_index.get(i)];
            p_app_date[i] = (year[p_index.get(i)] + "-" + month[p_index.get(i)] + "-" + day[p_index.get(i)]);
        }
        if(p_app_id.length == 0){
            jLabel4.setText("No Past Appointment");
            for(JButton button : buttons1){
                remove(button);
            }
            right1.setVisible(false);
            left1.setVisible(false);
        }else{
            int num1 = checkbtn(p_app_lec_name, buttons1);
            btn1 = new JButton[num1];
            for(int i = 0; i < num1; i++){
                btn1[i] = buttons1[i];
            }

            initial(p_app_lec_name, btn1, left1, right1);
            past.settext2(p_app_lec_name,p_app_date, p_app_st, p_app_et, btn1);
        }
        
        //Waiting
        JButton[] buttons2 = new JButton[]{jButton9, jButton10};
        w_app_id = new String[w_index.size()];
        w_app_lec_name = new String[w_index.size()];
        w_app_st = new int[w_index.size()];
        w_app_et = new int[w_index.size()];
        w_app_date = new String[w_index.size()];
        for(int i = 0; i < w_index.size(); i++){
            w_app_id[i] = app_id[w_index.get(i)];
            w_app_lec_name[i] = app_lec_name[w_index.get(i)];
            w_app_st[i] = app_st[w_index.get(i)];
            w_app_et[i] = app_et[w_index.get(i)];
            w_app_date[i] = (year[w_index.get(i)] + "-" + month[w_index.get(i)] + "-" + day[w_index.get(i)]);
        }
        if(w_app_id.length == 0){
            jLabel6.setText("");
            for(JButton button : buttons2){
                remove(button);
            }
            right2.setVisible(false);
            left2.setVisible(false);
        }else{
            int num2 = checkbtn(w_app_lec_name, buttons2);
            btn2 = new JButton[num2];
            for(int i = 0; i < num2; i++){
                btn2[i] = buttons2[i];
            }

            initial(w_app_lec_name, btn2, left2, right2);
            waiting.settext2(w_app_lec_name,w_app_date, w_app_st, w_app_et, btn2);
        }
        
        //Rejected
        JButton[] buttons3 = new JButton[]{jButton11, jButton12};
        r_app_id = new String[r_index.size()];
        r_app_lec_name = new String[r_index.size()];
        r_app_st = new int[r_index.size()];
        r_app_et = new int[r_index.size()];
        r_app_date = new String[r_index.size()];
        for(int i = 0; i < r_index.size(); i++){
            r_app_id[i] = app_id[r_index.get(i)];
            r_app_lec_name[i] = app_lec_name[r_index.get(i)];
            r_app_st[i] = app_st[r_index.get(i)];
            r_app_et[i] = app_et[r_index.get(i)];
            r_app_date[i] = (year[r_index.get(i)] + "-" + month[r_index.get(i)] + "-" + day[r_index.get(i)]);
        }
        if(r_app_id.length == 0){
            jLabel7.setText("");
            for(JButton button : buttons3){
                remove(button);
            }
            right3.setVisible(false);
            left3.setVisible(false);
        }else{
            int num3 = checkbtn(r_app_lec_name, buttons3);
            btn3 = new JButton[num3];
            for(int i = 0; i < num3; i++){
                btn3[i] = buttons3[i];
            }

            initial(r_app_lec_name, btn3, left3, right3);
            reject.settext2(r_app_lec_name,r_app_date, r_app_st, r_app_et, btn3);
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

        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        makeapp = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        right = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        left = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        back = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        right1 = new javax.swing.JButton();
        left1 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jButton10 = new javax.swing.JButton();
        right2 = new javax.swing.JButton();
        left2 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        jButton12 = new javax.swing.JButton();
        right3 = new javax.swing.JButton();
        left3 = new javax.swing.JButton();
        jButton11 = new javax.swing.JButton();

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel3.setText("Upcoming");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel4.setText("Past Appoinment");

        makeapp.setText("Make New Appointment");
        makeapp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                makeappActionPerformed(evt);
            }
        });

        jButton2.setText("jButton2");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        right.setBackground(new java.awt.Color(153, 153, 153));
        right.setText(">");
        right.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rightActionPerformed(evt);
            }
        });

        jButton3.setText("jButton3");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setText("jButton4");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        left.setBackground(new java.awt.Color(153, 153, 153));
        left.setText("<");
        left.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                leftActionPerformed(evt);
            }
        });

        jButton1.setText("jButton1");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        back.setText("BACK");
        back.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backActionPerformed(evt);
            }
        });

        jButton6.setText("jButton2");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jButton7.setText("jButton3");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jButton8.setText("jButton4");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        right1.setBackground(new java.awt.Color(153, 153, 153));
        right1.setText(">");
        right1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                right1ActionPerformed(evt);
            }
        });

        left1.setBackground(new java.awt.Color(153, 153, 153));
        left1.setText("<");
        left1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                left1ActionPerformed(evt);
            }
        });

        jButton5.setText("jButton1");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 22)); // NOI18N
        jLabel6.setText("Waiting");

        jButton10.setText("jButton2");
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        right2.setBackground(new java.awt.Color(153, 153, 153));
        right2.setText(">");
        right2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                right2ActionPerformed(evt);
            }
        });

        left2.setBackground(new java.awt.Color(153, 153, 153));
        left2.setText("<");
        left2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                left2ActionPerformed(evt);
            }
        });

        jButton9.setText("jButton1");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 22)); // NOI18N
        jLabel7.setText("Rejected");

        jButton12.setText("jButton2");
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });

        right3.setBackground(new java.awt.Color(153, 153, 153));
        right3.setText(">");
        right3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                right3ActionPerformed(evt);
            }
        });

        left3.setBackground(new java.awt.Color(153, 153, 153));
        left3.setText("<");
        left3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                left3ActionPerformed(evt);
            }
        });

        jButton11.setText("jButton1");
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(back)
                    .addComponent(jLabel4)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                            .addComponent(jLabel3)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(makeapp))
                        .addGroup(layout.createSequentialGroup()
                            .addGap(26, 26, 26)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(left)
                                    .addGap(18, 18, 18)
                                    .addComponent(jButton1)
                                    .addGap(18, 18, 18)
                                    .addComponent(jButton2)
                                    .addGap(18, 18, 18)
                                    .addComponent(jButton3)
                                    .addGap(18, 18, 18)
                                    .addComponent(jButton4)
                                    .addGap(18, 18, 18)
                                    .addComponent(right))
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(left1)
                                    .addGap(18, 18, 18)
                                    .addComponent(jButton5)
                                    .addGap(18, 18, 18)
                                    .addComponent(jButton6)
                                    .addGap(18, 18, 18)
                                    .addComponent(jButton7)
                                    .addGap(18, 18, 18)
                                    .addComponent(jButton8)
                                    .addGap(18, 18, 18)
                                    .addComponent(right1)))))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(left2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton9)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton10)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(right2)))
                        .addGap(36, 36, 36)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(left3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton11)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton12)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(right3)))))
                .addContainerGap(80, Short.MAX_VALUE))
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jButton1, jButton2, jButton3, jButton4});

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jButton5, jButton6, jButton7, jButton8});

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jButton10, jButton9});

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jButton11, jButton12});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(back)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jLabel3)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(right)
                            .addComponent(left))
                        .addGap(18, 18, 18)
                        .addComponent(jLabel4)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(right1)
                            .addComponent(left1)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(makeapp)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 27, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(right2)
                            .addComponent(left2)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(jButton11, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton12, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(right3)
                            .addComponent(left3))))
                .addGap(81, 81, 81))
        );

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jButton1, jButton2, jButton3, jButton4});

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jButton5, jButton6, jButton7, jButton8});

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jButton10, jButton9});

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jButton11, jButton12});

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        try {
            passid(upcoming.id()+1, u_app_id, "upcoming");
        } catch (IOException ex) {
            Logger.getLogger(view_appointment.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        try {
            passid(upcoming.id()+2, u_app_id, "upcoming");
        } catch (IOException ex) {
            Logger.getLogger(view_appointment.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        try {
            passid(upcoming.id()+3, u_app_id, "upcoming");
        } catch (IOException ex) {
            Logger.getLogger(view_appointment.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void rightActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rightActionPerformed
        upcoming.rightclick(u_app_lec_name, btn, left, right);
        upcoming.settext2(u_app_lec_name,u_app_date, u_app_st, u_app_et, btn);
    }//GEN-LAST:event_rightActionPerformed

    private void leftActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_leftActionPerformed
        upcoming.leftclick(u_app_lec_name, btn, left, right);
        upcoming.settext2(u_app_lec_name,u_app_date, u_app_st, u_app_et, btn);
    }//GEN-LAST:event_leftActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        try {
            passid(upcoming.id(), u_app_id, "upcoming");
        } catch (IOException ex) {
            Logger.getLogger(view_appointment.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void backActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backActionPerformed
        stu_home gui = new stu_home(ID);
        gui.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_backActionPerformed

    private void makeappActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_makeappActionPerformed
        try {
            view_lecture gui = new view_lecture(ID);
            gui.setVisible(true);
            this.dispose();
        } catch (IOException ex) {
            Logger.getLogger(view_appointment.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_makeappActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        try {
            passid(past.id()+1, p_app_id, "past");
        } catch (IOException ex) {
            Logger.getLogger(view_appointment.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        try {
            passid(past.id()+2, p_app_id, "past");
        } catch (IOException ex) {
            Logger.getLogger(view_appointment.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        try {
            passid(past.id()+3, p_app_id, "past");
        } catch (IOException ex) {
            Logger.getLogger(view_appointment.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton8ActionPerformed

    private void right1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_right1ActionPerformed
        past.rightclick(p_app_lec_name, btn1, left1, right1);
        past.settext2(p_app_lec_name,p_app_date, p_app_st, p_app_et, btn1);
    }//GEN-LAST:event_right1ActionPerformed

    private void left1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_left1ActionPerformed
        past.leftclick(p_app_lec_name, btn1, left1, right1);
        past.settext2(p_app_lec_name,p_app_date, p_app_st, p_app_et, btn1);
    }//GEN-LAST:event_left1ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        try {
            passid(past.id(), p_app_id, "past");
        } catch (IOException ex) {
            Logger.getLogger(view_appointment.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        try {
            passid(waiting.id()+1, w_app_id, "upcoming");
        } catch (IOException ex) {
            Logger.getLogger(view_appointment.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton10ActionPerformed

    private void right2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_right2ActionPerformed
        waiting.rightclick(w_app_lec_name, btn2, left2, right2);
        waiting.settext2(w_app_lec_name,w_app_date, w_app_st, w_app_et, btn2);
    }//GEN-LAST:event_right2ActionPerformed

    private void left2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_left2ActionPerformed
        waiting.leftclick(w_app_lec_name, btn2, left2, right2);
        waiting.settext2(w_app_lec_name,w_app_date, w_app_st, w_app_et, btn2);
    }//GEN-LAST:event_left2ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        try {
            passid(waiting.id(), w_app_id, "upcoming");
        } catch (IOException ex) {
            Logger.getLogger(view_appointment.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        try {
            passid(reject.id()+1, r_app_id, "reject");
        } catch (IOException ex) {
            Logger.getLogger(view_appointment.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton12ActionPerformed

    private void right3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_right3ActionPerformed
        reject.rightclick(r_app_lec_name, btn3, left3, right3);
        reject.settext2(r_app_lec_name,r_app_date, r_app_st, r_app_et, btn3);
    }//GEN-LAST:event_right3ActionPerformed

    private void left3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_left3ActionPerformed
        reject.leftclick(r_app_lec_name, btn3, left3, right3);
        reject.settext2(r_app_lec_name,r_app_date, r_app_st, r_app_et, btn3);
    }//GEN-LAST:event_left3ActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        try {
            passid(reject.id(), r_app_id, "reject");
        } catch (IOException ex) {
            Logger.getLogger(view_appointment.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton11ActionPerformed

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
            java.util.logging.Logger.getLogger(view_appointment.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(view_appointment.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(view_appointment.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(view_appointment.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new view_appointment().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton back;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JButton left;
    private javax.swing.JButton left1;
    private javax.swing.JButton left2;
    private javax.swing.JButton left3;
    private javax.swing.JButton makeapp;
    private javax.swing.JButton right;
    private javax.swing.JButton right1;
    private javax.swing.JButton right2;
    private javax.swing.JButton right3;
    // End of variables declaration//GEN-END:variables
}
