package com.example.dkboard.service

import com.example.dkboard.domain.Post
import com.example.dkboard.exception.PostNotDeletableException
import com.example.dkboard.exception.PostNotFoundException
import com.example.dkboard.exception.PostNotUpdatableException
import com.example.dkboard.repository.PostRepository
import com.example.dkboard.service.dto.PostCreateRequestDto
import com.example.dkboard.service.dto.PostUpdateRequestDto
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.longs.shouldBeGreaterThan
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull

@SpringBootTest
class PostServiceTest(
    private val postSevice: PostService,
    private val postRepository: PostRepository,
) : BehaviorSpec({
  given("게시글 생성자"){
      When("게시글 Input이 정상적으로 들어오면"){
          val postId = postSevice.createPost(PostCreateRequestDto(
              title = "제목",
              content = "내용",
              createdBy = "wally",

          ))
          then("게시글이 정상적으로 생성됨을 확인한다."){
              postId shouldBeGreaterThan 0L
              val post = postRepository.findByIdOrNull(postId)
              post shouldNotBe null
              post?.title shouldBe "제목"
              post?.content shouldBe "내용"
              post?.createdBy shouldBe "wally"
          }
      }
  }
    given("게시글 수정시"){
        val saved = postRepository.save(Post(title = "title", content = "content", createdBy = "wally"))
        When("정상 수정시"){
            val updatedId = postSevice.updatePost(saved.id, PostUpdateRequestDto(
                title = "update title",
                content = "update content",
                updatedBy = "wally"
            ))
            then("게시글이 정상적으로 수정됨을 확인한다.") {
                saved.id shouldBe updatedId
                val updated = postRepository.findByIdOrNull(updatedId)
                updated shouldNotBe null
                updated?.title shouldBe "update title"
                updated?.content shouldBe "update content"
            }
        }
        When("게시글이 없을 때"){
            then("게시글을 찾을 수 없다라는 예외가 발생한다."){
                shouldThrow<PostNotFoundException> { postSevice.updatePost(9999L, PostUpdateRequestDto(
                    title = "update title",
                    content = "update content",
                    updatedBy = "update wally"
                )) }
            }
        }
        When("작성자가 동일하지 않으면"){
            then("수정할 수 없는 게시물 입니다. 예외가 발생한다."){
                shouldThrow<PostNotUpdatableException> { postSevice.updatePost(1L, PostUpdateRequestDto(
                    title = "update title",
                    content = "update content",
                    updatedBy = "update wally"
                )) }
            }
        }
    }

    given("게시글 삭제시"){
        val saved = postRepository.save(Post(title = "title", content = "content", createdBy = "wally"))
        When("정상 삭제시") {
            val postId = postSevice.deletePost(saved.id, "wally")
            then("게시글이 정상적으로 삭제됨을 확인한다.") {
                postId shouldBe saved.id
                postRepository.findByIdOrNull(postId) shouldBe null
            }
        }
        When("작성자가 동일하지 않으면"){
            val saved2 = postRepository.save(Post(title = "title", content = "content", createdBy = "wally"))
            then("삭제할 수 없는 게시물입니다. 예외가 발생한다."){
                shouldThrow<PostNotDeletableException> { postSevice.deletePost(saved2.id, "wally2") }
            }
        }
    }
})
