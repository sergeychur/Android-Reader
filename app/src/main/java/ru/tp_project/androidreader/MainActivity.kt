package ru.tp_project.androidreader

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.action_view_userheader.view.*
import kotlinx.android.synthetic.main.activity_main.*
import ru.tp_project.androidreader.view_models.AuthViewModel


class MainActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController
    private lateinit var hostFragment: NavHostFragment

    private lateinit var googleSignInClient: GoogleSignInClient

    private lateinit var authViewModel: AuthViewModel

    private val code = 123

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        hostFragment = supportFragmentManager
            .findFragmentById(R.id.navFragment) as NavHostFragment? ?: return
        navController = hostFragment.navController

        val sideBar = findViewById<NavigationView>(R.id.nav_view)
        sideBar?.setupWithNavController(navController)

        appBarConfiguration = AppBarConfiguration(
            navController.graph
            , drawerLayout = drawer_layout
        ) // для бокового меню

        val toolBar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolBar)
        toolBar.setupWithNavController(navController, appBarConfiguration)

        initAuthViewModel()
        initObserver()
        initGoogleSignInClient()
        workWithPermissions()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account!!)
            } catch (e: ApiException) {
                updateUI(null)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.tollbar_menu, menu)
        menu.findItem(R.id.action_accept).isVisible = false
        menu.findItem(R.id.action_share).isVisible = false
        return true
    }

    private fun initAuthViewModel() {
        authViewModel = ViewModelProviders.of(this).get(AuthViewModel::class.java)
    }

    private fun initGoogleSignInClient() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
    }

    private fun initObserver() {
        authViewModel.authenticatedUserLiveData.observe(this, Observer<FirebaseUser> { user ->
            updateUI(user)
        })
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        authViewModel.signInWithGoogle(credential)
    }

    private fun updateUI(user: FirebaseUser?) {
        val sideBar = findViewById<NavigationView>(R.id.nav_view)

        if (user != null) {
            sideBar.inflateHeaderView(R.layout.action_view_userheader)
            val header = sideBar.getHeaderView(0)
            authViewModel.loadAvatarToView(header.avatar)
            header.nickname.text = user.displayName

            sideBar.menu.findItem(R.id.sign_out).isVisible = true
            sideBar.menu.findItem(R.id.sign_in).isVisible = false
        } else {
            sideBar.removeHeaderView(sideBar.getHeaderView(0))
            sideBar.menu.findItem(R.id.sign_out).isVisible = false
            sideBar.menu.findItem(R.id.sign_in).isVisible = true
        }
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun signOut() {
        authViewModel.signOut()
        googleSignInClient.signOut().addOnCompleteListener(this) {
            updateUI(null)
        }
    }

    fun onClick(item: MenuItem) {
        when (item.itemId) {
            R.id.sign_in -> signIn()
            R.id.sign_out -> signOut()
        }
    }

    private fun getNeededPermissions() : MutableList<String> {
        val info = packageManager.getPackageInfo(packageName, PackageManager.GET_PERMISSIONS)
        val permissions = info.requestedPermissions
        val neededPermissions = mutableListOf<String>()
        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                neededPermissions.add(permission)
            }
        }
        return neededPermissions
    }

    private fun requestPermissions(perms: MutableList<String>) {
        if (perms.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, perms.toTypedArray(), code)
        }
    }

    private fun workWithPermissions() {
        val perms = getNeededPermissions()
        requestPermissions(perms)
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            code -> {
                // If request is cancelled, the result arrays are empty.
                for (grantResult in grantResults) {
                    if (grantResult == PackageManager.PERMISSION_GRANTED) {
                        Log.println(Log.INFO, "kek", "permissions granted")
                        Toast.makeText(
                            this,
                            getText(R.string.permission_ok),
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        Log.println(Log.ERROR, "kek", "permissions denied")
                        Toast.makeText(
                            this,
                            getText(R.string.permission_fail),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
                return
            }

            // Add other 'when' lines to check for other
            // permissions this app might request.
            else -> {
                // Ignore all other requests.
            }
        }
    }

    companion object {
        private const val RC_SIGN_IN = 9001
    }
}
