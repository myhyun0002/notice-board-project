package com.example.noticeboardproject.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@ToString
@Table(indexes = {
        @Index(columnList = "title"),
        @Index(columnList = "hashtag"),
        @Index(columnList = "createdAt"),
        @Index(columnList = "modifiedAt"),
})
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Article extends AuditingFields{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter @Column(nullable = false)
    private String title; // 본문
    @Setter @Column(nullable = false,length = 10000)
    private String content; // 본문
    @Setter
    private String hashtag; // 태그

    @OrderBy("id")
    @ToString.Exclude
    @OneToMany(mappedBy = "article",cascade = CascadeType.ALL)
    private final Set<ArticleComment> articleComments = new LinkedHashSet<>();

    private Article(String title, String content,String hashtag) {
        this.title = title;
        this.content = content;
        this.hashtag = hashtag;
    }

    // of를 통해 클래스에 어떤 값을 넣어줘야 하는지 알려준다.
    public static Article of(String title, String content, String hashtag){
        return new Article(title,content,hashtag);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Article article = (Article) o;
        return getId() != null && Objects.equals(getId(), article.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
