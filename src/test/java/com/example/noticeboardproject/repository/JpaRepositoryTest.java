package com.example.noticeboardproject.repository;

import com.example.noticeboardproject.config.JpaConfig;
import com.example.noticeboardproject.domain.Article;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@ActiveProfiles("testDb")
@DisplayName("JPA 연결 테스트")
@Import(JpaConfig.class)
@DataJpaTest
class JpaRepositoryTest {
    private final ArticleRepository articleRepository;
    private final ArticleCommentRepository articleCommentRepository;

    public JpaRepositoryTest(
            @Autowired ArticleRepository articleRepository,
            @Autowired ArticleCommentRepository articleCommentRepository) {
        this.articleRepository = articleRepository;
        this.articleCommentRepository = articleCommentRepository;
    }

    @DisplayName("select 테스트")
    @Test
    public void givenTestData_whenSelecting_thenWorksFine() throws Exception{
        //given

        //when
        List<Article> articles = articleRepository.findAll();
        //then
        assertThat(articles).isNotNull().hasSize(300);
    }

    @DisplayName("insert 테스트")
    @Test
    public void givenTestData_whenInserting_thenWorksFine() throws Exception{
        //given
        long previousCount = articleRepository.count();
        //when
        Article savedArticle = articleRepository.save(Article.of("new article","new content","new hashtag"));
        //then
        assertThat(articleRepository.count()).isEqualTo(previousCount + 1);
    }

    @DisplayName("update 테스트")
    @Test
    public void givenTestData_whenUpdating_thenWorksFine() throws Exception{
        //given
        Article article = articleRepository.findById(1L).orElseThrow();
        String updateHashtag = "updateHashtag";
        article.setHashtag(updateHashtag);
        //when
        Article updateArticle = articleRepository.saveAndFlush(article);
        //then
        assertThat(updateArticle.getHashtag()).isEqualTo(updateHashtag);
    }

    @DisplayName("delete 테스트")
    @Test
    public void givenTestData_whenDeleting_thenWorksFine() throws Exception{
        //given
        Article article = articleRepository.findById(1L).orElseThrow();
        long previousArticleCount = articleRepository.count();
        long previousArticleCommentCount = articleCommentRepository.count();
        long deletedCommentSize = article.getArticleComments().size();
        //when
        articleRepository.delete(article);
        //then

        // 게시글 삭제 후 게시글 개수는 한 개 줄어들어야 함
        assertThat(articleRepository.count()).isEqualTo(previousArticleCount - 1);

        // 해당 게시글이 삭제되면 게시글에 해당하는 댓글들이 삭제되므로 전체 댓글 수가 게시글에 해당하는 댓글 수만큼 줄어들어야 한다.
        assertThat(articleCommentRepository.count()).isEqualTo(previousArticleCommentCount - deletedCommentSize);

    }
}
