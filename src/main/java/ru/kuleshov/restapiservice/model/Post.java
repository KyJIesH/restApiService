package ru.kuleshov.restapiservice.model;

import lombok.Data;

import java.util.UUID;

/**
 * Класс описывает сущность Post
 */
@Data
public class Post {
    private UUID id;
    private String post;
}
