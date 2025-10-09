# Automation Test Framework – UI + API + CI/CD

## ✅ Project Overview

This is a **Java-based Hybrid Automation Framework** that supports **UI Testing (Selenium WebDriver)** and **API Testing (REST Assured)** with **parallel execution, MySQL integration, reporting, and CI/CD using GitHub Actions**.

This framework is modular, scalable, and designed for real-time project usage.

---

## 📦 Project Modules Overview

This framework is organized into clean modules to support UI + API automation with reporting and CI/CD. Below is a summary of each module:

### ✅ 1. BaseTest (UI Setup)

Responsible for browser setup and teardown using Selenium WebDriver. Runs before every UI test.

* Initializes Chrome browser (headless in CI/CD)
* Handles browser cleanup
* Captures screenshots on failure

### ✅ 2. APIBaseTest (API Setup)

Sets up REST Assured configuration for API testing.

* Defines base URI
* Common headers and request specs
* Reusable API utilities

### ✅ 3. Test Classes

| Module                      | Purpose                                      |
| --------------------------- | -------------------------------------------- |
| `BlazeDemoTests.java`       | Contains 10 UI test cases for flight booking |
| `JsonPlaceholderTests.java` | Contains 10 API tests using REST Assured     |

### ✅ 4. Database Integration

Database inserter utility stores results in MySQL.

* UI results → `ui_tests` table
* API results → `api_responses` table
* Detailed logs → `execution_logs` table

### ✅ 5. Listeners

Listeners track execution and store results.

| Listener                 | Purpose                                  |
| ------------------------ | ---------------------------------------- |
| `SuiteExecutionListener` | Logs suite start/end + screenshot at end |
| `TestSuiteListener`      | Captures test lifecycle events           |
| `DbResultListener`       | Saves results in database                |
| `ReportListener`         | Sends logs to report system              |

### ✅ 6. Reporting

| File                       | Purpose                         |
| -------------------------- | ------------------------------- |
| `HtmlReportGenerator.java` | Generates HTML dashboard report |
| CSV/Excel Reports          | Stored per execution            |
| Screenshots                | Captured for failed UI tests    |

### ✅ 7. CI/CD Pipeline (GitHub Actions)

Automates test execution on each commit.

* Runs UI + API tests in parallel
* Uploads artifacts
* Sets up MySQL automatically

---

## 🚀 Features

| Feature            | Description                     |
| ------------------ |---------------------------------|
| UI Testing         | Selenium WebDriver + TestNG     |
| API Testing        | REST Assured (JSONPlaceholder)  |
| Parallel Execution | TestNG + Surefire Plugin        |
| Database Logging   | Saves execution results in MySQL|
| Reporting          | HTML, CSV,Excel,JUnit reports   |
| CI/CD Integration  | GitHub Actions workflow         |
| Screenshots        | Captured for failed UI tests    |
| Traceability       | US_ID and TC_ID mapping         |

---

## 📂 Project Structure

```
AutomationFramework
│
├── .github/
│   └── workflows/
│       └── ci.yml                     # GitHub CI pipeline for UI & API automation
│
├── artifacts/                         # Test execution artifacts
│   ├── api/                           # API logs, responses
│   ├── j-unit/                        # JUnit XML reports
│   ├── reports/                       # Generated reports (HTML/CSV/Excel)
│   └── screenshots/                   # Screenshots for UI test failures
│
│
├── src/
│   └── test/
│       └── java/
│           └── org/
│               └── automation/
│                   ├── api/                       # API automation module
│                   │   ├── ApiBaseTest.java
│                   │   ├── ApiTestMapper.java
│                   │   └── JsonPlaceholderTests.java
│                   │
│                   ├── config/                    # Configuration handling
│                   │   └── ConfigManager.java
│                   │
│                   ├── drivers/                   # WebDriver setup for UI
│                   │   └── WebDriverFactory.java
│                   │
│                   ├── listeners/                 # TestNG listeners
│                   │   ├── DbResultListener.java
│                   │   ├── ReportListener.java
│                   │   ├── SuiteExecutionListener.java
│                   │   └── TestSuiteListener.java
│                   │
│                   ├── reports/                   # Custom report generators
│                   │   ├── CsvReportGenerator.java
│                   │   ├── ExcelReportGenerator.java
│                   │   └── HtmlReportGenerator.java
│                   │
│                   ├── scheduler/                 # Parallel & scheduled executions
│                   │   └── ParallelTestScheduler.java
│                   │
│                   ├── ui/                        # UI automation module
│                   │   ├── BaseTest.java
│                   │   ├── BlazeDemoTests.java
│                   │   ├── DriverManager.java
│                   │   └── UiTestMapper.java
│                   │
│                   └── utils/                     # Utility helper functions
│                       ├── DatabaseInserter.java
│                       ├── DatabaseUtils.java
│                       ├── ExcelUtils.java
│                       ├── ReportUtils.java
│                       └── ScreenshotsUtils.java
│
├── pom.xml                                   # Maven dependencies & build settings
├── README.md                                 # Framework documentation
├── testng.xml                                # Master TestNG suite
├── testng-api.xml                            # API-only suite
└── testng-ui.xml                             # UI-only suite
```

---

## 🔧 Technologies Used

* **Java 23**
* **Selenium WebDriver**
* **REST Assured**
* **TestNG**
* **MySQL Database**
* **ExtentReports + Custom HTML Report**
* **Apache POI (Excel Reports)**
* **GitHub Actions (CI/CD)**

---

## 🔗 MySQL Table Structure

The framework stores execution logs in MySQL tables:

* `execution_log` – Stores UI and API test summary
* `api_responses` – Stores API request/response data
* `ui_results` – Stores UI test status and screenshots

---

## ⚙️ How It Works

### ✅ UI Test Execution Workflow

1. Start execution from **testng.xml**
2. **BaseTest** starts WebDriver
3. Run **BlazeDemoTests** (10 test cases)
4. Log results using **DbResultListener → DatabaseInserter**
5. Generate **HTML report** after execution
6. Upload artifacts via **GitHub Actions CI**

### ✅ API Test Execution Workflow

1. Start execution from **testng.xml**
2. **BaseApiTest** sets BaseURI
3. Run API tests (`JsonPlaceholderTests` / `10 test cases`)
4. Save responses into MySQL
5. Reports generated
6. Pipeline runs in **GitHub Actions**

---

## ⚙️ Parallel Execution

* Configured using TestNG and Maven Surefire plugin
* Runs **20 tests** (10 UI + 10 API) in parallel

---

## 🚀 How to Run

### ✅ Run from Command Line

```
mvn clean test
```

### ✅ Run Specific Tests

```
mvn -Dgroups=ui test
mvn -Dgroups=api test
```

---

## 📊 Reports Generated

| **Report Type | Location**            |
| Api artifacts | artifacts/api/        |
| HTML Report   | artifacts/reports/    |
| CSV Report    | artifacts/reports/    |
| JUnit Report  | artifacts/j-unit/     |
| Screenshots   | artifacts/screenshots |

---

## 🔄 CI/CD – GitHub Actions

Every commit triggers:
✔ Build + Test Execution
✔ Parallel UI + API Tests
✔ Upload HTML Report & Logs as Artifacts

---

## ✅ Advantages of This Framework

✔ Real-time test execution tracking via database
✔ Parallel execution saves time
✔ CI/CD automated testing pipeline
✔ Test traceability using US_ID and TC_ID
✔ Modular and reusable structure

---

Feel free to contribute or raise an issue if you want improvements.

📜 License

This project is licensed under the MIT License.
Feel free to use and modify it for your automation needs.


✨ Author

AutomationFramework – Designed for scalable, traceable, and fully automated testing pipelines.


License

MIT License © 2025 [Chandrakant Kumar]

Contact

For questions or support, contact: [chandrakant2522006@gmail.com]