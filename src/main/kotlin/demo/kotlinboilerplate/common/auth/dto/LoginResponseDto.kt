package demo.kotlinboilerplate.common.auth.dto

import demo.kotlinboilerplate.common.token.vo.CreateToken

data class LoginResponseDto(
    val memberId: Long,
    val email: String,
    val accessToken: String,
    val refreshToken: String
) {
    companion object {
        fun from(memberId: Long, email: String, token: CreateToken): LoginResponseDto {
            return LoginResponseDto(
                memberId = memberId,
                email = email,
                accessToken = token.accessToken,
                refreshToken = token.refreshToken,
            )
        }
    }
}
