package com.example.demo.security

import io.jsonwebtoken.*
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import java.security.Key
import java.util.*

@Component
class JwtUtil {

    @Value("\${jwt.secret:minhachavesecretasupersegurasupersegurasupersegura}")
    private lateinit var secret: String

    @Value("\${jwt.expiration:86400000}")
    private val expiration: Long = 86400000 // 24 horas

    private fun getSigningKey(): Key = Keys.hmacShaKeyFor(secret.toByteArray())

    fun generateToken(userDetails: UserDetails): String {
        val claims = mapOf<String, Any>()
        return createToken(claims, userDetails.username)
    }

    private fun createToken(claims: Map<String, Any>, subject: String): String {
        return Jwts.builder()
            .setClaims(claims)
            .setSubject(subject)
            .setIssuedAt(Date(System.currentTimeMillis()))
            .setExpiration(Date(System.currentTimeMillis() + expiration))
            .signWith(getSigningKey(), SignatureAlgorithm.HS256)
            .compact()
    }

    fun getUsernameFromToken(token: String): String {
        return getClaimFromToken(token, Claims::getSubject)
    }

    fun getExpirationDateFromToken(token: String): Date {
        return getClaimFromToken(token, Claims::getExpiration)
    }

    fun <T> getClaimFromToken(token: String, claimsResolver: (Claims) -> T): T {
        val claims = getAllClaimsFromToken(token)
        return claimsResolver(claims)
    }

    private fun getAllClaimsFromToken(token: String): Claims {
        return Jwts.parserBuilder()
            .setSigningKey(getSigningKey())
            .build()
            .parseClaimsJws(token)
            .body
    }

    private fun isTokenExpired(token: String): Boolean {
        val expiration = getExpirationDateFromToken(token)
        return expiration.before(Date())
    }

    fun validateToken(token: String, userDetails: UserDetails): Boolean {
        val username = getUsernameFromToken(token)
        return username == userDetails.username && !isTokenExpired(token)
    }
}
