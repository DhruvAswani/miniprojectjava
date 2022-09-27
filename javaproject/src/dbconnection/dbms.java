package dbconnection;

import java.sql.*;
import java.util.Random;
import java.util.Scanner;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

class sql{

}
public class dbms{
    static Connection con = null;
    static Scanner input= new Scanner(System.in);


    public static void main(String args[]) throws SQLException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/mobileshop", "root", "project_123");
            Statement stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery("SELECT sno,mobile_name,price from mobile_shop order by sno asc");
                    while (rs.next()){
                    int sno=rs.getInt("sno");
                    String mobile_name=rs.getString("mobile_name");
                    int price=rs.getInt("price");
                    System.out.println(sno + "  " + "\n" + mobile_name + " "+ "\n" + "Price- " + price+ "\n");
                    }
            int serial_no,count=0;
            System.out.println("PLEASE ENTER THE SERIAL NUMBER OF THE PHONE YOU ARE LOOKING FOR (THIS IS JUST FOR SHOWING YOU THE DETAILS OF YOUR FAVOURITE PHONES)");
            serial_no=input.nextInt();
            ResultSet rs1 = stmt.executeQuery("SELECT * FROM mobile_shop WHERE sno='"+serial_no+"'");
            ResultSetMetaData rsmd = rs1.getMetaData();
            int columnsNumber = rsmd.getColumnCount();
            int choice=0;

            if(rs1.next()){
                do{
                    if(choice==1){

                        System.out.println("PLEASE ENTER THE SERIAL NUMBER OF THE PHONE YOU ARE LOOKING FOR (THIS IS JUST FOR SHOWING YOU THE DETAILS OF YOUR FAVOURITE PHONES)");
                        serial_no=input.nextInt();
                        rs1 = stmt.executeQuery("SELECT * FROM mobile_shop WHERE sno='"+serial_no+"'");
                        rsmd = rs1.getMetaData();
                        columnsNumber = rsmd.getColumnCount();
                        if(rs1.next()){
                            for(int i = 2; i <= columnsNumber; i++){
                            System.out.print(rs1.getString(i) + "\n");
                             }
                        }
                    }
                    else{
                        for(int i = 2; i <= columnsNumber; i++){
                            System.out.print(rs1.getString(i) + "\n");
                        }
                    }
                    System.out.println();
                    System.out.println("do you want to look for more phones? press 1 for yes and 0 for no");
                    choice= input.nextInt();

                }
                while(choice==1);
            }

            //BILLING
            System.out.println("Do you want to buy any of the phones? Enter 1 for yes and 0 for no");
            choice=input.nextInt();
            stmt.executeUpdate("drop table BILLING");
            do{
                if(choice==1){
                    count++;
                    stmt.executeUpdate("create table BILLING (new_serial_no int(50),serial_no int(50), mobile_name varchar(100), price int(100))");
                    System.out.println("Enter the serial number of the phone you want to buy:");
                    serial_no=input.nextInt();
                    ResultSet rs2 = stmt.executeQuery("SELECT mobile_name,price FROM mobile_shop WHERE sno='"+serial_no+"'");
                    if(rs2.next()){
                        int new_serial_no=count;
                       String mobile_name=rs2.getString("mobile_name");
                       int price=rs2.getInt("price");
                       PreparedStatement ps=con.prepareStatement("insert into billing values(?,?,?,?)");
                       ps.setInt(1,new_serial_no);
                       ps.setInt(2,serial_no);
                       ps.setString(3,mobile_name);
                       ps.setInt(4,price);
                       ps.executeUpdate();
                      }
                    System.out.println("Do you want to add more in the cart? Type 2 for yes and 0 for no:");
                    choice=input.nextInt();

                }
                else if(choice==0){
                    break;
                }
                else{
                    count++;
                    System.out.println("Enter the serial number of the phone you want to buy:");
                                serial_no=input.nextInt();
                                ResultSet rs2 = stmt.executeQuery("SELECT mobile_name,price FROM mobile_shop WHERE sno='"+serial_no+"'");
                                        if(rs2.next()){
                                            int new_serial_no=count;
                                           String mobile_name=rs2.getString("mobile_name");
                                           int price=rs2.getInt("price");
                                           PreparedStatement ps=con.prepareStatement("insert into billing values(?,?,?,?)");
                                           ps.setInt(1,new_serial_no);
                                           ps.setInt(2,serial_no);
                                           ps.setString(3,mobile_name);
                                           ps.setInt(4,price);
                                           ps.executeUpdate();
                                          }
                                          System.out.println("Do you want to add more in the cart? Type 2 for yes and 0 for no:");
                                          choice=input.nextInt();

                }




            }
            while(choice==2);
            int new_serial_no=0;

            rs = stmt.executeQuery("SELECT new_serial_no,mobile_name,price from billing order by serial_no asc");
            while (rs.next()){
            new_serial_no=rs.getInt("new_serial_no");
            String mobile_name=rs.getString("mobile_name");
            int price=rs.getInt("price");
            System.out.println(new_serial_no + "  " + "\n" + mobile_name + " "+ "\n" + "Price- " + price+ "\n");
            }

            //Deletion

            System.out.println("do you want to delete something from the cart? press 1 for yes and 0 for no");
            choice= input.nextInt();
            do{
               if(choice==1){
                   System.out.println("Enter serial number of the phone you want to delete:");
                   new_serial_no=input.nextInt();
                   PreparedStatement ps1=con.prepareStatement("delete from billing where new_serial_no=?");
                   ps1.setInt(1,new_serial_no);
                   ps1.executeUpdate();
                   System.out.println("Do you want to delete more from the cart? press 1 for yes and 0 for no");
                   choice=input.nextInt();
                }
               else{
                   break;
               }

            }
            while(choice==1);

            //Calculating the total price
            ResultSet sum = stmt.executeQuery("SELECT SUM(Price) from billing");
            if(sum.next()){
                int total=sum.getInt("SUM(price)");
//               System.out.println(total);
            }


            con.close();

        } catch (Exception e) {
        System.out.println(e);
        }
        //String name;
       //String address;
        //String number;

        System.out.println("Enter your name:");
        String name = input.nextLine();

        System.out.println("Enter your address:");
        String address = input.nextLine();

        System.out.println("Enter your mobile number:");
        String number = input.nextLine();

        File myObj = new File("Bill.txt");
        try {
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        try {
            FileWriter myWriter = new FileWriter("Bill.txt");
            myWriter.write("VESIT is 8th wonder of world !!");
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        }
        catch (IOException e){
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

}

