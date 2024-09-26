package ru.kuleshov.restapiservice.repository;

import ru.kuleshov.restapiservice.model.Post;

import java.util.List;
import java.util.UUID;

/**
 * Интерфейс, описывающий методы,
 * реализованные в классе работы с БД
 */
public interface PostDataAccessible {

    Post createPost(Post post);

    Post getPost(UUID id);

    List<Post> getAllPosts();

    Post updatePost(Post post);

    boolean deletePost(UUID id);
}
