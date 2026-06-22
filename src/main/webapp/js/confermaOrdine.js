document.addEventListener("DOMContentLoaded", function() {
    
    //GESTIONE STAMPA RICEVUTA
    const btnPrint = document.getElementById('btnPrint');
    if (btnPrint) {
        btnPrint.addEventListener('click', function() {
            window.print();
        });
    }

    //GESTIONE HOVER
    const btnSecondaries = document.querySelectorAll('.btn-secondary');
    btnSecondaries.forEach(btn => {
        btn.addEventListener('mouseenter', function() {
            this.classList.add('btn-hover');
        });
        
        btn.addEventListener('mouseleave', function() {
            this.classList.remove('btn-hover');
        });
    });
});