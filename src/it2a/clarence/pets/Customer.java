package it2a.clarence.pets;
import java.util.Scanner;

public class Customer {

    public void cTransaction() {
        Scanner sc = new Scanner(System.in);
        String response;

        do {
            System.out.println("\n------------------------------");
            System.out.println("CUSTOMER MANAGEMENT");
            System.out.println("1. ADD CUSTOMER");
            System.out.println("2. VIEW CUSTOMERS");
            System.out.println("3. UPDATE CUSTOMER");
            System.out.println("4. DELETE CUSTOMER");
            System.out.println("5. EXIT");

            System.out.print("Enter Select\n" +
"ion: ");
            int action = -1;
            while (action < 1 || action > 5) {
                System.out.print("Enter a valid action (1-5): ");
                if (sc.hasNextInt()) {
                    action = sc.nextInt();
                } else {
                    System.out.println("Invalid input. Please enter a number.");
                    sc.next();
                }
            }

            Customer cust = new Customer();

            switch (action) {
                case 1:
                    cust.addCustomer();
                    cust.viewCustomers();
                    break;

                case 2:
                    cust.viewCustomers();
                    break;

                case 3:
                    cust.viewCustomers();
                    cust.updateCustomer();
                    cust.viewCustomers();
                    break;

                case 4:
                    cust.viewCustomers();
                    cust.deleteCustomer();
                    cust.viewCustomers();
                    break;

                case 5:
                    System.out.println("Exiting Customer Management...");
                    break;
            }

            System.out.println("Do you want to continue? yes/no: ");
            response = sc.next();

        } while (response.equalsIgnoreCase("yes"));
    }

    public void addCustomer() {
       Scanner sc = new Scanner(System.in);
        config conf = new config();
        Customer c = new Customer();

        System.out.print("Enter Full Name: ");
        String fullName = sc.nextLine();

        System.out.print("Enter Email: ");
        String email = sc.nextLine();

        System.out.print("Enter Phone Number: ");
        int phone = (int) c.numValid();

        //sc.nextLine(); // Clear the buffer
        System.out.print("Enter Address: ");
        String address = sc.nextLine();

        String qry = "INSERT INTO tbl_customer (full_name, email, phone, address) VALUES (?, ?, ?, ?)";
        conf.addRecord(qry, fullName, email, phone, address);
    }

    public void viewCustomers() {
        String qry = "SELECT * FROM tbl_customer";
        String[] headers = {"ID", "Full Name", "Email", "Phone", "Address"};
        String[] columns = {"c_id", "full_name", "email", "phone", "address"};
        config conf = new config();
        conf.viewRecords(qry, headers, columns);
    }

    public void updateCustomer() {
       Scanner sc = new Scanner(System.in);
        config conf = new config();
        Customer c = new Customer(); 

        System.out.print("Enter Customer ID to Update: ");
        int id = sc.nextInt();

        while (conf.getSingleValue("SELECT c_id FROM customer WHERE c_id = ?", id) == 0) {
            System.out.println("Selected ID doesn't exist. Try again:");
            id = sc.nextInt();
        }

        sc.nextLine(); // Clear buffer
        System.out.print("Enter New Full Name: ");
        String fullName = sc.nextLine();

        System.out.print("Enter New Email: ");
        String email = sc.nextLine();

        System.out.print("Enter New Phone Number: ");
        int phone = (int) c.numValid();

        //sc.nextLine(); // Clear the buffer
        System.out.print("Enter New Address: ");
        String address = sc.nextLine();

        String qry = "UPDATE tbl_customer SET full_name = ?, email = ?, phone = ?, address = ? WHERE c_id = ?";
        conf.updateRecord(qry, fullName, email, phone, address, id);
    }

    public void deleteCustomer() {
        Scanner sc = new Scanner(System.in);
        config conf = new config();

        System.out.print("Enter Customer ID to Delete: ");
        int id = sc.nextInt();

        while (conf.getSingleValue("SELECT c_id FROM tbl_customer WHERE c_id = ?", id) == 0) {
            System.out.println("Selected ID doesn't exist. Try again:");
            id = sc.nextInt();
        }

        String qry = "DELETE FROM customers WHERE c_id = ?";
        conf.deleteRecord(qry, id);
    }
    
    
    
    
    
    
    //<<<<<< Validation For Numbers <<<<<<<
    public double numValid() {
        Scanner sc = new Scanner(System.in);
        double value;
        while (true) {
            if (sc.hasNextDouble()) { // Check if the next input is a double
                value = sc.nextDouble();
                break; // Exit the loop if input is valid
            } else {
                System.out.print("Invalid input. Please enter a number: ");
                sc.next(); // Clear invalid input
            }
        }
        return value;
    /*end validation void*/}
    
    
    
    
}