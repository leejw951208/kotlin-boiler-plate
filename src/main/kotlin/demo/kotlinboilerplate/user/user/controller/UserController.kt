package demo.kotlinboilerplate.user.user.controller

import demo.kotlinboilerplate.common.exception.ExceptionEnum
import demo.kotlinboilerplate.common.swagger.SwaggerApiExceptionResponse
import demo.kotlinboilerplate.common.swagger.SwaggerApiResponse
import demo.kotlinboilerplate.user.user.dto.UserResponseDto
import demo.kotlinboilerplate.user.user.mapper.UserMapper
import demo.kotlinboilerplate.user.user.service.UserService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "User")
@RestController
@RequestMapping("user")
class UserController(
    private val userService: UserService,
    private val userMapper: UserMapper,
) {
    @Operation(summary = "사용자 정보 조회")
    @SwaggerApiResponse(responseCode = "200", description = "조회 성공")
    @SwaggerApiExceptionResponse(exceptions = [ExceptionEnum.NOT_FOUND_USER])
    @GetMapping("{id}")
    fun findUser(
        @Parameter(name = "id", description = "사용자 ID", example = "1")
        @PathVariable id: Long,
    ): ResponseEntity<UserResponseDto> {
        return ResponseEntity.ok(userMapper.toDto(userService.findOne(id)))
    }

}
