package demo.security.global.dto

data class ResponseStatusExceptionDto(
    val error: String,
    val path: String,
    val message: String,
    val status: Int,
    val timestamp: String
)