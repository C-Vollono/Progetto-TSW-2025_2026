document.addEventListener("DOMContentLoaded", function() {
    
    let slides = document.querySelectorAll(".carousel-slide");
    let dots = document.querySelectorAll(".dot");
    
    if (slides.length > 0) {
        let currentSlide = 0;
        let slideInterval;

        function showSlide(index) {
            slides[currentSlide].classList.remove("active");
            if (dots.length > 0) dots[currentSlide].classList.remove("active");

            currentSlide = index;
			
            slides[currentSlide].classList.add("active");
            if (dots.length > 0) dots[currentSlide].classList.add("active");
        }

        function nextSlide() {
            let next = (currentSlide + 1) % slides.length;
            showSlide(next);
        }

        function startSlideShow() {
            slideInterval = setInterval(nextSlide, 5000);
        }

        startSlideShow();
		
        dots.forEach((dot, index) => {
            dot.addEventListener("click", () => {
                clearInterval(slideInterval);
                
                showSlide(index);
                
                startSlideShow();
            });
        });
    }

});