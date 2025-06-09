package com.example.projetomobileads5a.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.projetomobileads5a.R

class LoginFragment : Fragment() {

    private val emailValido = "teste@email.com"
    private val senhaValida = "123456"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val editEmail = view.findViewById<EditText>(R.id.editEmail)
        val editSenha = view.findViewById<EditText>(R.id.editSenha)
        val btnLogin = view.findViewById<Button>(R.id.btnLogin)

        btnLogin.setOnClickListener {
            val email = editEmail.text.toString().trim()
            val senha = editSenha.text.toString().trim()

            if (email.isEmpty() || senha.isEmpty()) {
                Toast.makeText(requireContext(), "Preencha todos os campos", Toast.LENGTH_SHORT).show()
            } else if (email == emailValido && senha == senhaValida) {
                // Login bem-sucedido → navega para HomeFragment
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, HomeFragment())
                    .addToBackStack(null)
                    .commit()
            } else {
                Toast.makeText(requireContext(), "E-mail ou senha inválidos", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
