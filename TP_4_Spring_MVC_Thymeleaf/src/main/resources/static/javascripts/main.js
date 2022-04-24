let btnApply = document.getElementById('btnApply');
let dateDeb = document.getElementById('dateDeb');
let dateFin = document.getElementById('dateFin');
let btnReset = document.getElementById('btnReset');
let genreFiltre = document.querySelectorAll('#genreFiltre input[type=radio]');
let baseUrl = '/api/livres_filtre';

btnApply.addEventListener('click', e => {
    loadData();

})
btnReset.addEventListener('click', e => {
    genreFiltre.forEach(e => {
        e.checked = false
    });

})
function loadData(page = 1) {
    let date_deb = dateDeb.value;
    let date_fin = dateFin.value;
    let cat = '';
    genreFiltre.forEach(e => {
        if (e.checked)
            cat = e.value;
    });

    let url = baseUrl;
    url += '?d';
    if (date_deb)
        url += '&date_deb=' + date_deb;
    if (date_fin)
        url += '&date_fin=' + date_fin;
    if (cat)
        url += '&cat=' + cat;
    if (page)
        url += '&p=' + page;

    let xhr = new XMLHttpRequest();
    xhr.open('POST', url, true);
    xhr.onload = function () {
        if (this.status == 200) {
            let mdata = JSON.parse(this.responseText);
            let res = '';
            mdata.data.forEach(livre => {
                let content = getCard(livre);
                res += content;
            });
            let p = page ? page : 1;
            document.querySelector('.contentlivre').innerHTML = res;
            updatePagebar(p, mdata.page_limit, mdata.total_result);
        }
    }
    xhr.send();

}
function pagination() {
    let ul = document.querySelectorAll(".pagination > li");
    ul.forEach(li => {
        li.addEventListener('click', e => {
            e.preventDefault();
            let page = li.getAttribute('value');
            loadData(page);
        })
    });

}

function updatePagebar(page, page_limit, total_result) {

    let pb = document.querySelector('#paginationbar');
    let res = '';
    res += prevPage(page);
    let lastPage = Math.ceil((total_result / page_limit));
    for (var i = 1; i <= lastPage; i++) {
        let content = `<li class="page-item ${i == page ? 'active' : ''} " value="${i}">
								<a class="page-link">${i}</a>
							</li>`;
        res += content;
    }
    res += nextPage(page, page_limit, total_result);
    pb.innerHTML = res;
    //add listenner
    pagination()
}

function getCard(livre) {
    return `<div class="col-lg-3 col-md-4 col-sm-6 ps-2 pe-2 pb-2">
                        <div class="card">
                                <img src="${livre.image}" height="200px">
                                    <div class="card-body">
                                        <h5 class="card-title" style="white-space:nowrap;text-overflow:ellipsis;overflow:hidden;">${livre.titre}</h5>
                                        <p class="card-text">${livre.isbn + ""}</p>
                                        <p class="card-text">${formatDate(livre.date_de_parution)}</p>
                                        <div class="row">
                                            <div class="col">
                                            ${livre.nombre_pages} page
                                            </div>
                                            <div class="col">
                                            ${livre.note}
                                            </div>
                                        </div>
                                        <a href="/details/${livre.id}" class="mt-2 btn-outline-primary btn">
                                            Détails..
                                        </a>
                                    </div>
                                </div>
				    </div >`;
}

function formatDate(date) {
    var d = new Date(date),
        month = '' + (d.getMonth() + 1),
        day = '' + d.getDate(),
        year = d.getFullYear();

    if (month.length < 2)
        month = '0' + month;
    if (day.length < 2)
        day = '0' + day;

    return [day, month, year].join('-');
}
function prevPage(page) {
    return `<li class="page-item ${page <= 1 ? 'disabled' : ''}" value="${page > 1 ? (page - 1) : 1}">
						<a class="page-link" aria-label="Previous">
							<span aria-hidden="true">&laquo;</span>
							<span class="sr-only">Previous</span>
						</a>
					</li>`
}
function nextPage(page, page_limit, total_result) {
    let lastPage = (Math.ceil((total_result / page_limit)));
    return `<li class="page-item ${page >= lastPage ? 'disabled' : ''}" value="${(page + 1) <= lastPage ? (page + 1) : lastPage}">
						<a class="page-link" aria-label="Next">
							<span aria-hidden="true">&raquo;</span>
							<span class="sr-only">Next</span>
						</a>
					</li>`;
}




