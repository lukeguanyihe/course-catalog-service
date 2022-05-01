package com.kotlinspring.controller

import com.kotlinspring.dto.CourseDTO
import com.kotlinspring.entity.Course
import com.kotlinspring.service.CourseService
import com.kotlinspring.service.GreetingService
import com.kotlinspring.util.courseDTO
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.runs
import io.mockk.just
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.reactive.server.WebTestClient

@WebMvcTest(controllers = [CourseController::class])
@AutoConfigureWebTestClient
class CourseControllerUnitTest {

    @Autowired
    lateinit var webTestClient: WebTestClient

    @MockkBean
    lateinit var courseServiceMock : CourseService

    @Test
    fun addCourse(){
        val courseDTO = CourseDTO(null, "Build Restful APIs using SpringBoot and Kotlin", "Dilip S")

        every { courseServiceMock.addCourse(any()) } returns courseDTO(id = 1)

        val savedCourseDTO = webTestClient
            .post()
            .uri("/v1/courses")
            .bodyValue(courseDTO)
            .exchange()
            .expectStatus().isCreated
            .expectBody(CourseDTO::class.java)
            .returnResult()
            .responseBody


        Assertions.assertTrue {
            savedCourseDTO!!.id != null
        }
    }

    @Test
    fun addCourse_validation(){
        val courseDTO = CourseDTO(null, "", "")

        every { courseServiceMock.addCourse(any()) } returns courseDTO(id = 1)

        val response = webTestClient
            .post()
            .uri("/v1/courses")
            .bodyValue(courseDTO)
            .exchange()
            .expectStatus().isBadRequest
            .expectBody(String::class.java)
            .returnResult()
            .responseBody

        assertEquals("courseDTO.category must not be blank, courseDTO.name must not be blank", response)
    }

    @Test
    fun addCourse_runtimeException(){
        val courseDTO = CourseDTO(null, "aaaaa", "aaaaa")

        val errorMessage = "Unexpected Error occurred"
        every { courseServiceMock.addCourse(any()) } throws RuntimeException(errorMessage)

        val response = webTestClient
            .post()
            .uri("/v1/courses")
            .bodyValue(courseDTO)
            .exchange()
            .expectStatus().is5xxServerError
            .expectBody(String::class.java)
            .returnResult()
            .responseBody

        assertEquals(errorMessage, response)
    }


    @Test
    fun retrieveAllCourses() {

        every { courseServiceMock.retrieveAllCourses() }.returnsMany(
            listOf(
                courseDTO(id = 1),
                courseDTO(id = 2, name="data"))
        )

        val courseDTOs = webTestClient
            .get()
            .uri("/v1/courses")
            .exchange()
            .expectStatus().isOk
            .expectBodyList(CourseDTO::class.java)
            .returnResult()
            .responseBody

        Assertions.assertEquals(2, courseDTOs!!.size)
    }

    @Test
    fun updateCourse(){
        every { courseServiceMock.updateCourse(any(), any()) } returns courseDTO(id=100, name="Build Restful APIs using SpringBoot and Kotlin1")

        val updatedCourseDTO = CourseDTO(null, "Build Restful APIs using SpringBoot and Kotlin1", "Development")

        val updatedCourse = webTestClient
            .put()
            .uri("/v1/courses/{courseId}", 100)
            .bodyValue(updatedCourseDTO)
            .exchange()
            .expectStatus().isOk
            .expectBody(CourseDTO::class.java)
            .returnResult()
            .responseBody

        Assertions.assertEquals("Build Restful APIs using SpringBoot and Kotlin1", updatedCourse!!.name)
    }

//    @Test
//    fun deleteCourse(){
//
//        every { courseServiceMock.deleteCourse(any()) } just runs
//
//        val updatedCourse = webTestClient
//            .delete()
//            .uri("/v1/courses/{courseId}", 100)
//            .exchange()
//            .expectStatus().isNoContent
//    }
}