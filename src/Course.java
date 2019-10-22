import java.util.*;

public class Course {

    public int num_offered_course; // Number of courses being offered (MAX = 10,000)
    public int num_student; // Number of students (MAX = 100,000)
    public int num_course_per_student; // Number of courses per student (MAX = C)
    public String DIST; // UNIFORM,SKEWED,4-TIERED,NORMAL(Standard Normal Distribution)
    private String[] valid_string_input;

    public Course(){
        initial();
        input();
    }

    public void initial(){
        num_offered_course = 0;
        num_student = 0;
        num_course_per_student = 0;
        DIST = "";
        valid_string_input = new String[]{"UNIFORM","SKEWED","4-TIERED","NORMAL"};
    }

    private void input(){
        Scanner myObj = new Scanner(System.in);
        System.out.println("Enter the number of courses being offered");

//        try
//        {
//            num_offered_course = myObj.nextInt();
//        }
//
//        catch (java.util.InputMismatchException e)
//        {
//            System.out.println("Invalid Input");
//            return;
//        }
        num_offered_course = checkIntInputOne(myObj);
        System.out.println("You Entered " + num_offered_course + " courses being offered\n"); // Check input is received

        myObj = new Scanner(System.in);
        System.out.println("Enter the number of students");
        num_student = checkIntInputTwo(myObj);
        System.out.println("You Entered " + num_student + " students\n"); // Check input is received

        myObj = new Scanner(System.in);
        System.out.println("Enter the number of courses per student");
        num_course_per_student = checkIntInputThree(myObj);
        System.out.println("You Entered " + num_course_per_student + " courses per student\n"); // Check input is received

        myObj = new Scanner(System.in);
        System.out.println("Enter the type of distribution");
        DIST = checkStringInput(myObj);
        System.out.println("You Entered " + DIST + " distribution\n"); // Check input is received
    }

    private int checkIntInputOne(Scanner myObj){
        int buffer = 0;
        int print_checker = 0;

        if(myObj.hasNextInt()){
            buffer = myObj.nextInt();
        }

        while(buffer <= 0 || buffer > 10000){


            if(print_checker == 1) {
                System.out.println("Your input is out of range. Please enter a integer between 1 - 10,000");
            }

            while(!myObj.hasNextInt()) { // Check if the input is an integer
                print_checker = 1;
                myObj = new Scanner(System.in);
                System.out.println("Your input is not an integer. Please enter a integer");
            }

            buffer = myObj.nextInt();

        }
        return buffer;
    }

    private int checkIntInputTwo(Scanner myObj){
        int buffer = 0;
        int print_checker = 0;

        if(myObj.hasNextInt()){
            buffer = myObj.nextInt();
        }

        while(buffer <= 0 || buffer > 100000){


            if(print_checker == 1) {
                System.out.println("Your input is out of range. Please enter a integer between 1 - 10,000");
            }

            while(!myObj.hasNextInt()) { // Check if the input is an integer
                print_checker = 1;
                myObj = new Scanner(System.in);
                System.out.println("Your input is not an integer. Please enter a integer");
            }

            buffer = myObj.nextInt();

        }
        return buffer;
    }

    private int checkIntInputThree(Scanner myObj){
        int buffer = 0;
        int print_checker = 0;

        if(myObj.hasNextInt()){
            buffer = myObj.nextInt();
        }

        while(buffer <= 0 || buffer > num_offered_course){


            if(print_checker == 1) {
                System.out.println("Your input is out of range. Please enter a integer between 1 - 10,000");
            }

            while(!myObj.hasNextInt()) { // Check if the input is an integer
                print_checker = 1;
                myObj = new Scanner(System.in);
                System.out.println("Your input is not an integer. Please enter a integer");
            }

            buffer = myObj.nextInt();

        }
        return buffer;
    }

    private String checkStringInput(Scanner myObj){
        String buffer = myObj.nextLine();

        while(!Arrays.asList(valid_string_input).contains(buffer)){
            System.out.println("Your input is unacceptable. Please enter one of them (UNIFORM,SKEWED,4-TIERED,NORMAL)");
            myObj = new Scanner(System.in);
            buffer = myObj.nextLine();
        }
        return buffer;
    }

    public int getNum_offered_course() {
        return num_offered_course;
    }

    public int getNum_student() {
        return num_student;
    }

    public int getNum_course_per_student() {
        return num_course_per_student;
    }

    public String getDIST() {
        return DIST;
    }
}
