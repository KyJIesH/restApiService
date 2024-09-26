package ru.kuleshov.restapiservice.repository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.kuleshov.restapiservice.exception.NotFoundException;
import ru.kuleshov.restapiservice.mapper.PostMapper;
import ru.kuleshov.restapiservice.model.Post;

import java.util.List;
import java.util.UUID;

/**
 * Класс взаимодействия сущности {@link  Post} с БД
 */
@Slf4j
@Component
@AllArgsConstructor
public class PostDataAccessibleImpl implements PostDataAccessible {
    private static final String TAG = "DB";

    private final String tableName = "posts";
    private final String columnPrefix = "p_";
    private JdbcTemplate jdbcTemplate;
    private final PostMapper mapper = new PostMapper();

    @Override
    public Post createPost(Post post) {
        UUID uuid = UUID.randomUUID();

        String query = """
                INSERT INTO %s (%s, %s) VALUES (?, ?)
                """.formatted(tableName, columnPrefix + "id", columnPrefix + "post");

        int insertedRow = jdbcTemplate.update(query, uuid, post.getPost());
        if (insertedRow != 1) {
            return null;
        }
        return getPost(uuid);
    }

    @Override
    public Post getPost(UUID id) {

        String query = """
                SELECT * FROM %s WHERE %s = ?
                """.formatted(tableName, columnPrefix + "id");

        try {
            Post post = jdbcTemplate.queryForObject(query, mapper, id);
            return post;
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Пост не найден");
        }
    }

    @Override
    public List<Post> getAllPosts() {
        log.info("{} - Получение всех постов", TAG);

        String query = """
                SELECT * FROM %s
                """.formatted(tableName);

        List<Post> posts = jdbcTemplate.query(query, mapper);
        log.info("{} - Получены посты: {}", TAG, posts);
        return posts;
    }

    @Override
    public Post updatePost(Post post) {
        log.info("{} - Обновление поста {}", TAG, post);

        String query = """
                UPDATE %s SET %s = ? WHERE %s = ?
                """.formatted(tableName, columnPrefix + "post", columnPrefix + "id");

        int updatedRow = jdbcTemplate.update(query, post.getPost(), post.getId());
        if (updatedRow > 0) {
            log.info("{} - Пост {} обновлен", TAG, post);
        } else {
            log.error("{} - Пост {} НЕ обновлен", TAG, post);
        }
        return getPost(post.getId());
    }

    @Override
    public boolean deletePost(UUID id) {
        log.info("{} - Удаление поста по id {}", TAG, id);

        String query = """
                DELETE FROM %s WHERE %s = ?
                """.formatted(tableName, columnPrefix + "id");

        int deletedRow = jdbcTemplate.update(query, id);

        boolean condition = false;

        if (deletedRow > 0) {
            log.info("{} - Пост с id: {} удален", TAG, id);
            condition = true;
        } else {
            log.error("{} - Пост с id: {} НЕ удален", TAG, id);
        }
        return condition;
    }
}