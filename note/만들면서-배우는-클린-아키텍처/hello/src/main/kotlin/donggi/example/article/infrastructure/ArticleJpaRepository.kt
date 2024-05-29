package donggi.example.article.infrastructure

import donggi.example.article.domain.Article
import donggi.example.article.domain.ArticleRepository
import org.springframework.data.jpa.repository.JpaRepository

interface ArticleJpaRepository : JpaRepository<Article, Long>, ArticleRepository
