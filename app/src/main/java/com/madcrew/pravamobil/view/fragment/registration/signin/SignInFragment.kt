package com.madcrew.pravamobil.view.fragment.registration.signin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputLayout
import com.madcrew.pravamobil.R
import com.madcrew.pravamobil.databinding.FragmentSignInBinding
import com.madcrew.pravamobil.domain.BaseUrl.Companion.TOKEN
import com.madcrew.pravamobil.domain.Repository
import com.madcrew.pravamobil.models.requestmodels.ClientAuthorizationRequest
import com.madcrew.pravamobil.utils.*
import com.madcrew.pravamobil.view.activity.progress.ProgressViewModel
import com.madcrew.pravamobil.view.activity.progress.ProgressViewModelFactory
import com.madcrew.pravamobil.view.fragment.progress.checkdata.CheckDataFragment
import com.madcrew.pravamobil.view.fragment.progress.notadult.ClientIsNotAdultFragment
import com.madcrew.pravamobil.view.fragment.registration.enter.EnterFragment
import com.madcrew.pravamobil.view.fragment.registration.greetings.GreetingsFragment


class SignInFragment : Fragment() {

    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val repository = Repository()
        val viewModelFactory = SignInViewModelFactory(repository)
        val mViewModel = ViewModelProvider(this, viewModelFactory).get(SignInViewModel::class.java)

        val mainManager = parentFragmentManager
        val loginText = binding.signinLoginEditText
        val loginField = binding.signinLogin
        val passwordText = binding.signinPasswordEditText
        val passwordField = binding.signinPassword
//        val rememberMe = binding.signinRememberChek

        if (Preferences.getPrefsString("rememberMe",  requireContext()) == "true") {
            loginText.setText(Preferences.getPrefsString("login",  requireContext()))
            passwordText.setText(Preferences.getPrefsString("password", requireContext()))
//            if (isOnline(requireContext())){
//                binding.btSigninEnter.setDisable()
//                mViewModel.clientAuthorization(
//                    ClientAuthorizationRequest(
//                        TOKEN,
//                        loginText.text.toString(),
//                        passwordText.text.toString()
//                    )
//                )
//            } else {
//                noInternet(requireContext())
//            }
        }

        mViewModel.signResponse.observe(viewLifecycleOwner, { response ->
            if (response.isSuccessful) {
                when (response.body()!!.status) {
                    "done" -> {
                        Preferences.setPrefsString("clientId", response.body()!!.client.id, requireContext())
                        Preferences.setPrefsString("schoolId", response.body()!!.client.school_id, requireContext())
                        val progressStatus = response.body()!!.client.appProgress
                        val name = response.body()!!.client.firstName
//                        if(rememberMe.isChecked){
                            Preferences.setPrefsString("rememberMe", "true", requireContext())
                            Preferences.setPrefsString("login", loginText.text.toString().substring(2, 16), requireContext())
                            Preferences.setPrefsString("password", passwordText.text.toString(), requireContext())
//                        }
                        if (response.body()!!.client.adult == "true") {
                            Preferences.setPrefsString("adult", "student", requireContext())
                        } else {
                            Preferences.setPrefsString("adult", "parent", requireContext())
                        }
                        Preferences.setPrefsString("progressStatus", progressStatus, requireContext())
                        nextFragment(mainManager, GreetingsFragment(name, progressStatus))
                    }
                    "password" -> {
                        Preferences.setPrefsString("rememberMe", "false", requireContext())
                        binding.btSigninEnter.setEnable()
                        passwordField.isErrorEnabled = true
                        passwordField.error = resources.getString(R.string.wrong_password)
                    }
                    "notexist" -> {
                        Preferences.setPrefsString("rememberMe", "false", requireContext())
                        binding.btSigninEnter.setEnable()
                        passwordField.isErrorEnabled = true
                        passwordField.error = resources.getString(R.string.accaunt_not_found)
                    }
                }
            } else {
                showServerError(requireContext())
            }
        })

        loginText.doOnTextChanged { _, _, _, _ ->
            if (loginText.length() > 1) loginField.setErrorOff()
            if (loginText.length() == 16) passwordText.requestFocus()
        }

        passwordText.doOnTextChanged { _, _, _, _ ->
            if (passwordText.length() > 1) passwordField.setErrorOff()
            if (passwordText.length() == 8) this.view?.hideKeyboard()
        }

        binding.btSigninBack.setOnClickListener {
            previousFragment(mainManager, EnterFragment())
        }

        binding.btSigninEnter.setOnClickListener {
            if (loginText.length() == 16 || passwordText.length() == 8) {
                if (isOnline(requireContext())){
                    binding.btSigninEnter.setDisable()
                    mViewModel.clientAuthorization(
                        ClientAuthorizationRequest(
                            TOKEN,
                            loginText.text.toString(),
                            passwordText.text.toString()
                        )
                    )
                } else {
                    noInternet(requireContext())
                }
                this.view?.hideKeyboard()
            } else {
                if (loginText.length() < 16) loginField.setErrorOn()
                if (passwordText.length() < 8)passwordField.setErrorOn()
            }
        }
    }
}

private fun previousFragment(mainManager: FragmentManager, fragment: Fragment) {
    val transaction: FragmentTransaction = mainManager.beginTransaction()
    transaction.apply {
        setCustomAnimations(R.anim.slide_right_in, R.anim.slide_right_out)
        replace(R.id.enter_activity_fragment_container, fragment)
        commit()
    }

}

private fun nextFragment(mainManager: FragmentManager, fragment: Fragment) {
    val transaction: FragmentTransaction = mainManager.beginTransaction()
    transaction.apply {
        setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
        replace(R.id.enter_activity_fragment_container, fragment)
        commit()
    }
}

private fun TextInputLayout.setErrorOff() {
    this.error = null
    this.isErrorEnabled = false
}

private fun TextInputLayout.setErrorOn() {
    this.isErrorEnabled = true
    this.error = resources.getString(R.string.name_alert)
}
