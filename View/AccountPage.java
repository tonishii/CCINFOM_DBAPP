package View;

import javax.swing.*;
import java.awt.event.ActionListener;

public interface AccountPage {
    // Constants used for switching between the main account page and the account's signup page
    String MAINPAGE = "main";
    String SIGNUPPAGE = "signup";

    JPanel getSignUpPage();
    JPanel getMainPage();
    void initSignUpListeners(ActionListener signUpLtr, ActionListener backLtr);
    void nextPageName(String name);
    void nextMainPageName(String name);
}
