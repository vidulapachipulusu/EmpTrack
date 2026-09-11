// Smooth scroll navigation
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

// Hamburger menu toggle
const hamburger = document.querySelector('.hamburger');
const navMenu = document.querySelector('.nav-menu');

if (hamburger) {
    hamburger.addEventListener('click', () => {
        navMenu.classList.toggle('active');
        hamburger.classList.toggle('active');
    });

    // Close menu when link is clicked
    document.querySelectorAll('.nav-link').forEach(link => {
        link.addEventListener('click', () => {
            navMenu.classList.remove('active');
            hamburger.classList.remove('active');
        });
    });
}

// Intersection Observer for fade-in animations
const observerOptions = {
    threshold: 0.1,
    rootMargin: '0px 0px -100px 0px'
};

const observer = new IntersectionObserver((entries) => {
    entries.forEach(entry => {
        if (entry.isIntersecting) {
            entry.target.style.animation = 'fadeIn 0.6s ease forwards';
            observer.unobserve(entry.target);
        }
    });
}, observerOptions);

// Observe all feature cards, task cards, etc.
document.querySelectorAll('.feature-card, .task-card, .tech-item, .demo-box').forEach(el => {
    el.style.opacity = '0';
    observer.observe(el);
});

// Filter employees function (for demo)
function filterEmployees() {
    const searchInput = document.getElementById('searchInput');
    const departmentFilter = document.getElementById('departmentFilter');
    const resultsDiv = document.getElementById('results');

    const searchTerm = searchInput.value.toLowerCase();
    const department = departmentFilter.value;

    // Sample employee data
    const employees = [
        { id: 'EMP-001', name: 'Rahul Sharma', department: 'Engineering', salary: 85000, isActive: true },
        { id: 'EMP-002', name: 'Ananya Iyer', department: 'Engineering', salary: 42000, isActive: true },
        { id: 'EMP-003', name: 'Priya Verma', department: 'HR', salary: 55000, isActive: true },
        { id: 'EMP-004', name: 'Amit Patel', department: 'Finance', salary: 72000, isActive: true },
        { id: 'EMP-005', name: 'Neha Singh', department: 'Engineering', salary: 65000, isActive: true },
        { id: 'EMP-006', name: 'Rohan Gupta', department: 'Finance', salary: 48000, isActive: false },
        { id: 'EMP-007', name: 'Sneha Kapoor', department: 'HR', salary: 52000, isActive: true },
        { id: 'EMP-008', name: 'Vikram Desai', department: 'Engineering', salary: 90000, isActive: true },
        { id: 'EMP-009', name: 'Meera Nair', department: 'Finance', salary: 68000, isActive: true },
        { id: 'EMP-010', name: 'Arjun Reddy', department: 'HR', salary: 50000, isActive: true }
    ];

    // Filter employees
    const filtered = employees.filter(emp => {
        const matchesSearch = emp.name.toLowerCase().includes(searchTerm) ||
                            emp.id.toLowerCase().includes(searchTerm);
        const matchesDepartment = !department || emp.department === department;
        return matchesSearch && matchesDepartment;
    });

    // Display results
    if (filtered.length === 0) {
        resultsDiv.innerHTML = '<p>No employees found matching your criteria.</p>';
    } else {
        let html = '<table style="width: 100%; border-collapse: collapse;">';
        html += '<thead><tr style="background-color: #667eea; color: white;">';
        html += '<th style="padding: 10px; text-align: left; border: 1px solid #ddd;">ID</th>';
        html += '<th style="padding: 10px; text-align: left; border: 1px solid #ddd;">Name</th>';
        html += '<th style="padding: 10px; text-align: left; border: 1px solid #ddd;">Department</th>';
        html += '<th style="padding: 10px; text-align: left; border: 1px solid #ddd;">Salary</th>';
        html += '<th style="padding: 10px; text-align: left; border: 1px solid #ddd;">Status</th>';
        html += '</tr></thead><tbody>';

        filtered.forEach(emp => {
            const status = emp.isActive ? '<span style="color: green; font-weight: bold;">Active</span>' :
                                        '<span style="color: red; font-weight: bold;">Inactive</span>';
            html += `<tr style="border: 1px solid #ddd;">`;
            html += `<td style="padding: 10px; border: 1px solid #ddd;">${emp.id}</td>`;
            html += `<td style="padding: 10px; border: 1px solid #ddd;">${emp.name}</td>`;
            html += `<td style="padding: 10px; border: 1px solid #ddd;">${emp.department}</td>`;
            html += `<td style="padding: 10px; border: 1px solid #ddd;">₹${emp.salary.toLocaleString()}</td>`;
            html += `<td style="padding: 10px; border: 1px solid #ddd;">${status}</td>`;
            html += `</tr>`;
        });

        html += '</tbody></table>';
        resultsDiv.innerHTML = html;
    }
}

// Add enter key support for search
document.addEventListener('DOMContentLoaded', function() {
    const searchInput = document.getElementById('searchInput');
    if (searchInput) {
        searchInput.addEventListener('keypress', function(e) {
            if (e.key === 'Enter') {
                filterEmployees();
            }
        });
    }
});

// Scroll to top button
const scrollTopBtn = document.createElement('button');
scrollTopBtn.innerHTML = '<i class="fas fa-arrow-up"></i>';
scrollTopBtn.style.cssText = `
    position: fixed;
    bottom: 30px;
    right: 30px;
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    color: white;
    border: none;
    padding: 15px 15px;
    border-radius: 50%;
    cursor: pointer;
    display: none;
    z-index: 99;
    font-size: 18px;
    transition: all 0.3s ease;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
`;

document.body.appendChild(scrollTopBtn);

// Show/hide scroll to top button
window.addEventListener('scroll', () => {
    if (window.pageYOffset > 300) {
        scrollTopBtn.style.display = 'block';
    } else {
        scrollTopBtn.style.display = 'none';
    }
});

// Scroll to top
scrollTopBtn.addEventListener('click', () => {
    window.scrollTo({
        top: 0,
        behavior: 'smooth'
    });
});

// Add hover effect to scroll top button
scrollTopBtn.addEventListener('mouseover', () => {
    scrollTopBtn.style.transform = 'scale(1.1)';
});

scrollTopBtn.addEventListener('mouseout', () => {
    scrollTopBtn.style.transform = 'scale(1)';
});

// Counter animation for stats
function animateCounter(element, target, duration = 2000) {
    const start = 0;
    const increment = target / (duration / 16);
    let current = start;

    const timer = setInterval(() => {
        current += increment;
        if (current >= target) {
            element.textContent = target;
            clearInterval(timer);
        } else {
            element.textContent = Math.floor(current);
        }
    }, 16);
}

// Initialize tooltips
function initializeTooltips() {
    const tooltips = document.querySelectorAll('[data-tooltip]');
    tooltips.forEach(element => {
        element.addEventListener('mouseover', function() {
            const tooltip = document.createElement('div');
            tooltip.className = 'tooltip';
            tooltip.textContent = this.getAttribute('data-tooltip');
            tooltip.style.cssText = `
                position: absolute;
                background-color: #333;
                color: #fff;
                padding: 10px 15px;
                border-radius: 5px;
                font-size: 0.9rem;
                white-space: nowrap;
                z-index: 1000;
                margin-top: 10px;
            `;
            document.body.appendChild(tooltip);

            const rect = this.getBoundingClientRect();
            tooltip.style.left = rect.left + rect.width / 2 - tooltip.offsetWidth / 2 + 'px';
            tooltip.style.top = rect.bottom + 'px';

            this.addEventListener('mouseout', () => {
                tooltip.remove();
            });
        });
    });
}

// Call initialization functions
window.addEventListener('load', () => {
    initializeTooltips();
});

// Lazy loading for images
if ('IntersectionObserver' in window) {
    const imageObserver = new IntersectionObserver((entries, observer) => {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                const img = entry.target;
                img.src = img.dataset.src;
                img.classList.add('loaded');
                imageObserver.unobserve(img);
            }
        });
    });

    document.querySelectorAll('img[data-src]').forEach(img => imageObserver.observe(img));
}

// Form validation helper
function validateEmail(email) {
    const re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return re.test(email);
}

// Add active state to navigation based on scroll position
window.addEventListener('scroll', () => {
    let current = '';
    const sections = document.querySelectorAll('section');

    sections.forEach(section => {
        const sectionTop = section.offsetTop;
        const sectionHeight = section.clientHeight;
        if (pageYOffset >= sectionTop - 200) {
            current = section.getAttribute('id');
        }
    });

    document.querySelectorAll('.nav-link').forEach(link => {
        link.classList.remove('active');
        if (link.getAttribute('href').slice(1) === current) {
            link.classList.add('active');
            link.style.color = '#ffd700';
        } else {
            link.style.color = 'white';
        }
    });
});

// Ripple effect for buttons
document.querySelectorAll('.btn').forEach(button => {
    button.addEventListener('click', function(e) {
        const ripple = document.createElement('span');
        const rect = this.getBoundingClientRect();
        const size = Math.max(rect.width, rect.height);
        const x = e.clientX - rect.left - size / 2;
        const y = e.clientY - rect.top - size / 2;

        ripple.style.cssText = `
            width: ${size}px;
            height: ${size}px;
            background: rgba(255, 255, 255, 0.5);
            border-radius: 50%;
            position: absolute;
            left: ${x}px;
            top: ${y}px;
            pointer-events: none;
            animation: ripple 0.6s ease-out;
        `;

        this.style.position = 'relative';
        this.style.overflow = 'hidden';
        this.appendChild(ripple);

        setTimeout(() => ripple.remove(), 600);
    });
});

// Add ripple animation
const style = document.createElement('style');
style.textContent = `
    @keyframes ripple {
        to {
            transform: scale(4);
            opacity: 0;
        }
    }
`;
document.head.appendChild(style);

// Console welcome message
console.log('%cWelcome to EmpTrack!', 'color: #667eea; font-size: 20px; font-weight: bold;');
console.log('%cEmployee Report Generator using Stream API, File Handling, and Serialization', 'color: #764ba2; font-size: 14px;');
console.log('%cRepository: https://github.com/vidulapachipulusu/EmpTrack', 'color: #666; font-size: 12px;');