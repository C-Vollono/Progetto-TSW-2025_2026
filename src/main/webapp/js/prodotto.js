//Script per pagina prodotto.jsp
function changeMainImage(thumbnail) {
    const mainImage = document.getElementById("mainProductImage");
    mainImage.src = thumbnail.src;
    
    const allThumbnails = document.querySelectorAll(".thumbnail-img");
    allThumbnails.forEach(img => img.classList.remove("active-thumb"));
    thumbnail.classList.add("active-thumb");
}