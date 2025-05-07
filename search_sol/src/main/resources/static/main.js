let currentPage = 0;
const pageSize = 10;

function search(page = 0) {
    const keyword = document.getElementById("keyword").value;
    if (!keyword) return alert("검색어를 입력하세요");

    fetch(`/api/search?keyword=${encodeURIComponent(keyword)}&page=${page}&size=${pageSize}`)
        .then(response => response.json())
        .then(data => {
            currentPage = data.pageNumber;
            renderResults(data.content);
            renderPageInfo(data);
        });
}

function renderResults(items) {
    const container = document.getElementById("results");
    container.innerHTML = "";

    if (items.length === 0) {
        container.innerHTML = "<p>결과가 없습니다.</p>";
        return;
    }

    items.forEach(item => {
        const div = document.createElement("div");
        div.className = "result-item";
        div.innerHTML = `
                    <div class="entry">${item.entry}</div>
                    <div class="meta">(${item.type}, ${item.pos})</div>
                    <div class="definition">${item.definition}</div>
                `;
        container.appendChild(div);
    });
}

function renderPageInfo(data) {
    const info = document.getElementById("page-info");
    info.textContent = `페이지 ${data.pageNumber + 1} / ${data.totalPages}`;

    document.querySelector('.pagination button[onclick="prevPage()"]').disabled = data.first;
    document.querySelector('.pagination button[onclick="nextPage()"]').disabled = data.last;
}

function prevPage() {
    if (currentPage > 0) {
        search(currentPage - 1);
    }
}

function nextPage() {
    search(currentPage + 1);
}