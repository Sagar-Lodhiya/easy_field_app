package com.easyfield.activities

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.PowerManager
import android.provider.Settings
import android.util.Log
import android.view.View
import androidx.navigation.findNavController
import com.easyfield.R
import com.easyfield.base.BaseActivity
import com.easyfield.databinding.ActivityHomeBinding
import com.easyfield.utils.extension.alert
import com.easyfield.utils.extension.positiveButton
import com.google.firebase.Firebase
import com.google.firebase.messaging.messaging
import java.util.Locale


class HomeActivity : BaseActivity() {

    private lateinit var binding: ActivityHomeBinding

    fun requestAutoStartPermission(context: Context) {
        var intent = Intent()
        val manufacturer = Build.MANUFACTURER.lowercase(Locale.getDefault())



        if (manufacturer.contains("xiaomi")) {
            intent.setComponent(
                ComponentName(
                    "com.miui.securitycenter",
                    "com.miui.permcenter.autostart.AutoStartManagementActivity"
                )
            )
        } else if (manufacturer.contains("huawei")) {
            intent.setComponent(
                ComponentName(
                    "com.huawei.systemmanager",
                    "com.huawei.systemmanager.optimize.process.ProtectActivity"
                )
            )
        } else if (manufacturer.contains("oppo")) {





            intent.component = ComponentName(
                "com.coloros.safecenter",
                "com.coloros.safecenter.permission.startup.StartupAppListActivity"
            )
//            intent.setComponent(
//                ComponentName(
//                    "com.coloros.safecenter",
//                    "com.coloros.safecenter.permission.startup.StartupAppListActivity"
//                )
//            )
        } else if (manufacturer.contains("vivo") ) {
            intent.setComponent(
                ComponentName(
                    "com.vivo.permissionmanager",
                    "com.vivo.permissionmanager.activity.BgStartUpManagerActivity"
                )
            )
        } else {
            intent = Intent(Settings.ACTION_SETTINGS)
        }

        try {
            context.startActivity(intent)

        } catch (e: Exception) {
            Log.e("AutoStart", "Auto-start setting not found for $manufacturer")
            val intents = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            intents.data = Uri.parse("package:${context.packageName}")
            try {
                context.startActivity(intents)
            } catch (e: Exception) {
                Log.e("AutoStart", "Failed to open app settings", e)
            }
        }
    }

    fun requestBatteryPermission(){
        val intent = Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS)
        intent.data = Uri.parse("package:" + packageName)
        startActivity(intent)
    }

    fun isBatteryOptimizationIgnored(context: Context): Boolean {
        val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
        return powerManager.isIgnoringBatteryOptimizations(context.packageName)
    }

    fun isBatterySaverOn(context: Context): Boolean {
        val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
        return powerManager.isPowerSaveMode
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)


        initData()
        clickevents()

//        subscribetopic() //MP Remove comment when add firebase

//        if(isBatteryOptimizationIgnored(this)){
//
//        }
//        else{
//            alert {
//                setMessage("Please allow battery optimization for this app. for better Location Accuracy")
//                positiveButton("Allow") {
//                    requestBatteryPermission()
//                }
//            }
//        }

        if(isBatterySaverOn(this)){
            alert {
                setMessage("Please turn on battery saver for this app. for better Location Accuracy")
                positiveButton("Allow") {
                    val intent = Intent(Settings.ACTION_BATTERY_SAVER_SETTINGS)
                    startActivity(intent)
                }
            }
        }

        val pm = getSystemService(Context.POWER_SERVICE) as PowerManager
        if (!pm.isIgnoringBatteryOptimizations(packageName)) {
            val intent = Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS)
            intent.data = Uri.parse("package:$packageName")
            startActivity(intent)
        }



//        val intent = Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS)
//        startActivity(intent)

        if(intent.hasExtra("type")) {

            val type = intent.getStringExtra("type")



            if(type.equals("home")){


            }
            else if(type.equals("expense")){


                val action_id = intent.getIntExtra("action_id", 0)
                val bundle = Bundle()
                val product_id=action_id
                bundle.putString("expenseid", product_id.toString())
                findNavController(R.id.nav_host_fragment_content_home).navigate(R.id.expenseDetailFragment,bundle)
            }
            else if(type.equals("visit")){

                val action_id = intent.getIntExtra("action_id", 0)
                val bundle = Bundle()
                val product_id=action_id
                bundle.putString("visitid", product_id.toString())
                findNavController(R.id.nav_host_fragment_content_home).navigate(R.id.visitDetailFragment,bundle)
            }

            else if(type.equals("payment")){

                val action_id = intent.getIntExtra("action_id", 0)
                val bundle = Bundle()
                val product_id=action_id
                bundle.putString("paymentid", product_id.toString())
                findNavController(R.id.nav_host_fragment_content_home).navigate(R.id.paymentDetailFragment,bundle)
            }

        }

    }

    fun isGpsEnabled(context: Context): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }
    override fun onResume() {
        super.onResume()

        if(!isGpsEnabled(this)){
            alert {
                setMessage("Please turn on GPS for better location accuracy.")
                positiveButton(getString(R.string.str_exit)) {
                    finish()
                }
            }
        }
    }

    fun clickevents(){

        binding.llHome.setOnClickListener {
            if(binding.llHome.tag.equals("0")){


                val navController=findNavController(R.id.nav_host_fragment_content_home)

                val graph = navController.navInflater.inflate(R.navigation.home_navigation)
                graph.setStartDestination(R.id.dashboardFragment)
                navController.graph = graph

                setData(0)
            }
        }


        binding.llMenu.setOnClickListener {

            if(binding.llMenu.tag.equals("0")){


                val navController=findNavController(R.id.nav_host_fragment_content_home)

                val graph = navController.navInflater.inflate(R.navigation.home_navigation)
                graph.setStartDestination(R.id.menusFragment)
                navController.graph = graph

                setData(1)
            }

        }

        binding.llExpenses.setOnClickListener {

            if(binding.llExpenses.tag.equals("0")){

                val navController=findNavController(R.id.nav_host_fragment_content_home)

                val graph = navController.navInflater.inflate(R.navigation.home_navigation)
                graph.setStartDestination(R.id.expenseFragment)
                navController.graph = graph
                setData(2)
            }
        }



        binding.llAttedance.setOnClickListener {
            if(binding.llAttedance.tag.equals("0")){

                val navController=findNavController(R.id.nav_host_fragment_content_home)

                val graph = navController.navInflater.inflate(R.navigation.home_navigation)
                graph.setStartDestination(R.id.attendanceFragment)
                navController.graph = graph
                setData(4)
            }
        }

    }
    fun setData(pos:Int){


        if(pos==0){
            binding.llHome.tag="1"
            binding.llMenu.tag="0"

            binding.llExpenses.tag="0"
            binding.llAttedance.tag="0"


            binding.ivHome.setImageResource(R.drawable.ic_home_selected_menu)
            binding.ivMenu.setImageResource(R.drawable.ic_menu_menus)
            binding.ivExpense.setImageResource(R.drawable.ic_expense_menu)
            binding.ivAttendance.setImageResource(R.drawable.ic_attendance_menu)

            binding.txtHome.setTextColor(resources.getColor(R.color.green))
            binding.txtMenu.setTextColor(resources.getColor(R.color.menu_text_color))
            binding.txtExpense.setTextColor(resources.getColor(R.color.menu_text_color))
            binding.txtAttendance.setTextColor(resources.getColor(R.color.menu_text_color))




        }
        else if(pos==1)
        {
            binding.llHome.tag="0"
            binding.llMenu.tag="1"

            binding.llExpenses.tag="0"
            binding.llAttedance.tag="0"


            binding.ivHome.setImageResource(R.drawable.ic_home_menu)
            binding.ivMenu.setImageResource(R.drawable.ic_selected_menus)
            binding.ivExpense.setImageResource(R.drawable.ic_expense_menu)
            binding.ivAttendance.setImageResource(R.drawable.ic_attendance_menu)

            binding.txtHome.setTextColor(resources.getColor(R.color.menu_text_color))
            binding.txtMenu.setTextColor(resources.getColor(R.color.green))
            binding.txtExpense.setTextColor(resources.getColor(R.color.menu_text_color))
            binding.txtAttendance.setTextColor(resources.getColor(R.color.menu_text_color))


        }
        else if(pos==2){
            binding.llHome.tag="0"
            binding.llMenu.tag="0"

            binding.llExpenses.tag="1"
            binding.llAttedance.tag="0"

            binding.ivHome.setImageResource(R.drawable.ic_home_menu)
            binding.ivMenu.setImageResource(R.drawable.ic_menu_menus)
            binding.ivExpense.setImageResource(R.drawable.ic_selected_expense_menu)
            binding.ivAttendance.setImageResource(R.drawable.ic_attendance_menu)

            binding.txtHome.setTextColor(resources.getColor(R.color.menu_text_color))
            binding.txtMenu.setTextColor(resources.getColor(R.color.menu_text_color))
            binding.txtExpense.setTextColor(resources.getColor(R.color.green))
            binding.txtAttendance.setTextColor(resources.getColor(R.color.menu_text_color))

        }
        else if(pos==3){
            binding.llHome.tag="0"
            binding.llMenu.tag="0"

            binding.llExpenses.tag="0"
            binding.llAttedance.tag="0"


            binding.ivHome.setImageResource(R.drawable.ic_home_menu)
            binding.ivMenu.setImageResource(R.drawable.ic_menu_menus)
            binding.ivExpense.setImageResource(R.drawable.ic_expense_menu)
            binding.ivAttendance.setImageResource(R.drawable.ic_attendance_menu)

            binding.txtHome.setTextColor(resources.getColor(R.color.menu_text_color))
            binding.txtMenu.setTextColor(resources.getColor(R.color.menu_text_color))
            binding.txtExpense.setTextColor(resources.getColor(R.color.menu_text_color))
            binding.txtAttendance.setTextColor(resources.getColor(R.color.menu_text_color))
        }
        else if(pos==4){
            binding.llHome.tag="0"
            binding.llMenu.tag="0"

            binding.llExpenses.tag="0"

            binding.llAttedance.tag="1"

            binding.ivHome.setImageResource(R.drawable.ic_home_menu)
            binding.ivMenu.setImageResource(R.drawable.ic_menu_menus)
            binding.ivExpense.setImageResource(R.drawable.ic_expense_menu)
            binding.ivAttendance.setImageResource(R.drawable.ic_selected_attendance)

            binding.txtHome.setTextColor(resources.getColor(R.color.menu_text_color))
            binding.txtMenu.setTextColor(resources.getColor(R.color.menu_text_color))
            binding.txtExpense.setTextColor(resources.getColor(R.color.menu_text_color))
            binding.txtAttendance.setTextColor(resources.getColor(R.color.green))
        }



    }
    fun initData(){
        val navController = findNavController(R.id.nav_host_fragment_content_home)


        navController.addOnDestinationChangedListener { _, destination, _ ->

            when (destination.id) {

                R.id.menusFragment ,
                R.id.expenseFragment,
                R.id.attendanceFragment,
                R.id.dashboardFragment -> {
                    binding.llBottom.visibility= View.VISIBLE

                }

                R.id.cmsFragment,
                R.id.profileFragment,
                R.id.editProfileFragment,
                R.id.punchInFragment,
                R.id.addExpenseFragment,
                R.id.addLeaveFragment,
                R.id.leaveFragment,
                R.id.expenseAllFragment,
                R.id.notificationFragment,
                R.id.visitFragment ,
                R.id.addVisitFragment,
                R.id.paymentFragment,
                R.id.addPaymentFragment,
                R.id.partyFragment,
                R.id.addPartyFragment ,
                R.id.visitDetailFragment,
                R.id.partyDetailFragment,
                R.id.paymentDetailFragment,
                R.id.expenseDetailFragment,

                R.id.attendanceViewFragment,
                R.id.addVisitFragment->{
                    binding.llBottom.visibility= View.GONE
                }

            }
        }
    }

    fun subscribetopic(){

        Firebase.messaging.subscribeToTopic("AxisSeeds")
            .addOnCompleteListener { tasks ->
                var msg = "Subscribed"
                if (!tasks.isSuccessful) {
                    msg = "Subscribe failed"
                }
                Log.d("vishalnotification", msg)
            }

//        requestAutoStartPermission(this);
    }

    var doubleBackToExitPressedOnce: Boolean = false

//    override fun onBackPressed() {
//        if (doubleBackToExitPressedOnce) {
//            super.onBackPressed()
//            return
//        }
//
//        this.doubleBackToExitPressedOnce = true
//        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show()
//
//        Handler(Looper.getMainLooper()).postDelayed(Runnable {
//            doubleBackToExitPressedOnce = false
//        }, 2000)
//    }
}