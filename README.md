# Assignment 4: Facade Design Pattern

##  Project Overview
This repository contains an implementation of the **Facade Structural Design Pattern** in Java (JDK 17). 

The goal of this assignment is to simplify interactions with a complex Home Theater subsystem (`Amplifier`, `DiscPlayer`, `Projector`, `Screen`, `TheaterLights`, `PopcornPopper`) by providing a unified, clean interface via `HomeTheaterFacade`.

---

##  Project Structure
assignment-4-facade/
├── docs/
│   ├── Assignment_Report.docx
│   ├── report.md
│   ├── uml-class-diagram.mmd
│   └── uml-class-diagram.png
├── src/
│   ├── main/java/edu/aitu/facade/hometheater/
│   │   ├── client/       # Demo application using only the Facade
│   │   ├── facade/       # HomeTheaterFacade implementation
│   │   └── subsystem/    # Complex subsystem classes (independent)
│   └── test/java/        # Unit tests verifying Facade behavior
├── pom.xml
└── README.md


---

##  How to Run

### Build the project
```bash
mvn clean package
Run the Demo Application
Bash
mvn exec:java -Dexec.mainClass="edu.aitu.facade.hometheater.client.HomeTheaterDemo"
Run Unit Tests
Bash
mvn test
 Key Principles
Simplified Interface: Clients control the entire subsystem using high-level methods like watchMovie() and endMovie().

Subsystem Independence: Subsystem components remain fully decoupled from the Facade and can still be used directly by advanced clients.

Clean Code: Adheres to Single Responsibility, Meaningful Names, and Information Hiding principles.
