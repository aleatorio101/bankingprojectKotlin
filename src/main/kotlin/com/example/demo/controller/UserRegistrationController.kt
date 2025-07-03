import com.example.demo.model.Role
import com.example.demo.model.Usuario
import com.example.demo.repository.UsuarioRepository
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val usuarioRepository: UsuarioRepository,
    private val passwordEncoder: PasswordEncoder
) {

    @PostMapping("/register")
    fun register(@RequestBody request: RegisterRequest): ResponseEntity<String> {
        if (usuarioRepository.findByUsername(request.username) != null) {
            return ResponseEntity.badRequest().body("Usuário já existe")
        }

        val usuario = Usuario(
            username = request.username,
            password = passwordEncoder.encode(request.password),
            role = Role.CLIENTE
        )

        usuarioRepository.save(usuario)
        return ResponseEntity.ok("Usuário criado com sucesso")
    }
}

data class RegisterRequest(
    val username: String,
    val password: String
)
