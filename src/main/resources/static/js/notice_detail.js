// 공지사항 삭제 처리 함수
// - 확인창(confirm)으로 재확인 후, DELETE API 호출
// - 삭제 성공 시 목록 페이지로 리다이렉트
function deleteNotice(id) {
  if (!confirm('정말 삭제하시겠습니까?')) return;
  fetch(`/api/notice/${id}`, { method: 'DELETE' })
    .then(res => { if (res.ok) location.href = '/notice'; });
}
