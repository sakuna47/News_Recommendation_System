# News Recommendation System

## Overview
The **News Recommendation System** is a JavaFX-based application that allows users to read, interact with, and manage news articles. The system uses MongoDB for data storage and provides functionalities for users and administrators to manage articles, recommendations, and user interactions.

## Features
### User Features
- User authentication (Sign Up, Login, Logout)
- View articles by category
- Like, dislike, and rate articles
- Get personalized article recommendations using Jaccard similarity
- Delete user account

### Admin Features
- Admin authentication (Login, Logout)
- Add, edit, and remove articles
- View all users
- Remove users and their interactions
- Generate user activity reports

## Technologies Used
- **Java 17**
- **JavaFX** for the GUI
- **MongoDB** for database management
- **BCrypt** for password hashing

## Setup Instructions
### Prerequisites
- Java 17 or later
- MongoDB installed and running on `localhost:27017`
- Maven (if using a build tool)

### Installation
1. Clone the repository:
   ```sh
   git clone https://github.com/sakuna47/news-recommendation-system.git
   cd news-recommendation-system
   ```
2. Import the project into your favorite IDE (e.g., IntelliJ IDEA, Eclipse, NetBeans).
3. Ensure MongoDB is running.
4. Run the `HelloApplication.java` file to start the application.

## Project Structure
```
news-recommendation-system/
│── src/main/java/com/example/newsrecommendationsystem/
│   ├── Controllers/        # JavaFX controllers
│   ├── Database/           # Database connection logic
│   ├── Service/            # Business logic and recommendation algorithms
│   ├── HelloApplication.java  # Main entry point
│── src/main/resources/
│   ├── fxml/               # JavaFX UI layout files
│── pom.xml                 # Maven dependencies (if applicable)
```

## Usage
1. **Admin Login:** Use `admin` as the username and `1234` as the password.
2. **User Signup & Login:** Create a new account and log in to access news articles.
3. **Article Interaction:** View articles, rate, like, or dislike them.
4. **Admin Panel:** Add/remove users and articles, and generate reports.

## Future Improvements
- Implement an advanced recommendation system using machine learning
- Add user notifications
- Enhance UI/UX with better styling and animations

## License
This project is licensed under the MIT License.

## Contributors
- sakuna sankalpa(https://github.com/sakuna47)
