package ru.tp_project.androidreader.view_models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseUser
import ru.tp_project.androidreader.model.repos.UserRepository


class AuthViewModel(application: Application?) : AndroidViewModel(application!!) {
    private val userRepository: UserRepository = UserRepository()
    var authenticatedUserLiveData = MutableLiveData<FirebaseUser>()
        .apply { value = null }


    fun signInWithGoogle(googleAuthCredential: AuthCredential) {
        userRepository.firebaseSignInWithGoogle(googleAuthCredential) {user: FirebaseUser? ->
            authenticatedUserLiveData.postValue(user)
        }
    }

    fun signOut() {
        userRepository.signOut()
        authenticatedUserLiveData.postValue(null)
    }
}