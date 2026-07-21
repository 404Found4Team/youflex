// ===== [1] 장르 선택 모달 및 최대 3개 제한 로직 =====
const MAX_GENRE_SELECT = 3;
const tasteBtn = document.getElementById('tasteBtn');               // ❤️ 관련 장르 버튼
const genreModal = document.getElementById('genreModalBackdrop');   // 모달창 배경
const genreGrid = document.getElementById('genreGrid');             // 장르 칩 그리드
const genreSkipBtn = document.getElementById('genreSkipBtn');       // 취소 버튼
const genreDoneBtn = document.getElementById('genreDoneBtn');       // 저장 버튼

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
        }

        genreModal.classList.remove('open');
        alert(`선택하신 ${selectedGenres.length}개의 장르가 임시 매핑되었습니다.`);
    });
}


// ===== [2] 별점 클릭 처리 (0.5점 단위 + 폭죽 이펙트) ===============
const starBoxes = document.querySelectorAll(".star-box");
const ratingInput = document.querySelector("input[name='reviewRating']");
const allHalves = document.querySelectorAll(".star-box .half");

function updateStars(score) {
    allHalves.forEach((half) => {
        const parentBox = half.closest(".star-box");
        const boxValue = parseFloat(parentBox.getAttribute("data-value"));

        if (half.classList.contains("left")) {
            if (boxValue - 0.5 <= score) {
                half.classList.add("active");
            } else {
                half.classList.remove("active");
            }
        }
        else if (half.classList.contains("right")) {
            if (boxValue <= score) {
                half.classList.add("active");
            } else {
                half.classList.remove("active");
            }
        }
    });
}

if (ratingInput) {
    updateStars(parseFloat(ratingInput.value || 0));
}

allHalves.forEach((half) => {
    const parentBox = half.closest(".star-box");
    const boxValue = parseFloat(parentBox.getAttribute("data-value"));
    const currentHalfScore = half.classList.contains("left") ? boxValue - 0.5 : boxValue;

    half.addEventListener("click", () => {
        ratingInput.value = currentHalfScore;
        updateStars(currentHalfScore);
    });

    half.addEventListener("mouseover", () => {
        updateStars(currentHalfScore);
        createStarFireworks(half);
    });

    half.addEventListener("mouseout", () => {
        const confirmedScore = parseFloat(ratingInput.value || 0);
        updateStars(confirmedScore);
    });
});

function createStarFireworks(element) {
    const rect = element.getBoundingClientRect();
    const centerX = rect.left + rect.width / 2;
    const centerY = rect.top + rect.height / 2;

    const particleCount = 8;
    const colors = ['#f39c12', '#ffe066', '#ff6b6b', '#BAE6FD', '#4ade80'];

    for (let i = 0;i < particleCount;i++) {
        const particle = document.createElement('div');
        particle.className = 'star-particle';

        const randomColor = colors[Math.floor(Math.random() * colors.length)];
        const randomSize = Math.random() * 4 + 4;
        particle.style.backgroundColor = randomColor;
        particle.style.width = `${randomSize}px`;
        particle.style.height = `${randomSize}px`;

        particle.style.left = `${centerX - randomSize / 2}px`;
        particle.style.top = `${centerY - randomSize / 2}px`;

        const angle = (i * (360 / particleCount) + Math.random() * 20) * (Math.PI / 180);
        const distance = Math.random() * 35 + 25;
        const tx = Math.cos(angle) * distance;
        const ty = Math.sin(angle) * distance;

        particle.style.setProperty('--tx', `${tx}px`);
        particle.style.setProperty('--ty', `${ty}px`);

        document.body.appendChild(particle);

        particle.addEventListener('animationend', () => {
            particle.remove();
        });
    }
}


// ===== [3] 사진 업로드 미리보기 ===== 
const imgInput = document.getElementById('imgInput');
const imgPreview = document.getElementById('imgPreview');

if (imgInput && imgPreview) {
    imgInput.addEventListener('change', function() {
        const file = imgInput.files[0];
        if (!file) {
            imgPreview.style.display = 'none';
            return;
        }

        const reader = new FileReader();
        reader.onload = function(e) {
            imgPreview.src = e.target.result;
            imgPreview.style.display = 'block';
        };
        reader.readAsDataURL(file);
    });
}


// ===== [4] 서버 DB 기반 임시저장 관리 기능 =====

document.addEventListener('DOMContentLoaded', () => {
    // 페이지 로드 시 임시저장 목록 조회
    fetchDraftList();

	/* ====== 게시글 임시저장 클릭 이벤트 ====== */
	const draftBtn = document.getElementById('draftBtn');
	if (draftBtn) {
	    draftBtn.addEventListener('click', () => {
	        // [수정 1] 클릭하는 '시점'의 최신 DOM 요소들을 새로 가져옵니다.
	        const draftIdInput = document.getElementById('review_draft_id');
	        const titleInput = document.getElementById('review_title');
	        const contentInput = document.getElementById('review_content');
	        const reviewRelatedInput = document.getElementById('review_related');

	        // [수정 2] input태그의 현재 value를 읽어옵니다.
	        // - 첫 번째 저장 : value가 "0" 또는 "" 이므로 0 (-> 신규 INSERT 진행)
	        // - 두 번째 저장 : 첫 번째 저장 후 세팅된 ID값(예: 15)을 읽어옴 (-> 기존 글 UPDATE 진행)
	        const rawDraftId = draftIdInput ? parseInt(draftIdInput.value, 10) : 0;
	        
	        // ★ [수정 3 - 2번 방식 적용] isNaN() 없이 자기비교(x === x)로 NaN 판별
	        // rawDraftId가 NaN이면 (rawDraftId === rawDraftId) 결과가 false가 됩니다.
	        const draftId = (rawDraftId === rawDraftId) ? rawDraftId : 0;

	        const title = titleInput ? titleInput.value.trim() : '';
	        const content = contentInput ? contentInput.value.trim() : '';
	        const reviewRelated = reviewRelatedInput ? reviewRelatedInput.value.trim() : '';

	        if (!title && !content) {
	            alert('제목이나 내용 중 하나 이상 입력 후 임시저장해 주세요.');
	            return;
	        }

	        // DTO 객체 생성 (두 번째 클릭부터는 draftId > 0 값이 들어가 UPDATE 처리됨)
	        const draftDTO = {
	            reviewDraftId: draftId, 
	            reviewDraftTitle: title,
	            reviewDraftContent: content,
	            reviewDraftRelated: reviewRelated
	        };

	        fetch('/review/draft/save', {
	            method: 'POST',
	            headers: {
	                'Content-Type': 'application/json'
	            },
	            body: JSON.stringify(draftDTO)
	        })
	        .then(async response => {
	            if (response.status === 401) {
	                alert('로그인이 필요한 기능입니다.');
	                return;
	            }

	            const resText = await response.text();

	            if (response.ok) {
	                if (resText === 'MAX_LIMIT_EXCEEDED') {
	                    alert('임시저장은 최대 5개까지만 가능합니다.\n기존 임시저장 글을 삭제 후 다시 시도해주세요.');
	                    return;
	                }

	                // 서버에서 응답받은 새/기존 draftId 문자열을 숫자(정수)로 파싱합니다.
	                const savedDraftId = parseInt(resText, 10);
	                
	                // ★ [수정 4 - 2번 방식 적용 핵심★] 
	                // 1) (savedDraftId === savedDraftId) : isNaN()을 쓰지 않고 NaN이 아닌지 검사
	                // 2) (savedDraftId > 0) : 유효한 DB PK 번호인지 검사
	                if (savedDraftId === savedDraftId && savedDraftId > 0) {
	                    if (draftIdInput) {
	                        // <input id="review_draft_id">의 value를 생성된 ID로 즉시 덮어씁니다.
	                        // 이 처리가 되어야 다음 임시저장 클릭 시 UPDATE가 정상 수행됩니다!
	                        draftIdInput.value = savedDraftId;
	                    }
	                }

	                const now = new Date();
	                const hh = String(now.getHours()).padStart(2, '0');
	                const mm = String(now.getMinutes()).padStart(2, '0');
	                const timeStr = `${hh}:${mm}`;

	                const hint = document.getElementById('autosaveHint');
	                if (hint) {
	                    hint.textContent = `임시저장됨 (${timeStr})`;
	                    hint.classList.add('saved');
	                }

	                alert(`임시저장 되었습니다. (${timeStr})`);
	                
	                // 우측 사이드바/모달 등의 임시저장 목록 갱신
	                fetchDraftList();
	            } else {
	                alert('임시저장에 실패했습니다.');
	            }
	        })
	        .catch(error => {
	            console.error('Draft save error:', error);
	            alert('서버 통신 중 오류가 발생했습니다.');
	        });
	    });
	}
    
});

// 1. 임시저장 목록 서버에서 조회해 오기
function fetchDraftList() {
    fetch('/review/draft/list')
        .then(response => {
            if (!response.ok) throw new Error('목록 조회 실패');
            return response.json();
        })
        .then(drafts => {
            renderDraftList(drafts);
        })
        .catch(error => {
            console.error('Draft list fetch error:', error);
        });
}

// 2. 화면에 임시저장 목록 UI 렌더링하기
function renderDraftList(drafts) {
    const listEl = document.getElementById('draftList');
    const emptyMsg = document.getElementById('draftEmptyMsg');

    if (!listEl) return;

    listEl.innerHTML = '';

    if (!drafts || drafts.length === 0) {
        if (emptyMsg) emptyMsg.style.display = 'block';
        return;
    }
    if (emptyMsg) emptyMsg.style.display = 'none';

    drafts.forEach(draft => {
        const title = draft.reviewDraftTitle || '(제목 없음)';

        let timeStr = '';
        if (draft.reviewDraftSavedAt) {
            const dateObj = new Date(draft.reviewDraftSavedAt);
            const hh = String(dateObj.getHours()).padStart(2, '0');
            const mm = String(dateObj.getMinutes()).padStart(2, '0');
            timeStr = `${hh}:${mm}`;
        }

        const item = document.createElement('div');
        item.className = 'draft-item';

        const infoEl = document.createElement('div');
        infoEl.className = 'draft-info';

        const titleEl = document.createElement('div');
        titleEl.className = 'draft-title';
        titleEl.textContent = title;

        const timeEl = document.createElement('div');
        timeEl.className = 'draft-time';
        timeEl.textContent = timeStr;

        infoEl.appendChild(titleEl);
        infoEl.appendChild(timeEl);

        const actionsEl = document.createElement('div');
        actionsEl.className = 'draft-actions';

        const loadBtn = document.createElement('button');
        loadBtn.type = 'button';
        loadBtn.className = 'btn btn-sm btn-load';
        loadBtn.textContent = '불러오기';
        loadBtn.addEventListener('click', () => loadDraft(draft.reviewDraftId));

        const deleteBtn = document.createElement('button');
        deleteBtn.type = 'button';
        deleteBtn.className = 'btn btn-sm btn-delete';
        deleteBtn.textContent = '삭제';
        deleteBtn.addEventListener('click', () => deleteDraft(draft.reviewDraftId));

        actionsEl.appendChild(loadBtn);
        actionsEl.appendChild(deleteBtn);

        item.appendChild(infoEl);
        item.appendChild(actionsEl);

        listEl.appendChild(item);
    });
}

// 3. 불러오기 버튼 처리
function loadDraft(draftId) {
    fetch(`/review/draft/detail/${draftId}`)
        .then(response => {
            if (!response.ok) throw new Error('임시저장 조회 실패');
            return response.json();
        })
        .then(draft => {
            if (!draft) return;

            const draftIdInput = document.getElementById('review_draft_id');
            const titleInput = document.getElementById('review_title');
            const contentInput = document.getElementById('review_content');
            const relatedInput = document.getElementById('review_related');

            if (draftIdInput) {
                draftIdInput.value = draft.reviewDraftId;
            }

            if (titleInput) titleInput.value = draft.reviewDraftTitle || '';
            if (contentInput) contentInput.value = draft.reviewDraftContent || '';
            if (relatedInput) relatedInput.value = draft.reviewDraftRelated || '';

            alert('임시저장된 내용을 불러왔습니다.');
        })
        .catch(error => {
            console.error('Draft load error:', error);
            alert('임시저장 불러오기에 실패했습니다.');
        });
}

// 4. 삭제 버튼 처리 ([수정 2] DELETE 메서드 및 URI 맞춤)
function deleteDraft(draftId) {
    if (!confirm('이 임시저장 글을 삭제하시겠습니까?')) return;

    fetch(`/review/draft/delete/${draftId}`, {
        method: 'DELETE'
    })
        .then(response => {
            if (response.ok) {
                alert('삭제되었습니다.');

                const draftIdInput = document.getElementById('review_draft_id');
                if (draftIdInput && draftIdInput.value == draftId) {
                    draftIdInput.value = '0';
                }

                fetchDraftList();
            } else {
                alert('삭제에 실패했습니다.');
            }
        })
        .catch(error => {
            console.error('Draft delete error:', error);
            alert('삭제 중 오류가 발생했습니다.');
        });
}