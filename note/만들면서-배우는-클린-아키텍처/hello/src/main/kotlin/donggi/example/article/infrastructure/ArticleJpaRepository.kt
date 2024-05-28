package donggi.example.article.infrastructure

import donggi.example.article.domain.ArticleRepository
import donggi.example.domain.Article
import org.springframework.data.jpa.repository.JpaRepository

interface ArticleRepository : JpaRepository<Article, Long>, ArticleRepository
