// Custom JavaScript for Tech Challenge Restaurant API Documentation

document.addEventListener('DOMContentLoaded', function() {
    
    // Add copy functionality to code blocks
    addCopyButtons();
    
    // Add API endpoint styling
    styleApiEndpoints();
    
    // Add status code badges
    addStatusCodeBadges();
    
    // Initialize tooltips
    initializeTooltips();
    
    // Add smooth scrolling
    addSmoothScrolling();
    
    // Add search enhancements
    enhanceSearch();
    
    // Add theme toggle functionality
    enhanceThemeToggle();
    
    // Add progress indicator
    addProgressIndicator();
    
    // Initialize analytics (if needed)
    initializeAnalytics();
});

/**
 * Add copy buttons to code blocks
 */
function addCopyButtons() {
    const codeBlocks = document.querySelectorAll('pre code');
    
    codeBlocks.forEach(function(codeBlock) {
        const pre = codeBlock.parentElement;
        const button = document.createElement('button');
        
        button.className = 'copy-button';
        button.innerHTML = '📋';
        button.title = 'Copiar código';
        button.style.cssText = `
            position: absolute;
            top: 8px;
            right: 8px;
            background: rgba(0,0,0,0.7);
            color: white;
            border: none;
            border-radius: 4px;
            padding: 4px 8px;
            cursor: pointer;
            font-size: 12px;
            z-index: 1;
        `;
        
        pre.style.position = 'relative';
        pre.appendChild(button);
        
        button.addEventListener('click', function() {
            navigator.clipboard.writeText(codeBlock.textContent).then(function() {
                button.innerHTML = '✅';
                button.title = 'Copiado!';
                
                setTimeout(function() {
                    button.innerHTML = '📋';
                    button.title = 'Copiar código';
                }, 2000);
            });
        });
    });
}

/**
 * Style API endpoints
 */
function styleApiEndpoints() {
    const codeBlocks = document.querySelectorAll('code');
    
    codeBlocks.forEach(function(code) {
        const text = code.textContent;
        
        // Check if it's an HTTP method + path
        const httpMethodRegex = /^(GET|POST|PUT|DELETE|PATCH)\s+(.+)$/;
        const match = text.match(httpMethodRegex);
        
        if (match) {
            const method = match[1];
            const path = match[2];
            
            code.innerHTML = `
                <span class="api-method ${method.toLowerCase()}">${method}</span>
                <span class="api-path">${path}</span>
            `;
            code.classList.add('api-endpoint-code');
        }
    });
}

/**
 * Add status code badges
 */
function addStatusCodeBadges() {
    const statusCodes = {
        '200': 'success',
        '201': 'success',
        '204': 'success',
        '400': 'error',
        '401': 'error',
        '403': 'error',
        '404': 'error',
        '409': 'error',
        '422': 'error',
        '500': 'error',
        '503': 'error'
    };
    
    document.querySelectorAll('strong').forEach(function(element) {
        const text = element.textContent;
        const statusMatch = text.match(/^Status:\s*(\d{3})/);
        
        if (statusMatch) {
            const code = statusMatch[1];
            const type = statusCodes[code] || 'warning';
            
            element.innerHTML = `<span class="status-code ${type}">Status: ${code}</span>`;
        }
    });
}

/**
 * Initialize tooltips
 */
function initializeTooltips() {
    const tooltipElements = document.querySelectorAll('[title]');
    
    tooltipElements.forEach(function(element) {
        element.addEventListener('mouseenter', function(e) {
            const tooltip = document.createElement('div');
            tooltip.className = 'custom-tooltip';
            tooltip.textContent = e.target.title;
            tooltip.style.cssText = `
                position: absolute;
                background: rgba(0,0,0,0.8);
                color: white;
                padding: 4px 8px;
                border-radius: 4px;
                font-size: 12px;
                z-index: 1000;
                pointer-events: none;
                white-space: nowrap;
            `;
            
            document.body.appendChild(tooltip);
            
            const rect = e.target.getBoundingClientRect();
            tooltip.style.left = rect.left + 'px';
            tooltip.style.top = (rect.top - tooltip.offsetHeight - 5) + 'px';
            
            // Remove title to prevent default tooltip
            e.target.setAttribute('data-title', e.target.title);
            e.target.removeAttribute('title');
        });
        
        element.addEventListener('mouseleave', function(e) {
            const tooltip = document.querySelector('.custom-tooltip');
            if (tooltip) {
                tooltip.remove();
            }
            
            // Restore title
            if (e.target.getAttribute('data-title')) {
                e.target.title = e.target.getAttribute('data-title');
                e.target.removeAttribute('data-title');
            }
        });
    });
}

/**
 * Add smooth scrolling
 */
function addSmoothScrolling() {
    document.querySelectorAll('a[href^="#"]').forEach(function(anchor) {
        anchor.addEventListener('click', function(e) {
            e.preventDefault();
            
            const target = document.querySelector(this.getAttribute('href'));
            if (target) {
                target.scrollIntoView({
                    behavior: 'smooth',
                    block: 'start'
                });
            }
        });
    });
}

/**
 * Enhance search functionality
 */
function enhanceSearch() {
    const searchInput = document.querySelector('.md-search__input');
    
    if (searchInput) {
        // Add search suggestions
        searchInput.addEventListener('input', function(e) {
            const query = e.target.value.toLowerCase();
            
            if (query.length > 2) {
                // Add search highlighting logic here
                highlightSearchResults(query);
            }
        });
        
        // Add keyboard shortcuts
        document.addEventListener('keydown', function(e) {
            // Ctrl/Cmd + K to focus search
            if ((e.ctrlKey || e.metaKey) && e.key === 'k') {
                e.preventDefault();
                searchInput.focus();
            }
            
            // Escape to clear search
            if (e.key === 'Escape' && document.activeElement === searchInput) {
                searchInput.value = '';
                searchInput.blur();
            }
        });
    }
}

/**
 * Highlight search results
 */
function highlightSearchResults(query) {
    // Remove previous highlights
    document.querySelectorAll('.search-highlight').forEach(function(element) {
        element.classList.remove('search-highlight');
    });
    
    // Add new highlights
    const textNodes = getTextNodes(document.body);
    textNodes.forEach(function(node) {
        if (node.textContent.toLowerCase().includes(query)) {
            const parent = node.parentElement;
            if (parent && !parent.closest('.md-search')) {
                parent.classList.add('search-highlight');
            }
        }
    });
}

/**
 * Get all text nodes
 */
function getTextNodes(element) {
    const textNodes = [];
    const walker = document.createTreeWalker(
        element,
        NodeFilter.SHOW_TEXT,
        null,
        false
    );
    
    let node;
    while (node = walker.nextNode()) {
        textNodes.push(node);
    }
    
    return textNodes;
}

/**
 * Enhance theme toggle
 */
function enhanceThemeToggle() {
    const themeToggle = document.querySelector('[data-md-component="palette"]');
    
    if (themeToggle) {
        // Add transition effect
        document.body.style.transition = 'background-color 0.3s ease, color 0.3s ease';
        
        // Save theme preference
        const inputs = themeToggle.querySelectorAll('input');
        inputs.forEach(function(input) {
            input.addEventListener('change', function() {
                if (this.checked) {
                    localStorage.setItem('theme', this.value);
                }
            });
        });
        
        // Load saved theme
        const savedTheme = localStorage.getItem('theme');
        if (savedTheme) {
            const themeInput = themeToggle.querySelector(`input[value="${savedTheme}"]`);
            if (themeInput) {
                themeInput.checked = true;
            }
        }
    }
}

/**
 * Add progress indicator
 */
function addProgressIndicator() {
    const progressBar = document.createElement('div');
    progressBar.className = 'reading-progress';
    progressBar.style.cssText = `
        position: fixed;
        top: 0;
        left: 0;
        width: 0%;
        height: 3px;
        background: linear-gradient(90deg, #3f51b5, #ff4081);
        z-index: 1000;
        transition: width 0.3s ease;
    `;
    
    document.body.appendChild(progressBar);
    
    window.addEventListener('scroll', function() {
        const scrollTop = window.pageYOffset;
        const docHeight = document.body.scrollHeight - window.innerHeight;
        const scrollPercent = (scrollTop / docHeight) * 100;
        
        progressBar.style.width = scrollPercent + '%';
    });
}

/**
 * Initialize analytics (placeholder)
 */
function initializeAnalytics() {
    // Add Google Analytics or other analytics here if needed
    console.log('Analytics initialized');
    
    // Track page views
    if (typeof gtag !== 'undefined') {
        gtag('config', 'GA_MEASUREMENT_ID', {
            page_title: document.title,
            page_location: window.location.href
        });
    }
}

/**
 * Add interactive elements
 */
function addInteractiveElements() {
    // Add expand/collapse for long code blocks
    document.querySelectorAll('pre').forEach(function(pre) {
        if (pre.scrollHeight > 400) {
            pre.style.maxHeight = '400px';
            pre.style.overflow = 'hidden';
            
            const expandButton = document.createElement('button');
            expandButton.textContent = 'Mostrar mais';
            expandButton.className = 'expand-button';
            expandButton.style.cssText = `
                display: block;
                margin: 8px auto;
                padding: 4px 12px;
                background: #3f51b5;
                color: white;
                border: none;
                border-radius: 4px;
                cursor: pointer;
                font-size: 12px;
            `;
            
            pre.parentElement.appendChild(expandButton);
            
            expandButton.addEventListener('click', function() {
                if (pre.style.maxHeight === '400px') {
                    pre.style.maxHeight = 'none';
                    pre.style.overflow = 'visible';
                    expandButton.textContent = 'Mostrar menos';
                } else {
                    pre.style.maxHeight = '400px';
                    pre.style.overflow = 'hidden';
                    expandButton.textContent = 'Mostrar mais';
                }
            });
        }
    });
}

/**
 * Add table enhancements
 */
function enhanceTables() {
    document.querySelectorAll('table').forEach(function(table) {
        // Make tables responsive
        const wrapper = document.createElement('div');
        wrapper.className = 'table-wrapper';
        wrapper.style.cssText = `
            overflow-x: auto;
            margin: 1rem 0;
            border-radius: 8px;
            box-shadow: 0 2px 8px rgba(0,0,0,0.1);
        `;
        
        table.parentNode.insertBefore(wrapper, table);
        wrapper.appendChild(table);
        
        // Add sorting functionality to headers
        const headers = table.querySelectorAll('th');
        headers.forEach(function(header, index) {
            header.style.cursor = 'pointer';
            header.addEventListener('click', function() {
                sortTable(table, index);
            });
        });
    });
}

/**
 * Sort table by column
 */
function sortTable(table, column) {
    const tbody = table.querySelector('tbody');
    const rows = Array.from(tbody.querySelectorAll('tr'));
    
    const sortedRows = rows.sort(function(a, b) {
        const aText = a.cells[column].textContent.trim();
        const bText = b.cells[column].textContent.trim();
        
        // Try to parse as numbers
        const aNum = parseFloat(aText);
        const bNum = parseFloat(bText);
        
        if (!isNaN(aNum) && !isNaN(bNum)) {
            return aNum - bNum;
        }
        
        return aText.localeCompare(bText);
    });
    
    // Clear tbody and append sorted rows
    tbody.innerHTML = '';
    sortedRows.forEach(function(row) {
        tbody.appendChild(row);
    });
}

/**
 * Add keyboard navigation
 */
function addKeyboardNavigation() {
    document.addEventListener('keydown', function(e) {
        // Alt + Left/Right for navigation
        if (e.altKey) {
            const prevLink = document.querySelector('.md-footer__link--prev');
            const nextLink = document.querySelector('.md-footer__link--next');
            
            if (e.key === 'ArrowLeft' && prevLink) {
                e.preventDefault();
                prevLink.click();
            } else if (e.key === 'ArrowRight' && nextLink) {
                e.preventDefault();
                nextLink.click();
            }
        }
    });
}

// Initialize additional features when DOM is ready
document.addEventListener('DOMContentLoaded', function() {
    addInteractiveElements();
    enhanceTables();
    addKeyboardNavigation();
});

// Add service worker for offline functionality (optional)
if ('serviceWorker' in navigator) {
    window.addEventListener('load', function() {
        navigator.serviceWorker.register('/sw.js').then(function(registration) {
            console.log('SW registered: ', registration);
        }).catch(function(registrationError) {
            console.log('SW registration failed: ', registrationError);
        });
    });
}
