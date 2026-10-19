# Staff Payroll API

A small practice project built to **learn and understand** four things as they relate to Java:

- Interfaces
- Inheritance
- MongoDB
- Postman

The goal is understanding, not just working code. Each step was done to see *why* it works.

## How the pieces fit

```
Postman  →  Spring Boot app (Java classes)  →  MongoDB
```

Postman sends HTTP requests. MongoDB stores data. They cannot talk to each other directly, so a Java web app sits in the middle. Spring Boot was chosen for that middle layer because it is the industry standard.

## Design

| Class / Interface | Role |
|---|---|
| `Payable` (interface) | A promise: anything payable has `calculatePay()` |
| `Staff` (abstract class) | Parent with shared fields `id` and `name` |
| `FullTimeStaff` | Extends `Staff`, implements `Payable`. Pay = `monthlySalary` |
| `ContractStaff` | Extends `Staff`, implements `Payable`. Pay = `hourlyRate * hoursWorked` |
| `StaffRepository` | Interface extending `MongoRepository<Staff, String>`. Spring writes the code |
| `StaffController` | REST endpoints |

Rule of thumb used: **inheritance is "is-a"**, **interface is "can-do"**.

### Endpoints

| Method | Path | Purpose |
|---|---|---|
| POST | `/staff/fulltime` | Save a full-time staff member |
| POST | `/staff/contract` | Save a contract staff member |
| GET | `/staff` | List all staff |
| GET | `/staff/{id}/pay` | Get that person's pay |

The `/pay` endpoint calls `payable.calculatePay()` without checking the staff type. Java runs the right version at runtime. That is **polymorphism**, and it is the main point of the exercise.

## Setup steps taken

1. **Installed MongoDB** (Windows `.msi`, Complete setup, run as a service, with Compass).
    - The download page first showed a `.deb` (Linux) package. Fix: change the Platform dropdown to Windows x64 and Package to msi.
2. **Checked MongoDB works** by connecting Compass to `mongodb://localhost:27017`. Saw `admin`, `config`, `local`.
3. **Generated the Spring Boot project** in IntelliJ (New Project → Spring Boot):
    - Maven, Java, Jar, Properties configuration
    - Group `com.afsec`, Artifact `payroll`
    - Dependencies: **Spring Web** and **Spring Data MongoDB**
4. **Ran the app.** The log showed `state=CONNECTED` and `Started PayrollApplication`, proving the app reached MongoDB.
5. **Wrote the Java code:** `Payable`, `Staff`, `FullTimeStaff`, `ContractStaff`, `StaffRepository`, `StaffController`.
6. **Set up Postman:** a `Payroll` collection, requests for each endpoint, JSON bodies with the body type set to raw → JSON.

## What has been accomplished

- MongoDB installed and running as a service.
- Spring Boot project created and connected to MongoDB.
- Interface, abstract parent, and two child classes written.
- Repository and controller written, then moved into the correct packages.
- Postman installed and first requests sent.
- `GET /staff` returned `200 OK`.
- Ada and Tunde were POSTed and saved.
- Learned to read Spring logs, HTTP status codes, and Postman errors.

## Questions asked and how they were answered

**Does `@Id` make up for having no constructor?**
No. They do different jobs. `@Id` marks the field that holds the document's unique ID. If a class has no constructor, Java adds an empty one for free, and Spring uses it to build objects when reading from MongoDB. That free constructor disappears once you write any constructor, so then add an empty one back.

**What does Spring Boot have to do with this? I only listed Postman and MongoDB.**
Spring Boot was an addition. Postman and MongoDB can't talk directly, so a Java web app is needed in between. Spring Boot fills that role.

**Do I have to download Spring?**
No. It is not installed like a program. You generate a starter project from a form (start.spring.io or IntelliJ's New Project window) and open it.

**Can one interface "control" many classes? Does `Payable` write `calculatePay()` for both?**
Half right. Many classes can implement one interface, but the interface writes no code. Each class writes its own `calculatePay()`. The interface only forces the method to exist and lets you call it without knowing the exact class.

**Is `<>` a mapping keyword?**
No. It is Java **generics**. `MongoRepository<Staff, String>` fills two blanks: the type stored (`Staff`) and the ID type (`String`). Mapping is done by annotations such as `@Document`, `@Id`, `@PostMapping`.

**Can I write `List<Staff, String> id = new ArrayList<>();`?**
No. `List` has only one type blank. `Map<K, V>` has two, so a `Map<String, Staff>` is the right tool for looking up staff by id.

**Why `ECONNRESET` in Postman?**
Causes found: the `{{baseUrl}}` variable not resolving (Postman showed "No Environment"), and the app not running. Typing the full address `http://localhost:8080/...` worked. Longer-term fix: make `baseUrl` a collection variable.

**Why `405 Method Not Allowed` on `/staff`?**
The request used the wrong method. `/staff` accepts only GET, and `/staff/fulltime` and `/staff/contract` accept only POST. A 405 also proved the controller was loaded.

**Why did the controller and repository not work at first?**
The folders `controller` and `repository` sat next to `payroll` instead of inside it. Spring Boot only scans the package of the main class and below. Moving them inside `com.afsec.payroll` fixed it. The log showed `Found 0 MongoDB repository interfaces` before the fix.

**Why was Ada's salary `0.0`?**
The Java field was named `salary`, but the JSON sent `monthlySalary`. Spring ignores names it can't match, with no error. Fix: rename the field to `monthlySalary`.

**Why were there six Adas?**
Each POST creates a new document with a new id. Sending the same POST again makes a duplicate.

**Why did Tunde show no `hourlyRate` or `hoursWorked`?**
Getters were missing in `ContractStaff`. Getters control what is written to JSON. Setters control what is read from JSON. Both are needed.

**Why did Compass show nothing in `payroll`?**
The data was being saved to a database named `test`, not `payroll`. See "Still open" below.

**Is an empty `StaffRepository` a problem?**
No. `MongoRepository` already provides `save`, `findAll`, and `findById`. You only add methods for custom searches.

## Still open

- **404 on the POSTs and the pay requests.** Not solved yet. Next checks:
    1. Send GET `http://localhost:8080/staff` using the full address. If it returns 200, the controller is loaded and the problem is the URL or method of the failing request.
    2. Run `netstat -ano | findstr :8080` to check no old copy of the app is holding the port.
    3. Add `server.error.include-message=always` to `application.properties` so the 404 body shows a message.
    4. Use fresh ids from GET `/staff`, with no `<` `>` brackets.
- **Database name.** Data went to `test`. The likely cause is that Spring Boot 4.x renamed the property from `spring.data.mongodb.uri` to `spring.mongodb.uri`. This is not yet confirmed against the project's exact version. Change the line, re-run, and check Compass.
- **`ContractStaff`** needs getters and setters for `hourlyRate` and `hoursWorked`.
- **Clean up** duplicate documents, then POST each person once.

## Next steps

1. Fix the 404 using the checks above.
2. Confirm pay results: Ada `500000.0`, Tunde `200000.0`, fake id → `404`.
3. Open Compass and read the `_class` field to see how MongoDB remembers each subclass.
4. Answer the self-check questions:
    - Why is `Payable` an interface and `Staff` a class?
    - Why is `Staff` abstract?
    - How does `/pay` work without knowing the staff type?
    - What does `_class` do?
5. Stretch goals:
    - Add `InternStaff` (fixed stipend) and `POST /staff/intern`. The controller's pay logic should not change.
    - Add `PUT` and `DELETE`.
    - Return clean 404 messages.
    - Add validation (salary must be positive).
    - Write one unit test per `calculatePay()`.

## Common mistakes to remember

- Field names in Java must match the JSON names exactly.
- Getters and setters are both needed for JSON in and out.
- New files and packages must sit inside the main class's package.
- Restart the app after every code change.
- Use the right HTTP method for each endpoint.
- Don't use inheritance when an interface is enough.