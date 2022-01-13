CREATE TABLE links (
    id BIGINT AUTO_INCREMENT NOT NULL,
    cat_id BIGINT NOT NULL,
    uri TEXT NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE tags (
    id BIGINT AUTO_INCREMENT NOT NULL,
    tag_name VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE links2tags (
    link_id BIGINT NOT NULL,
    tag_id BIGINT NOT NULL,
    PRIMARY KEY (link_id, tag_id)
);

CREATE TABLE categories (
    id BIGINT AUTO_INCREMENT NOT NULL,
    cat_name VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE comments (
    id BIGINT AUTO_INCREMENT NOT NULL,
    comment TEXT NOT NULL,
    PRIMARY KEY (id)
);

-- ToDo индексы

ALTER TABLE links ADD CONSTRAINT links_cat_id_fk FOREIGN KEY (cat_id) REFERENCES categories (id);
ALTER TABLE links2tags ADD CONSTRAINT links2tags_link_id_fk FOREIGN KEY (link_id) REFERENCES links (id);
ALTER TABLE links2tags ADD CONSTRAINT links2tags_tags_id_fk FOREIGN KEY (tag_id) REFERENCES tags (id);
ALTER TABLE comments ADD CONSTRAINT comments_tags_id_fk FOREIGN KEY (id) REFERENCES links (id);
