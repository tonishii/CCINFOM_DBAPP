package view;

import javax.swing.*;
import java.awt.event.ActionListener;

public interface AccountPage {
    JPanel getSignUpPage();
    JPanel getMainPage();
    void initSignUpListeners(ActionListener signUpLtr, ActionListener backLtr);
    void nextPageName(String name);
}
