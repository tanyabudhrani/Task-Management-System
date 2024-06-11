# Task-Management-System
Task Management System: a comprehensive Java-based application designed to facilitate the efficient management of tasks within a project or organizational context

## Overview

This project is a Java-based Command-Line Task Management System designed to facilitate efficient task management within a project or organizational context. It allows users to create, modify, delete, and manage tasks, supporting both primitive and composite tasks. The system utilizes object-oriented programming principles and adheres to the Model-View-Controller (MVC) framework.

## Features

- **Primitive and Composite Tasks**: Supports the creation of both simple tasks and complex tasks with subtasks.
- **Task Operations**: Allows for creating, modifying, deleting, and viewing tasks.
- **Task Criteria**: Supports defining and applying criteria to filter tasks.
- **Serialization**: Tasks and criteria can be stored to and loaded from files.
- **JUnit Testing**: Comprehensive test cases to ensure the functionality of the system.

## Data Structures Used

- **HashMap**: Stores key-value pairs for task names and instances.
- **ArrayList**: Manages lists of tasks, subtasks, prerequisites, and criteria.
- **Serializable**: Allows objects to be stored and transmitted.
- **ObjectOutputStream and ObjectInputStream**: Handles writing and reading of objects to and from streams.

## Object-Oriented Capabilities

- **Encapsulation**: Restricts access to object components, utilizing private fields with getters and setters.
- **Inheritance**: Uses inheritance for code reusability.
- **Polymorphism**: Employs method overloading and overriding for flexible interface usage.

## MVC Framework

- **Model**: Represents the core logic of the task management system.
- **View**: The user interface, primarily command-line based with clear output formatting.
- **Controller**: Handles user input and facilitates interaction between the model and view.

## Requirements Implemented

- **Creating Primitive and Composite Tasks**: Allows setting task name, description, duration, and prerequisites or subtasks.
- **Modifying and Deleting Tasks**: Supports updating task properties and removing tasks.
- **Viewing Task Information**: Prints details of individual or all tasks.
- **Task Duration and Earliest Finish Time**: Calculates and reports task durations and earliest finish times.
- **Defining and Applying Criteria**: Enables criteria-based task filtering.
- **Storing and Loading Data**: Supports serialization of tasks and criteria.
- **Termination**: Gracefully exits the system.

## How to Run

1. **Compile the Program**: Ensure you have a Java compiler installed. Compile the program using:
   ```sh
   javac Main.java
