package com.icia.board.entity;

import com.icia.board.dto.CommentSaveDTO;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "comment_table")
public class CommentEntity extends BaseEntity {
    // 댓글번호, 작성자, 내용, 원글

    @Id@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    // 원 글의 게시글 번호를 참조하기 위한 설정(댓글:게시글 = N:1)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id") // 부모테이블(참조하고자 하는 테이블)의 pk 컬럼이름
    private BoardEntity boardEntity; // 참조하고자 하는 테이블을 관리하는 Entity

    // 원 글의 게시글 번호를 참조하기 위한 설정(댓글:게시글 = N:1)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id") // 부모테이블(참조하고자 하는 테이블)의 pk 컬럼이름
    private MemberEntity memberEntity; // 참조하고자 하는 테이블을 관리하는 Entity

    @Column
    private String commentWriter;

    @Column
    private String commentContents;

    public static CommentEntity toSaveEntity(CommentSaveDTO commentSaveDTO, BoardEntity boardEntity) {
        CommentEntity commentEntity = new CommentEntity();
        commentEntity.setCommentWriter(commentSaveDTO.getCommentWriter());
        commentEntity.setCommentContents(commentSaveDTO.getCommentContents());
        commentEntity.setBoardEntity(boardEntity);
        return commentEntity;
    }
}
