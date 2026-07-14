package com.youflex.service;

import java.util.List;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import com.youflex.dto.NoticeDTO;
import com.youflex.mapper.NoticeMapper;
import com.youflex.exception.NoticeNotFoundException;

@Service
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService {

    private final NoticeMapper noticeMapper;

    @Override
    public List<NoticeDTO> getNoticeList() {
        return noticeMapper.selectNoticeList();
    }

    @Override
    public NoticeDTO getNoticeDetail(int noticeId) {
        NoticeDTO notice = noticeMapper.selectNoticeById(noticeId);
        if (notice == null) {
            throw new NoticeNotFoundException("존재하지 않는 공지사항입니다. noticeId=" + noticeId);
        }
        // 조회수 증가
        noticeMapper.increaseNoticeHit(noticeId);
        notice.setNoticeHit(notice.getNoticeHit() + 1);
        return notice;
    }

    @Override
    public void createNotice(NoticeDTO noticeDTO) {
        noticeMapper.insertNotice(noticeDTO);
    }

    @Override
    public void updateNotice(NoticeDTO noticeDTO) {
        NoticeDTO existing = noticeMapper.selectNoticeById(noticeDTO.getNoticeId());
        if (existing == null) {
            throw new NoticeNotFoundException("존재하지 않는 공지사항입니다. noticeId=" + noticeDTO.getNoticeId());
        }
        noticeMapper.updateNotice(noticeDTO);
    }

    @Override
    public void deleteNotice(int noticeId) {
        NoticeDTO existing = noticeMapper.selectNoticeById(noticeId);
        if (existing == null) {
            throw new NoticeNotFoundException("존재하지 않는 공지사항입니다. noticeId=" + noticeId);
        }
        noticeMapper.deleteNotice(noticeId);
    }
}
