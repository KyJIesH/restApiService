package ru.kuleshov.restapiservice.service;

import ru.kuleshov.restapiservice.model.Post;

import java.util.List;
import java.util.UUID;

/**
 * Интерфейс, описывающий методы,
 * реализованные в сервисном слое
 */
public interface ServiceApi {

    Post createPost(Post post);

    Post getPost(UUID id);

    List<Post> getAllPosts();

    Post updatePost(Post post);

    boolean deletePost(UUID id);
}
