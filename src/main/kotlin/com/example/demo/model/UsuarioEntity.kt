
package com.example.demo.model

import jakarta.persistence.*
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.time.LocalDateTime

@Entity
data class Usuario(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    
    @Column(unique = true)
    private val username: String,
    
    private val password: String,
    
    @Enumerated(EnumType.STRING)
    val role: Role,
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id")
    val cliente: Cliente? = null,
    
    val dataCriacao: LocalDateTime = LocalDateTime.now(),
    
    var ativo: Boolean = true,
    
    var ultimoLogin: LocalDateTime? = null
) : UserDetails {
    
    override fun getAuthorities(): Collection<GrantedAuthority> {
        return listOf(SimpleGrantedAuthority("ROLE_${role.name}"))
    }
    
    override fun getUsername(): String = username
    override fun getPassword(): String = password
    override fun isAccountNonExpired(): Boolean = ativo
    override fun isAccountNonLocked(): Boolean = ativo
    override fun isCredentialsNonExpired(): Boolean = ativo
    override fun isEnabled(): Boolean = ativo
}

enum class Role {
    CLIENTE, GERENTE, ADMIN
}
