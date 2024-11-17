package it2a.clarence.pets;
import java.util.Scanner;

public class Pets {

    public void pTransaction() {
        Scanner sc = new Scanner(System.in);
        String response;

        do {
            System.out.println("\n------------------------------");
            System.out.println("PET MANAGEMENT");
            System.out.println("1. ADD PET");
            System.out.println("2. VIEW PETS");
            System.out.println("3. UPDATE PET");
            System.out.println("4. DELETE PET");
            System.out.println("5. EXIT");

            System.out.print("Enter Selection: ");
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

            Pets pets = new Pets();

            switch (action) {
                case 1:
                    pets.addPet();
                    pets.viewPets();
                    break;

                case 2:
                    pets.viewPets();
                    break;

                case 3:
                    pets.viewPets();
                    pets.updatePet();
                    pets.viewPets();
                    break;

                case 4:
                    pets.viewPets();
                    pets.deletePet();
                    pets.viewPets();
                    break;

                case 5:
                    System.out.println("Exiting Pet Management...");
                    break;
            }

            System.out.println("Do you want to continue? yes/no: ");
            response = sc.next();

        } while (response.equalsIgnoreCase("yes"));
    }


    
        
        public void addPet() {
            Pets p = new Pets();
            
        Scanner sc = new Scanner(System.in);
        config conf = new config();

        System.out.print("Enter Pet Name: ");
        String name = sc.nextLine();

        System.out.print("Enter Species: ");
        String species = sc.nextLine();

        System.out.print("Enter Breed: ");
        String breed = sc.nextLine();

        System.out.print("Enter Age: ");
        int age = (int) p.numValid();

        //sc.nextLine(); // Clear the buffer
        System.out.print("Enter Gender: ");
        String gender = sc.nextLine();

        System.out.print("Enter Fee: ");
        int fee = (int) p.numValid();

        String qry = "INSERT INTO tbl_pets (name, species, breed, age, gender, fee) VALUES (?, ?, ?, ?, ?, ?)";
        conf.addRecord(qry, name, species, breed, age, gender, fee);
    }

    public void viewPets() {
        String qry = "SELECT * FROM tbl_pets";
        String[] headers = {"ID", "Name", "Species", "Breed", "Age", "Gender", "Fee"};
        String[] columns = {"p_id", "name", "species", "breed", "age", "gender", "fee"};
        config conf = new config();
        conf.viewRecords(qry, headers, columns);
    }

    public void updatePet() {
       Scanner sc = new Scanner(System.in);
       Pets p = new Pets();
        config conf = new config();

        System.out.print("Enter Pet ID to Update: ");
        int id = sc.nextInt();

        while (conf.getSingleValue("SELECT p_id FROM tbl_pets WHERE p_id = ?", id) == 0) {
            System.out.println("Selected ID doesn't exist. Try again:");
            id = sc.nextInt();
        }

        sc.nextLine(); // Clear buffer
        System.out.print("Enter New Pet Name: ");
        String name = sc.nextLine();

        System.out.print("Enter New Species: ");
        String species = sc.nextLine();

        System.out.print("Enter New Breed: ");
        String breed = sc.nextLine();

        System.out.print("Enter New Age: ");
        int age = (int) p.numValid();

        //sc.nextLine(); // Clear buffer
        System.out.print("Enter New Gender: ");
        String gender = sc.nextLine();

        System.out.print("Enter New Fee: ");
        int fee = (int) p.numValid();

        String qry = "UPDATE tbl_pets SET name = ?, species = ?, breed = ?, age = ?, gender = ?, fee = ? WHERE p_id = ?";
        conf.updateRecord(qry, name, species, breed, age, gender, fee, id);
    }

    public void deletePet() {
        Scanner sc = new Scanner(System.in);
        config conf = new config();

        System.out.print("Enter Pet ID to Delete: ");
        int id = sc.nextInt();

        while (conf.getSingleValue("SELECT p_id FROM tbl_pets WHERE p_id = ?", id) == 0) {
            System.out.println("Selected ID doesn't exist. Try again:");
            id = sc.nextInt();
        }

        String qry = "DELETE FROM tbl_pets WHERE p_id = ?";
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