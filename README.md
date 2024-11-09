---
# Velvet Bloom - Developer Setup Guide

Welcome to the Velvet Bloom project! This guide will help you set up the project on your local environment for development. Follow the steps below to get started.

---

## Project Overview

Velvet Bloom is a monolithic e-commerce platform for a clothing store, featuring:
- A **backend** powered by **Spring Boot** and **Java 21**
- A **frontend** built with **React** and served by **Nginx**
- **MongoDB** for the database
- All components are containerized with **Docker** for consistency across environments.

---

## Prerequisites

Please ensure the following tools are installed on your machine:

- **Docker**: [Download Docker](https://docs.docker.com/get-docker/)
- **Git**: [Download Git](https://git-scm.com/book/en/v2/Getting-Started-Installing-Git)
- **Node.js** (for local frontend development): Version 18.x [Download Node.js](https://nodejs.org/)

---

## Project Structure

The repository structure is as follows:

```plaintext
velvet-bloom/
├── backend/                   # Spring Boot backend
│   ├── src/                   # Source files
│   ├── target/                # Compiled JAR files (after build)
│   ├── Dockerfile             # Dockerfile for backend
│   └── pom.xml                # Maven dependencies for backend
├── frontend/                  # React frontend
│   ├── public/                # Public assets
│   ├── src/                   # Source files (components, pages)
│   ├── Dockerfile             # Dockerfile for frontend
│   └── package.json           # NPM dependencies for frontend
├── docker-compose.yml         # Docker Compose file for multi-container setup
└── README.md                  # Project documentation


---
```
## Setting Up the Development Environment

Follow these steps to get your local environment up and running.

### 1. Clone the Repository

```bash
git clone https://github.com/kasunjayasanka/velvet-bloom.git
cd velvet-bloom
```

### 2. Configure Environment Variables (Optional)

To configure environment variables, copy the example files provided and rename them:

- For **frontend**:

  ```bash
  cp frontend/.env.example frontend/.env.local
  ```

  Update `frontend/.env.local` to set the backend URL or any other frontend-specific environment variables.

- For **backend**:

  ```bash
  cp backend/src/main/resources/application.example.properties backend/src/main/resources/application.properties
  ```

  Edit `application.properties` to set up the MongoDB URI, JWT secrets, and any other backend configurations.

### 3. Running the Project with Docker

To run the project locally with Docker, use Docker Compose to build and start the containers.

1. **Build and Start Services**:

   ```bash
   docker-compose up --build
   ```

   This will start the following services:
   - **Backend**: Accessible at [http://localhost:8080](http://localhost:8080)
   - **Frontend**: Accessible at [http://localhost:3000](http://localhost:3000)
   - **MongoDB**: Runs in the background on port `27017`

2. **Stop Services**:

   To stop the services, press `Ctrl+C` or run:

   ```bash
   docker-compose down
   ```

---

## Running Services Independently (Optional)

If you prefer running frontend and backend independently outside of Docker for development, follow these steps:

### Running the Backend

1. **Navigate to the backend directory**:

   ```bash
   cd backend
   ```

2. **Build the Application** (Optional if not using Docker):

   ```bash
   ./mvnw clean package
   ```

3. **Run the Application**:

   ```bash
   java -jar target/velvetbloom-0.0.1-SNAPSHOT.jar
   ```

The backend will run on [http://localhost:8080](http://localhost:8080).

### Running the Frontend

1. **Navigate to the frontend directory**:

   ```bash
   cd frontend
   ```

2. **Install dependencies**:

   ```bash
   npm install
   ```

3. **Run the Application**:

   ```bash
   npm start
   ```

The frontend will run on [http://localhost:3000](http://localhost:3000).

---

## Git Workflow for Collaboration

Follow these Git practices to ensure smooth collaboration:

1. **Create a New Branch**: Always work on a feature branch for new development.

   ```bash
   git checkout -b yourname/your-feature-name
   ```

2. **Commit Changes**: Use descriptive commit messages.

   ```bash
   git commit -m "Add feature XYZ"
   ```

3. **Push Branch to Remote**:

   ```bash
   git push origin yourname/your-feature-name
   ```

4. **Create a Pull Request**: Open a pull request on GitHub for code review and merge.

---

## Troubleshooting

1. **Database Connection Issues**: Ensure MongoDB is running and accessible on port `27017`.
2. **Port Conflicts**: If another service is using port `3000` or `8080`, stop that service or change the port in `docker-compose.yml`.
3. **Docker Build Errors**: Run `docker-compose down --rmi all` to remove images and try `docker-compose up --build` again.

---

## Contributing

We welcome contributions! Please follow our [CONTRIBUTING.md](./CONTRIBUTING.md) guidelines for submitting issues, pull requests, and feedback.

---

## License

This project is licensed under the MIT License.


--- 

This Markdown file provides all the necessary information to help developers set up and collaborate on the project efficiently.