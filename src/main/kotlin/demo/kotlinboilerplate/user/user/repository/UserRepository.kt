package demo.kotlinboilerplate.user.user.repository

import demo.kotlinboilerplate.user.user.domain.User
import demo.kotlinboilerplate.user.user.dto.UserSearchConditionDto

interface UserRepository {
    fun findOne(id: Long): User?

    fun findOne(loginId: String): User?

    fun findOne(
        loginId: String,
        password: String,
    ): User?

    fun findAll(
        page: Int,
        size: Int,
        order: String,
        orderBy: String,
        condition: UserSearchConditionDto,
    ): List<User>

    fun save(user: User): User

    fun delete(id: Long)
}
