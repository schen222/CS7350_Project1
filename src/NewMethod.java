import java.util.*;


/**
 * Algorithm Project
 *
 * @author Siyuan Chen
 * @date 2019/12/05
 */

public class NewMethod{

    private Course nCourse;

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
     * UNIFORM,SKEWED,4-TIERED,NORMAL
     */
    private String distribution;
    private List<Integer> classHasBeenPicked;
    private List<Integer> randomPool;

    /**
     * Adjacency list of distinct course conflicts (length = 2M)
     */
    private List<Integer> E;

    /**
     * Pointer for each Course I...
     */
    private int[] P;

    /**
     * Number of distinct pair-wise course conflicts
     */
    private int M;

    /**
     * Total number of pair-wise course conflicts
     */
    private int T;

    private int[] attendedCourse;

    private int[][] checkBox;

    /**
     * variables for part2 below
     * Used to store the degree of each node(course)
     */
    private static int[] degree;
    private static ArrayList<Integer> sortedNode;

     /**
     * Used to store node ordered by degree from big to small
     */
    private LinkedList<Integer> degreeOrder;

    private String ordering;

    private int[] color;

    private static int maxDegree;

    private NewMethod(){
        long startTime = System.nanoTime();
        nCourse = new Course();
        setVariables();
        identifyDistribution();
        identifyOrdering();
        output();
        long endTime = System.nanoTime();
        System.out.println("It took " + (endTime - startTime) + " nanoseconds");
        System.out.println(Arrays.toString(attendedCourse));
    }

    /**
     * This function is used to initialize all variables that are going to be used in this program
     */
    private void setVariables(){

        //Part 1
        numOfferedCourse = nCourse.getNumOfferedCourse();
        numStudent = nCourse.getNumStudent();
        numCoursePerStudent = nCourse.getNumCoursePerStudent();
        checkBox = new int[numOfferedCourse +1][numOfferedCourse +1];
        distribution = nCourse.getDistribution();
        classHasBeenPicked = new ArrayList<>();
        randomPool = new ArrayList<>();
        E = new ArrayList<>();
        P = new int[numOfferedCourse + 1];
        M = 0;
        T = 0;
        attendedCourse = new int[numOfferedCourse +1];

        //Part 2
        degree = new int[numOfferedCourse+1];
        sortedNode = new ArrayList<>();
        degreeOrder = new LinkedList<>();
        ordering = nCourse.getOrdering();
    }

    private void identifyDistribution(){
        if("UNIFORM".equals(distribution)) {
            uniformDistribution();
        }
        else if("SKEWED".equals(distribution)) {
            skewedDistribution();
        }
        else if("4-TIERED".equals(distribution)) {
            fourTieredDistribution();
        }
        else if("NORMAL".equals(distribution)) {
            normalDistribution();
        }
    }

    private void uniformDistribution(){
        pickRandomUnderUniform();
        afterCheckingConflict();
    }

    private void skewedDistribution(){
        double probFirstClass = (100 + (double)(numOfferedCourse - 1) * numOfferedCourse / 2) / (double) numOfferedCourse;
        double[] probEachClass = new double[numOfferedCourse + 1];
        for(int i = 2; i< numOfferedCourse +1; i++){
            probEachClass[i] = probFirstClass - (i-1);
        }
        for(int times=0; times<probFirstClass; times++){
            randomPool.add(1);
        }
        for(int i = 2; i< numOfferedCourse +1; i++){
            for(int j=0; j<probEachClass[i]*100; j++){
                randomPool.add(i);
            }
        }

        pickRandomUnderOtherDistribution();
        afterCheckingConflict();
    }

    private void fourTieredDistribution(){
        double first25 = (double) numOfferedCourse /(double)4;
        double second25 = first25 * 2;
        double third25 = first25 * 3;

        double[] probEachClass = new double[numOfferedCourse + 1];
        for(int i = 1; i< numOfferedCourse +1; i++){
            if(i<=first25){
                probEachClass[i] = 40 / first25;
            }
            else if(i>first25 && i<=second25){
                probEachClass[i] = 30 / (second25 - first25);
            }
            else if(i>second25 && i<third25){
                probEachClass[i] = 20 / (third25 - second25);
            }
            else if(i>third25 && i<= numOfferedCourse){
                probEachClass[i] = 10 / (numOfferedCourse - third25);
            }
            for(int times=0; times<probEachClass[i]*10000; times++){
                randomPool.add(i);
            }
        }
        pickRandomUnderOtherDistribution();
        afterCheckingConflict();
    }

    private void normalDistribution(){
        double normalMedian = (double)(numOfferedCourse - 1) / 2;

        Double[] probEachClass = new Double[numOfferedCourse + 1];
        for(int i = 1; i< numOfferedCourse +1; i++){
            probEachClass[i] = ((1/(Math.sqrt(2*Math.PI)))*Math.exp((-Math.pow(i-normalMedian,2))/2));
            for(int times=0; times<probEachClass[i]*100; times++){
                randomPool.add(i);
            }
        }

        pickRandomUnderOtherDistribution();
        afterCheckingConflict();

    }

    private void pickRandomUnderUniform(){

        /*
         * choose class(s) for each student
         */
        for(int student = 0; student < numStudent; student++){

            classHasBeenPicked = new ArrayList<>();
            for(int classPerStudent = 0; classPerStudent < numCoursePerStudent; classPerStudent++){

                /*
                 * Randomly choose a class
                 */
                int classPicked = (int)(Math.random() * numOfferedCourse) + 1;

                /*
                 * Check if the one is duplicated
                 */
                while(classHasBeenPicked.contains(classPicked)){
                    classPicked = (int)(Math.random() * numOfferedCourse) + 1;
                }

                /*
                 * Add all distinctly picked classes into list
                 */
                classHasBeenPicked.add(classPicked);
                attendedCourse[classPicked]++;
            }

            checkConflict();
        }
    }

    private void pickRandomUnderOtherDistribution(){

        /*
         * choose class(s) for each student
         */
        for(int student = 0; student < numStudent; student++){

            classHasBeenPicked = new ArrayList<>();
            for(int classPerStudent = 0; classPerStudent < numCoursePerStudent; classPerStudent++){

                /*
                 * Randomly choose a class from 1-10000
                 */
                int classPicked = (int)(Math.random() * randomPool.size());

                /*
                 * Check if the one is duplicated
                 */
                while(classHasBeenPicked.contains(randomPool.get(classPicked))){
                    classPicked = (int)(Math.random() * randomPool.size());
                }

                /*
                 * Add all distinctly picked classes into list
                 */
                classHasBeenPicked.add(randomPool.get(classPicked));
                attendedCourse[randomPool.get(classPicked)]++;
            }

            checkConflict();
        }
    }

    /**
     *  This function checks if conflicts are duplicated (already in ArrayList E). If not, these conflicts
     *  (includes its reserve like 12,21) would be added into unsorted ArrayList E.
     */
    private void checkConflict(){
        for(int i = 0; i< numCoursePerStudent; i++){
            for(int j = i+1; j< numCoursePerStudent; j++){
                checkBox[classHasBeenPicked.get(i)][classHasBeenPicked.get(j)] = 1;
                checkBox[classHasBeenPicked.get(j)][classHasBeenPicked.get(i)] = 1;
            }
        }
    }

    private void afterCheckingConflict(){
        int currentPosition = 0;
        for(int i = 1; i< numOfferedCourse +1; i++){
            P[i] = currentPosition;
            for(int j = 1; j< numOfferedCourse +1; j++){
                if(checkBox[i][j]==1){
                    E.add(j);
                    currentPosition++;
                }
            }
        }
        M = E.size()/2;
        T = (numCoursePerStudent * (numCoursePerStudent -1) / 2) * numStudent;
    }

    private void getDegree(){
        for(int i = 1; i < numOfferedCourse +1; i++){
            for(int j = 1; j< numOfferedCourse +1; j++){
                if(checkBox[i][j] == 1) {degree[i]++;}
            }
        }
        System.out.println("degree: " + Arrays.toString(degree));
    }

    private void identifyOrdering(){
        String smallest = "SMALLEST";
        String powell = "POWELL";
        String random = "RANDOM";
        String myChoice = "LARGEST";
        if(smallest.equals(ordering)){
            smallestLastOrdering();
        }
        else if(powell.equals(ordering)){
            welshPowellOrdering();
        }
        else if(random.equals(ordering)){
            uniformRandomOrdering();
        }
        else if(myChoice.equals(ordering)){
            largestLastOrdering();
        }
    }

    private void smallestLastOrdering(){
        getDegree();
    }

    private void welshPowellOrdering(){
        getDegree();
        sortDescendingDegree();
        System.out.println("size" + sortedNode.size());
        coloring();
    }

    private static void sortDescendingDegree(){

        int arrayLength = degree.length;
        int max;
        int maxIndex;
        for(int i = 1; i < arrayLength; i++){
            max = 0;
            maxIndex = 0;
            for(int j = 1; j < arrayLength; j++){
                if(degree[j] > max){
                    max = degree[j];
                    maxIndex = j;
                }
            }
            if(max != 0 && maxIndex != 0){
                if(i==1){
                    maxDegree = max;
                }
                sortedNode.add(maxIndex);
                degree[maxIndex]=0;
            }
        }
        System.out.println("sorted: " + sortedNode);
    }

    private void uniformRandomOrdering(){
        getDegree();
        sortRandomDegree();
        coloring();
    }

    private void sortRandomDegree(){
        int arrayLength = degree.length;
        int random;
        maxDegree = 0;
        for(int i=0; i<arrayLength; i++){
            boolean notAllZero = false;
            for(int j=0; j<arrayLength; j++){
                if(degree[j] != 0){
                    notAllZero = true;
                }
            }
            random = (int)(Math.random()*arrayLength);
            while(degree[random]==0 && notAllZero){
                random = (int)(Math.random()*arrayLength);
            }
            if(degree[random] != 0){
                if(degree[random] > maxDegree){
                    maxDegree = degree[random];
                }
                sortedNode.add(random);
                degree[random] = 0;
            }
        }
        System.out.println("sorted: " + sortedNode);
    }

    private void largestLastOrdering(){
        getDegree();
    }

    private void coloring(){
        color = new int[sortedNode.size()];
        Arrays.fill(color,0);
        int arrayListSize = sortedNode.size();

        int[][] coloringForEachNode = new int[arrayListSize][maxDegree+2];
        /*
         * colour the first vertex color 1
         */
        int colorNumber = 1;
        color[0] = colorNumber;

        /*
         * color all the vertices not connected to the coloured vertex, with the same color
         */

        for(int i=1; i<arrayListSize; i++){
            for(int j=0; j<i; j++){
                if(checkBox[sortedNode.get(i)][sortedNode.get(j)]==1){
                    coloringForEachNode[i][color[j]] = 1;
                }
            }
            boolean isFirst = true;
            for(int k=1; k<maxDegree+2; k++){
                if(isFirst && coloringForEachNode[i][k] == 0){
                    color[i] = k;
                    isFirst = false;
                }
            }
        }
    }
    /**
     *  This function prints out required output
     */
    private void output(){
        System.out.println("There is/are " + numOfferedCourse + " course(s) being offered");
        System.out.println("There is/are " + numStudent + " student(s)");
        System.out.println("There is/are " + numCoursePerStudent + " course(s) per student");
        System.out.println("The distribution chosen is " + distribution);
        System.out.println("We have " + M + " distinct pair-wise course conflicts");
        System.out.println("We have total " + T + " pair-wise course conflicts");
        System.out.println("Adjacency list of distinct course conflicts " + E);
        System.out.println("Pointer for each course I " + Arrays.toString(P));
        System.out.println("color: "+Arrays.toString(color));
    }

    public static void main(String[] args){
        NewMethod d = new NewMethod();
    }
}

