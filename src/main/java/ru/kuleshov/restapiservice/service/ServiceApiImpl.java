package ru.kuleshov.restapiservice.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.kuleshov.restapiservice.exception.ValidationException;
import ru.kuleshov.restapiservice.model.Post;
import ru.kuleshov.restapiservice.repository.PostDataAccessible;

import java.util.List;
import java.util.UUID;

/**
 * Сервис,
 * для работы с сущностью {@link  Post}
 */
@Slf4j
@Service
@AllArgsConstructor
public class ServiceApiImpl implements ServiceApi {
    private static final String TAG = "SERVICE";

    private PostDataAccessible postDataAccessible;

    @Override
    public Post createPost(Post post) {
        log.info("{} - Обработка запроса на добавление поста {}", TAG, post);
        checkPostValues(post);
        return postDataAccessible.createPost(post);
    }

    @Override
    public Post getPost(UUID id) {
        log.info("{} - Обработка запроса на получение поста по id {}", TAG, id);
        checkPostId(id);
        return postDataAccessible.getPost(id);
    }

    @Override
    public List<Post> getAllPosts() {
        log.info("{} - Обработка запроса на получение всех постов", TAG);
        return postDataAccessible.getAllPosts();
    }

    @Override
    public Post updatePost(Post post) {
        log.info("{} - Обработка запроса на обновление поста {}", TAG, post);
        checkPostId(post.getId());
        checkPostValues(post);
        return postDataAccessible.updatePost(post);
    }

    @Override
    public boolean deletePost(UUID id) {
        log.info("{} - Обработка запроса на удаление поста по id {}", TAG, id);
        checkPostId(id);
        return postDataAccessible.deletePost(id);
    }

    private void checkPostId(UUID id) {
        if (id == null) {
            throw new ValidationException("Некорректный формат id поста");
        }
    }

    private void checkPostValues(Post post) {
        if (post.getPost() == null || post.getPost().isEmpty()) {
            throw new ValidationException("Пустое значение post");
        }
    }
}
