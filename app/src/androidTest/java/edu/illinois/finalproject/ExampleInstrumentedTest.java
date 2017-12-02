package edu.illinois.finalproject;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    private static final String PROBLEMS_JSON = "[  \n" +
            "   {  \n" +
            "      \"problem\":\"1+1\",\n" +
            "      \"answer\":\"2\",\n" +
            "      \"solution\":\"https://firebasestorage.googleapis.com/v0/b/techdemonstration-gajankumar10.appspot.com/o/JPEG_20171116_162753_1438344484.jpg?alt=media&token=2d033e4a-7658-4179-8ef8-e48e9c32223f\"\n" +
            "   },\n" +
            "   {  \n" +
            "      \"problem\":\"What is the meaning of life?\",\n" +
            "      \"answer\":\"\",\n" +
            "      \"solution\":\"https://firebasestorage.googleapis.com/v0/b/techdemonstration-gajankumar10.appspot.com/o/JPEG_20171116_162616_-2106364910.jpg?alt=media&token=c6a7b573-e5be-4143-a02c-9162c6f5dcca\"\n" +
            "   },\n" +
            "   {  \n" +
            "      \"problem\":\"Given an arbitrary compact gauge group, does a non-trivial quantum Yang–Mills theory with a finite mass gap exist?\",\n" +
            "      \"answer\":\"If you can answer this, you will win a million dollars (for real)\",\n" +
            "      \"solution\":\"https://firebasestorage.googleapis.com/v0/b/techdemonstration-gajankumar10.appspot.com/o/JPEG_20171116_162616_-2106364910.jpg?alt=media&token=c6a7b573-e5be-4143-a02c-9162c6f5dcca\"\n" +
            "   }\n" +
            "]";

    private static final int INITIAL_NUM_COURSES = 2;
    private static final int INITIAL_NUM_UNITS = 2;
    private static final int WAIT_TIME_UPLOAD_TO_DATABASE_SEC = 2;
    private static final int WAIT_TIME_READ_FROM_DATABASE_SEC = 7;
    private static final String NEXT_INDEX_IN_PROBLEMS_LIST = "3";
    private static final String COURSES_ROOT = "Courses";
    private static final String UNITS_ROOT = "Units";
    private static final String PROBLEMS_ROOT = "Problems";
    private static final String TAG = ExampleInstrumentedTest.class.getSimpleName();
    private static String SAMPLE_COURSE_KEY;
    private static String SAMPLE_COURSE_OF_UNITS_KEY;
    private static String SAMPLE_UNIT_OF_PROBLEMS_KEY;

    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("edu.illinois.finalproject", appContext.getPackageName());
    }

    @Test
    public void addCoursesToDatabaseTest() throws Exception {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference coursesRef = database.getReference(COURSES_ROOT);

        //Create and initialize courses
        Course[] courses = new Course[INITIAL_NUM_COURSES];
        for (int i = 0; i < courses.length; i++) {
            courses[i] = new Course();
            courses[i].setName(String.valueOf(i));
        }

        //Push courses to FB database
        for (Course course : courses) {
            coursesRef.push().setValue(course);
        }

        final CountDownLatch countDownLatch = new CountDownLatch(1);
        countDownLatch.await(WAIT_TIME_UPLOAD_TO_DATABASE_SEC, TimeUnit.SECONDS);
    }

    @Test
    public void addCourseOfUnitsToDatabase() throws Exception {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference unitsRef = database.getReference(UNITS_ROOT);

        //create and initialize an array of Units
        Unit[] units = new Unit[INITIAL_NUM_UNITS];
        for (int i = 0; i < units.length; i++) {
            units[i] = new Unit();
            units[i].setName(String.valueOf(i));
        }

        //initialize CcourseOfUnits using the array of units
        CourseOfUnits courseOfUnits = new CourseOfUnits(Arrays.asList(units));

        //push courseOfUnits to FB database
        unitsRef.push().setValue(courseOfUnits);

        final CountDownLatch countDownLatch = new CountDownLatch(1);
        countDownLatch.await(WAIT_TIME_UPLOAD_TO_DATABASE_SEC, TimeUnit.SECONDS);
    }

    @Test
    public void addUnitOfProblemsToDatabaseTest() throws Exception {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference problemsRef = database.getReference(PROBLEMS_ROOT);

        //Create unitOfProblems by loading in array of problems generated from sample static JSON
        Gson gson = new Gson();
        Problem[] problems = gson.fromJson(PROBLEMS_JSON, Problem[].class);
        UnitOfProblems unitOfProblems = new UnitOfProblems(Arrays.asList(problems));

        problemsRef.push().setValue(unitOfProblems);

        final CountDownLatch countDownLatch = new CountDownLatch(1);
        countDownLatch.await(WAIT_TIME_UPLOAD_TO_DATABASE_SEC, TimeUnit.SECONDS);
    }

    @Test
    public void readCoursesFromDatabase() throws Exception {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference coursesRef = database.getReference(COURSES_ROOT);

        coursesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<Map<String, Course>> coursesGti =
                        new GenericTypeIndicator<Map<String, Course>>() {};
                Map<String, Course> keyToCoursesMap = dataSnapshot.getValue(coursesGti);

                Log.d(TAG, "-------DISPLAYING COURSES-------");
                assert keyToCoursesMap != null;
                for (Map.Entry<String, Course> entry : keyToCoursesMap.entrySet()) {
                    //Save key of first course
                    if (SAMPLE_COURSE_KEY == null) {
                        SAMPLE_COURSE_KEY = entry.getKey();
                    }
                    Log.d(TAG, entry.getKey());
                    Log.d(TAG, entry.getValue().toString());
                }
                countDownLatch.countDown();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        countDownLatch.await(WAIT_TIME_READ_FROM_DATABASE_SEC, TimeUnit.SECONDS);
    }

    @Test
    public void readCoursesOfUnitsFromDatabase() throws Exception {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference unitsRef = database.getReference(UNITS_ROOT);

        unitsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<Map<String, CourseOfUnits>> coursesGti =
                        new GenericTypeIndicator<Map<String, CourseOfUnits>>() {};
                Map<String, CourseOfUnits> keyToCoursesOfUnitsMap = dataSnapshot.getValue(coursesGti);

                Log.d(TAG, "-------DISPLAYING COURSEOFUNIT-------");
                assert keyToCoursesOfUnitsMap != null;
                for (Map.Entry<String, CourseOfUnits> entry : keyToCoursesOfUnitsMap.entrySet()) {
                    //Save first CourseOfUnits Key to eventually store under the appropriate course
                    if (SAMPLE_COURSE_OF_UNITS_KEY == null) {
                        SAMPLE_COURSE_OF_UNITS_KEY = entry.getKey();
                    }

                    Log.d(TAG, entry.getKey());
                    Log.d(TAG, entry.getValue().getUnitsInCourse().toString());
                }
                countDownLatch.countDown();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        countDownLatch.await(WAIT_TIME_READ_FROM_DATABASE_SEC, TimeUnit.SECONDS);
    }

    @Test
    public void readUnitOfProblemsFromDatabase() throws Exception {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference problemsRef = database.getReference(PROBLEMS_ROOT);

        problemsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<Map<String, UnitOfProblems>> unitProblemsGti =
                        new GenericTypeIndicator<Map<String, UnitOfProblems>>() {};
                Map<String, UnitOfProblems> keyToUnitOfProblemsMap = dataSnapshot.getValue(unitProblemsGti);

                Log.d(TAG, "-------DISPLAYING UNITOFPROBLEM-------");
                assert keyToUnitOfProblemsMap != null;
                for (Map.Entry<String, UnitOfProblems> entry : keyToUnitOfProblemsMap.entrySet()) {
                    //Save first key to UnitOfProblems to eventually save to appropriate unit
                    if (SAMPLE_UNIT_OF_PROBLEMS_KEY == null) {
                        SAMPLE_UNIT_OF_PROBLEMS_KEY = entry.getKey();
                    }
                    Log.d(TAG, entry.getKey());
                    Log.d(TAG, entry.getValue().getProblems().toString());
                }
                countDownLatch.countDown();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        countDownLatch.await(WAIT_TIME_READ_FROM_DATABASE_SEC, TimeUnit.SECONDS);
    }

    @Test
    public void updateCourseToUnitToProblemConnectionTest() throws Exception {
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference coursesRef = database.getReference(COURSES_ROOT);
        coursesRef.child(SAMPLE_COURSE_KEY).child("keyToCourseOfUnits").setValue(SAMPLE_COURSE_OF_UNITS_KEY);

        DatabaseReference unitsRef = database.getReference(UNITS_ROOT);
        unitsRef.child(SAMPLE_COURSE_OF_UNITS_KEY)
                .child("unitsInCourse")
                .child("0")
                .child("keyToUnitOfProblems").setValue(SAMPLE_UNIT_OF_PROBLEMS_KEY);

        final CountDownLatch countDownLatch = new CountDownLatch(1);
        countDownLatch.await(WAIT_TIME_UPLOAD_TO_DATABASE_SEC, TimeUnit.SECONDS);
    }

    @Test
    public void updateUnitWithNewProblemTest() throws Exception {
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference problemsRef = database.getReference(PROBLEMS_ROOT);
        Problem problemToAddToDatabase = new Problem("2+2", "5", "Because I said so");
        problemsRef.child(SAMPLE_UNIT_OF_PROBLEMS_KEY).child("problems")
                .child(NEXT_INDEX_IN_PROBLEMS_LIST).setValue(problemToAddToDatabase);

        final CountDownLatch countDownLatch = new CountDownLatch(1);
        countDownLatch.await(WAIT_TIME_UPLOAD_TO_DATABASE_SEC, TimeUnit.SECONDS);
    }


}
