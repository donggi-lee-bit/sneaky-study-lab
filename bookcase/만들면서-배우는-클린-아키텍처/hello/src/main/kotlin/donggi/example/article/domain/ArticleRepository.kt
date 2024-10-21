package donggi.example.article.domain

import donggi.example.domain.Article

interface ArticleRepository {
    fun save(article: Article): Article
}
