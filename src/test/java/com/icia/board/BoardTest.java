package com.icia.board;

import com.icia.board.common.PagingConst;
import com.icia.board.dto.*;
import com.icia.board.entity.BoardEntity;
import com.icia.board.entity.CommentEntity;
import com.icia.board.repository.BoardRepository;
import com.icia.board.repository.CommentRepository;
import com.icia.board.service.BoardService;
import com.icia.board.service.CommentService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;

import javax.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class BoardTest {

    @Autowired
    private BoardService bs;

    @Autowired
    private BoardRepository br;

    @Autowired
    private CommentService cs;

    @Autowired
    private CommentRepository cr;

    @Test
    @Transactional
    @Rollback
    @DisplayName("글작성 테스트")
    public void boardSaveTest() {
        bs.save(new BoardSaveDTO("테스트작성자", "테스트비밀번호", "테스트제목", "테스트내용"));
    }

    @Test
    @DisplayName("글작성 30개")
    public void boardSaveTest30() {
//        for (int i=1; i<=30; i++) {
//            bs.save(new BoardSaveDTO("작성자"+i,"비밀번호"+i,"제목"+i,"내용"+i));
//        }
        // IntStream.range(1, 30) : 1부터 29
        // IntStream.rangeClosed(1, 30) : 1부터 30
        IntStream.rangeClosed(1, 30).forEach(i -> {
            bs.save(new BoardSaveDTO("작성자"+i,"비밀번호"+i,"제목"+i,"내용"+i));
        });
    }

    @Test
    @Transactional
    @Rollback
    @DisplayName("목록조회 테스트")
    public void boardListTest() {
        for (int i=1; i<=5; i++) {
            bs.save(new BoardSaveDTO("조회용작성자"+i,"조회용비밀번호"+i,"조회용제목"+i,"조회용내용"+i));
        }
        assertThat(bs.findAll().size()).isEqualTo(5);
    }

    @Test
    @Transactional
    @Rollback
    @DisplayName("상세조회 테스트")
    public void boardDetailTest() {
        assertThat(bs.findById(bs.save(new BoardSaveDTO("상세조회용작성자", "상세조회용비밀번호", "상세조회용제목", "상세조회용내용")))).isNotNull();
//        assertThat(bs.findById(bs.save(new BoardSaveDTO("상세조회용작성자", "상세조회용비밀번호", "상세조회용제목", "상세조회용내용"))).getBoardWriter()).isEqualTo("상세조회용작성자");
    }

    @Test
    @Transactional
    @Rollback
    @DisplayName("수정 테스트")
    public void boardUpdateTest() {

        Long boardId = bs.save(new BoardSaveDTO("수정전작성자", "수정전비밀번호", "수정전제목", "수정전내용"));
        BoardDetailDTO boardDetailDTO = bs.findById(boardId);
        bs.update(new BoardUpdateDTO(boardId, "수정후작성자", "수정후비밀번호", "수정후제목", "수정후내용"));
        assertThat(boardDetailDTO.getBoardTitle()).isNotEqualTo(bs.findById(boardId).getBoardTitle());

    }

    @Test // 자바 기본
    @DisplayName("삼항연산자")
    public void test1() {
        int num = 10;
        int num2 = 0;
        if (num == 10) {
            num2 = 5;
        } else {
            num2 = 100;
        }

        num2 = (num==10)? 5: 100;
    }

    @Test
    @Transactional
    @DisplayName("페이징 테스트")
    public void pagingTest() {
        int page = 5;
        Page<BoardEntity> boardEntities = br.findAll(PageRequest.of(page, PagingConst.PAGE_LIMIT, Sort.by(Sort.Direction.DESC, "id")));
        // Page 객체가 제공해주는 메서드 확인
        System.out.println("boardEntities.getContent() = " + boardEntities.getContent()); // 요청 페이지에 들어있는 데이터
        System.out.println("boardEntities.getTotalElements() = " + boardEntities.getTotalElements()); // 전체 글 갯수
        System.out.println("boardEntities.getNumber() = " + boardEntities.getNumber()); // 요청페이지(jpa 기준)
        System.out.println("boardEntities.getTotalPages() = " + boardEntities.getTotalPages()); // 전체 페이지 갯수
        System.out.println("boardEntities.getSize() = " + boardEntities.getSize()); // 한 페이지에 보여지는 글 갯수
        System.out.println("boardEntities.hasPrevious() = " + boardEntities.hasPrevious()); // 이전페이지 존재 여부
        System.out.println("boardEntities.isFirst() = " + boardEntities.isFirst()); // 첫페이지인지 여부
        System.out.println("boardEntities.isLast() = " + boardEntities.isLast()); // 마지막 페이지인지 여부

        // Page<BoardEntity> -> Page<BoardPagingDTO>
        // map(): 엔티티가 담긴 페이지 객체를 dto가 담긴 페이지객체로 변환해주는 역할
        Page<BoardPagingDTO> boardList = boardEntities.map(
                board -> new BoardPagingDTO(board.getId(),
                                            board.getBoardWriter(),
                                            board.getBoardTitle())
        );
        System.out.println("boardList.getContent() = " + boardList.getContent()); // 요청 페이지에 들어있는 데이터
        System.out.println("boardList.getTotalElements() = " + boardList.getTotalElements()); // 전체 글 갯수
        System.out.println("boardList.getNumber() = " + boardList.getNumber()); // 요청페이지(jpa 기준)
        System.out.println("boardList.getTotalPages() = " + boardList.getTotalPages()); // 전체 페이지 갯수
        System.out.println("boardList.getSize() = " + boardList.getSize()); // 한 페이지에 보여지는 글 갯수
        System.out.println("boardList.hasPrevious() = " + boardList.hasPrevious()); // 이전페이지 존재 여부
        System.out.println("boardList.isFirst() = " + boardList.isFirst()); // 첫페이지인지 여부
        System.out.println("boardList.isLast() = " + boardList.isLast()); // 마지막 페이지인지 여부
    }

    // 댓글 작성 테스트
    @Test
    @Transactional
    @Rollback(value = false)
    @DisplayName("댓글작성 테스트")
    public void CommentSaveTest() {
        // 내가 한거
//        Long boardId = bs.save(new BoardSaveDTO("댓글달글작성자3", "댓글달글비밀번호3", "댓글달글제목3", "댓글달글내용3"));
////        bs.findById(boardId);
//        Long commentId = cs.save(new CommentSaveDTO(boardId, "댓글작성자3", "댓글내용3"));
//        assertThat(commentId).isNotNull();

        // 선생님
        // 게시글 존재
        BoardSaveDTO boardSaveDTO = new BoardSaveDTO("bw1", "bp1", "bt1", "bc1");
        Long boardId = bs.save(boardSaveDTO);
        // 댓글
        CommentSaveDTO commentSaveDTO = new CommentSaveDTO(boardId, "cw1", "cc1");
        cs.save(commentSaveDTO);
    }

    @Test
    @Transactional
    @DisplayName("댓글 조회")
    public void CommentFindByIdTest() {
        CommentEntity commentEntity = cr.findById(1L).get();
        System.out.println("commentEntity.toString() = " + commentEntity.toString());
        System.out.println("commentEntity.getId() = " + commentEntity.getId());
        System.out.println("commentEntity.getCommentWriter() = " + commentEntity.getCommentWriter());
        System.out.println("commentEntity.getCommentContents() = " + commentEntity.getCommentContents());
        System.out.println("commentEntity.getBoardEntity() = " + commentEntity.getBoardEntity());
        System.out.println("commentEntity.getBoardEntity().getBoardTitle() = " + commentEntity.getBoardEntity().getBoardTitle());

        CommentEntity commentEntity1 = cr.findById(2L).get();
        System.out.println("commentEntity.toString() = " + commentEntity1.toString());
        System.out.println("commentEntity.getId() = " + commentEntity1.getId());
        System.out.println("commentEntity.getCommentWriter() = " + commentEntity1.getCommentWriter());
        System.out.println("commentEntity.getCommentContents() = " + commentEntity1.getCommentContents());
        System.out.println("commentEntity.getBoardEntity() = " + commentEntity1.getBoardEntity());
        System.out.println("commentEntity.getBoardEntity().getBoardTitle() = " + commentEntity1.getBoardEntity().getBoardTitle());
    }

    @Test
    @Transactional
    @DisplayName("댓글 목록 출력")
    public void findAllTest() {
        List<CommentDetailDTO> commentDetailDTOS = cs.findAll(1L);
        for(CommentDetailDTO c: commentDetailDTOS) {
            System.out.println("c.toString() = " + c.toString());
        }
    }

    @Test
    @Transactional
    @Rollback(value = false)
    @DisplayName("게시글 삭제")
    public void boardDelete() {
        br.deleteById(1L);
    }

}
