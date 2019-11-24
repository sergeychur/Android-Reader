package ru.tp_project.androidreader

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val host: NavHostFragment = supportFragmentManager
            .findFragmentById(R.id.navFragment) as NavHostFragment? ?: return
        navController = host.navController

        val sideBar = findViewById<NavigationView>(R.id.nav_view)
        sideBar?.setupWithNavController(navController)

        appBarConfiguration = AppBarConfiguration(
            navController.graph
            , drawerLayout = drawer_layout
        ) // для бокового меню
        val toolBar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolBar)
        toolBar.setupWithNavController(navController, appBarConfiguration)
    }

    fun onAddTaskBtnClick(view: View) {
        navController.navigate(R.id.newTaskFragment)
    }
}
