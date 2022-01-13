package ru.notebot.dao;

import org.springframework.jdbc.core.RowCallbackHandler;
import ru.notebot.model.Link;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LinkRowCallbackHandler implements RowCallbackHandler {

    private Map<Long, Link> linkMap = new HashMap<>();

    @Override
    public void processRow(ResultSet rs) throws SQLException {
        Long linkId = rs.getLong("id");
        Link link = linkMap.get(linkId);
        if (link == null) {
            link = Link.builder()
                    .id(rs.getLong("id"))
                    .catId(rs.getLong("cat_id"))
                    .catName(rs.getString("cat_name"))
                    .uri(rs.getString("uri"))
                    .comment(rs.getString("comment"))
                    .build();
            linkMap.put(linkId, link);
        }
        String tag = rs.getString("tag_name");
        if (tag != null) {
            link.getTags().add(tag);
        }
    }

    public List<Link> getLinks() {
        return new ArrayList<>(linkMap.values());
    }
}
