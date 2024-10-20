package com.threeping.mudium.musicalboard.service;

import com.threeping.mudium.musicalboard.aggregate.ActiveStatus;
import com.threeping.mudium.musicalboard.aggregate.MusicalPost;
import com.threeping.mudium.musicalboard.dto.MusicalPostDTO;
import com.threeping.mudium.musicalboard.dto.MusicalPostListDTO;
import com.threeping.mudium.musicalboard.repository.MusicalBoardRepository;
import com.threeping.mudium.user.aggregate.dto.UserDTO;
import com.threeping.mudium.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

class MusicalBoardServiceImplTests {

    @Mock
    private MusicalBoardRepository musicalBoardRepository;

    @InjectMocks
    private MusicalBoardServiceImpl musicalBoardService;

    @Mock
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @DisplayName("특정 뮤지컬 게시글 전부 조회한다.")
    @Test
    void findAllPosts() {
        // given
        Long musicalId = 1L;
        Pageable pageable = PageRequest.of(0, 10, Sort.by("createdAt").descending());
        List<Object[]> content = Arrays.asList(
                new Object[]{createMockPost(1L, "제목", "내용", 1L, 1L), "user1"},
                new Object[]{createMockPost(2L, "제목2", "내용2", 2L, 1L), "user2"}
        );
        Page<Object[]> mockPosts = new PageImpl<>(content, pageable, content.size());

        when(musicalBoardRepository.findAllByMusicalIdAndActiveStatus(musicalId, ActiveStatus.ACTIVE, pageable))
                .thenReturn(mockPosts);
        when(userService.findByUserId(1L)).thenReturn(createMockUser(1L, "user1"));
        when(userService.findByUserId(2L)).thenReturn(createMockUser(2L, "user2"));

        // when
        Page<MusicalPostListDTO> postList = musicalBoardService.findAllPost(musicalId, pageable);

        // then
        assertNotNull(postList, "게시글 리스트는 null이 아니다.");
        assertFalse(postList.isEmpty(), "게시글 리스트는 비어있지 않다.");
        assertEquals(postList.getContent().size(), 2, "게시글 리스트의 사이즈는 2이다.");

        assertEquals(postList.getContent().get(0).getTitle(), "제목", "첫번째 게시글의 제목은 '제목'이다.");
        assertEquals(postList.getContent().get(0).getWriter(), "user1", "첫번째 게시글의 작성자 닉네임은 'user1'이다.");

        assertEquals(postList.getContent().get(1).getTitle(), "제목2", "두번째 게시글의 제목은 '제목2'이다.");
        assertEquals(postList.getContent().get(1).getWriter(), "user2", "두번째 게시글의 작성자 닉네임은 'user2'이다.");

        verify(musicalBoardRepository).findAllByMusicalIdAndActiveStatus(musicalId, ActiveStatus.ACTIVE, pageable);
    }

    private UserDTO createMockUser(Long id, String nickName) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(id);
        userDTO.setNickname(nickName);
        return userDTO;
    }

    private MusicalPost createMockPost(Long postId, String title, String content, Long userId, Long musicalId) {
        MusicalPost musicalPost = new MusicalPost();
        musicalPost.setMusicalId(musicalId);
        musicalPost.setTitle(title);
        musicalPost.setContent(content);
        musicalPost.setUserId(userId);
        musicalPost.setMusicalPostId(postId);
        musicalPost.setCreatedAt(Timestamp.from(Instant.now()));
        musicalPost.setViewCount(1L);

        return musicalPost;
    }

    @DisplayName("특정 뮤지컬 게시판의 특정 게시글을 조회한다.")
    @Test
    void getMusicalDetail() {
        // given
        Long musicalBoardId = 1L;
        Long userId = 1L;
        String nickName = "nickName";

        MusicalPost musicalPost = new MusicalPost();
        musicalPost.setMusicalPostId(musicalBoardId);
        musicalPost.setUserId(userId);
        musicalPost.setActiveStatus(ActiveStatus.ACTIVE);
        musicalPost.setContent("some content");
        musicalPost.setTitle("some title");
        musicalPost.setCreatedAt(Timestamp.from(Instant.now()));
        musicalPost.setUpdatedAt(Timestamp.from(Instant.now()));

        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(userId);
        userDTO.setNickname(nickName);

        when(musicalBoardRepository.findMusicalPostByMusicalPostIdAndActiveStatus(musicalBoardId, ActiveStatus.ACTIVE))
                .thenReturn(Optional.of(musicalPost));

        when(userService.findByUserId(userId)).thenReturn(userDTO);

        MusicalPostDTO result = musicalBoardService.findPost(musicalBoardId);

        // then
        assertNotNull(result, "조회된 게시글은 null이 아니다.");
        assertEquals(result.getContent(), "some content");
        assertEquals(result.getTitle(), "some title");
        assertEquals(result.getNickname(), nickName);

        verify(musicalBoardRepository).findMusicalPostByMusicalPostIdAndActiveStatus(musicalBoardId, ActiveStatus.ACTIVE);
        verify(userService).findByUserId(userId);
    }

    @DisplayName("특정 뮤지컬에 관한 게시글을 등록한다.")
    @Test
    void createPost() {
        // given
        Long musicalId = 1L;
        Long userId = 1L;
        MusicalPostDTO postDTO = new MusicalPostDTO();
        postDTO.setTitle("갈라쇼 후기");
        postDTO.setContent("너무 재밌었습니다!");

        musicalBoardService.createPost(musicalId, userId, postDTO);

        // then
        verify(musicalBoardRepository, times(1)).save(argThat(post ->
                        post.getMusicalId().equals(musicalId) &&
                                post.getUserId().equals(userId) &&
                                post.getTitle().equals(postDTO.getTitle()) &&
                                post.getContent().equals(postDTO.getContent())
                ));
    }

    @DisplayName("특정 뮤지컬에 관한 게시글을 수정한다.")
    @Test
    void updatePost() {
        // given
        Long musicalPostId = 1L;
        Long userId = 1L;
        MusicalPostDTO postDTO = new MusicalPostDTO();
        postDTO.setContent("내용 수정");
        postDTO.setTitle("제목 수정");

        MusicalPost musicalPost = new MusicalPost();
        musicalPost.setMusicalPostId(musicalPostId);
        musicalPost.setUserId(userId);
        musicalPost.setTitle("원래 제목");
        musicalPost.setContent("원래 내용");
        musicalPost.setCreatedAt(Timestamp.from(Instant.now()));
        musicalPost.setViewCount(1L);
        musicalPost.setLikeCount(0L);

        when(musicalBoardRepository.findMusicalPostByMusicalPostIdAndUserIdAndActiveStatus(
                musicalPostId, userId, ActiveStatus.ACTIVE
        )).thenReturn(Optional.of(musicalPost));

        // when
        musicalBoardService.updatePost(musicalPostId, userId, postDTO);

        // then
        verify(musicalBoardRepository).findMusicalPostByMusicalPostIdAndUserIdAndActiveStatus(
                musicalPostId, userId, ActiveStatus.ACTIVE
        );

        verify(musicalBoardRepository).save(argThat(post ->
                post.getMusicalPostId().equals(musicalPostId) &&
                        post.getUserId().equals(userId) &&
                        post.getTitle().equals(postDTO.getTitle()) &&
                        post.getContent().equals(postDTO.getContent())
        ));
    }

    @DisplayName("특정 뮤지컬 게시글을 삭제한다.")
    @Test
    void deletePost() {
        // given
        Long musicalPostId = 1L;
        Long userId = 1L;
        MusicalPost musicalPost = new MusicalPost();
        musicalPost.setMusicalPostId(musicalPostId);
        musicalPost.setUserId(userId);
        musicalPost.setTitle("삭제될 글 제목");
        musicalPost.setContent("삭제될 글 내용");
        musicalPost.setCreatedAt(Timestamp.from(Instant.now()));
        musicalPost.setViewCount(1L);
        musicalPost.setLikeCount(0L);

        when(musicalBoardRepository.findMusicalPostByMusicalPostIdAndUserIdAndActiveStatus(
                musicalPostId, userId, ActiveStatus.ACTIVE
        )).thenReturn(Optional.of(musicalPost));

        // when
        musicalBoardService.deletePost(musicalPostId, userId);

        // then
        verify(musicalBoardRepository).findMusicalPostByMusicalPostIdAndUserIdAndActiveStatus(
                musicalPostId, userId, ActiveStatus.ACTIVE
        );
        verify(musicalBoardRepository).save(argThat(post -> post.getActiveStatus().equals(ActiveStatus.INACTIVE)));
    }
}