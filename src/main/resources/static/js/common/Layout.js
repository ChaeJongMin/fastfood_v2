document.addEventListener('DOMContentLoaded', function() {
    fetch('/templates/common/footer.html')
        .then(response => response.text())
        .then(data => {
            const footerPlaceholder = document.getElementById('footer-placeholder');
            footerPlaceholder.innerHTML = data;
            const styleTags = data.match(/<style>([\s\S]*?)<\/style>/);
            if (styleTags && styleTags[1]) {
                const customStyle = document.createElement('style');
                customStyle.innerHTML = styleTags[1];
                document.head.appendChild(customStyle);
            }
        })
        .catch(error => console.log(error));
});