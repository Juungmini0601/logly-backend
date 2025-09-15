package org.logly.domain.tag;

import java.util.Optional;

public interface TagRepository {
    Tag save(Tag tag);

    Optional<Tag> findByName(String name);
}
