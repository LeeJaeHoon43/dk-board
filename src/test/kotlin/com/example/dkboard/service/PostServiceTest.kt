package com.example.dkboard.service

import com.example.dkboard.domain.Comment
import com.example.dkboard.domain.Post
import com.example.dkboard.exception.PostNotDeletableException
import com.example.dkboard.exception.PostNotFoundException
import com.example.dkboard.exception.PostNotUpdatableException
import com.example.dkboard.repository.CommentRepository
import com.example.dkboard.repository.PostRepository
import com.example.dkboard.service.dto.PostCreateRequestDto
import com.example.dkboard.service.dto.PostSearchRequestDto
import com.example.dkboard.service.dto.PostUpdateRequestDto
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.longs.shouldBeGreaterThan
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.string.shouldContain
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull

@SpringBootTest
class PostServiceTest(
    private val postSevice: PostService,
    private val postRepository: PostRepository,
    private val commentRepository: CommentRepository,
) : BehaviorSpec({
    beforeSpec {
        postRepository.saveAll(
            listOf(
                Post(title = "title1", content = "content1", createdBy = "wally1"),
                Post(title = "title12", content = "content2", createdBy = "wally1"),
                Post(title = "title13", content = "content3", createdBy = "wally1"),
                Post(title = "title14", content = "content4", createdBy = "wally1"),
                Post(title = "title15", content = "content5", createdBy = "wally1"),
                Post(title = "title6", content = "content6", createdBy = "wally2"),
                Post(title = "title7", content = "content7", createdBy = "wally2"),
                Post(title = "title8", content = "content8", createdBy = "wally2"),
                Post(title = "title9", content = "content9", createdBy = "wally2"),
                Post(title = "title10", content = "content10", createdBy = "wally2")
            )
        )
    }
    given("게시글 생성자") {
        When("게시글 Input이 정상적으로 들어오면") {
            val postId = postSevice.createPost(
                PostCreateRequestDto(
                    title = "제목",
                    content = "내용",
                    createdBy = "wally"
                )
            )
            then("게시글이 정상적으로 생성됨을 확인한다.") {
                postId shouldBeGreaterThan 0L
                val post = postRepository.findByIdOrNull(postId)
                post shouldNotBe null
                post?.title shouldBe "제목"
                post?.content shouldBe "내용"
                post?.createdBy shouldBe "wally"
            }
        }
    }
    given("게시글 수정시") {
        val saved = postRepository.save(Post(title = "title", content = "content", createdBy = "wally"))
        When("정상 수정시") {
            val updatedId = postSevice.updatePost(
                saved.id,
                PostUpdateRequestDto(
                    title = "update title",
                    content = "update content",
                    updatedBy = "wally"
                )
            )
            then("게시글이 정상적으로 수정됨을 확인한다.") {
                saved.id shouldBe updatedId
                val updated = postRepository.findByIdOrNull(updatedId)
                updated shouldNotBe null
                updated?.title shouldBe "update title"
                updated?.content shouldBe "update content"
            }
        }
        When("게시글이 없을 때") {
            then("게시글을 찾을 수 없다라는 예외가 발생한다.") {
                shouldThrow<PostNotFoundException> {
                    postSevice.updatePost(
                        9999L,
                        PostUpdateRequestDto(
                            title = "update title",
                            content = "update content",
                            updatedBy = "update wally"
                        )
                    )
                }
            }
        }
        When("작성자가 동일하지 않으면") {
            then("수정할 수 없는 게시물 입니다. 예외가 발생한다.") {
                shouldThrow<PostNotUpdatableException> {
                    postSevice.updatePost(
                        1L,
                        PostUpdateRequestDto(
                            title = "update title",
                            content = "update content",
                            updatedBy = "update wally"
                        )
                    )
                }
            }
        }
    }

    given("게시글 삭제시") {
        val saved = postRepository.save(Post(title = "title", content = "content", createdBy = "wally"))
        When("정상 삭제시") {
            val postId = postSevice.deletePost(saved.id, "wally")
            then("게시글이 정상적으로 삭제됨을 확인한다.") {
                postId shouldBe saved.id
                postRepository.findByIdOrNull(postId) shouldBe null
            }
        }
        When("작성자가 동일하지 않으면") {
            val saved2 = postRepository.save(Post(title = "title", content = "content", createdBy = "wally"))
            then("삭제할 수 없는 게시물입니다. 예외가 발생한다.") {
                shouldThrow<PostNotDeletableException> { postSevice.deletePost(saved2.id, "wally2") }
            }
        }
    }

    given("게시글 상세조회시") {
        val saved = postRepository.save(Post(title = "title", content = "content", createdBy = "wally"))
        When("정상 조회시") {
            val post = postSevice.getPost(saved.id)
            then("게시글의 내용이 정상적으로 변환됨을 확인한다.") {
                post.id shouldBe saved.id
                post.title shouldBe "title"
                post.content shouldBe "content"
                post.createdBy shouldBe "wally"
            }
        }
        When("게시글이 없을 때") {
            then("게시글을 찾을 수 없다라는 예외가 발생한다.") {
                shouldThrow<PostNotFoundException> { postSevice.getPost(9999L) }
            }
        }
        When("댓글 추가시") {
            commentRepository.save(Comment(content = "댓글 내용1", post = saved, createdBy = "댓글 작성자"))
            commentRepository.save(Comment(content = "댓글 내용2", post = saved, createdBy = "댓글 작성자"))
            commentRepository.save(Comment(content = "댓글 내용3", post = saved, createdBy = "댓글 작성자"))
            val post = postSevice.getPost(saved.id)
            then("댓글이 함께 조회됨을 확인한다.") {
                post.comments.size shouldBe 3
                post.comments[0].content shouldBe "댓글 내용1"
                post.comments[1].content shouldBe "댓글 내용2"
                post.comments[2].content shouldBe "댓글 내용3"
                post.comments[0].createdBy shouldBe "댓글 작성자"
                post.comments[1].createdBy shouldBe "댓글 작성자"
                post.comments[2].createdBy shouldBe "댓글 작성자"
            }
        }
    }

    given("게시글 목록 조회시") {
        When("정상 조회시") {
            val postPage = postSevice.findPageBy(PageRequest.of(0, 5), PostSearchRequestDto())
            then("게시글 페이지가 반환된다.") {
                postPage.number shouldBe 0
                postPage.size shouldBe 5
                postPage.content.size shouldBe 5
                postPage.content[0].title shouldContain "title"
                postPage.content[0].createdBy shouldContain "wally"
            }
        }
        When("타이틀로 검색") {
            val postPage = postSevice.findPageBy(PageRequest.of(0, 5), PostSearchRequestDto(title = "title1"))
            then("타이틀에 해당하는 게시글이 반환된다.") {
                postPage.number shouldBe 0
                postPage.size shouldBe 5
                postPage.content.size shouldBe 5
                postPage.content[0].title shouldContain "title1"
                postPage.content[0].createdBy shouldContain "wally"
            }
        }
        When("작성자로 검색") {
            val postPage = postSevice.findPageBy(PageRequest.of(0, 5), PostSearchRequestDto(createdBy = "wally1"))
            then("작성자에 해당하는 게시글이 반환된다") {
                postPage.number shouldBe 0
                postPage.size shouldBe 5
                postPage.content.size shouldBe 5
                postPage.content[0].title shouldContain "title"
                postPage.content[0].createdBy shouldBe "wally1"
            }
        }
    }
})
