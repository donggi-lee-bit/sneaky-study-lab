package donggi.example.article.service

import donggi.example.article.controller.dto.ArticleCreateRequest
import donggi.example.article.domain.Article
import donggi.example.article.domain.ArticleRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ArticleService(
    private val articleRepository: ArticleRepository
) {
    @Transactional
    fun create(request: ArticleCreateRequest) =
        articleRepository.save(
            Article(
                title = request.title
            )
        )
}
