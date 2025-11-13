package com.easyfield.activities



import android.content.Intent
import android.os.Bundle
import com.easyfield.base.BaseActivity
import com.easyfield.databinding.ActivityHelpBinding
import com.easyfield.utils.AppConstants
import com.easyfield.utils.PreferenceHelper
import com.getkeepsafe.taptargetview.TapTarget
import com.getkeepsafe.taptargetview.TapTargetSequence


class HelpActivity : BaseActivity() {

    private lateinit var binding: ActivityHelpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHelpBinding.inflate(layoutInflater)
        setContentView(binding.root)





        val taptarget= TapTargetSequence(this)
            .targets(
                TapTarget.forView(binding.ivNotification, "Notifications","Stay updated with all important alerts and announcements here.").targetRadius(100),
                TapTarget.forView(binding.ivProfile, "Profile", "Manage your personal details and account settings in one place."),

                TapTarget.forView(
                    binding.btnPunchIn,
                    "Punch In",
                    "Easily mark your attendance and start your workday with a tap."
                ).targetRadius(150).transparentTarget(true),
                TapTarget.forView(binding.llExpense, "Expense", "Track and manage your pending expenses effortlessly.")
                    .targetRadius(100).transparentTarget(true),
                TapTarget.forView(binding.llVisit, "Visit", "Check your scheduled visits for today and stay organized.")
                    .targetRadius(100).transparentTarget(true),
                TapTarget.forView(binding.llKmTravelled, "KM Travelled", "Monitor the total distance you’ve traveled for work-related tasks.")
                    .targetRadius(100).transparentTarget(true),
                TapTarget.forView(binding.llWorkingDays, "Working Days", "View your attendance summary and track your total working days.")
                    .targetRadius(100).transparentTarget(true),

                TapTarget.forView(binding.ivHome, "Home", "Return to the dashboard anytime for a quick overview."),
                TapTarget.forView(binding.ivMenu, "Menu", "Access all app features from here."),
                TapTarget.forView(binding.ivExpense, "Expense", "Quickly manage and submit your expenses with ease."),
                TapTarget.forView(binding.ivAttendance, "Attendance", "Review your attendance records and stay on top of your work hours."),
            )

//            .dimColor(R.color.green)
//            .outerCircleColor(R.color.green)
//            .targetCircleColor(R.color.white)
//            .textColor(R.color.white)


            .listener(object : TapTargetSequence.Listener {
                // This listener will tell us when interesting(tm) events happen in regards
                // to the sequence
                override fun onSequenceFinish() {
                    // Yay

                    PreferenceHelper[AppConstants.PREF_KEY_IS_HELP_SCREEN] = true

                    startActivity(Intent(this@HelpActivity, HomeActivity::class.java))
                    finish()


                }

                override fun onSequenceStep(lastTarget: TapTarget, targetClicked: Boolean) {
                    // Perform action for the current target

                }

                override fun onSequenceCanceled(lastTarget: TapTarget) {

                    PreferenceHelper[AppConstants.PREF_KEY_IS_HELP_SCREEN] = true

                    startActivity(Intent(this@HelpActivity, HomeActivity::class.java))
                    finish()

                }
            })

        taptarget.start()

    }


}