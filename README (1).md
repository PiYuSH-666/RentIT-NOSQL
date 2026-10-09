# RentIT — Vehicle Rental System

RentIT is a Java Object-Oriented Programming (OOP) mini-project for CSC2104. It provides a desktop vehicle-rental interface built with Java Swing and stores application data in local text files.

## Features

- Browse cars, bikes, and scooters.
- Search and filter the vehicle catalogue.
- Sign up and log in to a customer account.
- View vehicle details and create rental bookings.
- Calculate rental cost from the vehicle's daily rate and booking duration.
- Pay using an in-app credit wallet and add credits.
- View and cancel bookings.
- Save customer, booking, credit, and vehicle-availability data locally.

## Technologies

- Java
- Java Swing for the desktop GUI
- Java collections for managing vehicles and bookings
- Local `.txt` files for persistence
- Java file locking in storage code to coordinate access to shared files

No web service or paid service is required to run the desktop application.

## Object-Oriented Programming concepts

| Concept | Example in RentIT |
| --- | --- |
| Classes and objects | `Vehicle`, `Car`, `Bike`, `Scooter`, `Booking`, `Customer` |
| Encapsulation | Classes keep state in fields and expose methods to read or change it |
| Inheritance | `Car`, `Bike`, and `Scooter` specialize `Vehicle` |
| Abstraction | The common `Vehicle` type represents shared vehicle behaviour |
| Interface | `Rentable` defines a rental-related operation |
| Polymorphism | Specific vehicle objects can be handled through a common `Vehicle` reference; `Rentable` can be used as an interface reference |
| Composition/has-a relationships | A `Booking` works with a vehicle and a credit wallet |
| Collections | Managers maintain collections of vehicles and bookings |
| Exception handling | Booking validation and insufficient-credit or unavailable-vehicle cases use exceptions |
| Custom exceptions | `InvalidBookingException`, `InsufficientCreditsException`, and `VehicleNotAvailableException` |
| File handling | Storage classes read and write local text files |
| Separation of responsibilities | Business classes are in `rentit`; Swing screens are in `rentit.ui` |

These are educational examples rather than a production rental platform.

## Requirements

- A Java Development Kit (JDK) compatible with the project's source code. Java 21 is the recommended environment for this project.
- Eclipse IDE for Java Developers, or another Java IDE.
- The complete project source and its required image assets.

## Run in Eclipse

The GUI entry point is `rentit.ui.AuthenticationFrame`. `rentit.Main` contains a console-based demonstration and is not the normal desktop GUI entry point.

### If you already have the Eclipse project

1. Open Eclipse.
2. In **Package Explorer**, select the RentIT project.
3. Confirm the project has no compile errors. If needed, right-click the project and choose **Refresh**.
4. Expand `src` and the `rentit.ui` package.
5. Open `AuthenticationFrame.java`.
6. Right-click inside the editor and select **Run As → Java Application**.
7. The login window should open. You can log in with a sample account or use the sign-up screen to create a local account.

### If you are importing the project from GitHub

1. Download the repository as a ZIP from GitHub, or clone it using Git.
2. Extract it to a folder on your computer.
3. In Eclipse, choose **File → Import… → General → Existing Projects into Workspace**.
4. Select **Select root directory**, browse to the extracted project folder, and select the project if Eclipse detects it.
5. Click **Finish**.
6. If Eclipse does not detect an Eclipse project, create a Java project and copy the source packages and required assets into the matching locations. Make sure the package paths match the declarations: `rentit` and `rentit.ui`.
7. Run `AuthenticationFrame.java` as a Java Application.

The exact import choices depend on which Eclipse project metadata files are included in the repository.

## Project structure

The main source packages are organized approximately as follows:

```text
RentIT/
├── src/
│   ├── rentit/
│   │   ├── Main.java
│   │   ├── Vehicle.java
│   │   ├── Car.java
│   │   ├── Bike.java
│   │   ├── Scooter.java
│   │   ├── Rentable.java
│   │   ├── Booking.java
│   │   ├── BookingManager.java
│   │   ├── RentalManager.java
│   │   ├── Customer.java
│   │   ├── CreditWallet.java
│   │   └── ... storage, authentication, and exception classes
│   └── rentit/ui/
│       ├── AuthenticationFrame.java
│       ├── LoginPanel.java
│       ├── SignupPanel.java
│       ├── RentITFrame.java
│       ├── VehiclesPanel.java
│       └── BookingsPanel.java
├── images/                  # UI images used by the application
├── *.txt                    # Local sample/runtime data files, if included
└── README.md
```

The actual folder names can differ slightly depending on the Eclipse project layout and repository contents.

## Local data files

RentIT uses text files in the application's working directory for local persistence. Depending on the features used, these can include customer records, bookings, credit balances, and vehicle availability.

- The `.txt` files committed with the project should contain fictional sample data only.
- The application may create or update data files while it runs.
- Relative file paths are resolved against the working directory configured by Eclipse or the launch environment. If a data file appears to be missing, check the Run Configuration's working directory.
- Do not delete or edit data files while RentIT is running. Back them up before resetting demo data.
- Files with a `.lock` suffix are runtime coordination files. They are not application records and generally should not be committed.
- File locking helps coordinate concurrent access, but separate text files do not provide a fully crash-atomic database transaction.

## Scope and limitations

RentIT is a student desktop application intended to demonstrate Java OOP concepts. It uses local text-file storage rather than a database or hosted backend. It is not designed as a production system for real customers or payments.

## Academic project

**Course:** CSC2104 — Object Oriented Programming  
**Project:** RentIT — Vehicle Rental System
