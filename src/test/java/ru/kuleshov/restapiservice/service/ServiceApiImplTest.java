package ru.kuleshov.restapiservice.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.kuleshov.restapiservice.model.Post;
import ru.kuleshov.restapiservice.repository.PostDataAccessible;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Тест:
 * - перед стартом заполненяет БД 100к сущностями
 * - получает 1млн рандомных сущностей из БД и считает статистику
 */
@Slf4j
@SpringBootTest
class ServiceApiImplTest {
    private static final String TAG = "TEST";
    private static Long COUNT_POST = 100000L;
    private static Long COUNT_GET = 1000000L;

    @Autowired
    private PostDataAccessible postDataAccessible;

    private List<UUID> uuids = new ArrayList<>();
    private static final List<Long> executionTimes = Collections.synchronizedList(new ArrayList<>());

    /**
     * Метод, перед стартом, заполненяет БД 100к сущностями
     */
    @BeforeEach
    void setUp() {
        long startTime = System.nanoTime();

        ExecutorService executorService = Executors.newFixedThreadPool(10);
        List<Future<UUID>> futures = new ArrayList<>();

        try {
            for (int i = 0; i < COUNT_POST; i++) {
                final int postIndex = i;
                Future<UUID> future = executorService.submit(() -> {
                    Post post = new Post();
                    post.setPost("Text : " + postIndex);
                    return postDataAccessible.createPost(post).getId();
                });
                futures.add(future);
            }

            for (Future<UUID> future : futures) {
                try {
                    future.get();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } finally {
            executorService.shutdown();
        }
        for (Future<UUID> future : futures) {
            try {
                UUID uuid = future.get();
                uuids.add(uuid);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        log.info("{} uuids created", uuids.size());
        long endTime = System.nanoTime();
        long finalTime = (endTime - startTime) / 1000000;

        log.info("{} - БД заполнена за: {}ms", TAG, finalTime);

    }

    /**
     * Метод получает 1млн рандомных сущностей из БД и считает статистику
     */
    @Test
    void getPost() {
        long startTime = System.nanoTime();
        ExecutorService executorService = Executors.newFixedThreadPool(100);
        List<Future<Post>> futures = new ArrayList<>();

        try {
            for (int i = 0; i < COUNT_GET; i++) {

                int rndNumber = (int) (Math.random() * uuids.size());
                UUID uuid = uuids.get(rndNumber);

                Future<Post> future = executorService.submit(() -> {
                    long start = System.nanoTime();
                    Post post = postDataAccessible.getPost(uuid);
                    long executionTime = (System.nanoTime() - start) / 1000000;
                    executionTimes.add(executionTime);
                    return post;
                });
                futures.add(future);
            }
            for (Future<Post> future : futures) {
                try {
                    Post post = future.get();
                    if (post == null) {
                        log.error("{} Post не найден", TAG);
                    }
                } catch (Exception e) {
                    log.error("Ошибка при получении сообщения: {}", e.getMessage(), e);
                }
            }
        } finally {
            executorService.shutdown();
        }
        long endTime = System.nanoTime();
        long finalTime = (endTime - startTime) / 1000000;
        logStatistics(finalTime);
    }

    private void logStatistics(Long finalTime) {
        if (ServiceApiImplTest.executionTimes.isEmpty()) {
            log.warn("{} - Отсутствует запись времени", TAG);
            return;
        }

        Collections.sort(ServiceApiImplTest.executionTimes);
        long totalTime = ServiceApiImplTest.executionTimes.stream().mapToLong(Long::longValue).sum();
        double median = getPercentile(ServiceApiImplTest.executionTimes, 50);
        double percentile95 = getPercentile(ServiceApiImplTest.executionTimes, 95);
        double percentile99 = getPercentile(ServiceApiImplTest.executionTimes, 99);

        log.info("{} - Статистика времени выполнения:", TAG);
        log.info("{} - Фактическое время выполнения: {}ms", TAG, finalTime);
        log.info("{} - Общее время: {}ms", TAG, totalTime);
        log.info("{} - Медиана: {}ms", TAG, median);
        log.info("{} - 95-й перцентиль: {}ms", TAG, percentile95);
        log.info("{} - 99-й перцентиль: {}ms", TAG, percentile99);
    }

    private double getPercentile(List<Long> sortedValues, double percentile) {
        if (sortedValues.isEmpty()) {
            throw new IllegalArgumentException("Пустой список");
        }

        int index = (int) Math.ceil(percentile / 100.0 * sortedValues.size()) - 1;
        return sortedValues.get(index);
    }
}