package com.example.demo.service

import com.example.demo.repository.UsuarioRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class UserDetailsServiceImpl(
    private val usuarioRepository: UsuarioRepository
) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        return usuarioRepository.findByUsername(username)
            .orElseThrow { UsernameNotFoundException("Usuário não encontrado: $username") }
    }
}
