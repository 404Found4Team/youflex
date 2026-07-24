// 상단 공통 네비게이션 바를 현재 탭(공지사항/Q&A)에 맞게 강조
// (body의 data-nav-current는 "notice"로 고정돼 있어서 그대로 두면 항상 공지사항만 강조됨)
function syncTopNavActive(target) {
  document.querySelectorAll('.category-nav a[data-nav]').forEach((a) => {
    a.classList.toggle('active', a.dataset.nav === target);
  });
}

function applyNoticeHashState() {
  const isQna = location.hash === '#qna';
  const targetBtn = document.querySelector(
    `.notice-tabs button[data-tab-target="${isQna ? 'qna' : 'notice'}"]`
  );
  targetBtn && targetBtn.click();
  syncTopNavActive(isQna ? 'qna' : 'notice');
}

// 공지사항/Q&A 목록을 10개 단위로 나눠 보여주는 클라이언트 페이지네이션.
// 서버는 목록 전체를 이미 다 렌더링해서 내려주므로(별도 페이징 API 없음), 여기서는
// tbody의 <tr>들을 그룹으로 나눠 display만 토글하는 방식으로 처리한다.
// searchInputId/matchRow를 넘기면 검색어 입력에 따라 매칭되는 행만 걸러서 페이지네이션한다
// (서버 재조회 없이 이미 렌더링된 행 중에서 필터링만 하는 방식).
function initTablePagination(tbodyId, paginationId, pageSize, searchInputId, matchRow) {
  const tbody = document.getElementById(tbodyId);
  const paginationEl = document.getElementById(paginationId);
  if (!tbody || !paginationEl) return;

  // "등록된 ~이 없습니다" 안내 행(colspan)은 검색/페이지 계산에서 제외 (검색어 없을 때는 그대로 유지되는 문구)
  const allRows = Array.from(tbody.children).filter((tr) => !tr.querySelector('td[colspan]'));
  const columnCount = tbody.closest('table').querySelectorAll('thead th').length;

  let rows = allRows;
  let currentPage = 1;
  let currentKeyword = '';
  let noResultRow = null;

  // 검색 결과가 0건일 때만 보여주는 안내 행 ("등록된 ~이 없습니다"와는 별개)
  function ensureNoResultRow() {
    if (noResultRow) return noResultRow;
    noResultRow = document.createElement('tr');
    const td = document.createElement('td');
    td.colSpan = columnCount;
    td.style.textAlign = 'center';
    td.style.color = 'var(--text-2)';
    td.textContent = '검색 결과가 없습니다.';
    noResultRow.appendChild(td);
    tbody.appendChild(noResultRow);
    return noResultRow;
  }

  function createPageBtn(label, extraClass, onClick) {
    const btn = document.createElement('button');
    btn.type = 'button';
    btn.className = extraClass ? `page-btn ${extraClass}` : 'page-btn';
    btn.textContent = label;
    btn.addEventListener('click', onClick);
    return btn;
  }

  function render() {
    const totalPages = Math.max(1, Math.ceil(rows.length / pageSize));
    if (currentPage > totalPages) currentPage = totalPages;

    allRows.forEach((tr) => { tr.style.display = 'none'; });
    rows.forEach((tr, i) => {
      const page = Math.floor(i / pageSize) + 1;
      tr.style.display = page === currentPage ? '' : 'none';
    });

    // 검색어가 있는데 매칭되는 행이 하나도 없을 때만 "검색 결과가 없습니다" 노출
    if (currentKeyword !== '' && rows.length === 0) {
      ensureNoResultRow().style.display = '';
    } else if (noResultRow) {
      noResultRow.style.display = 'none';
    }

    paginationEl.innerHTML = '';
    paginationEl.appendChild(createPageBtn('‹', 'prev', () => currentPage > 1 && goTo(currentPage - 1)));
    for (let i = 1; i <= totalPages; i++) {
      paginationEl.appendChild(createPageBtn(i, i === currentPage ? 'active' : '', () => goTo(i)));
    }
    paginationEl.appendChild(createPageBtn('›', 'next', () => currentPage < totalPages && goTo(currentPage + 1)));
  }

  function goTo(page) {
    currentPage = page;
    render();
  }

  function applyFilter(keyword) {
    currentKeyword = keyword.trim().toLowerCase();
    rows = currentKeyword === '' ? allRows : allRows.filter((tr) => matchRow(tr, currentKeyword));
    currentPage = 1;
    render();
  }

  render();

  const searchInput = searchInputId && document.getElementById(searchInputId);
  if (searchInput && matchRow) {
    searchInput.addEventListener('input', () => applyFilter(searchInput.value));
  }
}

// 공지사항 검색 매칭: 제목만 대상 (작성자는 항상 "관리자" 고정이라 검색 의미 없음)
function matchNoticeRow(tr, keywordLower) {
  const titleText = tr.children[1] ? tr.children[1].textContent.toLowerCase() : '';
  return titleText.includes(keywordLower);
}

// Q&A 검색 매칭: 제목 OR 작성자 (리뷰 게시글 검색과 동일한 방식)
function matchQnaRow(tr, keywordLower) {
  const titleText = tr.children[1] ? tr.children[1].textContent.toLowerCase() : '';
  const authorText = tr.children[2] ? tr.children[2].textContent.toLowerCase() : '';
  return titleText.includes(keywordLower) || authorText.includes(keywordLower);
}

document.addEventListener('DOMContentLoaded', () => {
  applyNoticeHashState();

  // 페이지 안의 "공지사항"/"Q&A" 탭을 직접 눌렀을 때도 상단 네비게이션 강조를 같이 맞춰줌
  document.querySelectorAll('.notice-tabs button[data-tab-target]').forEach((btn) => {
    btn.addEventListener('click', () => syncTopNavActive(btn.dataset.tabTarget));
  });

  initTablePagination('noticeListBody', 'noticePagination', 10, 'noticeSearchKeyword', matchNoticeRow);
  initTablePagination('qnaListBody', 'qnaPagination', 10, 'qnaSearchKeyword', matchQnaRow);
});
// 05_notice.html 안에서 05_notice.html#qna 로 이동하는 것처럼 같은 문서 안 해시만 바뀌는 경우
// DOMContentLoaded가 다시 발생하지 않으므로 hashchange도 같이 들어야 함
window.addEventListener('hashchange', applyNoticeHashState);
