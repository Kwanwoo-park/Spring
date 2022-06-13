package com.example.spring.service.posts;

import com.example.spring.domain.posts.Posts;
import com.example.spring.domain.posts.PostsRepository;
import com.example.spring.web.dto.PostsListResponseDto;
import com.example.spring.web.dto.PostsResponseDto;
import com.example.spring.web.dto.PostsSaveRequestDto;
import com.example.spring.web.dto.PostsUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostsService {
    private final PostsRepository postsRepository;

    @Transactional
    public Long save(PostsSaveRequestDto requestDto) {
        return postsRepository.save(requestDto.toEntity()).getId();
    }

    @Transactional
    public Long update(Long id, PostsUpdateRequestDto requestDto) {
        Posts posts = postsRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));
        posts.update(requestDto.getTitle(), requestDto.getContent());

        return id;
    }

    @Transactional
    public void delete(Long id) {
        Posts posts = postsRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("해당 게시글이 없습니다. id=" +id));

        postsRepository.delete(posts); /*   -JpaRepository에서 이미 delete method를 지원하고 있으니 이를 활용합니다.
                                            -엔티티를 파라미터를 삭제할 수도 있고, deleteById method를 이용하면 id로
                                            삭제할 수도 있습니다.
                                            -존재하는 Posts인지 확인을 위해 엔티티 조회 후 그대로 삭제합니다.*/
    }

    public PostsResponseDto findById(Long id) {
        Posts entity = postsRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));

        return new PostsResponseDto(entity);
    }

    @Transactional(readOnly = true)
    public List<PostsListResponseDto> findAllDesc() {
        return postsRepository.findAllDesc().stream()
                .map(PostsListResponseDto::new).collect(Collectors.toList());
    }
}
