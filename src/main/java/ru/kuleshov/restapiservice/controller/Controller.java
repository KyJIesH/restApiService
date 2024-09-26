package ru.kuleshov.restapiservice.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kuleshov.restapiservice.model.Post;
import ru.kuleshov.restapiservice.service.ServiceApi;

import java.util.List;
import java.util.UUID;

/**
 * Контроллер для выполнения CRUD операций над сущностью {@link  Post}
 */
@RestController
@RequestMapping("/posts")
@Slf4j
@AllArgsConstructor
public class Controller {
    private static final String TAG = "CONTROLLER";
    private final ServiceApi serviceApi;

    @PostMapping
    public ResponseEntity<Post> createPost(@RequestBody Post post) {
        log.info("{} - Пришел запрос на добавление поста {}", TAG, post);
        return new ResponseEntity<>(serviceApi.createPost(post), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Post> getPost(@PathVariable UUID id) {
        log.info("{} - Пришел запрос на получение поста по id {}", TAG, id);
        return new ResponseEntity<>(serviceApi.getPost(id), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Post>> getAllPosts() {
        log.info("{} - Пришел запрос на получение всех постов", TAG);
        return new ResponseEntity<>(serviceApi.getAllPosts(), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<Post> updatePost(@RequestBody Post post) {
        log.info("{} - Пришел запрос на обновление поста {}", TAG, post);
        return new ResponseEntity<>(serviceApi.updatePost(post), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePost(@PathVariable UUID id) {
        log.info("{} - Пришел запрос на удаление поста по id {}", TAG, id);
        if (serviceApi.deletePost(id)) {
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}