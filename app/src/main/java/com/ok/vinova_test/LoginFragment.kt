package com.ok.vinova_test

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import org.koin.androidx.viewmodel.ext.android.viewModel
import androidx.navigation.fragment.findNavController
import com.ok.vinova_test.databinding.FragmentLoginBinding
import com.ok.vinova_test.viewmodel.ShareDataViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val shareDataViewModel : ShareDataViewModel by activityViewModels()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initWatcher()
        binding.btnLogin.setOnClickListener {
            shareDataViewModel.setValue(binding.etUsername.text.toString())
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initWatcher() {

        fun textWatcherUsername(isUsername: Boolean): TextWatcher {
            return (object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    val username = (binding.etUsername.text.toString())
                    val password = (binding.etPassword.text.toString())
                    // Part 1
                    binding.btnLogin.isEnabled = (username.isEmpty() || password.isEmpty()).not()
                    // Part 2
                    val patternUsername = "[a-zA-Z0-9]+".toRegex()
                    val patternPassword = "[a-zA-z0-9@!?_]{5,}".toRegex()
                    when {
                        (isUsername && patternUsername.matches(username).not()) -> {
                            Toast.makeText(
                                context,
                                "Enter proper user name",
                                Toast.LENGTH_LONG
                            ).show()
                            binding.btnLogin.isEnabled = false
                        }
                        (isUsername.not() && patternPassword.matches(password).not()) -> {
                            Toast.makeText(
                                context,
                                "Password not meeting the criteria",
                                Toast.LENGTH_LONG
                            ).show()
                            binding.btnLogin.isEnabled = false
                        }
                        else -> {
                            binding.btnLogin.isEnabled = (username.isEmpty() || password.isEmpty()).not()
                        }
                    }
                }
            })
        }

        binding.etUsername.addTextChangedListener(textWatcherUsername(true))
        binding.etPassword.addTextChangedListener(textWatcherUsername(false))
    }
}