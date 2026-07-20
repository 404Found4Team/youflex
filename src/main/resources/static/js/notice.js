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
function initTablePagination(tbodyId, paginationId, pageSize) {
  const tbody = document.getElementById(tbodyId);
  const paginationEl = document.getElementById(paginationId);
  if (!tbody || !paginationEl) return;

  // "등록된 ~이 없습니다" 안내 행(colspan)은 페이지 계산에서 제외
  const rows = Array.from(tbody.children).filter((tr) => !tr.querySelector('td[colspan]'));
  // 데이터가 없거나 10개 이하여도 항상 최소 1페이지는 보여줌
  const totalPages = Math.max(1, Math.ceil(rows.length / pageSize));

  let currentPage = 1;

  function createPageBtn(label, extraClass, onClick) {
    const btn = document.createElement('button');
    btn.type = 'button';
    btn.className = extraClass ? `page-btn ${extraClass}` : 'page-btn';
    btn.textContent = label;
    btn.addEventListener('click', onClick);
    return btn;
  }

  function render() {
    rows.forEach((tr, i) => {
      const page = Math.floor(i / pageSize) + 1;
      tr.style.display = page === currentPage ? '' : 'none';
    });

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

  render();
}

document.addEventListener('DOMContentLoaded', () => {
  applyNoticeHashState();

  // 페이지 안의 "공지사항"/"Q&A" 탭을 직접 눌렀을 때도 상단 네비게이션 강조를 같이 맞춰줌
  document.querySelectorAll('.notice-tabs button[data-tab-target]').forEach((btn) => {
    btn.addEventListener('click', () => syncTopNavActive(btn.dataset.tabTarget));
  });

  initTablePagination('noticeListBody', 'noticePagination', 10);
  initTablePagination('qnaListBody', 'qnaPagination', 10);
});
// 05_notice.html 안에서 05_notice.html#qna 로 이동하는 것처럼 같은 문서 안 해시만 바뀌는 경우
// DOMContentLoaded가 다시 발생하지 않으므로 hashchange도 같이 들어야 함
window.addEventListener('hashchange', applyNoticeHashState);
