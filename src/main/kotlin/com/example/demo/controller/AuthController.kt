package com.example.demo.controller

import com.example.demo.dto.AuthResponse
import com.example.demo.dto.LoginRequest
import com.example.demo.model.Usuario
import com.example.demo.security.JwtUtil
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authenticationManager: AuthenticationManager,
    private val jwtUtil: JwtUtil
) {

    @PostMapping("/login")
    fun login(@Valid @RequestBody loginRequest: LoginRequest): ResponseEntity<AuthResponse> {
        val authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                loginRequest.username,
                loginRequest.password
            )
        )

        val usuario = authentication.principal as Usuario
        val token = jwtUtil.generateToken(usuario)

        val response = AuthResponse(
            token = token,
            username = usuario.username,
            role = usuario.role.name
        )

        return ResponseEntity.ok(response)
    }
}
