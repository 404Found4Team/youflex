document.getElementById('noticeSaveBtn').addEventListener('click', () => {
  const title = document.getElementById('notice_title').value.trim();
  const content = document.getElementById('notice_content').value.trim();
  if (!title) {
    alert('제목을 입력해주세요.');
    return;
  }
  if (!content) {
    alert('내용을 입력해주세요.');
    return;
  }

  const noticeId = document.getElementById('noticeUpdateForm').dataset.noticeId;

  fetch(`/api/notice/${noticeId}`, {
    method: 'PUT',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ noticeTitle: title, noticeContent: content })
  }).then(res => {
    if (res.ok) {
      location.href = `/notice/${noticeId}`;
    } else if (res.status === 403) {
      alert('관리자만 수정할 수 있습니다.');
    } else if (res.status === 404) {
      alert('존재하지 않는 공지사항입니다.');
    } else {
      alert('수정에 실패했습니다.');
    }
  });
});
