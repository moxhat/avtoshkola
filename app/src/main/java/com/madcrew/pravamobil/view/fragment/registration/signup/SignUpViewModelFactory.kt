package com.madcrew.pravamobil.view.fragment.registration.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.madcrew.pravamobil.domain.Repository
import com.madcrew.pravamobil.view.fragment.registration.school.SchoolViewModel

class SignUpViewModelFactory (private val repository: Repository): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SignUpViewModel(repository) as T
    }
}