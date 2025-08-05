// AWS-Style Documentation JavaScript
class DocumentationApp {
    constructor() {
        this.currentPage = this.getCurrentPage();
        this.init();
    }

    init() {
        this.setupNavigation();
        this.setupMobileMenu();
        this.setupScrollEffects();
        this.setupCodeCopy();
        this.highlightCurrentPage();
        this.setupFeatureCards();
    }

    getCurrentPage() {
        const path = window.location.pathname;
        const page = path.split('/').pop().replace('.html', '') || 'index';
        return page;
    }

    setupNavigation() {
        // Handle navigation clicks
        document.querySelectorAll('.nav-item').forEach(item => {
            item.addEventListener('click', (e) => {
                // Remove active class from all items
                document.querySelectorAll('.nav-item').forEach(nav => {
                    nav.classList.remove('active');
                });
                
                // Add active class to clicked item
                item.classList.add('active');
                
                // Update page content if it's a single-page app
                const href = item.getAttribute('href');
                if (href && href.startsWith('#')) {
                    e.preventDefault();
                    this.loadPageContent(href.substring(1));
                }
            });
        });
    }

    setupMobileMenu() {
        // Mobile menu toggle
        const mobileToggle = document.createElement('button');
        mobileToggle.className = 'mobile-menu-toggle';
        mobileToggle.innerHTML = '☰';
        mobileToggle.style.cssText = `
            display: none;
            position: fixed;
            top: 1rem;
            left: 1rem;
            z-index: 1001;
            background: var(--bg-card);
            border: 1px solid var(--border-primary);
            color: var(--text-primary);
            padding: 0.5rem;
            border-radius: 4px;
            font-size: 1.25rem;
            cursor: pointer;
        `;

        document.body.appendChild(mobileToggle);

        // Show mobile toggle on small screens
        const checkMobile = () => {
            if (window.innerWidth <= 768) {
                mobileToggle.style.display = 'block';
            } else {
                mobileToggle.style.display = 'none';
                document.querySelector('.sidebar').classList.remove('open');
            }
        };

        window.addEventListener('resize', checkMobile);
        checkMobile();

        // Toggle sidebar on mobile
        mobileToggle.addEventListener('click', () => {
            document.querySelector('.sidebar').classList.toggle('open');
        });

        // Close sidebar when clicking outside on mobile
        document.addEventListener('click', (e) => {
            const sidebar = document.querySelector('.sidebar');
            const toggle = mobileToggle;
            
            if (window.innerWidth <= 768 && 
                !sidebar.contains(e.target) && 
                !toggle.contains(e.target)) {
                sidebar.classList.remove('open');
            }
        });
    }

    setupScrollEffects() {
        // Smooth scrolling for anchor links
        document.querySelectorAll('a[href^="#"]').forEach(anchor => {
            anchor.addEventListener('click', function (e) {
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

        // Fade in animation on scroll
        const observerOptions = {
            threshold: 0.1,
            rootMargin: '0px 0px -50px 0px'
        };

        const observer = new IntersectionObserver((entries) => {
            entries.forEach(entry => {
                if (entry.isIntersecting) {
                    entry.target.classList.add('fade-in');
                }
            });
        }, observerOptions);

        // Observe cards and sections
        document.querySelectorAll('.card, .feature-card, .endpoint').forEach(el => {
            observer.observe(el);
        });
    }

    setupCodeCopy() {
        // Add copy buttons to code blocks
        document.querySelectorAll('.code-block').forEach(block => {
            const copyBtn = document.createElement('button');
            copyBtn.className = 'copy-btn';
            copyBtn.innerHTML = '📋';
            copyBtn.title = 'Copy to clipboard';
            copyBtn.style.cssText = `
                position: absolute;
                top: 0.5rem;
                right: 0.5rem;
                background: var(--bg-tertiary);
                border: 1px solid var(--border-primary);
                color: var(--text-secondary);
                padding: 0.25rem 0.5rem;
                border-radius: 4px;
                cursor: pointer;
                font-size: 0.875rem;
                transition: var(--transition);
            `;

            // Make code block relative for absolute positioning
            block.style.position = 'relative';
            block.appendChild(copyBtn);

            copyBtn.addEventListener('click', async () => {
                const code = block.querySelector('pre').textContent;
                try {
                    await navigator.clipboard.writeText(code);
                    copyBtn.innerHTML = '✅';
                    copyBtn.style.color = 'var(--accent-success)';
                    setTimeout(() => {
                        copyBtn.innerHTML = '📋';
                        copyBtn.style.color = 'var(--text-secondary)';
                    }, 2000);
                } catch (err) {
                    console.error('Failed to copy code:', err);
                }
            });

            copyBtn.addEventListener('mouseenter', () => {
                copyBtn.style.background = 'var(--bg-hover)';
                copyBtn.style.color = 'var(--text-primary)';
            });

            copyBtn.addEventListener('mouseleave', () => {
                copyBtn.style.background = 'var(--bg-tertiary)';
                copyBtn.style.color = 'var(--text-secondary)';
            });
        });
    }

    highlightCurrentPage() {
        // Highlight current page in navigation
        const currentPath = window.location.pathname;
        const currentPage = currentPath.split('/').pop() || 'index.html';
        
        document.querySelectorAll('.nav-item').forEach(item => {
            const href = item.getAttribute('href');
            if (href === currentPage || 
                (currentPage === 'index.html' && href === './') ||
                (currentPage === '' && href === './')) {
                item.classList.add('active');
            }
        });
    }

    setupFeatureCards() {
        // Add click handlers to feature cards
        document.querySelectorAll('.feature-card').forEach(card => {
            card.addEventListener('click', () => {
                const link = card.dataset.link;
                if (link) {
                    window.location.href = link;
                }
            });

            // Add hover effects
            card.addEventListener('mouseenter', () => {
                card.style.transform = 'translateY(-8px)';
            });

            card.addEventListener('mouseleave', () => {
                card.style.transform = 'translateY(-4px)';
            });
        });
    }

    // Utility method to load page content dynamically (if needed)
    async loadPageContent(page) {
        try {
            const response = await fetch(`pages/${page}.html`);
            if (response.ok) {
                const content = await response.text();
                document.querySelector('.content-body').innerHTML = content;
                
                // Re-setup interactions for new content
                this.setupScrollEffects();
                this.setupCodeCopy();
            }
        } catch (error) {
            console.error('Error loading page:', error);
        }
    }

    // Method to update breadcrumb
    updateBreadcrumb(items) {
        const breadcrumb = document.querySelector('.breadcrumb');
        if (breadcrumb) {
            breadcrumb.innerHTML = items.map((item, index) => {
                if (index === items.length - 1) {
                    return `<span>${item}</span>`;
                } else {
                    return `<a href="${item.link || '#'}">${item.text || item}</a> <span>/</span>`;
                }
            }).join(' ');
        }
    }

    // Method to show loading state
    showLoading() {
        const loader = document.createElement('div');
        loader.className = 'loading-spinner';
        loader.innerHTML = `
            <div style="
                display: flex;
                justify-content: center;
                align-items: center;
                height: 200px;
                color: var(--text-secondary);
            ">
                <div style="
                    width: 40px;
                    height: 40px;
                    border: 3px solid var(--border-primary);
                    border-top: 3px solid var(--accent-primary);
                    border-radius: 50%;
                    animation: spin 1s linear infinite;
                "></div>
            </div>
        `;
        
        // Add spin animation
        const style = document.createElement('style');
        style.textContent = `
            @keyframes spin {
                0% { transform: rotate(0deg); }
                100% { transform: rotate(360deg); }
            }
        `;
        document.head.appendChild(style);
        
        return loader;
    }
}

// Initialize the app when DOM is loaded
document.addEventListener('DOMContentLoaded', () => {
    window.docApp = new DocumentationApp();
});

// Add some global utilities
window.docUtils = {
    // Format code blocks with syntax highlighting (basic)
    formatCode: (code, language = 'javascript') => {
        // Basic syntax highlighting could be added here
        return `<pre><code class="language-${language}">${code}</code></pre>`;
    },
    
    // Create notification
    notify: (message, type = 'info') => {
        const notification = document.createElement('div');
        notification.style.cssText = `
            position: fixed;
            top: 2rem;
            right: 2rem;
            background: var(--bg-card);
            border: 1px solid var(--border-primary);
            color: var(--text-primary);
            padding: 1rem 1.5rem;
            border-radius: 6px;
            box-shadow: var(--shadow-lg);
            z-index: 1002;
            max-width: 300px;
            animation: slideIn 0.3s ease-out;
        `;
        
        const colors = {
            success: 'var(--accent-success)',
            error: 'var(--accent-error)',
            warning: 'var(--accent-warning)',
            info: 'var(--accent-secondary)'
        };
        
        notification.style.borderLeftColor = colors[type] || colors.info;
        notification.innerHTML = `
            <div style="display: flex; align-items: center; gap: 0.5rem;">
                <span style="color: ${colors[type] || colors.info};">
                    ${type === 'success' ? '✅' : type === 'error' ? '❌' : type === 'warning' ? '⚠️' : 'ℹ️'}
                </span>
                <span>${message}</span>
            </div>
        `;
        
        document.body.appendChild(notification);
        
        setTimeout(() => {
            notification.style.animation = 'slideOut 0.3s ease-in';
            setTimeout(() => notification.remove(), 300);
        }, 3000);
    }
};

// Add slide animations for notifications
const notificationStyles = document.createElement('style');
notificationStyles.textContent = `
    @keyframes slideIn {
        from { transform: translateX(100%); opacity: 0; }
        to { transform: translateX(0); opacity: 1; }
    }
    
    @keyframes slideOut {
        from { transform: translateX(0); opacity: 1; }
        to { transform: translateX(100%); opacity: 0; }
    }
`;
document.head.appendChild(notificationStyles);
