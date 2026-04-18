/* ============================================
   MAIN.JS (v3.1) - Infinite Loop Slideshow
   Slideshow chay vong tron lien mach
   ============================================ */

document.addEventListener('DOMContentLoaded', function() {

    // ============================
    // SLIDESHOW VONG TRON LIEN MACH
    // ============================
    var track = document.getElementById('slideshowTrack');
    var dotsContainer = document.getElementById('slideDots');

    if (!track) return;

    var originalSlides = track.querySelectorAll('.slide');
    var totalOriginal = originalSlides.length;

    if (totalOriginal === 0) return;

    // === BUOC 1: Clone slide dau va cuoi de tao hieu ung vong tron ===
    // Them ban sao slide cuoi vao DAU track
    var lastClone = originalSlides[totalOriginal - 1].cloneNode(true);
    lastClone.classList.add('clone');
    track.insertBefore(lastClone, track.firstChild);

    // Them ban sao slide dau vao CUOI track
    var firstClone = originalSlides[0].cloneNode(true);
    firstClone.classList.add('clone');
    track.appendChild(firstClone);

    // Bay gio track co: [clone-cuoi] [slide1] [slide2] [slide3] [slide4] [clone-dau]
    // Index:               0          1        2        3        4         5

    var currentIndex = 1; // Bat dau tu slide that dau tien (index 1)
    var isTransitioning = false;
    var autoPlayInterval;

    // Dat vi tri ban dau (khong co animation)
    track.style.transition = 'none';
    track.style.transform = 'translateX(-' + (currentIndex * 100) + '%)';

    // === BUOC 2: Tao cham indicator ===
    if (dotsContainer) {
        for (var i = 0; i < totalOriginal; i++) {
            var dot = document.createElement('button');
            dot.className = 'slide-dot' + (i === 0 ? ' active' : '');
            dot.setAttribute('data-index', i);
            dot.addEventListener('click', function() {
                if (isTransitioning) return;
                var targetOriginal = parseInt(this.getAttribute('data-index'));
                goToSlide(targetOriginal + 1); // +1 vi co clone o dau
            });
            dotsContainer.appendChild(dot);
        }
    }

    // === BUOC 3: Ham chuyen slide ===
    function goToSlide(index) {
        if (isTransitioning) return;
        isTransitioning = true;
        currentIndex = index;

        track.style.transition = 'transform 0.6s ease';
        track.style.transform = 'translateX(-' + (currentIndex * 100) + '%)';

        updateDots();
    }

    // Khi animation ket thuc, kiem tra co can nhay ve vi tri that khong
    track.addEventListener('transitionend', function() {
        // Neu dang o clone-dau (cuoi track) → nhay ve slide that dau tien
        if (currentIndex >= totalOriginal + 1) {
            track.style.transition = 'none';
            currentIndex = 1;
            track.style.transform = 'translateX(-' + (currentIndex * 100) + '%)';
        }

        // Neu dang o clone-cuoi (dau track) → nhay ve slide that cuoi cung
        if (currentIndex <= 0) {
            track.style.transition = 'none';
            currentIndex = totalOriginal;
            track.style.transform = 'translateX(-' + (currentIndex * 100) + '%)';
        }

        isTransitioning = false;
        updateDots();
    });

    // Cap nhat cham indicator
    function updateDots() {
        if (!dotsContainer) return;
        var realIndex = currentIndex - 1; // Tru 1 vi clone o dau
        if (realIndex < 0) realIndex = totalOriginal - 1;
        if (realIndex >= totalOriginal) realIndex = 0;

        var dots = dotsContainer.querySelectorAll('.slide-dot');
        dots.forEach(function(dot, i) {
            dot.classList.toggle('active', i === realIndex);
        });
    }

    // === BUOC 4: Nut prev/next ===
    window.changeSlide = function(direction) {
        if (isTransitioning) return;
        goToSlide(currentIndex + direction);
        resetAutoPlay();
    };

    // === BUOC 5: Tu dong chay moi 3 giay ===
    function startAutoPlay() {
        clearInterval(autoPlayInterval);
        autoPlayInterval = setInterval(function() {
            goToSlide(currentIndex + 1);
        }, 3000);
    }

    function resetAutoPlay() {
        clearInterval(autoPlayInterval);
        startAutoPlay();
    }

    startAutoPlay();

    // Dung khi hover
    var slideshow = document.getElementById('slideshow');
    if (slideshow) {
        slideshow.addEventListener('mouseenter', function() {
            clearInterval(autoPlayInterval);
        });
        slideshow.addEventListener('mouseleave', function() {
            startAutoPlay();
        });
    }

    // ============================
    // FADE-IN ANIMATION
    // ============================
    var fadeElements = document.querySelectorAll('.fade-in');

    if ('IntersectionObserver' in window) {
        var observer = new IntersectionObserver(function(entries) {
            entries.forEach(function(entry) {
                if (entry.isIntersecting) {
                    entry.target.classList.add('visible');
                    observer.unobserve(entry.target);
                }
            });
        }, {
            threshold: 0.15,
            rootMargin: '0px 0px -50px 0px'
        });

        fadeElements.forEach(function(el) {
            observer.observe(el);
        });
    } else {
        fadeElements.forEach(function(el) {
            el.classList.add('visible');
        });
    }

    // ============================
    // SMOOTH SCROLL
    // ============================
    document.querySelectorAll('a[href^="#"]').forEach(function(anchor) {
        anchor.addEventListener('click', function(e) {
            var targetId = this.getAttribute('href');
            if (targetId === '#') return;
            var target = document.querySelector(targetId);
            if (target) {
                e.preventDefault();
                target.scrollIntoView({ behavior: 'smooth', block: 'start' });
                var navMenu = document.getElementById('navMenu');
                if (navMenu) navMenu.classList.remove('open');
            }
        });
    });
});