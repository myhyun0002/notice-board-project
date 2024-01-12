package com.example.noticeboardproject.repository;

import com.example.noticeboardproject.domain.ArticleComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleCommentRepository extends JpaRepository<ArticleComment,Long> {
}
