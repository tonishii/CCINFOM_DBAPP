package schemaobjects;

import java.util.Scanner;

public interface Account {
    public int login(Scanner scn);
    public void displayView(Scanner scn);
    public void signUp(Scanner scn);
}
