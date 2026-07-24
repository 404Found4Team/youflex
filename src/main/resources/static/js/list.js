document.getElementById('advToggleBtn').addEventListener('click', () => {
    document.getElementById('advPanel').classList.toggle('open');
});

// ===== 정렬 버튼: 클릭 시 sort 파라미터를 붙여 목록 재요청 =====
// [수정] 기존 'button'만 선택하던 것에서 'a' 태그 및 '.highlight' 요소도 선택되도록 확장
const sortButtons = document.querySelectorAll('.sort-group button, .sort-group a, .category-nav .highlight');

// ===== 페이지 로드 시 현재 URL의 sort 값에 맞춰 active 클래스 부여 =====
function applyActiveSortFromUrl() {
    const params = new URLSearchParams(window.location.search);
    const currentSort = params.get('sort'); // 없으면 null

    sortButtons.forEach(btn => {
        const btnSort = btn.dataset.sort || (btn.classList.contains('highlight') ? 'highlight' : null);
        if (btnSort && btnSort === currentSort) {
            btn.classList.add('active');
        } else {
            btn.classList.remove('active');
        }
    });
}
applyActiveSortFromUrl();

sortButtons.forEach(btn => {
    btn.addEventListener('click', (e) => {
        // [수정] a 태그일 경우 기본 페이지 이동 동작 방지
        if (btn.tagName === 'A' && (btn.dataset.sort || btn.classList.contains('highlight'))) {
            e.preventDefault();
        }

        // [수정] 클릭 시 기존 active 클래스를 제거하고 현재 클릭한 버튼에 active 추가
        sortButtons.forEach(b => b.classList.remove('active'));
        btn.classList.add('active');

        // [수정] data-sort 값이 있거나 highlight 클래스가 있으면 sort 파라미터로 처리
        const sortVal = btn.dataset.sort || (btn.classList.contains('highlight') ? 'highlight' : null);
        if (sortVal) {
            // [수정] 하이라이트는 특정 플랫폼이 아닌 "전체 게시글" 중 review_highlighted='Y'가 대상이므로,
            //        이전에 선택돼 있던 platform 필터가 남아있으면 결과가 그 플랫폼으로만 좁혀지는 버그가 있었음 -> 클릭 시 초기화
            const overrides = { sort: sortVal };
            if (sortVal === 'highlight') {
                overrides.platform = null;
                // [추가] 하이라이트 진입 시 highlightOnly on. 이후 최신순/좋아요순/조회수순을 눌러도
                //        overrides에 highlightOnly가 없으면 goToFilteredList가 URL의 값을 그대로 유지하므로
                //        하이라이트 필터가 계속 살아있음 (platform과 동일한 방식)
                overrides.highlightOnly = true;	// 추가: 하이라이트 진입 시 필터 on
            }
            goToFilteredList(overrides);
        }
    });
});

// ===== [1] 장르 선택 모달 및 최대 3개 제한 로직 =====
const MAX_GENRE_SELECT = 3;
const tasteBtn = document.getElementById('listTasteBtn');           // 상세검색 > 취향선택 버튼
const genreModal = document.getElementById('genreModalBackdrop');   // 모달창 배경
const genreGrid = document.getElementById('genreGrid');             // 장르 칩 그리드
const genreSkipBtn = document.getElementById('genreSkipBtn');       // 취소 버튼
const genreDoneBtn = document.getElementById('genreDoneBtn');       // 저장 버튼
let selectedListGenres = [];	// 파일 상단에 변수 선언 추가

// 1. 장르 선택 모달 열기
if (tasteBtn && genreModal) {
    tasteBtn.addEventListener('click', () => {
        genreModal.classList.add('open');
    });
}

// 2. 장르 선택 모달 닫기 (취소 클릭 시)
if (genreSkipBtn && genreModal) {
    genreSkipBtn.addEventListener('click', () => {
        genreModal.classList.remove('open');
    });
}

// 3. 장르 칩 클릭 이벤트 핸들러 (최대 3개 제한 핵심 로직)
if (genreGrid) {
    const genreChips = genreGrid.querySelectorAll('.genre-chip');

    genreChips.forEach((chip) => {
        chip.addEventListener('click', () => {
            const selectedCount = genreGrid.querySelectorAll('.genre-chip.selected').length;

            if (!chip.classList.contains('selected') && selectedCount >= MAX_GENRE_SELECT) {
                alert(`관심 장르는 최대 ${MAX_GENRE_SELECT}개까지만 선택할 수 있어요.`);
                return;
            }

            chip.classList.toggle('selected');
        });
    });
}

// 4. 저장 완료 버튼 클릭 시 처리
if (genreDoneBtn && genreModal) {
    genreDoneBtn.addEventListener('click', () => {
        const selectedChips = genreGrid.querySelectorAll('.genre-chip.selected');
        const selectedGenres = [];

        selectedChips.forEach(chip => {
            selectedGenres.push({
                id: chip.getAttribute('data-genre-id'),
                name: chip.querySelector('span').textContent.trim()
            });
        });

        const form = document.getElementById('reviewForm');
        if (form) {
            form.querySelectorAll('input[name="genreCategoryIds"]').forEach(el => el.remove());

            selectedGenres.forEach(genre => {
                const hiddenInput = document.createElement('input');
                hiddenInput.type = 'hidden';
                hiddenInput.name = 'genreCategoryIds';
                hiddenInput.value = genre.id;
                form.appendChild(hiddenInput);
            });
        } else {
            // 목록 페이지(상세검색)에는 reviewForm이 없으므로,이 else 분기에서 선택한 장르 id를 selectedListGenres 배열에 저장
            // 목록 페이지(상세검색) : 검색 조건 배열에 저장
            selectedListGenres = selectedGenres.map(g => g.id);
        }

        genreModal.classList.remove('open');
    });
}

// ===== "검색 적용" 버튼: 키워드 + 기간 + 장르를 모두 모아 검색 요청 =====
const applySearchBtn = document.getElementById('applySearchBtn');
if (applySearchBtn) {
    applySearchBtn.addEventListener('click', () => {
        const keyword = document.getElementById('searchKeyword').value.trim();
        const period = document.getElementById('searchPeriod').value;

        // [수정] genres -> genreCategoryIds : ReviewListSearchDTO 필드명과 일치시킴 (파라미터명 불일치로 서버 바인딩 안 되던 문제)
        goToFilteredList({ keyword, period, genreCategoryIds: selectedListGenres });
    });
}

const searchForm = document.getElementById('searchForm');
if (searchForm) {
    searchForm.addEventListener('submit', () => {
        const keyword = document.getElementById('searchKeyword').value.trim();
        const period = document.getElementById('searchPeriod') ? document.getElementById('searchPeriod').value : 'all';

        // [수정] genres -> genreCategoryIds : ReviewListSearchDTO 필드명과 일치시킴 (파라미터명 불일치로 서버 바인딩 안 되던 문제)
        goToFilteredList({ keyword, period, genreCategoryIds: selectedListGenres });
    });
}

// ===== 공통: 현재 정렬/검색 조건을 쿼리 파라미터로 만들어 목록 페이지 재요청 =====
function goToFilteredList(overrides = {}) {
    const params = new URLSearchParams(window.location.search);

    // 검색/정렬 조건이 바뀌면 페이지는 1페이지로 초기화
    params.set('page', '1');
    if (overrides.page !== undefined) params.set('page', String(overrides.page));

    if (overrides.sort !== undefined) params.set('sort', overrides.sort);
    if (overrides.platform !== undefined) {
        if (overrides.platform) params.set('platform', overrides.platform);
        else params.delete('platform');
    }
    // [추가] highlightOnly는 overrides에 없으면 아예 건드리지 않아서(=URL에 남아있던 값 그대로 유지)
    //        최신순/좋아요순/조회수순 클릭 시에도 하이라이트 필터가 계속 유지되게 함
    if (overrides.highlightOnly !== undefined) {
        if (overrides.highlightOnly) params.set('highlightOnly', 'true');
        else params.delete('highlightOnly');
    }
    if (overrides.keyword !== undefined) {
        if (overrides.keyword) params.set('keyword', overrides.keyword);
        else params.delete('keyword');
    }
    if (overrides.period !== undefined) {
        if (overrides.period && overrides.period !== 'all') params.set('period', overrides.period);
        else params.delete('period');
    }

    // [수정] overrides.genres -> overrides.genreCategoryIds 로 변경
    // [수정] 콤마로 합친 문자열 하나(set) 대신, 같은 이름의 파라미터를 여러 개(append) 붙이는 방식으로 변경
    //        -> Spring이 List<Integer> genreCategoryIds 로 안전하게 바인딩하도록 하기 위함
    //        (기존 방식인 params.set('genres', arr.join(',')) 는 파라미터명도 틀렸고,
    //         콤마 문자열 하나로는 List<Integer> 바인딩이 보장되지 않음)
    if (overrides.genreCategoryIds !== undefined) {
        params.delete('genreCategoryIds'); // 기존 값 초기화 후 다시 채움
        if (overrides.genreCategoryIds.length > 0) {
            overrides.genreCategoryIds.forEach(id => params.append('genreCategoryIds', id));
        }
    }
	
	window.location.href = `${window.location.pathname}?${params.toString()}`;
}

// ===== 페이지네이션 버튼 클릭 -> 페이지 이동 =====
// [수정] 기존에는 이 리스너가 goToFilteredList() 함수 안(그것도 navigate 코드보다 뒤)에 등록되어 있어서,
//        goToFilteredList가 한 번도 호출되지 않은 최초 페이지 로드 상태에서는 페이지네이션 버튼에
//        클릭 이벤트가 전혀 걸려있지 않아 눌러도 아무 반응이 없던 버그가 있었음 -> 최상위로 이동해 페이지 로드 시 한 번만 등록
document.addEventListener('click', (event) => {
    const button = event.target.closest('button.page-btn');
    if (!button || button.disabled) return;
    if (!button.closest('.pagination')) return;

    const pageText = button.textContent.trim();
    const totalPages = Number(button.closest('.pagination').dataset.totalPages || '0');
    const blockStart = Number(button.closest('.pagination').dataset.blockStart || '1');
    const blockEnd = Number(button.closest('.pagination').dataset.blockEnd || '1');

    // [수정] 관리자 회원관리 탭과 동일하게 1페이지씩 이동하던 ‹/› 대신 10페이지 블록 단위로 이동하는 «/»로 교체
    // 삭제: if (pageText === '‹') { if (currentPage > 1) goToFilteredList({ page: currentPage - 1 }); return; }
    // 삭제: if (pageText === '›') { if (currentPage < totalPages) goToFilteredList({ page: currentPage + 1 }); return; }
    if (pageText === '«') {
        if (blockStart > 1) goToFilteredList({ page: blockStart - 1 });
        return;
    }

    if (pageText === '»') {
        if (blockEnd < totalPages) goToFilteredList({ page: blockEnd + 1 });
        return;
    }

    const pageNumber = Number(pageText);
    if (!Number.isNaN(pageNumber) && pageNumber <= totalPages) {
        goToFilteredList({ page: pageNumber });
    }
});