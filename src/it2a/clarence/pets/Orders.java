package it2a.clarence.pets;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Orders {
    public void oTransaction() {
        Scanner sc = new Scanner(System.in);
        String response;
        do {
            System.out.println("\n------------------------------");
            System.out.println("ORDER DETAIL");
            System.out.println("1. ADD ORDER DETAIL");
            System.out.println("2. VIEW ORDER DETAIL");
            System.out.println("3. UPDATE ORDER DETAIL");
            System.out.println("4. DELETE ORDER DETAIL");
            System.out.println("5. EXIT");

            System.out.print("Enter Selection: ");
            int action = -1;

            while (action < 1 || action > 5) {
                System.out.print("Enter Action: ");
                if (sc.hasNextInt()) {
                    action = sc.nextInt();
                    if (action < 1 || action > 5) {
                        System.out.println("Invalid input. Please choose a number between 1 and 5.");
                    }
                } else {
                    System.out.println("Invalid input. Please enter a number.");
                    sc.next();
                }
            }

            Orders or = new Orders();
            switch (action) {
                case 1:
                    or.addOrders();
                    break;
                case 2:
                    or.viewOrders();
                    break;
                case 3:
                or.viewOrders();
                    or.updateOrders();
                    or.viewOrders();
                    break;
                case 4:
                or.viewOrders();
                    or.deleteOrder();
                    or.viewOrders();
                    break;
                case 5:
                    System.out.println("Exiting...");
                    break;
            }

            if (action != 5) {
                System.out.print("Do you want to continue? yes/no: ");
                response = sc.next();
            } else {
                response = "no";
            }
        } while (response.equalsIgnoreCase("yes"));
    }

    
    
    
    
    
    
    // Add Orders
    public void addOrders() {
       Scanner sc = new Scanner(System.in);
       Orders o = new Orders();
        config conf = new config();

        // Select Customer
        Customer cs = new Customer();
        cs.viewCustomers();
        System.out.print("Enter the ID of the customer: ");
        int cid = sc.nextInt();
        String sql = "SELECT c_id FROM tbl_customers WHERE c_id = ?";
        while (conf.getSingleValue(sql, cid) == 0) {
            System.out.print("Customer does not exist, Select again: ");
            cid = sc.nextInt();
        }

        // Select Pet
Pets pt = new Pets();
pt.viewPets();

        System.out.print("Enter the ID of the pet: ");
        int pid = sc.nextInt();
        sql = "SELECT pet_id FROM tbl_pets WHERE pet_id = ?";
        while (conf.getSingleValue(sql, pid) == 0) {
            System.out.print("Pet does not exist, Select again: ");
            pid = sc.nextInt();
        }

        // Get Fee and Quantity
        double fee = conf.getSingleValue("SELECT pet_fee FROM tbl_pets WHERE pet_id = ?", pid);
        System.out.print("Enter quantity: ");
    double quantity = (double) o.numValid();
        double totalFee = fee * quantity;

        // Display total fee
        System.out.println("----------------------------------------");
        System.out.println("Total fee is: " + totalFee);
        System.out.println("----------------------------------------");
        System.out.print("Enter received cash: ");
        double rcash = (double) o.numValid();

        while (rcash < totalFee) {
            System.out.print("Insufficient cash, try again: ");
            rcash = (double) o.numValid();
        }
        
        int change = (int) (rcash - totalFee);
       System.out.println("Change: " + change);
        

        // Save Order
        LocalDate currdate = LocalDate.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        String date = currdate.format(format);
        String status = "Pending";
        String orderqry = "INSERT INTO tbl_orders (c_id, pet_id, o_qnty, o_due, cash, o_date, o_status) VALUES (?, ?, ?, ?, ?, ?, ?)";

        conf.addRecord(orderqry, cid, pid, quantity, totalFee, rcash, date, status);
        System.out.println("Order added successfully!");
    }

    
    
    
    
    
    
    
    // View Orders
    public void viewOrders() {
        config conf = new config();

        String qry = "SELECT o_id, c_name, pet_name, o_due, o_date, o_status FROM tbl_orders " +
                     "LEFT JOIN tbl_customers ON tbl_customers.c_id = tbl_orders.c_id " +
                     "LEFT JOIN tbl_pets ON tbl_pets.pet_id = tbl_orders.pet_id";
        String[] hrds = {"OID", "Customer", "Pet", "Due", "Date", "Status"};
        String[] clms = {"o_id", "c_name", "pet_name", "o_due", "o_date", "o_status"};

        conf.viewRecords(qry, hrds, clms);
    }

    
    
    
    
    
    
    
    // Update Orders
    public void updateOrders() {
       Scanner sc = new Scanner(System.in);
       Orders o = new Orders();
        config conf = new config();



        // Select Order ID
        System.out.print("Enter the Order ID to update: ");
        int oid = sc.nextInt();
        String sql = "SELECT o_id FROM tbl_orders WHERE o_id = ?";
        while (conf.getSingleValue(sql, oid) == 0) {
            System.out.print("Order does not exist, Select again: ");
            oid = sc.nextInt();
        }

        // Update Order Details
        Customer cs = new Customer();
        cs.viewCustomers();
        System.out.print("Enter new Customer ID: ");
        int cid = sc.nextInt();
        sql = "SELECT c_id FROM tbl_customers WHERE c_id = ?";
        while (conf.getSingleValue(sql, cid) == 0) {
            System.out.print("Customer does not exist, Select again: ");
            cid = sc.nextInt();
        }

Pets pt = new Pets();
pt.viewPets();
        System.out.print("Enter the ID of the pet: ");
        int pid = sc.nextInt();
        sql = "SELECT pet_id FROM tbl_pets WHERE pet_id = ?";
        while (conf.getSingleValue(sql, pid) == 0) {
            System.out.print("Pet does not exist, Select again: ");
            pid = sc.nextInt();
        }

        // Update Fee and Quantity
        double fee = conf.getSingleValue("SELECT pet_fee FROM tbl_pets WHERE pet_id = ?", pid);
        System.out.print("Enter new quantity: ");
    double quantity = (double) o.numValid();
        double totalFee = fee * quantity;

        System.out.println("----------------------------------------");
        System.out.println("Updated total fee is: " + totalFee);
        System.out.println("----------------------------------------");
        System.out.print("Enter new received cash: ");
        double rcash = (double) o.numValid();

        while (rcash < totalFee) {
            System.out.print("Insufficient cash, try again: ");
            rcash = (int) o.numValid();
        }

       int change = (int) (rcash - totalFee);
       System.out.println("Change: " + change);


        // Update Order Record
        String orderQry = "UPDATE tbl_orders SET c_id = ?, pet_id = ?, o_qnty = ?, o_due = ?, cash = ? WHERE o_id = ?";
        conf.updateRecord(orderQry, cid, pid, quantity, totalFee, rcash, oid);

        System.out.println("Order updated successfully!");
    }

    
    
    
    
    
    
    // Delete Orders
    public void deleteOrder() {
       Scanner sc = new Scanner(System.in);
        config conf = new config();


        // Prompt for Order ID to delete
        System.out.print("Enter the Order ID to delete: ");
        int oid = sc.nextInt();

        // Check if the order ID exists
        String sql = "SELECT o_id FROM tbl_orders WHERE o_id = ?";
        while (conf.getSingleValue(sql, oid) == 0) {
            System.out.print("Order does not exist, please enter a valid Order ID: ");
            oid = sc.nextInt();
        }

        // Confirm deletion
        System.out.print("Are you sure you want to delete this order? (yes/no): ");
        String confirm = sc.next();
        if (confirm.equalsIgnoreCase("yes")) {
            String deleteQry = "DELETE FROM tbl_orders WHERE o_id = ?";
            conf.deleteRecord(deleteQry, oid);
            System.out.println("Order deleted successfully!");
        } else {
            System.out.println("Order deletion canceled.");
        }
    }

    
    
    
    
    // Validate Numbers
    public double numValid() {
        Scanner sc = new Scanner(System.in);
        double value;
        while (true) {
            if (sc.hasNextDouble()) {
                value = sc.nextDouble();
                break;
            } else {
                System.out.print("Invalid input. Please enter a number: ");
                sc.next();
            }
        }
        return value;
    }
}