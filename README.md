# Consultation Management System - Overview
This Java project is an assignment for Object-Oriented Programming module in semester 4 of my diploma program at APU. This is a duo porject, and my collaborator is Kee Wen Yew. The assignment, which is titled "APU consultation management system", is developed using Apache Netbeans integrated developed environment (IDE). My team has made references to existing consultation booking systems such as APU's own ApSpace when creating the GUI and backend logic of the system. This includes strict validations in registration and login processes, decent-looking display of past and upcoming appointments, and a fluid consultation booking process. Other than that, lecturers are able to manage appointments and handle reschedule requests efficiently. Besides that, as per requirements requested, students and lecturers can leave their feedback and comments for after appointment sessions.

Plus by using Apache Netbeans, the process of creating the program's GUI was a piece of cake. This is because the IDE provides drag-and-drop functionality for all sorts of GUI elements such as text labels, text fields, buttons, and so on. As a result, Apache Netbeans helped in removing the complexity of designing the system's GUI (much needed due to tight submission window, and as Java beginners). Therefore, the main benefit or learning outcome from this assignment is allowing my partner and I to enhance our understanding in OOP concepts (classes, inheritance, polymorphism, etc.), knowing Java syntax and comprehending the overall hefty process of working with JDK.


# Project Assumption (And How It Works)
1. Consultation is using a time-blocking system, where consultations are strictly scheduled in fixed 30-minute slots.
2. Each consultation only lasts for 30 minutes and students can only make one appointment at a time.
3. Lecturers are always ready for consultations after setting it as available; Thus, all the appointments will be automatically accepted by the lecturer when students make an appointment.
4. Students and lecturers are tech-savvy and can easily understand the interaction between the interface design.
5. Students have their own student ID (TP number) and lecturers have their own lecturer ID (LC number) that is normally used in APU.
6. All IDs in APU is unique and not repeating.
7. This project neither interacts with live server nor locally-hosted servers. For simplicity, text files were our data storage. 
