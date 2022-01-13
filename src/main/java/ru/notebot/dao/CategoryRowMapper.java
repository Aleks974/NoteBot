package ru.notebot.dao;

import org.springframework.jdbc.core.RowMapper;
import ru.notebot.model.Category;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CategoryRowMapper implements RowMapper<Category> {
    @Override
    public Category mapRow(ResultSet rs, int i) throws SQLException {
       return new Category(rs.getLong("id"), rs.getString("cat_name"));
    }
}
