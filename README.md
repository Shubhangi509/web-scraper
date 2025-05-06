# Web Scraper Application

This application provides a complete solution for scraping product information from e-commerce websites, with domain-specific extractors for different websites.

## Project Structure

- **Backend**: Spring Boot application with custom extractors for different websites
- **Frontend**: React application for easy URL input and CSV download

## How to Run the Project

### Prerequisites

1. Java 17 (or later) and Gradle installed
2. MySQL Server installed and running
3. Create a new MySQL database for the application
4. Node.js and npm installed (for frontend)

### Database Setup

1. Install MySQL Server if not already installed
2. Create a new database:
   ```sql
   CREATE DATABASE webscraper;
   ```
3. Update the database configuration in `src/main/resources/application.properties`:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/webscraper
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   ```

### Running the Backend

1. Make sure you have Java 17 (or later) and Gradle installed.

2. Navigate to the project root directory and build the project:
   ```bash
   ./gradlew build
   ```

3. Run the Spring Boot application:
   ```bash
   ./gradlew bootRun
   ```

   The backend server will start on http://localhost:8080

### Running the Frontend

1. Make sure you have Node.js and npm installed.

2. Navigate to the frontend directory:
   ```bash
   cd frontend
   ```

3. Install dependencies:
   ```bash
   npm install
   ```

4. Start the frontend development server:
   ```bash
   npm start
   ```

   The frontend will be available at http://localhost:3000

## Using the Application

1. Open your browser and navigate to http://localhost:3000
2. Enter a product or category URL in the input field
3. Click "Check URL" to determine the URL type
4. Click "Download as CSV" to retrieve the scraped data

## Supported Websites

Currently, the application supports:
- Amazon.in (products and category pages)

## Adding New Website Support

To add support for a new website:

1. Create domain-specific extractors in the appropriate directory:
   - `scripts/[domain]/[tld]/extractor/ProductPageExtractor.java`
   - `scripts/[domain]/[tld]/extractor/CategoryPageExtractor.java`

2. Create a domain-specific classifier:
   - `scripts/[domain]/[tld]/classifier/[Domain]Classifier.java`

## API Endpoints

- `/scraper/fetch?url={url}` - Fetch raw HTML for a URL
- `/scraper/extract?url={url}` - Extract data from a URL
- `/scraper/scrape?url={url}` - Fetch and extract data in one step
- `/scraper/scrape-category?url={url}` - Scrape all products from a category page
- `/scraper/export-csv?url={url}` - Export scraped data as CSV
- `/scraper/check-url-type?url={url}` - Check if a URL is for a product or category

## Troubleshooting

- If you encounter connection issues, check your internet connection and website access.
- Some websites may have anti-scraping measures in place. The application uses a simple user agent, which may need to be updated.
- Make sure all required dependencies are installed.
- Check the logs for any specific error messages.
- For database issues, ensure your database configuration is correct. 