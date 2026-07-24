package com.youflex.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.youflex.dto.ReviewDTO;
import com.youflex.dto.ReviewListSearchDTO;
import com.youflex.service.GenreCategoryService;
import com.youflex.service.ReviewListService;
import com.youflex.service.notice.NoticeService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ReviewListController {

	private final ReviewListService reviewListService;
	private final GenreCategoryService genreCategoryService; // [추가] 취향 선택 모달용 장르 목록 조회
	private final NoticeService noticeService;

	/**
	 * 게시글 목록 페이지 - /review/list - /review/list?page=2 -
	 * /review/list?keyword=검색어&sort=latest&period=all
	 */
	@GetMapping("/review/list")
	public String list(ReviewListSearchDTO searchDTO, Model model) {

		// [수정] 전체 건수/총 페이지 수를 findList보다 먼저 계산 - page가 totalPages를 넘는 경우
		//        아래에서 findList 호출 전에 보정하기 위함
		int totalCount = reviewListService.countList(searchDTO);
		int pageSize = reviewListService.getPageSize();
		int totalPages = (int) Math.ceil((double) totalCount / pageSize);

		// [추가] page가 totalPages보다 크면(게시글이 줄었거나 오래된 링크로 들어온 경우) totalPages로 보정.
		//        이 보정이 없으면 아래 블록 페이지네이션 계산에서 blockStart > blockEnd가 되어
		//        #numbers.sequence()가 역순으로 뒤집혀 버튼이 잘못 렌더링됨
		if (totalPages > 0 && searchDTO.getPage() > totalPages) {
			searchDTO.setPage(totalPages);
		}

		// 게시글 목록 조회 (서비스에서 offset/size 자동 세팅, 위에서 보정된 page 기준)
		List<ReviewDTO> postList = reviewListService.findList(searchDTO);

		model.addAttribute("postList", postList);
		model.addAttribute("currentPage", searchDTO.getPage());
		model.addAttribute("totalPages", totalPages);
		model.addAttribute("keyword", searchDTO.getKeyword());
		model.addAttribute("sort", searchDTO.getSort() == null ? "latest" : searchDTO.getSort());
		model.addAttribute("platform", searchDTO.getPlatform() == null ? "all" : searchDTO.getPlatform());
		// [추가] 하이라이트만 보기 여부 - view에서 레이아웃(카드형/행형) 분기 기준으로 사용
		model.addAttribute("highlightOnly", searchDTO.isHighlightOnly());

		// [추가] 관리자 회원관리 탭(admin.js의 MEMBER_PAGE_BLOCK_SIZE)과 동일한 10페이지 블록 페이지네이션 계산
		int blockSize = 10;
		int currentBlock = (int) Math.ceil((double) searchDTO.getPage() / blockSize);
		int blockStart = (currentBlock - 1) * blockSize + 1;
		int blockEnd = Math.min(blockStart + blockSize - 1, totalPages);
		model.addAttribute("blockStart", blockStart);
		model.addAttribute("blockEnd", blockEnd);

		// [추가] 상단 고정 공지사항 - 최신 1건만 노출
		model.addAttribute("noticeList", noticeService.getRecentNoticeList(1));

		// [추가] 상세검색 > 취향선택 모달에 뿌릴 장르 목록
		model.addAttribute("genres", genreCategoryService.getAllGenres());

		return "review/list";
	}
}
