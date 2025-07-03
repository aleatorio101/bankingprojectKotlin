
package com.example.demo.dto

import jakarta.validation.constraints.NotBlank

data class LoginRequest(
    @field:NotBlank(message = "Username é obrigatório")
    val username: String,
    
    @field:NotBlank(message = "Password é obrigatório")
    val password: String
)

data class AuthResponse(
    val token: String,
    val type: String = "Bearer",
    val username: String,
    val role: String
)
