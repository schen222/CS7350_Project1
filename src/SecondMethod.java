import java.util.*;

public class SecondMethod {

    public Course nCourse;
    public int num_offered_course; // Number of courses being offered (MAX = 10,000)
    public int num_student; // Number of students (MAX = 100,000)
    public int num_course_per_student; // Number of courses per student (MAX = C)
    public String DIST; // UNIFORM,SKEWED,4-TIERED,NORMAL
    private List<Integer> class_has_been_picked;
    private List<Integer> random_pool;
    private List<Integer> E; // Adjacency list of distinct course conflicts (length = 2M)
    private int[] P; // Pointer for each Course I...
    private int M; // Number of distinct pair-wise course conflicts
    private int T; // Total number of pair-wise course conflicts

    private List<List<Integer>> lists_for_all_courses;

    public SecondMethod(){
        long startTime = System.nanoTime();
        nCourse = new Course();
        set_variables();
        identify_distribution();
        output();
        long endTime = System.nanoTime();
        System.out.println("It took " + (endTime - startTime) + " nanoseconds");
    }

    private void set_variables(){
        num_offered_course = nCourse.getNumOfferedCourse();
        num_student = nCourse.getNumStudent();
        num_course_per_student = nCourse.getNumCoursePerStudent();
        DIST = nCourse.getDistribution();
        class_has_been_picked = new ArrayList<Integer>();
        random_pool = new ArrayList<Integer>();
        E = new ArrayList<Integer>();
        P = new int[num_offered_course + 1];
        M = 0;
        T = 0;
        lists_for_all_courses = new ArrayList<List<Integer>>();
        for(int i=0; i<=num_offered_course; i++){
            List<Integer> list_for_each_course = new ArrayList<Integer>();
            lists_for_all_courses.add(list_for_each_course);
        }
    }

    private void identify_distribution(){
        if("UNIFORM".equals(DIST)) {
            uniform_distribution();
        }
        else if("SKEWED".equals(DIST)) {
            skewed_distribution();
        }
        else if("4-TIERED".equals(DIST)) {
            four_tiered_distribution();
        }
        else if("NORMAL".equals(DIST)) {
            normal_distribution();
        }
    }

    private void uniform_distribution(){
        pick_random_under_uniform();
    }

    private void skewed_distribution(){
        int prob_first_class = (100 + (num_offered_course - 1) * num_offered_course / 2) / num_offered_course;
        int[] prob_each_class = new int[num_offered_course + 1];
        for(int i=2; i<num_offered_course+1; i++){
            prob_each_class[i] = prob_first_class - (i-1);
        }
        for(int times=0; times<prob_first_class; times++){
            random_pool.add(1);
        }
        for(int i=2; i<num_offered_course+1; i++){
            for(int j=0; j<prob_each_class[i]; j++){
                random_pool.add(i);
            }
        }

        pick_random_under_other_distribution();
    }

    private void four_tiered_distribution(){
        double first_25 = (double)num_offered_course/(double)4;
        double second_25 = first_25 * 2;
        double third_25 = first_25 * 3;

        double[] prob_each_class = new double[num_offered_course + 1];
        for(int i=1; i<num_offered_course+1; i++){
            if(i<=first_25){
                prob_each_class[i] = 40 / (int)first_25;
            }
            else if(i>first_25 && i<=second_25){
                prob_each_class[i] = 30 / (int)(second_25 - first_25);
            }
            else if(i>second_25 && i<third_25){
                prob_each_class[i] = 20 / (int)(third_25 - second_25);
            }
            else if(i>third_25 && i<=num_offered_course){
                prob_each_class[i] = 10 / (int)(num_offered_course - third_25);
            }
            for(int times=0; times<prob_each_class[i]; times++){
                random_pool.add(i);
            }
        }
        pick_random_under_other_distribution();
        after_checking_conflict();
    }

    private void normal_distribution(){
        double normal_median = (num_offered_course - 1) / 2;

        Double[] prob_each_class = new Double[num_offered_course + 1];
        for(int i=1; i<num_offered_course+1; i++){
            prob_each_class[i] = ((1/(Math.sqrt(2*Math.PI)))*Math.exp((-Math.pow(i-normal_median,2))/2));
            for(int times=0; times<prob_each_class[i]*100; times++){
                random_pool.add(i);
            }
        }

        pick_random_under_other_distribution();
        after_checking_conflict();
    }

    private void pick_random_under_uniform(){
        for(int student = 0; student < num_student; student++){ // choose class(s) for each student

            class_has_been_picked = new ArrayList<Integer>();
            for(int class_per_student = 0; class_per_student < num_course_per_student; class_per_student++){

                int class_picked = (int)(Math.random() * num_offered_course) + 1; // Randomly choose a class

                while(class_has_been_picked.contains(Integer.toString(class_picked))){ // Check if the one is duplicated
                    class_picked = (int)(Math.random() * num_offered_course) + 1;
                }
                class_has_been_picked.add(class_picked); //Add all distinctly picked classes into list
            }

            check_conflict();
        }
        after_checking_conflict();
    }

    private void pick_random_under_other_distribution(){
        for(int student = 0; student < num_student; student++){ // choose class(s) for each student

            class_has_been_picked = new ArrayList<Integer>();
            for(int class_per_student = 0; class_per_student < num_course_per_student; class_per_student++){

                int class_picked = (int)(Math.random() * random_pool.size()); // Randomly choose a class from 1-10000

                while(class_has_been_picked.contains(Integer.toString(random_pool.get(class_picked)))){ // Check if the one is duplicated
                    class_picked = (int)(Math.random() * random_pool.size());
                }
                class_has_been_picked.add(random_pool.get(class_picked)); //Add all distinctly picked classes into list
            }

            check_conflict();
        }
        after_checking_conflict();
    }

    /*
        This function checks if conflicts are duplicated (already in ArrayLists). If not, these conflicts
        would be added into corresponding ArrayList.
     */
    private void check_conflict(){
        for(int i = 0; i < num_course_per_student; i++){
            int current_position = 0;
            boolean reach_end = false;
            if(current_position != i && reach_end == false){
                lists_for_all_courses.get(class_has_been_picked.get(i)).add(class_has_been_picked.get(current_position));
                current_position++;
                if(current_position > 5){
                    reach_end = true;
                }
            }
            else if(current_position == i && reach_end == false){
                current_position++;
                if(current_position > 5){
                    reach_end = true;
                }
            }

        }
    }

    private void after_checking_conflict(){
        add_conflict_to_E();
    }

    private void add_conflict_to_E(){
        int current_position = 0;
        for(int i=1; i<=num_offered_course; i++){
            if(!lists_for_all_courses.get(i).isEmpty()){
                P[i] = current_position;
                for(int j=0; j<lists_for_all_courses.get(i).size(); j++){
                    E.add(lists_for_all_courses.get(i).get(j));
                    current_position++;
                }
            }
        }
        M = E.size()/2;
        T = (num_course_per_student * (num_course_per_student-1) / 2) * num_student;
    }
    /*
        This function sorts the ArrayList E order by first character of each element,
        then the output would look like 12,13,21,24,31,34...
     */
//    private void sorting(){
//        Collections.sort(E);
//        System.out.println(E);
//    }

    /*
        After sorting, the E looks like 1,1,1,1,1,2,2,3,3,3,4...(Only shows the first character)
        P[1] points at where 1 firstly appears which is E[0],
        For other values, like 2, the way to figure out where 2 firstly appears is check its left element, if it is 2
        then the one is not I am looking for; if it is 1, which means it is 2 firstly appears
     */
//    private void find_P(){
//        P[0] = -1;
//        for(int i=0; i<E.size();i++){
//            if(i==0){
//                P[Character.getNumericValue(E.get(i).charAt(0))] = 0;
//            }
//            else if(i>0){
//                if(E.get(i).charAt(0) != E.get(i-1).charAt(0)){
//                    P[Character.getNumericValue(E.get(i).charAt(0))] = i;
//                }
//            }
//        }
//    }

    /*
        Since I only need one class as our each element in List E and I have used function find_P to set each pointer
        in P[], I just remove the first character of each element in List E.
        And I calculate some values for outputs
     */
//    private void final_adjustment(){
//        for(int i=0; i<E.size(); i++){
//            String replace = E.get(i).substring(1); // Store a string without the first character in original string
//            E.set(i,replace); // Replace the element with new string
//        }
//        M = E.size()/2;
//        T = (num_course_per_student * (num_course_per_student-1) / 2) * num_student;
//    }

    /*
        This function prints out required output
     */
    private void output(){
        System.out.println("There is/are " + num_offered_course + " course(s) being offered");
        System.out.println("There is/are " + num_student + " student(s)");
        System.out.println("There is/are " + num_course_per_student + " course(s) per student");
        System.out.println("The distribution chosen is " + DIST);
        System.out.println("We have " + M + " distinct pair-wise course conflicts");
        System.out.println("We have total " + T + " pair-wise course conflicts");
        System.out.println("Adjacency list of distinct course conflicts " + E);
        System.out.println("Pointer for each course I " + Arrays.toString(P));
    }

    public static void main(String[] args){
        SecondMethod M = new SecondMethod();
    }
}

