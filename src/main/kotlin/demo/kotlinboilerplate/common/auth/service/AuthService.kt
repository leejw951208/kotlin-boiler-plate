package demo.kotlinboilerplate.common.auth.service

import demo.kotlinboilerplate.common.auth.dto.LoginResponseDto
import demo.kotlinboilerplate.common.auth.dto.LoginRequestDto
import demo.kotlinboilerplate.common.token.TokenProvider
import demo.kotlinboilerplate.common.auth.dto.JoinRequestDto
import demo.kotlinboilerplate.common.token.persistence.entity.TokenEntity
import demo.kotlinboilerplate.common.token.persistence.repository.TokenEntityRepository
import demo.kotlinboilerplate.member.persistence.entity.MemberEntity
import demo.kotlinboilerplate.member.persistence.repository.MemberEntityRepository
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException

@Service
class AuthService(
    private val memberRepository: MemberEntityRepository,
    private val jwtProvider: TokenProvider,
    private val authenticationManagerBuilder: AuthenticationManagerBuilder,
    private val tokenRepository: TokenEntityRepository
) {
    @Transactional
    fun join(requestDto: JoinRequestDto) {
        val isExistsEmail = memberRepository.existsByEmail(requestDto.email)
        if (isExistsEmail) throw ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 가입된 이메일 입니다.")

        val createdMember = MemberEntity(requestDto.email, requestDto.password, requestDto.name)
        createdMember.isApproved(true)
        createdMember.setCreatedBy(0)
        memberRepository.save(createdMember)
    }

    @Transactional
    fun login(requestDto: LoginRequestDto): LoginResponseDto {
        val authenticationToken = UsernamePasswordAuthenticationToken(requestDto.email, requestDto.password)
        val authentication = authenticationManagerBuilder.`object`.authenticate(authenticationToken)
        val createdToken = jwtProvider.createToken(authentication.name, authentication.authorities.joinToString(",") { it.authority })

        tokenRepository.save(TokenEntity(authentication.name.toLong(), createdToken.refreshToken))

        return LoginResponseDto.from(authentication.name.toLong(), requestDto.email, createdToken)
    }
}