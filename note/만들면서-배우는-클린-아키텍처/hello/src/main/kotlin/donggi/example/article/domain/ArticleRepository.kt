package donggi.example.article.domain

interface ArticleRepository {
    fun save(article: Article): Article
}
