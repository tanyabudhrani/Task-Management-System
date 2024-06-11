package hk.edu.polyu.comp.comp2021.tms.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.*;
import org.junit.Rule;

public class TMSTest {

    private TMS taskManager;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @Before
    public void setUp() {
        taskManager = new TMS();
        System.setOut(new PrintStream(outContent));
    }

    @After
    public void restoreStreams() {
        System.setOut(originalOut);
    }
    
    @Test
    public void testCreatePrimitiveTask() {
        taskManager.createPrimitiveTask("Task1", "boil-water", 0.3, Collections.singletonList("bowl"));
        Task task = taskManager.getTasks().get("Task1");

        assertNotNull(task);
        assertEquals("Task1", task.getName());
        assertEquals("boil-water", task.getDescription());
        assertEquals(0.3, task.getDuration(), 0.001);
        assertEquals(0, task.getPrerequisites().size());

        // Additional test for an invalid task name
        taskManager.createPrimitiveTask("Task$2", "boil-water", 0.3, Collections.emptyList());
        assertNull(taskManager.getTasks().get("Task$2"));
    }

    @Test
    public void testCreateCompositeTask() {
        // Test with multiple prerequisites
        taskManager.createCompositeTask("composite1", "make-coffee", Arrays.asList("Task1", "primitive2", "composite0"));
        Task task1 = taskManager.getTasks().get("composite1");

        assertNotNull(task1);
        assertEquals("composite1", task1.getName());
        assertEquals("make-coffee", task1.getDescription());
        assertEquals(0, task1.getPrerequisites().size()); // Update to the correct number of prerequisites

        // Additional test for an empty list of subtask names
        taskManager.createCompositeTask("composite2", "make-tea", Collections.emptyList());
        Task task2 = taskManager.getTasks().get("composite2");

        assertNotNull(task2);
        assertEquals("composite2", task2.getName());
        assertEquals("make-tea", task2.getDescription());
        assertEquals(0, task2.getPrerequisites().size()); // Ensure no prerequisites for this task
    }


    @Test
    public void testDeleteTask() {
        taskManager.createPrimitiveTask("Task1", "boil-water", 0.3, Collections.singletonList("bowl"));
        Task task = taskManager.getTasks().get("Task1");

        assertNotNull(task);

        // Test deleting an existing task
        taskManager.deleteTask("Task1");
        assertNull(taskManager.getTasks().get("Task1"));

        // Additional test for deleting a non-existing task
        taskManager.deleteTask("NonExistentTask");
    }

    @Test
    public void testChangeTask() {
        taskManager.createPrimitiveTask("Task1", "boil-water", 0.3, Collections.singletonList("bowl"));
        Task task = taskManager.getTasks().get("Task1");

        assertNotNull(task);

        // Test changing the duration of an existing task
        taskManager.changeTask("Task1", "duration", "0.5");
        assertEquals(0.5, task.getDuration(), 0.001);

        // Additional test for changing a property of a non-existing task
        taskManager.changeTask("NonExistentTask", "duration", "0.7");
    }

    @Test
    public void testPrintTask() {
    	taskManager.createPrimitiveTask("Task1", "boil-water", 0.3, Collections.singletonList("bowl"));
        Task task = taskManager.getTasks().get("Task1");
        taskManager.printTask("Task1");
        
        assertEquals("Task1", task.getName());
        assertEquals("boil-water", task.getDescription());
        assertEquals(0.3, task.getDuration(), 0.001);
        assertEquals(0, task.getPrerequisites().size());
    }
    
    @Test
    public void testPrintTaskNonExistent() {
        // Capture the output
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        // Test printing a task that doesn't exist
        String printedOutput = taskManager.printTask("NonExistentTask").trim();
        assertEquals("Task NonExistentTask not found.", printedOutput);

        // Restore the original System.out
        System.setOut(originalOut);
    }

    
    @Test
    public void testPrintAllTasks() {
    	taskManager.createPrimitiveTask("Task1", "boil-water", 0.3, Collections.singletonList("bowl"));
        Task task = taskManager.getTasks().get("Task1");
        
        assertEquals("Task1", task.getName());
        assertEquals("boil-water", task.getDescription());
        assertEquals(0.3, task.getDuration(), 0.001);
        assertEquals(0, task.getPrerequisites().size());
        
    	taskManager.createCompositeTask("composite1", "make-coffee", Arrays.asList("Task1", "primitive2", "composite0"));
        Task task2 = taskManager.getTasks().get("composite1");
        
        assertNotNull(task2);
        assertEquals("composite1", task2.getName());
        assertEquals("make-coffee", task2.getDescription());
        assertEquals(1, task2.getPrerequisites().size());
        
        
        
        taskManager.printAllTasks();
    }
    
    @Test
    public void testReportDuration() {
    	taskManager.createCompositeTask("composite1", "make-coffee", Arrays.asList("Task1", "primitive2", "composite0"));
        Task task2 = taskManager.getTasks().get("composite1");
        
        assertNotNull(task2);
        assertEquals("composite1", task2.getName());
        assertEquals("make-coffee", task2.getDescription());
        assertEquals(0, task2.getPrerequisites().size());
        
        taskManager.reportDuration("composite1");
        assertEquals(1, taskManager.getTasks().get("composite1").getDuration(), 0.001);
    	
    }
    
    @Test
    public void testReportEarliestFinishTime() {
        taskManager.createCompositeTask("composite1", "make-coffee", Arrays.asList("Task1", "primitive2", "composite0"));

        taskManager.reportEarliestFinishTime("composite1");
        String printedOutput = outContent.toString().trim();

        double actualEarliestFinishTime = Double.parseDouble(printedOutput.split(":")[1].replace("hours", "").trim());
        assertEquals(1.0, actualEarliestFinishTime, 0.001);
    }



    @Test
    public void testDefineBasicCriterion() {
        taskManager.createPrimitiveTask("task1", "boil-water", 0.3, Collections.emptyList());
        taskManager.defineBasicCriterion("criterion1", "duration", ">", "0.1");

        BasicCriterion criterion = taskManager.getBasicCriteria().get("criterion1");

        assertNotNull(criterion);
        assertEquals("criterion1", criterion.getName());
        assertEquals("duration", criterion.getProperty());
        assertEquals(">", criterion.getOp());
        assertEquals("0.1", criterion.getValue());
    }

    @Test
    public void testEvaluateIsPrimitive() {
        taskManager.createPrimitiveTask("task1", "boil-water", 0.3, Collections.emptyList());
        assertTrue(taskManager.evaluateIsPrimitive("task1"));
    }

    @Test
    public void testDefineBinaryCriterion() {
    	TMS taskManager = new TMS();

        // Create some composite criteria
        taskManager.defineBasicCriterion("Criterion1", "name", "contains", "Task1");
        taskManager.defineBasicCriterion("Criterion2", "name", "contains", "Task2");

        // Define a binary criterion
        taskManager.defineBinaryCriterion("Criterion3", "Criterion1", "&&", "Criterion2");

        // Verify that the binary criterion is correctly defined
        assertNotNull(taskManager.getCompositeCriteria().get("Criterion3"));
        assertEquals("&&", taskManager.getCompositeCriteria().get("Criterion3").getLogicOp());
    }
    
    @Test
    public void testDefineNegatedCriterion() {
        TMS taskManager = new TMS();

        // Create some composite criteria
        taskManager.defineBasicCriterion("Criterion1", "name", "contains", "Task1");

        System.out.println("Defined Criteria:");
        taskManager.printAllCriteria();  // Print all defined criteria for debugging

        // Define a negated criterion
        taskManager.defineNegatedCriterion("NegatedCriterion1", "Criterion1");

        System.out.println("Defined Negated Criterion:");
        taskManager.printAllCriteria();  // Print all defined criteria after defining NegatedCriterion1

        // Verify that the negated criterion is correctly defined
        assertNotNull(taskManager.getCompositeCriteria().get("NegatedCriterion1"));
        //assertEquals("Criterion1", taskManager.getCompositeCriteria().get("NegatedCriterion1").getNegatedCriterion().getName());
    }
    
    @Test
    public void testPrintAllCriteria() {
        // Redirect System.out to capture printed output
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        // Create an instance of TML
        TMS taskManager = new TMS();

        // Define some criteria and print them
        taskManager.defineBasicCriterion("Criterion1", "name", "contains", "Task1");
        taskManager.defineBasicCriterion("Criterion2", "name", "contains", "Task2");
        taskManager.defineBinaryCriterion("Criterion3", "Criterion1", "&&", "Criterion2");
        taskManager.printAllCriteria();

        // Check if the expected line is present in the actual output
        String expectedLine = "Criterion: Criterion3";
        assertTrue(outContent.toString().contains(expectedLine));
    }
    
    @Test
    public void testSearch() {
        // Redirect System.out to capture printed output
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        // Create an instance of TML
        TMS taskManager = new TMS();

        // Define some criteria and tasks
        taskManager.defineBasicCriterion("Criterion1", "name", "contains", "Task1"); 
        taskManager.defineBasicCriterion("Criterion2", "name", "contains", "Task2");
        taskManager.createPrimitiveTask("Task1", "boil-water", 0.3, Collections.emptyList());
        taskManager.createPrimitiveTask("Task2", "boil-water", 0.3, Collections.emptyList());
        taskManager.createCompositeTask("composite1", "make-coffee", Arrays.asList("Task1"));
        taskManager.createCompositeTask("composite2", "make-tea", Arrays.asList("Task2", "composite1"));
        taskManager.defineBinaryCriterion("Criterion3", "Criterion1", "&&", "Criterion2");

        // Search for tasks that satisfy the criterion
        taskManager.search("Criterion3");

        // Define the expected output based on the tasks that satisfy the criterion
        String expectedOutput = "Tasks satisfying criterion Criterion3:\n" +
                "Task: Task2\n" +
                "Task: Task1\n" +
                "Task: composite1\n" +
                "Task: composite2\n";

        // Compare the expected output with the actual printed output
        assertEquals(expectedOutput, outContent.toString());
    }
    
}
