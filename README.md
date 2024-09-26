# RestApiService

Тестовое задание

Создать Rest API сервис обработки запросов к одной таблице БД.
Сервис должен уметь добавлять новую запись в таблицу, обновлять и удалять запись в таблице по id этой записи.
Должна быть возможность получить одну запись по id.
Должны быть созданы тесты для данного сервиса:
- создание 100тыс новых записей в таблице
- выборка 1млн произвольных записей через 100 подключений со сбором статистики по времени (общее, медианное, 95 и 99 процентиль)

Использовать:
Java 17
Spring, Spring Boot
БД H2

Тестовое задание необходимо выполнить полностью.
Тест должен не падать по памяти и работать адекватное время.
___
## Результат работы тестов

1. Результат работы заполнения БД 100к записями и выполнения **100к** запросов к БД
   ![](https://github.com/KyJIesH/restApiService/blob/master/file/h2%20-%20100%D0%BA.png)

3. Результат работы заполнения БД 100к записями и выполнения **1млн** запросов к БД
   ![](https://github.com/KyJIesH/restApiService/blob/master/file/h2%20-%201%D0%BC%D0%BB%D0%BD.png)
