package ru.tp_project.androidreader.model.repos

import android.content.Context
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.tp_project.androidreader.R
import ru.tp_project.androidreader.model.AppDb
import ru.tp_project.androidreader.model.data_models.User
import javax.inject.Singleton


@Singleton
class UserRepository {
    private var currentUserID = R.integer.single_user_id
    private val firebaseAuth = FirebaseAuth.getInstance()

    fun getCurrentUserID(context: Context): Int {
        return context.resources.getInteger(currentUserID)
    }

    fun getUserStatistic(context: Context, onResult: (isSuccess: Boolean, user: User?) -> Unit) {
        GlobalScope.launch {
            val user = withContext(Dispatchers.Default) {
                AppDb.getInstance(context).userStatisticDao()
                    .load(context.resources.getInteger(currentUserID))
            }
            onResult(true, user)
        }
    }

    fun firebaseSignInWithGoogle(
        googleAuthCredential: AuthCredential,
        onResult: (user: FirebaseUser?) -> Unit
    ) {
        GlobalScope.launch {
            firebaseAuth.signInWithCredential(googleAuthCredential).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // TODO Im'not sure if we need to do smth with it
                    // val isNew = task.result?.additionalUserInfo?.isNewUser
                    onResult(firebaseAuth.currentUser)
                } else {
                    onResult(null)
                }
            }
        }
    }

    fun signOut() {
        firebaseAuth.signOut()
    }

    fun getCurrentFirebaseUser(): FirebaseUser? {
        return firebaseAuth.currentUser
    }

}