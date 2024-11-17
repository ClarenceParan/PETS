package it2a.clarence.pets;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        boolean exit1 = true;  // Corrected declaration inside main
        Scanner sc = new Scanner(System.in);

        do {
            System.out.println("WELCOME TO APP DEMO");
            System.out.println("");
            System.out.println("1. CUSTOMER");
            System.out.println("2. PETS");
            System.out.println("3. ORDER");
            System.out.println("4. REPORTS");
            System.out.println("5. EXIT");

            System.out.print("Enter action: ");
            int action = sc.nextInt();

            switch (action) {
                case 1: {
                    Customer cs = new Customer();
                    cs.cTransaction();
                    break;
                }
                case 2: {
                    Pets pt = new Pets();
                    pt.pTransaction();
                    break;
                }
                case 3: {
                    Orders or = new Orders();
                    or.oTransaction();
                    break;
                }
                case 5: {
                    System.out.println("Exit Selected... type 'yes' to continue: ");
                    String resp = sc.next();
                    if (resp.equalsIgnoreCase("yes")) {
                        exit1 = false;
                    }
                    break;
                }
            }
        } while (exit1);

        sc.close();
    }
}