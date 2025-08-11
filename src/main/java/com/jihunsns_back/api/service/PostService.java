package com.jihunsns_back.api.service;

import com.jihunsns_back.domain.entity.Post;
import com.jihunsns_back.domain.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {
 private final PostRepository postRepository;

 public Post save(Post post){
     return postRepository.save(post);
 }

 public List<Post> findAll(){
     return postRepository.findAll();
 }
 public Optional<Post> findById(Long id){
     return postRepository.findById(id);
 }
 public void deleteByid(Long id) {
     postRepository.deleteById(id);
 }
}
