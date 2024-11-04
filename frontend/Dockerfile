# frontend/Dockerfile
FROM node:18-alpine

# Set the working directory
WORKDIR /app

# Install dependencies
COPY package.json package-lock.json ./
RUN npm install

# Copy the source files
COPY . .

# Build the React app
RUN npm run build

# Serve the app with a lightweight web server
RUN npm install -g serve
EXPOSE 3000
CMD ["serve", "-s", "build"]
