//import modules and classes
import java.io.*;
import java.util.*;

public class Main {
    //Maximum number of students
    private static final int maxStudents = 100;
    //Array of objects to store student details
    private static final Student[] students = new Student[maxStudents];
    //Student counter
    private static int studentCount = 0;
    //Declaring scanner object to read user input
    private static final Scanner scanner = new Scanner(System.in);

    //Gives an entry point to the main method
    public static void main(String[] args) {
        int choice = 0; //Initializing the choice variable
        Scanner scanner;
        System.out.println();
        System.out.println("      Student Records      ");
        System.out.println();

        do {
            //Displaying main menu
            mainMenu();
            scanner = new Scanner(System.in);
            try {
                choice = scanner.nextInt();
                scanner.nextLine();

                //Declaring switch statement to handle choice cases
                switch (choice) {
                    case 1:
                        availableSeats();
                        break;
                    case 2:
                        registerStudent();
                        break;
                    case 3:
                        deleteStudent();
                        break;
                    case 4:
                        findStudent();
                        break;
                    case 5:
                        storeStudentDetailsToFile();
                        break;
                    case 6:
                        loadStudentDetailsFromFile();
                        break;
                    case 7:
                        viewStudentsSortedByName();
                        break;
                    case 8:
                        additionalControls();
                        break;
                    case 9:
                        System.out.println("Exiting...");
                        break;
                    default:
                        System.err.println("Invalid choice. Please try a number between 1 and 9.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Error. Please choose a valid option.");
            }
        } while (choice != 9);
    }

    //Method to display main menu
    private static void mainMenu() {
        System.out.println("\nStudent Records: \n");
        System.out.println("1. Check available seats");
        System.out.println("2. Register student (with ID)");
        System.out.println("3. Delete student");
        System.out.println("4. Find student (with student ID)");
        System.out.println("5. Store student details into a file");
        System.out.println("6. Load student details from the file");
        System.out.println("7. View the list of students based on their names");
        System.out.println("8. Additional controls");
        System.out.println("9. Exit");
        System.out.print("Enter your choice: ");
    }

    //Method to display available seats
    private static void availableSeats() {
        System.out.println("Available seats: " + (maxStudents - studentCount));
    }

    //Method to register a student
    private static void registerStudent() {
        if (studentCount >= maxStudents) {
            System.err.println("Number of students exceeded maximum number of available seats.");
            return;
        }

        System.out.print("Enter student ID: ");
        String id = scanner.nextLine();
        if (!validStudentId(id)) {
            System.out.println("Invalid student ID. It must be 8 characters long and start with 'w'.");
            return;
        }
        if (duplicateStudentId(id)) {
            System.out.println("Student ID already exists. Please check the Student ID.");
            return;
        }

        String name = getValidName(scanner);
        if (name.isEmpty()) {
            System.out.println("Student name is compulsory.");
            return;
        }

        students[studentCount++] = new Student(id, name);
        System.out.println("Student registered successfully.");
    }

    //Method to delete a student using their student ID
    private static void deleteStudent() {
        System.out.print("Enter student ID to delete: ");
        String id = scanner.nextLine();
        // Loop through the list of students to find the student with the entered ID
        for (int i = 0; i < studentCount; i++) {
            // Check if the current student's ID matches the entered ID
            if (students[i].getId().equals(id)) {
                // Replace the student to be deleted with the last student in the list
                students[i] = students[studentCount - 1]; // Move last student to deleted position
                // Decrement the student count and set the last student position to null
                students[--studentCount] = null;
                System.out.println("Student deleted successfully.");
                return;
            }
        }
        System.err.println("Student not found.");
    }

    //Method to find a student using the Student ID
    private static void findStudent() {
        System.out.print("Enter student ID to find: ");
        String id = scanner.nextLine();
        // Loop through the list of students to find the student with the entered ID
        for (int i = 0; i < studentCount; i++) {
            // Check if the current student's ID matches the entered ID
            if (students[i].getId().equals(id)) {
                // Print the student's details
                System.out.println(students[i]);
                return;
            }
        }
        System.err.println("Student not found.");
    }

    //Method to store student details to the relevant file
    private static void storeStudentDetailsToFile() {
        // Use a try-with-resources statement to automatically close the PrintWriter
        try (PrintWriter writer = new PrintWriter(new FileWriter("students.txt"))) {
            for (int i = 0; i < studentCount; i++) {
                // Get the marks of the current student
                int[] marks = students[i].getMarks();
                // Write the student details to the file, separated by commas
                writer.println(students[i].getId() + "," + students[i].getName() + "," + marks[0] + "," + marks[1] + "," + marks[2]);
            }
            System.out.println("Student details stored to file successfully.");
        } catch (IOException e) {
            // If an IOException occurs, print an error message with the exception details
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }

    //Method to load student details from the relevant file
    private static void loadStudentDetailsFromFile() {
        // Use a try-with-resources statement to automatically close the Scanner
        try (Scanner scanner = new Scanner(new File("students.txt"))) {
            // Initialize studentCount to 0 before loading new student data
            studentCount = 0;
            // Loop through each line in the file while there are more lines and the student count is below the maximum
            while ((scanner.hasNextLine()) && studentCount < maxStudents) {
                // Read the next line from the file
                String line = scanner.nextLine();
                // Split the line into parts separated by commas
                String[] parts = line.split(",");
                // Check if the line contains the expected number of parts
                if (parts.length == 5) {
                    // Extract the student ID, name, and marks from the parts
                    String id = parts[0];
                    String name = parts[1];
                    int mark1 = Integer.parseInt(parts[2]);
                    int mark2 = Integer.parseInt(parts[3]);
                    int mark3 = Integer.parseInt(parts[4]);
                    // Create a new Student object and add it to the array
                    students[studentCount] = new Student(id, name, mark1, mark2, mark3);
                    // Increment the student count
                    studentCount++;
                }
            }
            System.out.println("Student details loaded from file successfully.");
        } catch (IOException e) {
            // If an IOException occurs, print an error message with the exception details
            System.err.println("Error reading from file: " + e.getMessage());
        }
    }

    //Display the list of sorted students by the alphabetical order of their name
    private static void viewStudentsSortedByName() {
        // Create a copy of the students array up to the current student count
        Student[] sortedStudents = Arrays.copyOf(students, studentCount);
        // Sort the copied array of students by their names
        sortStudentsByName(sortedStudents);
        // Loop through the sorted array and print each student's details
        for (Student student : sortedStudents) {
            System.out.println(student);
        }
    }

    //Sort students by the alphabetical order of their name
    private static void sortStudentsByName(Student[] students) {
        // Outer loop to iterate through each student in the array (excluding the last one)
        for (int i = 0; i < students.length - 1; i++) {
            // Inner loop to compare the current student with the subsequent students
            for (int j = i + 1; j < students.length; j++) {
                // Compare the names of the students and check if they are out of order
                if (students[i].getName().compareTo(students[j].getName()) > 0) {
                    // Swap the students if they are out of order
                    Student temp = students[i];
                    students[i] = students[j];
                    students[j] = temp;
                }
            }
        }
    }

    //Displaying the submenu to manage student details
    private static void additionalControls() {
        System.out.println("\nManage student details: \n");
        System.out.println("a. Update student name");
        System.out.println("b. Add module marks");
        System.out.println("c. Generate student summary");
        System.out.println("d. Generate complete student report");
        System.out.print("Enter your choice: ");
        char choice = scanner.next().charAt(0);
        scanner.nextLine();
        switch (choice) {
            case 'a':
                updateStudent();
                break;
            case 'b':
                addModuleMarks();
                break;
            case 'c':
                generateSummary();
                break;
            case 'd':
                generateCompleteReport();
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }

    //Method to update the students name if there's any change
    private static void updateStudent() {
        System.out.print("Enter student ID: ");
        // Read the entered student ID from the input
        String id = scanner.nextLine();
        // Loop through the list of students to find the student with the entered ID
        for (Student student : students) {
            // Check if the current student is not null and if their ID matches the entered ID
            if (student != null && student.getId().equals(id)) {
                // Get a valid new name for the student from the user
                String name = getValidName(scanner);
                // Update the student's name with the new name
                student.setName(name);
                System.out.println("Student name updated successfully.");
                return;
            }
        }
        System.out.println("Student not found.");
    }

    //Method to add module marks of a registered student using student ID
    private static void addModuleMarks() {
        System.out.print("Enter student ID to add module marks: ");
        // Read the entered student ID from the input
        String id = scanner.nextLine();
        // Loop through the list of students to find the student with the entered ID
        for (int i = 0; i < studentCount; i++) {
            // Check if the current student's ID matches the entered ID
            if (students[i].getId().equals(id)) {
                // Prompt and get valid marks for the three modules
                int mark1 = getValidMark("Enter module 1 mark: ");
                int mark2 = getValidMark("Enter module 2 mark: ");
                int mark3 = getValidMark("Enter module 3 mark: ");
                // Set the marks for the student
                students[i].setMarks(mark1, mark2, mark3);
                System.out.println("Module marks added successfully.");
                return;
            }
        }
        System.err.println("Student not found.");
    }

    //Method to generate a summary of the students who passed the respective modules
    private static void generateSummary() {
        System.out.println("Total student registrations: " + studentCount);
        // Create an array to count the number of students who scored more than 40 marks in each module
        int[] countAbove40 = new int[3];
        // Loop through each student
        for (int i = 0; i < studentCount; i++) {
            // Get the marks for the current student
            int[] marks = students[i].getMarks();
            // Loop through the three modules
            for (int j = 0; j < 3; j++) {
                // If the mark for the current module is greater than 40, increment the respective count
                if (marks[j] > 40) {
                    countAbove40[j]++;
                }
            }
        }
        // Print the summary of students who scored more than 40 marks in each module
        System.out.println("Total no of students who scored more than 40 marks:");
        System.out.println("Module 1: " + countAbove40[0]);
        System.out.println("Module 2: " + countAbove40[1]);
        System.out.println("Module 3: " + countAbove40[2]);
    }

    //Method to generate a complete report os each student registered.
    private static void generateCompleteReport() {
        // Create a copy of the students array up to the student count
        Student[] sortedStudents = Arrays.copyOf(students, studentCount);
        // Sort the copied array of students by their average marks
        sortStudentsByAverageMarks(sortedStudents);
        System.out.println("Complete report:");
        // Loop through each sorted student to print their details
        for (Student student : sortedStudents) {
            // Get the marks for the current student
            int[] marks = student.getMarks();
            // Calculate the total and average marks
            int total = marks[0] + marks[1] + marks[2];
            double average = total / 3.0;
            // Get the grade for the student
            String grade = student.getGrade();
            // Print the student's details in the specified format
            System.out.println("ID: " + student.getId() + ", Name: " + student.getName() + ", Marks: [Module 1: " + marks[0] + ", Module 2: " + marks[1] + ", Module 3: " + marks[2] + "], Total: " + total + ", Average: " + average + ", Grade: " + grade);
        }
    }

    //Method to sort the students by average marks using Bubble sort
    private static void sortStudentsByAverageMarks(Student[] students) {
        // Outer loop to iterate through the students array
        for (int i = 0; i < students.length - 1; i++) {
            // Initialize a flag to check if any swapping happened
            boolean swapped = false;
            // Inner loop for comparing adjacent students based on their average marks
            for (int j = 0; j < students.length - i - 1; j++) {
                // Calculate the average marks of the current and next student
                double avg1 = calculateAverage(students[j]);
                double avg2 = calculateAverage(students[j + 1]);

                // If the average of the current student is less than the next student, swap them
                if (avg1 < avg2) {
                    // Swap students[j] with students[j + 1]
                    Student temp = students[j];
                    students[j] = students[j + 1];
                    students[j + 1] = temp;
                    // Set the flag to true indicating a swap occurred
                    swapped = true;
                }
            }
            // If no elements were swapped, array is sorted
            if (!swapped) {
                break;
            }
        }
    }

    //Method to ensure that the same student ID is not repeated twice
    private static boolean duplicateStudentId(String id) {
        // Loop through all students in the array
        for (int i = 0; i < studentCount; i++) {
            // Check if the ID of the current student matches the provided ID
            if (students[i].getId().equals(id)) {
                // Return true if a match is found, indicating a duplicate ID
                return true;
            }
        }
        // Return false if no matching ID is found, indicating no duplicates
        return false;
    }

    //Method to calculate the average of each student
    private static double calculateAverage(Student student) {
        // Retrieve the marks array from the student object
        int[] marks = student.getMarks();
        // Calculate the total marks by summing the marks for all three modules
        int total = marks[0] + marks[1] + marks[2];
        // Return the average marks by dividing the total by 3.0 to ensure floating-point division
        return total / 3.0;
    }

    //Method to check the if the mark is a valid mark
    private static int getValidMark(String prompt) {
        // Initialize mark to an invalid value (-1) to start the validation loop
        int mark = -1;
        // Continue looping until a valid mark (0 to 100) is provided
        while (mark < 0 || mark > 100) {
            // Print the prompt to ask the user for input
            System.out.print(prompt);
            // Check if the next input is an integer
            if (scanner.hasNextInt()) {
                // Read the integer value from the user
                mark = scanner.nextInt();
                // Clear the newline character left by nextInt()
                scanner.nextLine();
                // Check if the mark is within the valid range (0 to 100)
                if (mark < 0 || mark > 100) {
                    System.out.println("Invalid mark. It must be between 0 and 100.");
                }
            } else {
                // Inform the user that the input is invalid and must be a number
                System.out.println("Invalid input. Please enter a number between 0 and 100.");
                // Clear the invalid input
                scanner.nextLine();
            }
        }
        // Return the valid mark
        return mark;
    }

    //Method to ensure that the Student ID is in the correct format
    private static boolean validStudentId(String id) {
        return id.length() == 8 && id.startsWith("w");
    }

    //Method to ensure that a valid name is input with alphabetical characters
    private static String getValidName(Scanner scanner) {
        // Start an infinite loop to repeatedly prompt the user for input until a valid name is entered
        while (true) {
            // Print prompt to ask the user for the student name
            System.out.print("Enter student name: ");
            // Read the user's input as a string
            String name = scanner.nextLine();
            // Check if the name contains only alphabetic characters
            if (name.matches("[a-zA-Z]+")) {
                // If valid, return the name
                return name;
            }
            System.out.println("Invalid name. Names must contain only alphabetic characters. Please try again.");
        }
    }
}
