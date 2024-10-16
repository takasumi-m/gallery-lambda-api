package com.photowave.repository.impl;

import com.photowave.repository.GetPostsRepository;
import com.photowave.repository.entity.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
public class GetPostsRepositoryImpl implements GetPostsRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<SearchPostsEntity> searchPosts(String caption, String location, LocalDate postDate, LocalDateTime postDatetime, List<String> tagList) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaQuery<SearchPostsEntity> query = cb.createQuery(SearchPostsEntity.class);

        // FROM
        Root<Post> post = query.from(Post.class);

        // SELECT
        query.select(cb.construct(SearchPostsEntity.class,
                post.get("postId"),
                post.get("caption"),
                post.get("location"),
                post.get("postDate"),
                post.get("postDatetime")
        ));

        // WHERE
        List<Predicate> predicates = new ArrayList<>();
        // status
        predicates.add(cb.equal(post.get("status"), "enabled"));
        // caption
        if (!Objects.isNull(caption) && !caption.isEmpty()) {
            predicates.add(cb.like(post.get("caption"), "%" + caption + "%"));
        }
        // location
        if (!Objects.isNull(location) && !location.isEmpty()) {
            predicates.add(cb.like(post.get("location"), "%" + location + "%"));
        }
        // postDate
        if (!Objects.isNull(postDate)) {
            predicates.add(cb.equal(post.get("postDate"), postDate));
        }
        // postDatetime
        if (!Objects.isNull(postDatetime)) {
            predicates.add(cb.lessThanOrEqualTo(post.get("postDatetime"), postDatetime));
        }
        // tagList
        if (!Objects.isNull(tagList) && !tagList.isEmpty()) {
            Subquery<Long> tagSubquery = query.subquery(Long.class);
            Root<PostTags> postTags = tagSubquery.from(PostTags.class);
            Join<PostTags, Tag> tag = postTags.join("tag", JoinType.INNER);
            tagSubquery.select(postTags.get("postId"))
                    .where(tag.get("name").in(tagList))
                    .groupBy(postTags.get("postId"))
                    .having(cb.equal(cb.countDistinct(postTags.get("tagId")), tagList.size()));

            predicates.add(post.get("postId").in(tagSubquery));
        }

        // WHERE句を適用
        query.where(cb.and(predicates.toArray(new Predicate[0])));

        // ORDER BY
        query.orderBy(cb.desc(post.get("postDatetime")));

        // クエリの実行(LIMIT指定)
        TypedQuery<SearchPostsEntity> typedQuery = entityManager.createQuery(query).setMaxResults(50);
        return typedQuery.getResultList();
    }
}
