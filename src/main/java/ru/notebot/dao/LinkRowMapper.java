package ru.notebot.dao;

import org.springframework.jdbc.core.RowMapper;
import ru.notebot.model.Link;

import java.sql.ResultSet;
import java.sql.SQLException;

public class LinkRowMapper implements RowMapper<Link> {
    @Override
    public Link mapRow(ResultSet rs, int i) throws SQLException {
        System.out.println(rs);
        long id = rs.getLong("id");
        long catId = rs.getLong("cat_id");
        String catName = rs.getString("cat_name");
        String uri = rs.getString("uri");
        String comment = rs.getString("comment");;
        return Link.builder()
                .id(id)
                .catId(catId)
                .catName(catName)
                .uri(uri)
                .comment(comment)
                .build();
    }
}
