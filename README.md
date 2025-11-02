# Sweet Shop Management System

---
## Setup Instructions

### Prerequisites
- **Java JDK 11** or higher ([Download](https://www.oracle.com/java/technologies/downloads/))
- **Maven 3.6+** ([Download](https://maven.apache.org/download.cgi))
- **Git** ([Download](https://git-scm.com/downloads))

### Installation Steps

1. **Clone the repository**
```bash
git clone https://github.com/Abul00/Sweet-shop-management.git
cd sweet-shop-management
```

2. **Verify Java and Maven installation**
```bash
java -version
mvn -version
```

3. **Build the project**
```bash
mvn clean install
```

---

## Running the Application

### Console Application
```bash
# Compile and run
mvn clean compile
mvn exec:java -Dexec.mainClass="com.sweetshop.Main"
```

### Web Frontend

Simply open `frontend/index.html` in your browser:
```bash
# Windows
start frontend/index.html

```
---

## Running Tests

### Run all tests
```bash
mvn test
```

### Generate test report
```bash
mvn surefire-report:report
# Report will be in: target/site/surefire-report.html
```
---
# Technologies Used

# Backend
- **Language**: Java 11
- **Build Tool**: Maven 3.6+
- **Testing Framework**: JUnit 5.9.3

### Frontend (Optional)
- **HTML5** for structure
- **CSS3** for styling
- **Vanilla JavaScript** for functionality
- **LocalStorage** for data persistence

---

## 📁 Project Structure
```
sweet-shop-management/
├── src/
│   ├── main/java/com/sweetshop/
│   │   ├── model/
│   │   │   └── Sweet.java                    # Sweet entity class
│   │   ├── exception/
│   │   │   └── InsufficientStockException.java # Custom exception
│   │   ├── service/
│   │   │   └── SweetShop.java                # Business logic layer
│   │   └── Main.java                         # Console application entry point
│   └── test/java/com/sweetshop/
│       └── service/
│           └── SweetShopTest.java            # Comprehensive test suite (30+ tests)
├── frontend/                                 #  Web interface
│   ├── index.html
│   ├── css/styles.css
│   └── js/
│       ├── app.js
│       ├── sweetShopUI.js
│       ├── storage.js
│       └── utils.js
├── pom.xml                                   # Maven configuration
├── README.md                                 # This file
└── .gitignore                                # Git ignore rules
```

---

## 👨‍💻 Author

- GitHub: https://github.com/Abul00/
- Email: abulhassan7411@gmail.com

---

