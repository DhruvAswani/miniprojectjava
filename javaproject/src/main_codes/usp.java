package main_codes;

import java.util.Scanner;

public class usp {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        class cred{
            String username;
            String email;
            public void ask(){
                System.out.println("Enter username");
                username = sc.nextLine();
                System.out.println("Enter email adr");
                email = sc.nextLine();
            }
        }
        cred c = new cred();
        c.ask();

    }
}
