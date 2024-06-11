package hk.edu.polyu.comp.comp2021.tms.model;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

class TMS implements Serializable {
    private static final long serialVersionUID = 1L;

    private Map<String, Task> tasks;
    private Map<String, CompositeCriterion> compositeCriteria;
    private Map<String, BasicCriterion> basicCriteria;

    public Map<String, Task> getTasks() {
        return tasks;
    }
    public Map<String, CompositeCriterion> getCompositeCriteria() {
        return compositeCriteria;
    }
    public Map<String, BasicCriterion> getBasicCriteria() {
        return basicCriteria;
    }

    public TMS() {
        this.tasks = new HashMap<>();
        this.compositeCriteria = new HashMap<>();
        this.basicCriteria = new HashMap<>();
    }

    public void createPrimitiveTask(String name, String description, double d, List<String> prerequisiteNames) {
        Task newTask = new Task(name, description, 0);

        //check if the name is in the correct format
        boolean nameCheck = true;
        for (int i = 0; i < name.length(); i++){
            if (!(Character.isDigit(name.charAt(i)) || Character.isLetter(name.charAt(i)))){
                System.out.println("Not digit or letter");
                nameCheck = false;
                break;
            }
        }
        if (nameCheck == true){
            nameCheck = (name.length() < 8) && !Character.isDigit(name.charAt(0));
        }
        if (nameCheck == false){
            System.out.println("Name must be at least 8 characters long,\nMust contain only letters and digits,\nAnd must not start with a Digit");
            return;
        }

        //check if the duration is in the correct format
        if (d < 0){
            System.out.println("Duration cannot be negative");
            return;
        }

        //check if the description is in the correct format
        for (int i = 0; i < description.length(); i++){
            if (!(Character.isDigit(description.charAt(i)) || Character.isLetter(description.charAt(i)) || description.charAt(i) == '-')) {
                System.out.println("Not digit or letter or a hyphen");
                return;
            }
        }

        tasks.put(name, newTask);
        if (d > 0) {
            newTask.setDuration(d);
        }

        for (String prerequisiteName : prerequisiteNames) {
            Task prerequisite = tasks.get(prerequisiteName);
            if (prerequisite != null) {
                newTask.addPrerequisite(prerequisite);
            }
        }
    }

    public void createCompositeTask(String name, String description, List<String> subtaskNames) {
        Task newTask = new Task(name, description, 0); // Duration for composite tasks is initially set to 0

        // Calculate duration based on the durations of subtasks
        double compositeDuration = 1;
        for (String subtaskName : subtaskNames) {
            Task subtask = tasks.get(subtaskName);
            if (subtask != null) {
                newTask.addPrerequisite(subtask);
                compositeDuration = Math.max(compositeDuration, calculateDuration(subtask));
            }
        }
        newTask.setDuration(compositeDuration); // Set the duration of the composite task
        tasks.put(name, newTask);
    }

    public void deleteTask(String name) {
        Task taskToDelete = tasks.get(name);
        if (taskToDelete != null) {
            tasks.remove(name);

            for (Task task : tasks.values()) {
                task.removePrerequisite(taskToDelete);
            }
        }
    }

    public void changeTask(String name, String property, String newValue) {
        Task taskToChange = tasks.get(name);
        if (taskToChange != null) {
            switch (property.toLowerCase()) {
                case "name":
                    taskToChange.setName(newValue);
                    break;
                case "description":
                    taskToChange.setDescription(newValue);
                    break;
                case "duration":
                    taskToChange.setDuration(Double.parseDouble(newValue));
                    break;
                case "prerequisites":
                    List<Task> newPrerequisites = Arrays.asList(newValue.split(",")).stream()
                            .map(prerequisiteName -> tasks.get(prerequisiteName.trim()))
                            .filter(Objects::nonNull)
                            .collect(Collectors.toList());
                    taskToChange.setPrerequisites(newPrerequisites);
                    break;
                case "subtasks":
                    List<Task> newSubtasks = Arrays.asList(newValue.split(",")).stream()
                            .map(subtaskName -> tasks.get(subtaskName.trim()))
                            .filter(Objects::nonNull)
                            .collect(Collectors.toList());
                    taskToChange.setPrerequisites(newSubtasks);
                    break;
            }
        }
    }

    public String printTask(String name) {
        Task task = tasks.get(name);
        if (task != null) {
            StringBuilder result = new StringBuilder("Task Information:\n");
            result.append("Name: ").append(task.getName()).append("\n");
            result.append("Description: ").append(task.getDescription()).append("\n");
            result.append("Duration: ").append(task.getDuration()).append(" hours\n");
            if (evaluateIsPrimitive(name)) {
                result.append("Prerequisites: ");
                for (Task prerequisite : task.getPrerequisites()) {
                    result.append(prerequisite.getName()).append(", ");
                }
            } else {
                result.append("Subtasks: ");
                for (Task subtask : task.getPrerequisites()) {
                    result.append(subtask.getName()).append(", ");
                }
            }
            return result.toString();
        } else {
            return "Task " + name + " not found.";
        }
    }

    public void printAllTasks() {
        for (Task task : tasks.values()) {
            printTask(task.getName());
            System.out.println("------------------------");
        }
    }

    public String reportDuration(String name) {
        Task task = tasks.get(name);
        if (task != null) {
            double duration = calculateDuration(task);
            return "Duration of task " + name + ": " + duration + " hours";
        } else {
            return "Task " + name + " not found.";
        }
    }

    private double calculateDuration(Task task) {
        if (task.getPrerequisites().isEmpty()) {
            return task.getDuration();  // For simple tasks
        } else {
            double maxSubtaskDuration = 0;
            for (Task subtask : task.getPrerequisites()) {
                double subtaskDuration = calculateDuration(subtask);
                if (subtaskDuration > maxSubtaskDuration) {
                    maxSubtaskDuration = subtaskDuration;
                }
            }
            return maxSubtaskDuration;
        }
    }

    public void reportEarliestFinishTime(String name) {
        Task task = tasks.get(name);
        if (task != null) {
            double earliestFinishTime = calculateEarliestFinishTime(task);
            System.out.println("Earliest finish time of task " + name + ": " + earliestFinishTime + " hours");
        }
    }


    private double calculateEarliestFinishTime(Task task) {
        double earliestFinishTime = task.getDuration();
        for (Task prerequisite : task.getPrerequisites()) {
            double prerequisiteFinishTime = calculateEarliestFinishTime(prerequisite);
            if (prerequisiteFinishTime > earliestFinishTime) {
                earliestFinishTime = prerequisiteFinishTime;
            }
        }
        return earliestFinishTime;
    }

    public void defineBasicCriterion(String criterionName, String property, String op, String value) {
        BasicCriterion basicCriterion = null;

        switch (property.toLowerCase()) {
            case "name":
            case "description":
                if (op.contains("contains") && value.startsWith("\"") && value.endsWith("\"")) {
                    basicCriterion = new BasicCriterion(criterionName, property, op, value);
                }
                break;
            case "duration":
                if (isValidDurationOperator(op) && isValidRealValue(value)) {
                    basicCriterion = new BasicCriterion(criterionName, property, op, value);
                }
                break;
            case "prerequisites":
            case "subtasks":
                if (op.contains("contains") && !value.isEmpty()) {
                    basicCriterion = new BasicCriterion(criterionName, property, op, value);
                }
                break;
        }

        if (basicCriterion != null) {
            basicCriteria.put(criterionName, basicCriterion);
        }
    }


    private boolean isValidDurationOperator(String op) {
        String[] validOperators = { ">", "<", ">=", "<=", "==", "!=" };
        return Arrays.asList(validOperators).contains(op);
    }

    private boolean isValidRealValue(String value) {
        try {
            Double.parseDouble(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }


    public boolean evaluateIsPrimitive(String name) {
        Task task = tasks.get(name);
        return task != null && task.getPrerequisites().isEmpty();
    }



    public void defineNegatedCriterion(String name1, String name2) {
        CompositeCriterion criterion2 = compositeCriteria.get(name2);
        if (criterion2 != null) {
            CompositeCriterion negatedCriterion = new CompositeCriterion(name1);
            negatedCriterion.setNegatedCriterion(criterion2);
            compositeCriteria.put(name1, negatedCriterion);
        } else {
            // If the base criterion is not found, create a negated criterion with a placeholder name
            CompositeCriterion negatedCriterion = new CompositeCriterion(name1);
            negatedCriterion.setNegatedCriterion(new CompositeCriterion("Placeholder"));
            compositeCriteria.put(name1, negatedCriterion);
            System.out.println("Base Criterion " + name2 + " not found.");
        }
    }



    public void defineBinaryCriterion(String name1, String name2, String logicOp, String name3) {
        CompositeCriterion criterion2 = compositeCriteria.get(name2);
        CompositeCriterion criterion3 = compositeCriteria.get(name3);

        CompositeCriterion binaryCriterion = new CompositeCriterion(name1);
        binaryCriterion.setLogicOp(logicOp);
        binaryCriterion.setSubCriteria(Arrays.asList(criterion2, criterion3));
        compositeCriteria.put(name1, binaryCriterion);
    }



    public void printAllCriteria() {
        for (CompositeCriterion criterion : compositeCriteria.values()) {
            System.out.println("Criterion: " + criterion.getName());
            printCriterionDetails(criterion, 1);
            System.out.println("------------------------"); // Add a separator for better readability
        }
    }

    private void printCriterionDetails(CompositeCriterion criterion, int indentation) {
        if (criterion == null) {
            return;  // Skip if the criterion is null
        }

        String spaces = " ".repeat(indentation * 2);
        System.out.println(spaces + "Criterion: " + criterion.getName());
        System.out.println(spaces + "  LogicOp: " + criterion.getLogicOp()); // Print logical operator for all criteria

        for (CompositeCriterion subCriterion : criterion.getSubCriteria()) {
            printCriterionDetails(subCriterion, indentation + 1);
        }

        if (criterion.getNegatedCriterion() != null) {
            System.out.println(spaces + "  Negated Criterion: " + criterion.getNegatedCriterion().getName());
            printCriterionDetails(criterion.getNegatedCriterion(), indentation + 1);
        }
    }




    public void search(String criterionName) {
        CompositeCriterion criterion = compositeCriteria.get(criterionName);
        if (criterion != null) {
            System.out.println("Tasks satisfying criterion " + criterionName + ":");
            for (Task task : tasks.values()) {
                if (task != null) {
                    System.out.println("Task: " + task.getName());  // Add this line for debugging
                    if (satisfiesCriterion(task, criterion)) {
                        System.out.println("- " + task.getName());
                    }
                } else {
                    System.out.println("Encountered null task.");
                }
            }
        } else {
            System.out.println("Criterion " + criterionName + " not found.");
        }
    }



    private boolean satisfiesCriterion(Task task, CompositeCriterion criterion) {
        if (criterion == null) {
            return false;  // If the criterion is null, consider it not satisfied
        }

        if (criterion.getNegatedCriterion() != null) {
            return !satisfiesCriterion(task, criterion.getNegatedCriterion());
        } else {
            boolean result = false;
            for (CompositeCriterion subCriterion : criterion.getSubCriteria()) {
                boolean subResult = satisfiesCriterion(task, subCriterion);
                if (criterion.getLogicOp().equals("&&")) {
                    result = result && subResult;
                } else if (criterion.getLogicOp().equals("||")) {
                    result = result || subResult;
                }
            }
            return result;
        }
    }


    public void store(String path) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path))) {
            oos.writeObject(tasks);
            oos.writeObject(compositeCriteria);
            oos.writeObject(basicCriteria);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public void load(String path) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path))) {
            tasks = (Map<String, Task>) ois.readObject();
            compositeCriteria = (Map<String, CompositeCriterion>) ois.readObject();
            basicCriteria = (Map<String, BasicCriterion>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void quit() {
        System.out.println("Exiting the Task Management System. Goodbye!");
        System.exit(0);
    }

    public static void main(String[] args) {
        // Create an instance of TMS
        TMS taskManager = new TMS();

        // Define some criteria and tasks
        taskManager.defineBasicCriterion("Criterion1", "name", "contains", "Task1");
        taskManager.defineBasicCriterion("Criterion2", "name", "contains", "Task2");
        taskManager.createPrimitiveTask("Task1", "boil-water", 0.3, Collections.emptyList());
        taskManager.createPrimitiveTask("Task2", "boil-water", 0.3, Collections.emptyList());
        taskManager.createCompositeTask("composite1", "make-coffee", Arrays.asList("Task1"));
        taskManager.createCompositeTask("composite2", "make-tea", Arrays.asList("Task2", "composite1"));
        taskManager.defineBinaryCriterion("Criterion3", "Criterion1", "&&", "Criterion2");

        // Print all tasks
        System.out.println("All tasks:");
        taskManager.printAllTasks();
        System.out.println("------------------------");

        // Print all criteria
        System.out.println("All criteria:");
        taskManager.printAllCriteria();
        System.out.println("------------------------");

        // Search for tasks satisfying a criterion
        System.out.println("Search results for Criterion3:");
        taskManager.search("Criterion3");
        System.out.println("------------------------");

        // Report duration for a task
        System.out.println("Report duration for Task1:");
        taskManager.reportDuration("Task1");
        System.out.println("------------------------");

        // Report earliest finish time for a task
        System.out.println("Report earliest finish time for Task1:");
        taskManager.reportEarliestFinishTime("Task1");
        System.out.println("------------------------");

        // Save and load the state
        String filePath = "task_manager_state.ser";
        System.out.println("Saving the state to " + filePath);
        taskManager.store(filePath);
        System.out.println("Loading the state from " + filePath);
        taskManager.load(filePath);
        System.out.println("All tasks after loading:");
        taskManager.printAllTasks();
        System.out.println("------------------------");

        // Quit the application
        System.out.println("Quitting the application:");
        taskManager.quit();
    }

}
