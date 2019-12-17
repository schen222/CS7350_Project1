import java.util.*;

public class Course {

    /**
     * Number of courses being offered (MAX = 10,000)
     */
    private int numOfferedCourse;

    /**
     * Number of students (MAX = 100,000)
     */
    private int numStudent;

    /**
     * Number of courses per student (MAX = C)
     */
    private int numCoursePerStudent;

    /**
     * UNIFORM,SKEWED,4-TIERED,NORMAL(Standard Normal Distribution)
     */
    private String distribution;
    private String[] validDistributionInput;

    /**
     * SMALLEST, POWELL, RANDOM, LARGEST
     */
    private String ordering;
    private String[] validOrderingInput;

    public Course(){
        initial();
        input();
    }

    private void initial(){
        numOfferedCourse = 0;
        numStudent = 0;
        numCoursePerStudent = 0;
        distribution = "";
        validDistributionInput = new String[]{"UNIFORM","SKEWED","4-TIERED","NORMAL"};
        ordering = "";
        validOrderingInput = new String[]{"SMALLEST","POWELL","RANDOM","ORIGIN"};
    }

    private void input(){
        Scanner myObj = new Scanner(System.in);
        System.out.println("Enter the number of courses being offered");

        numOfferedCourse = checkIntInputOne(myObj);

        /*
         * Check input is received
         */
        System.out.println("You Entered " + numOfferedCourse + " courses being offered\n");

        myObj = new Scanner(System.in);
        System.out.println("Enter the number of students");
        numStudent = checkIntInputTwo(myObj);

        /*
         * Check input is received
         */
        System.out.println("You Entered " + numStudent + " students\n");

        myObj = new Scanner(System.in);
        System.out.println("Enter the number of courses per student");
        numCoursePerStudent = checkIntInputThree(myObj);

        /*
         * Check input is received
         */
        System.out.println("You Entered " + numCoursePerStudent + " courses per student\n");

        myObj = new Scanner(System.in);
        System.out.println("Enter the type of distribution");
        distribution = checkDistributionInput(myObj);

        /*
         * Check input is received
         */
        System.out.println("You Entered " + distribution + " distribution\n");

        myObj = new Scanner(System.in);
        System.out.println("Enter the type of ordering algorithm");
        ordering = checkOrderingInput(myObj);

        /*
         * Check input is received
         */
        System.out.println("You Entered " + ordering + " ordering algorithm\n");
    }

    private int checkIntInputOne(Scanner myObj){
        int buffer = 0;
        int printChecker = 0;

        if(myObj.hasNextInt()){
            buffer = myObj.nextInt();
        }

        while(buffer <= 0 || buffer > 10000){


            if(printChecker == 1) {
                System.out.println("Your input is out of range. Please enter a integer between 1 - 10,000");
            }

            /*
             * Check if the input is an integer
             */
            while(!myObj.hasNextInt()) {
                printChecker = 1;
                myObj = new Scanner(System.in);
                System.out.println("Your input is not an integer. Please enter a integer");
            }

            buffer = myObj.nextInt();

        }
        return buffer;
    }

    private int checkIntInputTwo(Scanner myObj){
        int buffer = 0;
        int printChecker = 0;

        if(myObj.hasNextInt()){
            buffer = myObj.nextInt();
        }

        while(buffer <= 0 || buffer > 100000){


            if(printChecker == 1) {
                System.out.println("Your input is out of range. Please enter a integer between 1 - 10,000");
            }

            /*
             * Check if the input is an integer
             */
            while(!myObj.hasNextInt()) {
                printChecker = 1;
                myObj = new Scanner(System.in);
                System.out.println("Your input is not an integer. Please enter a integer");
            }

            buffer = myObj.nextInt();

        }
        return buffer;
    }

    private int checkIntInputThree(Scanner myObj){
        int buffer = 0;
        int printChecker = 0;

        if(myObj.hasNextInt()){
            buffer = myObj.nextInt();
        }

        while(buffer <= 0 || buffer > numOfferedCourse){


            if(printChecker == 1) {
                System.out.println("Your input is out of range. Please enter a integer between 1 - 10,000");
            }

            /*
             * Check if the input is an integer
             */
            while(!myObj.hasNextInt()) {
                printChecker = 1;
                myObj = new Scanner(System.in);
                System.out.println("Your input is not an integer. Please enter a integer");
            }

            buffer = myObj.nextInt();

        }
        return buffer;
    }

    private String checkDistributionInput(Scanner myObj){
        String buffer = myObj.nextLine();

        while(!Arrays.asList(validDistributionInput).contains(buffer)){
            System.out.println("Your input is unacceptable. Please enter one of them (UNIFORM,SKEWED,4-TIERED,NORMAL)");
            myObj = new Scanner(System.in);
            buffer = myObj.nextLine();
        }
        return buffer;
    }

    private String checkOrderingInput(Scanner myObj){
        String buffer = myObj.nextLine();

        while(!Arrays.asList(validOrderingInput).contains(buffer)){
            System.out.println("Your input is unacceptable. Please enter one of them (UNIFORM,SKEWED,4-TIERED,NORMAL)");
            myObj = new Scanner(System.in);
            buffer = myObj.nextLine();
        }
        return buffer;
    }

    public int getNumOfferedCourse() {
        return numOfferedCourse;
    }

    public int getNumStudent() {
        return numStudent;
    }

    public int getNumCoursePerStudent() {
        return numCoursePerStudent;
    }

    public String getDistribution() {
        return distribution;
    }

    public String getOrdering(){
        return ordering;
    }
}
