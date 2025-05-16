package javaassignment;

import javax.swing.JOptionPane;

/**
 *
 * @author Khoo Guo Hao
 */
public class UserInputValidationException extends Exception {
    private String text, fieldValue;
    
    public UserInputValidationException(String text, String paneTitle) {
        JOptionPane.showMessageDialog(null, text, paneTitle, 0);
    }
    
    public UserInputValidationException(String paneTitle, int length) {
        if (length <= 0)
            JOptionPane.showMessageDialog(null, "Don't leave any fields empty!", paneTitle, 0);
        else if (length >= 91)
            JOptionPane.showMessageDialog(null, "Value entered has reached word limit!", paneTitle, 0);
    }
}


class UserRepeatRegisterException extends UserInputValidationException {
    public UserRepeatRegisterException(String repeatingField) {
        super("The user with ("+repeatingField+") already has an account. Identity theft is illegal.", "Account Conflict Error");
    }
}


class WrongIDFormatException extends UserInputValidationException {
    public WrongIDFormatException() {
        super("Invalid ID.", "Not Existent ID");
    }
}


class WrongPasswordFormatException extends UserInputValidationException {
    public WrongPasswordFormatException() {
        super("Password must be in the correct format, as stated in \"i\".", "Password Format Error");
    }
}


class InputLengthException extends UserInputValidationException {
    public InputLengthException() {
        super("Fill up both ID and password field before submitting!", "Empty Field Error");
    }
    public InputLengthException(int length) {
        super("Empty Field Error", length);
    }
}


class WrongEmailFormatException extends UserInputValidationException {
    public WrongEmailFormatException() {
        super("Invalid email.", "Email Format Error");
    }
}
