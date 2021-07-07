package com.github.arsengir.repository;

import com.github.arsengir.model.Post;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class PostRepositoryImpl implements PostRepository {

    private final Map<Long, Post> postMap = new ConcurrentHashMap<>();
    private final AtomicInteger sequenceId = new AtomicInteger(0);

    public List<Post> all() {
        return new ArrayList<>(postMap.values());
    }

    public Optional<Post> getById(long id) {
        return Optional.ofNullable(postMap.get(id));
    }

    public Post save(Post post) {
        long id = post.getId();
        if (id == 0 || id > sequenceId.get()) {
            id = sequenceId.incrementAndGet();
            post.setId(id);
        }
        postMap.put(id, post);
        return post;
    }

    public void removeById(long id) {
        postMap.remove(id);
    }
}
