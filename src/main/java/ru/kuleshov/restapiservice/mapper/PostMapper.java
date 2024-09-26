package ru.kuleshov.restapiservice.mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.kuleshov.restapiservice.model.Post;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

/**
 * Маппер сущности {@link  Post}
 */
public class PostMapper implements RowMapper<Post> {

    @Override
    public Post mapRow(ResultSet rs, int rowNum) throws SQLException {

        Post post = new Post();
        UUID id = rs.getObject("p_id", UUID.class);
        String pPost = rs.getString("p_post");

        post.setId(id);
        post.setPost(pPost);

        return post;
    }
}
