package demo.kotlinboilerplate.common.token.mapper

import demo.kotlinboilerplate.common.token.domain.Token
import demo.kotlinboilerplate.common.token.persistence.entity.TokenEntity
import org.mapstruct.Mapper

@Mapper
interface TokenMapper {
    fun toDomain(entity: TokenEntity): Token
}