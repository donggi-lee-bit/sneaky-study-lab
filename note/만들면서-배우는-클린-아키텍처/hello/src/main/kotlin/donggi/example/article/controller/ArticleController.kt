package donggi.example.article.controller

import donggi.example.article.controller.dto.ArticleCreateRequest
import donggi.example.article.service.ArticleService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController("/v1/articles")
class ArticleController(
    private val articleService: ArticleService
) {

    @PostMapping
    fun create(@RequestBody request: ArticleCreateRequest) {
        articleService.create(request)
    }
}
