package com.carecrafter.body.features

import android.app.Notification
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupWithNavController
import com.carecrafter.R
import com.carecrafter.databinding.ActivityWaterIntakeBinding
import com.google.android.material.navigation.NavigationView

class WaterIntakeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWaterIntakeBinding
    private lateinit var navController: NavController
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navigationView: NavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWaterIntakeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navigationView = findViewById(R.id.navigationView)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController
        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView.setupWithNavController(navController)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav)
        drawerLayout.addDrawerListener(toggle)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toggle.syncState()

        navController.addOnDestinationChangedListener {_, destination, _ ->
            if(destination.id == R.id.homeFragment3){
                toolbar.visibility = View.VISIBLE
                navigationView.visibility = View.VISIBLE
                binding.notification.visibility = View.VISIBLE
                toolbar.title = ""
            }
            else if (destination.id == R.id.statisticFragment){
                toolbar.visibility = View.GONE
                navigationView.visibility = View.GONE
                binding.notification.visibility = View.GONE

            }
            else if (destination.id == R.id.reminderFragment){
                toolbar.visibility = View.GONE
                navigationView.visibility = View.GONE
                binding.notification.visibility = View.GONE
            }
            else if (destination.id == R.id.settingFragment){
                toolbar.visibility = View.GONE
                navigationView.visibility = View.GONE
                binding.notification.visibility = View.GONE
            }

        }
    }
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.fragmentContainerView)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()

    }
    override fun onBackPressed() {
        super.onBackPressed()
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START)
        }else{
            onBackPressedDispatcher.onBackPressed()
        }
    }
}