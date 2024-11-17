package it2a.clarence.pets;

import java.sql.*;
import java.util.Scanner;

public class main {
    private static final String DATABASE_URL = "jdbc:sqlite:Pets.db";

    public static void main(String[] args) {
        try (Connection conn = connect()) {

            // Create tables if they don't exist
            createTablesIfNotExists(conn);

            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.println("Pet Adoption System");
                System.out.println("1. View Available Pets");
                System.out.println("2. Adopt Pet");
                System.out.println("3. View Adoption History");
                System.out.println("4. Exit");

                System.out.print("Select an option: ");
                int choice = scanner.nextInt();
                scanner.nextLine();  // Consume the newline character

                switch (choice) {
                    case 1:
                        viewAvailablePets(conn);
                        break;
                    case 2:
                        adoptPet(conn, scanner);
                        break;
                    case 3:
                        viewAdoptionHistory(conn);
                        break;
                    case 4:
                        System.out.println("Exiting...");
                        return;
                    default:
                        System.out.println("Invalid choice. Try again.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Database connection error: " + e.getMessage());
        }
    }

    private static Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(DATABASE_URL);
            System.out.println("Connection to SQLite has been established.");
        } catch (SQLException e) {
            System.out.println("Database connection failed: " + e.getMessage());
        }
        return conn;
    }

    private static void createTablesIfNotExists(Connection conn) throws SQLException {
    // Create pets table if it doesn't exist with breed information and available column
    String createPetsTable = "CREATE TABLE IF NOT EXISTS pets ("
            + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
            + "name TEXT NOT NULL, "
            + "species TEXT NOT NULL, "
            + "breed TEXT, "
            + "age INTEGER, "
            + "available BOOLEAN DEFAULT 1)";
    try (Statement stmt = conn.createStatement()) {
        stmt.executeUpdate(createPetsTable);
    }

    // Add 'available' column if it doesn't exist
    String alterPetsTable = "ALTER TABLE pets ADD COLUMN available BOOLEAN DEFAULT 1";
    try (Statement stmt = conn.createStatement()) {
        stmt.executeUpdate(alterPetsTable);
    } catch (SQLException e) {
        // Ignore error if column already exists
        if (!e.getMessage().contains("duplicate column name: available")) {
            throw e;
        }
    }

    // Create adoption history table if it doesn't exist
    String createAdoptionHistoryTable = "CREATE TABLE IF NOT EXISTS adoption_history ("
            + "adoption_id INTEGER PRIMARY KEY AUTOINCREMENT, "
            + "pet_id INTEGER NOT NULL, "
            + "adopter_name TEXT NOT NULL, "
            + "adopter_age INTEGER, "
            + "adopter_address TEXT, "
            + "adopter_contact_number TEXT, "
            + "adoption_date TEXT NOT NULL, "
            + "FOREIGN KEY (pet_id) REFERENCES pets(id))";
    try (Statement stmt = conn.createStatement()) {
        stmt.executeUpdate(createAdoptionHistoryTable);
    }
}


    // View all available pets
    private static void viewAvailablePets(Connection conn) throws SQLException {
        String query = "SELECT id, name, species, breed, age FROM pets WHERE available = 1";
        try (PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            System.out.println("\nAvailable Pets for Adoption:");
            System.out.println("+-----+----------------+---------+---------+-----+");
            System.out.println("| ID  | Name           | Species | Breed   | Age |");
            System.out.println("+-----+----------------+---------+---------+-----+");
            if (!rs.next()) {
                System.out.println("| No available pets found.                                |");
            } else {
                do {
                    System.out.printf("| %-3d | %-14s | %-7s | %-7s | %-3d |\n",
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("species"),
                            rs.getString("breed"),
                            rs.getInt("age"));
                } while (rs.next());
            }
            System.out.println("+-----+----------------+---------+---------+-----+");
        }
    }

    private static void adoptPet(Connection conn, Scanner scanner) throws SQLException {
    // Validate adopter's information
    String adopterName = getAdopterName(scanner);
    int adopterAge = getAdopterAge(scanner);
    String adopterAddress = getAdopterAddress(scanner);
    String adopterContactNumber = getAdopterContactNumber(scanner);

    // Display available pets and get a valid pet ID
    viewAvailablePets(conn);
    int petId = getValidPetId(conn, scanner);

    // Check if the pet is available and adopt it
    String checkPetQuery = "SELECT available FROM pets WHERE id = ?";
    try (PreparedStatement stmt = conn.prepareStatement(checkPetQuery)) {
        stmt.setInt(1, petId);
        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next() && rs.getBoolean("available")) {
                // Pet is available for adoption, proceed with the adoption
                String updatePetQuery = "UPDATE pets SET available = 0 WHERE id = ?";
                try (PreparedStatement updateStmt = conn.prepareStatement(updatePetQuery)) {
                    updateStmt.setInt(1, petId);
                    updateStmt.executeUpdate();
                }

                // Record the adoption with new details
                String insertAdoptionQuery = "INSERT INTO adoption_history (pet_id, adopter_name, adopter_age, "
                        + "adopter_address, adopter_contact_number, adoption_date) "
                        + "VALUES (?, ?, ?, ?, ?, CURRENT_TIMESTAMP)";
                try (PreparedStatement stmt2 = conn.prepareStatement(insertAdoptionQuery)) {
                    stmt2.setInt(1, petId);
                    stmt2.setString(2, adopterName);
                    stmt2.setInt(3, adopterAge);
                    stmt2.setString(4, adopterAddress);
                    stmt2.setString(5, adopterContactNumber);
                    stmt2.executeUpdate();
                }

                System.out.println("Pet adopted successfully!");
            } else {
                System.out.println("Pet not available or does not exist.");
            }
        }
    }
}

private static String getAdopterName(Scanner scanner) {
    String name;
    while (true) {
        System.out.print("Enter adopter's name: ");
        name = scanner.nextLine().trim();
        if (!name.isEmpty()) {
            break;
        }
        System.out.println("Name cannot be empty. Please enter a valid name.");
    }
    return name;
}
private static int getAdopterAge(Scanner scanner) {
    int age = -1;
    while (age < 18 || age > 120) {
        System.out.print("Enter adopter's age (18-120): ");
        if (scanner.hasNextInt()) {
            age = scanner.nextInt();
            scanner.nextLine();  // Consume the newline character
            if (age < 18 || age > 120) {
                System.out.println("Age must be between 18 and 120. Please try again.");
            }
        } else {
            System.out.println("Invalid input. Please enter a valid age.");
            scanner.nextLine();  // Consume the invalid input
        }
    }
    return age;
}
private static String getAdopterAddress(Scanner scanner) {
    String address;
    while (true) {
        System.out.print("Enter adopter's address: ");
        address = scanner.nextLine().trim();
        if (!address.isEmpty()) {
            break;
        }
        System.out.println("Address cannot be empty. Please enter a valid address.");
    }
    return address;
}
private static String getAdopterContactNumber(Scanner scanner) {
    String contactNumber;
    while (true) {
        System.out.print("Enter adopter's contact number: ");
        contactNumber = scanner.nextLine().trim();
        if (contactNumber.matches("\\d{10}")) {  // Matches exactly 10 digits
            break;
        } else {
            System.out.println("Invalid contact number. Please enter a valid 10-digit phone number.");
        }
    }
    return contactNumber;
}
private static int getValidPetId(Connection conn, Scanner scanner) throws SQLException {
    int petId = -1;
    while (true) {
        System.out.print("Enter pet ID to adopt: ");
        if (scanner.hasNextInt()) {
            petId = scanner.nextInt();
            scanner.nextLine();  // Consume the newline character

            // Check if the pet ID is valid (exists and is available)
            String checkPetQuery = "SELECT available FROM pets WHERE id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(checkPetQuery)) {
                stmt.setInt(1, petId);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next() && rs.getBoolean("available")) {
                        return petId;  // Valid pet ID
                    } else {
                        System.out.println("Invalid or unavailable pet ID. Please try again.");
                    }
                }
            }
        } else {
            System.out.println("Invalid pet ID. Please enter a valid number.");
            scanner.nextLine();  // Consume the invalid input
        }
    }
}


 
private static void viewAdoptionHistory(Connection conn) throws SQLException {
    String query = "SELECT ah.adoption_id, p.name AS pet_name, ah.adopter_name, ah.adopter_age, "
            + "ah.adopter_address, ah.adopter_contact_number, ah.adoption_date "
            + "FROM adoption_history ah "
            + "JOIN pets p ON ah.pet_id = p.id";
    try (PreparedStatement stmt = conn.prepareStatement(query);
         ResultSet rs = stmt.executeQuery()) {

        System.out.println("Adoption History:");
        if (!rs.next()) {
            System.out.println("No adoption history found.");
        } else {
            do {
                System.out.println("Adoption ID: " + rs.getInt("adoption_id")
                        + ", Pet: " + rs.getString("pet_name")
                        + ", Adopter: " + rs.getString("adopter_name")
                        + ", Age: " + rs.getInt("adopter_age")
                        + ", Address: " + rs.getString("adopter_address")
                        + ", Contact Number: " + rs.getString("adopter_contact_number")
                        + ", Date: " + rs.getString("adoption_date"));
            } while (rs.next());
        }
    }
}


}

