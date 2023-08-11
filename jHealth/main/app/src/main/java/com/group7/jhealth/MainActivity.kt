package com.group7.jhealth

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.group7.jhealth.database.*
import com.group7.jhealth.dialogs.AddCalorieDialog
import com.group7.jhealth.dialogs.AddWorkoutDialog
import com.group7.jhealth.dialogs.DrinkCupSizeDialog
import com.group7.jhealth.fragments.*
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmResults
import io.realm.kotlin.createObject
import io.realm.kotlin.where
import java.util.*
import kotlin.collections.ArrayList

/**
 * Main Activity class
 * menu & navigation to all the fragments this app has to offer
 * login form and properties
 * database connection
 * @constructor Creates an empty group.
 *
 */
class MainActivity : AppCompatActivity(), HomeFragment.HomeFragmentListener,
    DrinkCupSizeDialog.DrinkCupSizeDialogListener,
    OnIntakeLongClickListener, WaterTrackerFragment.WaterTrackerFragmentListener,
    LoginFormFragment.LoginFormFragmentListener, RecordEntryFragment.RecordEntryFragmentListener,
    WorkoutDetail.WorkoutDetailFragmentListener, AddWorkoutDialog.AddWorkoutDialogListener,
    CalorieCounterFragment.CalorieCounterFragmentListener, AddCalorieDialog.AddCalorieDialogListener,
    SleepMonitoringFragment.SleepMonitoringFragmentListener {

    private lateinit var preferences: SharedPreferences
    private lateinit var preferencesEditor: SharedPreferences.Editor
    private lateinit var realm: Realm
    private var isInMenu = false

    /**
     * Perform initialization of all fragments.
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.
     *     <b><i>Note: Otherwise it is null.</i></b>
     *
     * @see #onStart
     * @see #onSaveInstanceState
     * @see #onRestoreInstanceState
     * @see #onPostCreate
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        preferences = this.getSharedPreferences(SHARED_PREF_FILE, MODE_PRIVATE)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        //File(this.filesDir.path).deleteRecursively()
        Realm.init(this)
        val config = RealmConfiguration.Builder().name(REALM_CONFIG_FILE_NAME).build()
        Realm.setDefaultConfiguration(config)
        realm = Realm.getDefaultInstance()

        navView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> {
                    val bundle = bundleOf("eben" to "occcc")
                    navController.navigate(R.id.action_global_navigate_to_home_fragment)
                }
                R.id.nav_diet_monitoring -> {
                    val arraylistWeightProgress: ArrayList<WeightProgress> = ArrayList(realm.where<WeightProgress>().findAll())
                    val arraylistCalorieIntake: ArrayList<CalorieIntake> = ArrayList(realm.where<CalorieIntake>().findAll())
                    val bundle = bundleOf(
                        KEY_BUNDLE_WEIGHT_HISTORY to arraylistWeightProgress,
                        KEY_BUNDLE_CALORIE_HISTORY to arraylistCalorieIntake
                    )
                    navController.navigate(R.id.action_global_navigate_to_diet_monitoring_fragment, bundle)
                }
                R.id.nav_sleep_monitoring -> {
                    val sleepData = realm.where<SleepData>().findFirst()
                    val bundle = bundleOf(KEY_BUNDLE_SLEEP_DATA to sleepData)
                    navController.navigate(R.id.action_global_navigate_to_sleep_monitoring_fragment, bundle)
                }
                R.id.nav_water_tracker -> {
                    val arraylist: ArrayList<WaterIntake> = ArrayList(realm.where<WaterIntake>().findAll())
                    val bundle = bundleOf(
                        KEY_BUNDLE_INTAKE_HISTORY to arraylist,
                        KEY_BUNDLE_WATER_INTAKE_TARGET to calculateWaterConsumption().toInt()
                    )
                    navController.navigate(R.id.action_global_navigate_to_water_tracker_fragment, bundle)
                }
                R.id.nav_workout_plan -> {
                    navController.navigate(R.id.action_global_navigate_to_workout_plan_fragment)
                }
            }
            true
        }


        if (preferences.getBoolean(KEY_PREF_IS_FIRST_LAUNCH, true)) {
            preferencesEditor = preferences.edit()
            preferencesEditor.putBoolean(KEY_PREF_IS_FIRST_LAUNCH, false)
            preferencesEditor.apply()
            findNavController(R.id.nav_host_fragment).navigate(R.id.action_global_navigate_to_login_form_fragment)
        }
        triggerHourlyNotification()
    }

    /**
     * Trigger hourly notifications.
     *
     */
    private fun triggerHourlyNotification() {
        /*
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        calendar[Calendar.HOUR_OF_DAY] = 6
        calendar[Calendar.MINUTE] = 14
        calendar[Calendar.SECOND] = 1
        */
        val notifyIntent = Intent(this, BroadcastReceiver::class.java)
        val pendingIntent =
            PendingIntent.getBroadcast(this, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        val alarmManager = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP, /*calendar.timeInMillis*/ System.currentTimeMillis(),
            AlarmManager.INTERVAL_HOUR, pendingIntent
        )
    }

    /**
     *Inflate the menu
     *  this adds items to the action bar if it is present.
     * @param menu The options menu in which you place your items.
     * @return You must return true for the menu to be displayed
     *         if you return false it will not be shown.
     */
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    /**
     * based on the option the user clicks -> redirects you to that page
     * @param clickedItemId represents the ID of the item
     * options to choose from:
     * Diet Monitoring
     * Sleep Monitoring
     * Water Tracking
     * Workout Planning
     */
    override fun onClickListener(clickedItemId: Int) {
        val navController = findNavController(R.id.nav_host_fragment)

        when (clickedItemId) {
            R.id.dietMonitoringButton -> {
                val arraylistWeightProgress: ArrayList<WeightProgress> = ArrayList(realm.where<WeightProgress>().findAll())
                val arraylistCalorieIntake: ArrayList<CalorieIntake> = ArrayList(realm.where<CalorieIntake>().findAll())
                val bundle = bundleOf(
                    KEY_BUNDLE_WEIGHT_HISTORY to arraylistWeightProgress,
                    KEY_BUNDLE_CALORIE_HISTORY to arraylistCalorieIntake
                )
                navController.navigate(R.id.action_global_navigate_to_diet_monitoring_fragment, bundle)
            }
            R.id.sleepMonitoringButton -> {
                val sleepData = realm.where<SleepData>().findFirst()
                val bundle = bundleOf(KEY_BUNDLE_SLEEP_DATA to sleepData)
                navController.navigate(R.id.action_global_navigate_to_sleep_monitoring_fragment, bundle)
            }
            R.id.waterTrackerButton -> {
                val arraylist: ArrayList<WaterIntake> = ArrayList(realm.where<WaterIntake>().findAll())
                val bundle = bundleOf(
                    KEY_BUNDLE_INTAKE_HISTORY to arraylist,
                    KEY_BUNDLE_WATER_INTAKE_TARGET to calculateWaterConsumption().toInt()
                )
                navController.navigate(R.id.action_global_navigate_to_water_tracker_fragment, bundle)
            }
            R.id.workoutPlanButton -> {
                navController.navigate(R.id.action_global_navigate_to_workout_plan_fragment)
            }
        }
    }

    /**
     * based on the ID of the item chosen
     * @param item represents the menu options the user can choose from:
     * settings(preferences) and update user info (login form)
     * @return  boolean Return false to allow normal menu processing to proceed, true to consume it here.
     *  @see #onCreateOptionsMenu
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)

        when (item.itemId) {
            R.id.action_update_user_info -> {
                navController.navigate(R.id.action_global_navigate_to_login_form_fragment)
            }
            R.id.action_statistics -> {
                val waterIntakeList: ArrayList<WaterIntake> = ArrayList(realm.where<WaterIntake>().findAll())
                val calorieIntakeList: ArrayList<CalorieIntake> = ArrayList(realm.where<CalorieIntake>().findAll())
                val weightProgress: ArrayList<WeightProgress> = ArrayList(realm.where<WeightProgress>().findAll())
                val bundle = bundleOf(
                    KEY_BUNDLE_INTAKE_HISTORY to waterIntakeList,
                    KEY_BUNDLE_CALORIE_HISTORY to calorieIntakeList,
                    KEY_BUNDLE_WEIGHT_HISTORY to weightProgress,
                    KEY_BUNDLE_AVERAGE_WEIGHT_LIFTED to calculateAverageWeightLifted(),
                    KEY_BUNDLE_SLEEP_DATA to realm.where<SleepData>().findFirst()
                )
                navController.navigate(R.id.action_global_navigate_to_stats_fragment, bundle)
            }
        }
        isInMenu = true
        return super.onOptionsItemSelected(item)
    }

    /**
     *takes in the cup size that is chosen
     *@param chosenSize cup size
     */
    override fun drinkCupSizeDialogListener(chosenSize: Int) {
        try {

        realm.beginTransaction()
        val userInfo = realm.where<UserInfo>().findFirst()
        userInfo?.drinkCupSize = chosenSize
        realm.commitTransaction()}
        catch (err:Exception)
        {err.printStackTrace()}
    }

    /**
     * action when button for water intake clicked
     */
    override fun onLongClickWaterIntakeRecyclerViewItem(intake: WaterIntake) {
        Log.e("Long clicked", intake.intakeAmount.toString() + " " + intake.time)
    }

    /**
     * action when button for adding a cup is clicked
     */
    override fun onAddDrinkingCupButtonClicked() {
        DrinkCupSizeDialog().show(supportFragmentManager, "")
    }

    /**
     * updates the Database with the selected water intake
     */
    override fun addWaterIntakeToDatabase() {
        try {
            realm.beginTransaction()
            val waterIntake: WaterIntake =
                realm.createObject<WaterIntake>((realm.where<WaterIntake>().findAll().size) + 1)
            waterIntake.intakeAmount = realm.where<UserInfo>().findFirst()?.drinkCupSize!!
            waterIntake.time = Calendar.getInstance().time
            waterIntake.iconId = getCupIcon(waterIntake.intakeAmount)
            realm.commitTransaction()

            val arraylist: ArrayList<WaterIntake> = ArrayList(realm.where<WaterIntake>().findAll())
            val bundle = bundleOf(KEY_BUNDLE_INTAKE_HISTORY to arraylist)
            getFragmentAsObject()!!.arguments = bundle
        } catch (err: Exception) {
            Toast.makeText(applicationContext, getString(R.string.null_database_notification), Toast.LENGTH_SHORT).show()
        }
    }

    override fun clearDatabase() {
        realm.beginTransaction()
        val rows: RealmResults<WaterIntake> = realm.where(WaterIntake::class.java).findAll()
        rows.deleteAllFromRealm()
        realm.commitTransaction()
    }

    /**
     * Chooses the correct cup icon based on user input
     * @param intakeAmount selected amount of water intake
     * @return the appropriate cup size based on the intake amount
     */
    private fun getCupIcon(intakeAmount: Int): Int {
        return when (intakeAmount) {
            50, 100, 150 -> R.drawable.ic_tea_cup
            200, 250, 300 -> R.drawable.ic_water_glass
            350, 400, 450 -> R.drawable.ic_small_water_bottle
            500, 550, 600 -> R.drawable.ic_water_bottle
            650, 700, 750, 800, 1000, 1500 -> R.drawable.ic_large_water_bottle
            else -> R.drawable.ic_custom_cup
        }
    }

    /**
     * navigation between fragments (Menu, Home)
     * using the Back button
     */
    override fun onBackPressed() {
        if (isInMenu) {
            isInMenu = false
            findNavController(R.id.nav_host_fragment).navigate(R.id.action_global_navigate_to_home_fragment)
        } else
            super.onBackPressed()
    }

    /**
     * Called when the fragment is no longer in use.  This is called
     * after {@link #onStop()} and before {@link #onDetach()}.
     */
    override fun onDestroy() {
        realm.close()
        super.onDestroy()
    }

    /**
     * updates Realm with user's input
     * name
     * age
     * weight
     * wakeupTime
     * sleepTime
     * workoutDuration
     */
    override fun updateUserInfoDatabase(
        name: String,
        age: Int,
        gender: String,
        weight: Int,
        wakeUpTime: Date,
        sleepTime: Date,
        workoutDuration: Int,
        isTakingMed: Boolean
    ) {
        try {
            realm.beginTransaction()
        } catch (err: Exception) {
            err.printStackTrace()
        }

        val user: UserInfo? = if (realm.where<UserInfo>().findFirst() != null) {
            realm.where<UserInfo>().findFirst()
        } else {
            realm.createObject<UserInfo>((realm.where<UserInfo>().findAll().size) + 1)
            realm.where<UserInfo>().findFirst()
        }
        user?.name = name
        user?.age = age
        user?.gender = gender
        user?.weight = weight
        user?.wakeUpTime = wakeUpTime
        user?.sleepTime = sleepTime
        user?.workoutDuration = workoutDuration
        user?.isTakingMed = isTakingMed

        preferencesEditor = preferences.edit()
        preferencesEditor.putBoolean(KEY_PREF_IS_USR_TAKING_MED, isTakingMed)
        preferencesEditor.apply()
        realm.commitTransaction()
    }

    /**
     * updates user interface of LoginFormFragment
     */
    override fun updateUserInfoUI() {
        displayUserInfo()
    }

    private fun getFragmentAsObject(): Fragment? {
        val navHost = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
        navHost?.let { navFragment ->
            navFragment.childFragmentManager.primaryNavigationFragment?.let { fragment ->
                return fragment
            }
        }
        return null
    }

    /**
     * gathers user data from Realm
     * name
     * age
     * weight
     * wakeupTime
     * sleepTime
     */
    private fun displayUserInfo() {
        if (realm.where<UserInfo>().findFirst() != null) {
            val user: UserInfo? = realm.where<UserInfo>().findFirst()

            val bundle = bundleOf(KEY_BUNDLE_USER_INFO to user)
            getFragmentAsObject()!!.arguments = bundle
        }
    }

    /**
     * calculates daily water consumption target
     */
    private fun calculateWaterConsumption(): Double {
        var consumptionInLitre = 0.0

        if (realm.where<UserInfo>().findFirst() != null) {
            val user: UserInfo? = realm.where<UserInfo>().findFirst()

            consumptionInLitre =
                (user!!.weight / 23) + (WATER_NEED_FOR_ONE_MINUTE_EXERCISE * user.workoutDuration)

            if (user.gender == KEY_GENDER_MALE)
                consumptionInLitre += 1    //Men need more water than woman.
        }
        /*Age is not that important*/
        return consumptionInLitre
    }

    override fun addWeightProgressToDatabase(weight: Int) {
        try {
            realm.beginTransaction()
            val weightProgress: WeightProgress =
                realm.createObject<WeightProgress>((realm.where<WeightProgress>().findAll().size) + 1)

            weightProgress.time = Calendar.getInstance().time
            weightProgress.weightAmount = weight
            realm.commitTransaction()

            val arraylist: ArrayList<WeightProgress> = ArrayList(realm.where<WeightProgress>().findAll())
            val bundle = bundleOf(KEY_BUNDLE_WEIGHT_HISTORY to arraylist)
            getFragmentAsObject()!!.arguments = bundle

        } catch (err: Exception) {
            Toast.makeText(applicationContext, getString(R.string.null_database_notification), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onAddWorkoutButtonClicked() {
        AddWorkoutDialog().show(supportFragmentManager, "")
    }

    override fun addWorkoutDialogListener(reps: Int, weight: Int) {
        try {
            realm.beginTransaction()
            val weightProgress: WorkoutInfo =
                realm.createObject<WorkoutInfo>((realm.where<WorkoutInfo>().findAll().size) + 1)

            weightProgress.numberOfReps = reps
            weightProgress.weightAmount = weight
            realm.commitTransaction()

        } catch (err: Exception) {
            Toast.makeText(applicationContext, getString(R.string.null_database_notification), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onAddCalorieButtonClicked() {
        AddCalorieDialog().show(supportFragmentManager, "")
    }

    override fun addCalorieDialogListener(foodName: String, calorie: Int) {
        try {
            realm.beginTransaction()
            val calorieIntake: CalorieIntake =
                realm.createObject<CalorieIntake>((realm.where<CalorieIntake>().findAll().size) + 1)

            calorieIntake.time = Calendar.getInstance().time
            calorieIntake.foodName = foodName
            calorieIntake.calorie = calorie
            realm.commitTransaction()

            val arraylist: ArrayList<CalorieIntake> = ArrayList(realm.where<CalorieIntake>().findAll())

            val bundle = bundleOf(KEY_BUNDLE_CALORIE_HISTORY to arraylist)
            getFragmentAsObject()!!.arguments = bundle

        } catch (err: Exception) {
            Toast.makeText(applicationContext, getString(R.string.null_database_notification), Toast.LENGTH_SHORT).show()
        }
    }

    private fun calculateAverageWeightLifted(): Double {
        var sumOfWeights = 0
        val workoutInfo: ArrayList<WorkoutInfo> = ArrayList(realm.where<WorkoutInfo>().findAll())

        for (i in workoutInfo.indices) {
            sumOfWeights += workoutInfo[i].weightAmount
        }
        return sumOfWeights / workoutInfo.size.toDouble()
    }

    override fun updateDatabase(happyCtr: Int, mehCtr: Int, sadCtr: Int) {
        try {
            realm.beginTransaction()
        } catch (err: Exception) {
            err.printStackTrace()
        }

        val user: SleepData? = if (realm.where<SleepData>().findFirst() != null) {
            realm.where<SleepData>().findFirst()
        } else {
            realm.createObject<SleepData>((realm.where<SleepData>().findAll().size) + 1)
            realm.where<SleepData>().findFirst()
        }
        user?.happySleepCtr = happyCtr
        user?.mehSleepCtr = mehCtr
        user?.sadSleepButton = sadCtr
        realm.commitTransaction()
    }
}