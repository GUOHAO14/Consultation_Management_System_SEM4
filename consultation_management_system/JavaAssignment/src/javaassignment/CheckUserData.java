package javaassignment;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author Khoo Guo Hao
 */
public class CheckUserData {
    private String ID, name, password, email, intakeOrJobTitle, intake;
    
    CheckUserData(String ID, String password) {
        this.ID = ID;
        this.password = password;
    }
     
    CheckUserData(String ID, String name, String password, String email, String intakeOrJobTitle) {
        this.ID = ID;
        this.name = name;
        this.password = password;
        this.email = email;
        this.intakeOrJobTitle = intakeOrJobTitle;
    }
    
    public boolean CheckMatchingLoginData() throws IOException {
        boolean loginStatus = false;
        try {
            BufferedReader userBr = new BufferedReader(new FileReader("user.txt"));
            String eachline;
            int counter = 0;
            while ((eachline = userBr.readLine()) != null) {
                String [] userDetails = eachline.split(",");
                if (userDetails[0].equals(ID) && userDetails[2].equals(password)) {
                    loginStatus = true;
                }
                counter++;
            }
            if (!loginStatus) {
                throw new UserInputValidationException("Incorrect credentials. Register an account if you haven't.", "Invalid Credentials");
            }
        } catch (UserInputValidationException e) {
            loginStatus = false;
        }
        return loginStatus;
    }
    
    //method overloading --> for login page
    public boolean CheckForEmptyData() {
        boolean errorStatus = true;
        try {
            if (ID.isEmpty() || password.isEmpty()) {
                throw new InputLengthException();
            }
        } catch (UserInputValidationException e) {
            errorStatus = false; 
        }
        
        return errorStatus;
    }
    
    
    //method overloading --> for register page
    public boolean CheckDataLength() {
        String [] userDataList = {ID, name, password, email, intakeOrJobTitle};
        boolean errorStatus = true;
        try {
            for (String data : userDataList) {
                int dataLen = data.length();
                //check for empty or extremely long string (spam/troll)
                if (dataLen <= 0 || dataLen > 50) {
                    throw new InputLengthException(dataLen);
                }
            }
        } catch (UserInputValidationException e) {
            errorStatus = false;
        }
        return errorStatus;
    }
    
    
    public boolean CheckRegisterDataConflict() throws IOException {
        boolean errorStatus;
        try {
            BufferedReader userBr = new BufferedReader(new FileReader("user.txt"));
            String eachline;
            int counter = 0;
            while ((eachline = userBr.readLine()) != null) {
                String [] userDetails = eachline.split(",");
                if (userDetails[0].equals(ID)) {
                    System.out.println(userDetails[0]);
                    throw new UserRepeatRegisterException(ID);
                }
                if (userDetails[3].equals(email)) {
                    System.out.println(userDetails[3]);
                    throw new UserRepeatRegisterException(email);
                }
                counter++;
            }
            errorStatus = true;
        } catch (UserRepeatRegisterException e) {
            errorStatus = false;
        } 
        return errorStatus;
    }
    
    
    public boolean CheckIDFormat(String role) {
        boolean errorStatus = true;
        try {
            if ((role.equals("student") && !ID.matches("^TP[0-9]{6}$")) || (role.equals("lecturer") && !ID.matches("^LC[0-9]{6}$")))
                throw new WrongIDFormatException();
        } catch (UserInputValidationException e) {
           errorStatus = false;
        }
        return errorStatus;
    }
    
    
    public boolean CheckPasswordFormat() {
        boolean errorStatus = true;
        try {
            if (password.contains(" ")) {
                throw new WrongPasswordFormatException();
            }
            else if (!password.matches(".*[a-z].*") || !password.matches(".*[A-Z].*") || !password.matches(".*\\d.*") || !password.matches(".*[\\W_].*")) {
                throw new WrongPasswordFormatException();
            }
        } catch (UserInputValidationException e) {
            errorStatus = false;
        }
        return errorStatus;
    }
    
    
    public boolean CheckEmailFormat() {
        boolean errorStatus = true;
        try {
            if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"))
                throw new WrongEmailFormatException();
        } catch (UserInputValidationException e) {
           errorStatus = false;
        }
        return errorStatus;
    }
}


    