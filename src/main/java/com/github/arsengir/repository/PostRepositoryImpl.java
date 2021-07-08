package com.github.arsengir.repository;

import com.github.arsengir.model.Post;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class PostRepositoryImpl implements PostRepository {

    private final Map<Long, Post> postMap = new ConcurrentHashMap<>();
    private final AtomicInteger sequenceId = new AtomicInteger(0);

    public List<Post> all() {
        return postMap.values().stream()
                .filter(p -> !p.isRemoved())
                .collect(Collectors.toList());
    }

    public Optional<Post> getById(long id) {
        final  var post = postMap.get(id);
        if (post != null && post.isRemoved()) {
            return Optional.empty();
        }
        return Optional.ofNullable(post);
    }

    public Optional<Post> save(Post post) {
        long id = post.getId();
        if (id == 0) {
            id = sequenceId.incrementAndGet();
            post.setId(id);
        } else {
            if (!postMap.containsKey(id) || postMap.get(id).isRemoved()) {
                return Optional.empty();
            }
        }

        postMap.put(id, post);
        return Optional.of(post);
    }

    public void removeById(long id) {
        postMap.get(id).setRemoved(true);
    }
}
