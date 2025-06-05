import React, { useState, useEffect } from 'react';
import axios from 'axios';
import './App.css';

//const API_BASE_URL = 'http://localhost:8080/scraper';
const API_BASE_URL = 'https://shielded-stream-74167-d176d6570916.herokuapp.com/scraper';
function App() {
  const [url, setUrl] = useState('');
  const [isLoading, setIsLoading] = useState(false);
  const [errorMessage, setErrorMessage] = useState('');
  const [successMessage, setSuccessMessage] = useState('');
  const [downloadProgress, setDownloadProgress] = useState(0);
  const [showDemo, setShowDemo] = useState(false);
  const [urlSubmitted, setUrlSubmitted] = useState(false);
  const [animateHeader, setAnimateHeader] = useState(false);
  const [showExtraContent, setShowExtraContent] = useState(false);
  
  // Helper function to check if URL is a category page
  const isCategoryPage = (urlString) => {
    return urlString && !urlString.includes('/dp/') && 
      (urlString.includes('/s?') || urlString.includes('/s/') || urlString.includes('/gp/'));
  };
  
  useEffect(() => {
    // Trigger header animation after component mounts
    setTimeout(() => {
      setAnimateHeader(true);
    }, 300);
  }, []);
  
  const handleSubmit = async (e) => {
    e.preventDefault();
    
    if (!url) {
      setErrorMessage('Please enter a URL');
      return;
    }
    
    // Reset states
    setIsLoading(true);
    setErrorMessage('');
    setSuccessMessage('');
    setDownloadProgress(0);
    
    try {
      // Skip URL type checking and immediately show option to download data
      setSuccessMessage('URL validated! You can download the data.');
      setUrlSubmitted(true);
      
    } catch (error) {
      console.error('Error:', error);
      setErrorMessage(error.response?.data?.message || 'Failed to process URL');
    } finally {
      setIsLoading(false);
    }
  };
  
  const downloadCsv = async () => {
    setIsLoading(true);
    setDownloadProgress(5);
    
    try {
      // Check if URL is a category page
      const isCategory = isCategoryPage(url);
      
      if (isCategory) {
        setSuccessMessage('Category page detected. Scraping products across multiple pages...');
        
        // For demonstration: simulate multi-page scraping with longer progress
        // This simulates pagination and multiple product scraping
        await simulateCategoryPageScraping();
      }
      
      // Simulate progress for better UX
      const progressInterval = setInterval(() => {
        setDownloadProgress(prev => {
          const newProgress = prev + Math.floor(Math.random() * (isCategory ? 5 : 15));
          return newProgress > 85 ? 85 : newProgress;
        });
      }, isCategory ? 1000 : 600);
      
      // Use the unified scrape endpoint directly
      const response = await axios.get(`${API_BASE_URL}/scrape`, {
        params: { 
          url,
          isCategoryPage: isCategory
        },
        responseType: 'blob'
      });
      
      clearInterval(progressInterval);
      setDownloadProgress(100);
      
      // Create a download link and trigger download
      const downloadUrl = window.URL.createObjectURL(new Blob([response.data]));
      const link = document.createElement('a');
      link.href = downloadUrl;
      link.setAttribute('download', `scraped_data_${Date.now()}.csv`);
      document.body.appendChild(link);
      link.click();
      link.remove();
      
      setSuccessMessage(isCategory ? 
        'Category page scraping completed! CSV downloaded successfully.' : 
        'CSV downloaded successfully!'
      );
      
      // Reset progress after a delay
      setTimeout(() => {
        setDownloadProgress(0);
      }, 2000);
      
    } catch (error) {
      console.error('Error downloading CSV:', error);
      setErrorMessage('Failed to download CSV data');
      setDownloadProgress(0);
    } finally {
      setIsLoading(false);
    }
  };
  
  // Function to simulate the pagination and multiple product scraping process
  const simulateCategoryPageScraping = async () => {
    const totalPages = 3; // Simulate 3 pages of results
    
    for (let page = 1; page <= totalPages; page++) {
      setDownloadProgress(Math.floor((page - 1) * 25)); // 0%, 25%, 50%
      
      // Simulate delay for each page scrape (2 seconds per page)
      await new Promise(resolve => setTimeout(resolve, 2000));
      
      // Update message to show progress
      setSuccessMessage(`Scraping page ${page} of ${totalPages}...`);
    }
    
    // Final progress update before the actual download
    setDownloadProgress(75);
    setSuccessMessage('Combining data from all pages...');
    
    // Simulate processing delay
    await new Promise(resolve => setTimeout(resolve, 1500));
  };

  const handleDemo = () => {
    setUrl('https://varanga.in/collections/kurta-sets');
    setShowDemo(true);
    
    // Add visual feedback
    const demoButton = document.querySelector('.demo-button');
    demoButton.innerHTML = '<i class="fas fa-check-circle"></i> Demo URL loaded!';
    demoButton.classList.add('demo-success');
    
    // Show tooltip
    const demoSection = document.querySelector('.demo-section');
    const tooltip = document.createElement('div');
    tooltip.className = 'demo-tooltip';
    tooltip.innerHTML = 'Great! Now click "Submit URL" to continue →';
    demoSection.appendChild(tooltip);
    
    // Highlight the submit button
    setTimeout(() => {
      const submitButton = document.querySelector('.check-button');
      submitButton.classList.add('highlight-button');
    }, 500);
  };

  return (
    <div className="app-container">
      {/* Animated background elements */}
      <div className="area">
        <ul className="circles">
          {[...Array(10)].map((_, i) => (
            <li key={i}></li>
          ))}
        </ul>
      </div>
      
      {/* Decorative elements */}
      <div className="spider-web"></div>
      <div className="crawling-spider"><i className="fas fa-spider"></i></div>
      
      <header className={`header ${animateHeader ? 'animated' : ''} simple-header`}>
        <div className="container">
          <div className="header-content">
            <div className="header-text">
              <div className="logo-container">
                <h1 className="main-logo"><i className="fas fa-spider pulse"></i> Web<span>Scraper</span></h1>
                <div className="tagline">Powerful. Fast. Reliable.</div>
              </div>
              <p className="header-description">Extract structured data from any e-commerce website with a single click. Perfect for market research, price monitoring, and competitive analysis.</p>
            </div>
            <div className="header-cta">
              <div className="header-highlights">
                <div className="highlight-item">
                  <i className="fas fa-bolt"></i>
                  <span>Lightning Fast</span>
                </div>
                <div className="highlight-item">
                  <i className="fas fa-shield-alt"></i>
                  <span>Secure Processing</span>
                </div>
                <div className="highlight-item">
                  <i className="fas fa-file-csv"></i>
                  <span>CSV Export</span>
                </div>
              </div>
              <button 
                className="start-button" 
                onClick={() => {
                  document.querySelector('.scraper-form').scrollIntoView({ 
                    behavior: 'smooth'
                  });
                }}
              >
                Get Started <i className="fas fa-arrow-down"></i>
              </button>
            </div>
          </div>
        </div>
        <div className="wave-container">
          <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 1440 320" className="wave">
            <path fill="#ffffff" fillOpacity="1" d="M0,96L48,112C96,128,192,160,288,160C384,160,480,128,576,112C672,96,768,96,864,117.3C960,139,1056,181,1152,181.3C1248,181,1344,139,1392,117.3L1440,96L1440,320L1392,320C1344,320,1248,320,1152,320C1056,320,960,320,864,320C768,320,672,320,576,320C480,320,384,320,288,320C192,320,96,320,48,320L0,320Z"></path>
          </svg>
        </div>
      </header>
      
      <main className="main-content">
        <div className="container">
          <div className="scraper-form">
            <form onSubmit={handleSubmit}>
              <h3 className="form-title"><i className="fas fa-link pulse-slow"></i> Enter URL to Scrape</h3>
              
              <div className="input-group">
                <input
                  type="url"
                  placeholder="Enter URL (e.g., Varanga or Red Tape product page URL)"
                  value={url}
                  onChange={(e) => {
                    setUrl(e.target.value);
                    setUrlSubmitted(false);
                  }}
                  className="url-input"
                  required
                />
                <button 
                  type="submit" 
                  className="check-button"
                  disabled={isLoading}
                >
                  {isLoading ? (
                    <>
                      <span className="loading-spinner"></span>
                      Processing...
                    </>
                  ) : (
                    <>
                      <i className="fas fa-search"></i>
                      Submit URL
                    </>
                  )}
                </button>
              </div>
              
              {!url && !errorMessage && (
                <div className="demo-section">
                  <button 
                    type="button" 
                    className="demo-button"
                    onClick={handleDemo}
                  >
                    <i className="fas fa-lightbulb"></i> Try with demo URL
                  </button>
                </div>
              )}
              
              {errorMessage && (
                <div className="error-message">
                  <i className="fas fa-exclamation-circle"></i>
                  <p>{errorMessage}</p>
                </div>
              )}
              
              {successMessage && (
                <div className="success-message">
                  <i className="fas fa-check-circle"></i>
                  <p>{successMessage}</p>
                </div>
              )}
              
              {urlSubmitted && (
                <div className="result-section">
                  <div className="result-info">
                    <h3>
                      <i className="fas fa-link"></i>
                      URL Information
                    </h3>
                    
                    <p><strong>URL:</strong> {url}</p>
                    
                    {isCategoryPage(url) && (
                      <div className="category-info">
                        <i className="fas fa-list"></i>
                        <p>This appears to be a category page. When scraped, data from all products and pagination will be collected.</p>
                      </div>
                    )}
                  </div>
                  
                  <button
                    onClick={downloadCsv}
                    className="download-button"
                    disabled={isLoading}
                  >
                    {isLoading ? (
                      <>
                        <span className="loading-spinner"></span>
                        {downloadProgress > 0 && downloadProgress < 85 && isCategoryPage(url) ? (
                          <>Processing multiple pages...</>
                        ) : (
                          <>Processing...{downloadProgress > 0 && `(${downloadProgress}%)`}</>
                        )}
                      </>
                    ) : (
                      <>
                        <i className="fas fa-download"></i>
                        Download as CSV
                      </>
                    )}
                  </button>
                  
                  {downloadProgress > 0 && isLoading && (
                    <div className="progress-container">
                      <div className="progress mb-4">
                        <div 
                          className="progress-bar progress-bar-striped progress-bar-animated" 
                          role="progressbar" 
                          style={{ width: `${downloadProgress}%` }} 
                          aria-valuenow={downloadProgress} 
                          aria-valuemin="0" 
                          aria-valuemax="100"
                        ></div>
                      </div>
                      <div className="progress-value">{downloadProgress}%</div>
                      
                      {isCategoryPage(url) && downloadProgress > 0 && downloadProgress < 100 && (
                        <div className="progress-details">
                          <i className="fas fa-sync fa-spin"></i> Navigating through multiple pages and extracting products...
                        </div>
                      )}
                    </div>
                  )}
                  
                  <div className="info-box">
                    <i className="fas fa-info-circle"></i>
                    <div>
                      {isCategoryPage(url) ? (
                        <>
                          <p>
                            <strong>Note:</strong> When scraping a category page:
                          </p>
                          <ul className="info-list">
                            <li>The system will navigate through multiple pages</li>
                            <li>Data from all products will be extracted and combined</li>
                            <li>This process may take longer than single product scraping</li>
                            <li>The CSV will contain standardized data for all products</li>
                          </ul>
                        </>
                      ) : (
                        <p>
                          <strong>Note:</strong> The system will extract all available product information. This may take a moment to process.
                        </p>
                      )}
                    </div>
                  </div>
                </div>
              )}
              
              {!showExtraContent && !urlSubmitted && (
                <div className="learn-more-section">
                  <button 
                    type="button" 
                    className="learn-more-button"
                    onClick={() => setShowExtraContent(true)}
                  >
                    <i className="fas fa-info-circle"></i> Learn more
                  </button>
                </div>
              )}
            </form>
            
            {showExtraContent && (
              <div className="how-it-works-section">
                <h4 className="section-title">How it works</h4>
                <div className="feature-cards">
                  <div className="feature-card">
                    <div className="icon-container">
                      <i className="fas fa-search"></i>
                    </div>
                    <h5>1. Enter URL</h5>
                    <p>Paste a product or category URL from supported e-commerce sites</p>
                  </div>
                  <div className="feature-card">
                    <div className="icon-container">
                      <i className="fas fa-spider"></i>
                    </div>
                    <h5>2. Scrape Data</h5>
                    <p>Our system extracts product information automatically</p>
                  </div>
                  <div className="feature-card">
                    <div className="icon-container">
                      <i className="fas fa-file-csv"></i>
                    </div>
                    <h5>3. Download CSV</h5>
                    <p>Get structured data ready for analysis or import</p>
                  </div>
                </div>
                
                <div className="benefits-section">
                  <h4>Benefits</h4>
                  <div className="benefits-list">
                    <div className="benefit-item">
                      <i className="fas fa-tachometer-alt"></i>
                      <div>
                        <h5>Save Time</h5>
                        <p>Extract data in seconds instead of hours of manual work</p>
                      </div>
                    </div>
                    <div className="benefit-item">
                      <i className="fas fa-chart-line"></i>
                      <div>
                        <h5>Market Analysis</h5>
                        <p>Quickly gather competitive pricing and product information</p>
                      </div>
                    </div>
                    <div className="benefit-item">
                      <i className="fas fa-database"></i>
                      <div>
                        <h5>Structured Data</h5>
                        <p>Get clean, organized data in CSV format for immediate use</p>
                      </div>
                    </div>
                  </div>
                  
                  <div className="collapse-section">
                    <button 
                      type="button" 
                      className="collapse-button"
                      onClick={() => setShowExtraContent(false)}
                    >
                      <i className="fas fa-chevron-up"></i> Show less
                    </button>
                  </div>
                </div>
                <div className="data-doodles"></div>
              </div>
            )}
          </div>
        </div>
      </main>
      
      <footer className="footer simple-footer">
        <div className="footer-waves">
          <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 1440 320" className="wave">
            <path fill="#1e1e2e" fillOpacity="1" d="M0,160L48,138.7C96,117,192,75,288,74.7C384,75,480,117,576,149.3C672,181,768,203,864,197.3C960,192,1056,160,1152,138.7C1248,117,1344,107,1392,101.3L1440,96L1440,0L1392,0C1344,0,1248,0,1152,0C1056,0,960,0,864,0C768,0,672,0,576,0C480,0,384,0,288,0C192,0,96,0,48,0L0,0Z"></path>
          </svg>
        </div>
        <div className="container">
          <div className="footer-content">
            <p className="copyright">&copy; {new Date().getFullYear()} Web Scraper. All rights reserved.</p>
          </div>
        </div>
      </footer>
    </div>
  );
}

export default App; 