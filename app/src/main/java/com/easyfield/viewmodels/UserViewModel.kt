package com.easyfield.viewmodels


import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.core.content.FileProvider
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.codeflixweb.callenza.network.Event
import com.codeflixweb.callenza.network.Resource
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.easyfield.R
import com.easyfield.attendance.response.attendancedetail.AttendanceDetailResponse
import com.easyfield.attendance.response.attendancelist.AttendanceListResponse
import com.easyfield.attendance.response.leavelist.LeaveListResponse
import com.easyfield.base.response.commonresponse.CommonResponse
import com.easyfield.base.response.dropdownresponse.DropDownResponse
import com.easyfield.dashboard.response.HomeResponse
import com.easyfield.expense.response.expensedetail.ExpenseDetailResponse
import com.easyfield.expense.response.expenselist.ExpenseListResponse
import com.easyfield.login.response.loginresponse.LoginResponse
import com.easyfield.login.response.loginresponse.User
import com.easyfield.menu.response.MenuResponse
import com.easyfield.notification.response.NotificationListResponse
import com.easyfield.party.request.AddPartyRequest
import com.easyfield.party.response.PartyListResponse
import com.easyfield.party.response.partydetailresponse.PartyDetailResponse
import com.easyfield.payment.request.AddPaymentRequest
import com.easyfield.payment.response.PaymentListResponse
import com.easyfield.payment.response.paymentDetailresponse.PaymentDetailResponse
import com.easyfield.profile.response.ProfileResponse
import com.easyfield.punchin.request.PunchRequest
import com.easyfield.punchin.response.PunchResponse
import com.easyfield.splash.models.settingResponse.SettingResponse
import com.easyfield.utils.AppConstants
import com.easyfield.utils.Kirtiman
import com.easyfield.utils.PreferenceHelper
import com.easyfield.utils.extension.compress
import com.easyfield.utils.extension.errorLog
import com.easyfield.utils.extension.log
import com.easyfield.visit.request.AddVisitRequest
import com.easyfield.visit.response.visitdetail.VisitDetailResponse
import com.easyfield.visit.response.visitlistresponse.VisitListResponse
import com.lezasolutions.callenza.utils.extension.hasInternetConnection
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Response
import java.io.File
import java.io.IOException


class UserViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = UserRepository()

    private val launcher = MutableLiveData<Boolean>()


    private val _authResponse = MutableLiveData<Event<Resource<String>>>()
    val authResponse: LiveData<Event<Resource<String>>> = _authResponse


    private val _settingResponse = MutableLiveData<Event<Resource<SettingResponse>>>()
    val settingResponse: LiveData<Event<Resource<SettingResponse>>> = _settingResponse

    private val _dropdownResponse = MutableLiveData<Event<Resource<DropDownResponse>>>()
    val dropdownResponse: LiveData<Event<Resource<DropDownResponse>>> = _dropdownResponse


    private val _logoutResponse = MutableLiveData<Event<Resource<CommonResponse>>>()
    val logoutResponse: LiveData<Event<Resource<CommonResponse>>> = _logoutResponse


    private val _loginResponse = MutableLiveData<Event<Resource<LoginResponse>>>()
    val loginResponse: LiveData<Event<Resource<LoginResponse>>> = _loginResponse


    private val _profileResponse = MutableLiveData<Event<Resource<ProfileResponse>>>()
    val profileResponse: LiveData<Event<Resource<ProfileResponse>>> = _profileResponse

    private val _homeResponse = MutableLiveData<Event<Resource<HomeResponse>>>()
    val homeResponse: LiveData<Event<Resource<HomeResponse>>> = _homeResponse

    private val _menuResponse = MutableLiveData<Event<Resource<MenuResponse>>>()
    val menuResponse: LiveData<Event<Resource<MenuResponse>>> = _menuResponse

    private val _punchInResponse = MutableLiveData<Event<Resource<PunchResponse>>>()
    val punchInResponse: LiveData<Event<Resource<PunchResponse>>> = _punchInResponse

    private val _trackingResponse = MutableLiveData<Event<Resource<PunchResponse>>>()
    val trackingResponse: LiveData<Event<Resource<PunchResponse>>> = _trackingResponse

    private val _attendanceListResponse = MutableLiveData<Event<Resource<AttendanceListResponse>>>()
    val attendanceListResponse: LiveData<Event<Resource<AttendanceListResponse>>> =
        _attendanceListResponse


    private val _attendanceDetailResponse =
        MutableLiveData<Event<Resource<AttendanceDetailResponse>>>()
    val attendanceDetailResponse: LiveData<Event<Resource<AttendanceDetailResponse>>> =
        _attendanceDetailResponse

    private val _leaveListResponse = MutableLiveData<Event<Resource<LeaveListResponse>>>()
    val leaveListResponse: LiveData<Event<Resource<LeaveListResponse>>> = _leaveListResponse


    private val _expenseListResponse = MutableLiveData<Event<Resource<ExpenseListResponse>>>()
    val expenseListResponse: LiveData<Event<Resource<ExpenseListResponse>>> = _expenseListResponse

    private val _expenseDetailResponse = MutableLiveData<Event<Resource<ExpenseDetailResponse>>>()
    val expenseDetailResponse: LiveData<Event<Resource<ExpenseDetailResponse>>> =
        _expenseDetailResponse


    private val _visitListResponse = MutableLiveData<Event<Resource<VisitListResponse>>>()
    val visitListResponse: LiveData<Event<Resource<VisitListResponse>>> = _visitListResponse

    private val _visitDetailResponse = MutableLiveData<Event<Resource<VisitDetailResponse>>>()
    val visitDetailResponse: LiveData<Event<Resource<VisitDetailResponse>>> = _visitDetailResponse


    private val _paymentListResponse = MutableLiveData<Event<Resource<PaymentListResponse>>>()
    val paymentListResponse: LiveData<Event<Resource<PaymentListResponse>>> = _paymentListResponse


    private val _paymentDetailResponse = MutableLiveData<Event<Resource<PaymentDetailResponse>>>()
    val paymentDetailResponse: LiveData<Event<Resource<PaymentDetailResponse>>> =
        _paymentDetailResponse


    private val _partyListResponse = MutableLiveData<Event<Resource<PartyListResponse>>>()
    val partyListResponse: LiveData<Event<Resource<PartyListResponse>>> = _partyListResponse

    private val _partyDetailResponse = MutableLiveData<Event<Resource<PartyDetailResponse>>>()
    val partyDetailResponse: LiveData<Event<Resource<PartyDetailResponse>>> = _partyDetailResponse


    private val _commonResponse = MutableLiveData<Event<Resource<CommonResponse>>>()
    val commonResponse: LiveData<Event<Resource<CommonResponse>>> = _commonResponse

    private val _notificationListResponse =
        MutableLiveData<Event<Resource<NotificationListResponse>>>()
    val notificationListResponse: LiveData<Event<Resource<NotificationListResponse>>> =
        _notificationListResponse


    fun launch(): LiveData<Boolean> {
        viewModelScope.launch {
//            fetchFCMToken()
            val isLoggedIn = PreferenceHelper[AppConstants.PREF_KEY_IS_LOGGED_IN, false]
            delay(2000)
            launcher.postValue(isLoggedIn!!)
        }
        return launcher
    }

    private fun fetchFCMToken() {


        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                errorLog { "Fetching FCM registration token failed ==> ${task.exception}" }
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result
            log { "FCM Token ==> $token" }

            Log.w("FCMToken", "" + token)

            PreferenceHelper[AppConstants.PREF_KEY_FCM_TOKEN] = token
        })
    }


    fun settings() = viewModelScope.launch {

        _settingResponse.postValue(Event(Resource.Loading()))
        Log.w("axisloading", "true")



        try {
            if (getApplication<Kirtiman>().hasInternetConnection()) {
                Log.w("axisloading", "nettrue")
                val response = repository.settings()
                if (response.code() == 200) {
                    _settingResponse.postValue(handleSettingResponse(response))
                } else {
                    _authResponse.postValue(
                        handleauthMessage(
                            response.errorBody()!!.charStream().readText()
                        )
                    )
                }


            } else {
                Log.w("axisloading", "netfalse")

                _settingResponse.postValue(
                    Event(
                        Resource.Error(
                            getApplication<Kirtiman>().getString(
                                R.string.no_internet_connection
                            ), null
                        )
                    )
                )
            }
        } catch (t: Throwable) {

            Log.w("vishalerror", "" + t.message.toString())
            when (t) {
                is IOException -> {
                    _settingResponse.postValue(
                        Event(
                            Resource.Error("login" + t.message.toString(), null)
                        )
                    )
                }

                else -> {
                    _settingResponse.postValue(
                        Event(
                            Resource.Error(
                                getApplication<Kirtiman>().getString(
                                    R.string.conversion_error
                                ), null
                            )
                        )
                    )
                }
            }
        }
    }

    fun dropdown() = viewModelScope.launch {

        _dropdownResponse.postValue(Event(Resource.Loading()))

        try {
            if (getApplication<Kirtiman>().hasInternetConnection()) {

                val response = repository.dropdown()
                if (response.code() == 200) {
                    _dropdownResponse.postValue(handleDropDownResponse(response))
                } else {
                    _authResponse.postValue(
                        handleauthMessage(
                            response.errorBody()!!.charStream().readText()
                        )
                    )
                }



                Log.w("vishalerror", "sucessif")


            } else {
                Log.w("vishalerror", "sucesselse")
                _dropdownResponse.postValue(
                    Event(
                        Resource.Error(
                            getApplication<Kirtiman>().getString(
                                R.string.no_internet_connection
                            ), null
                        )
                    )
                )
            }
        } catch (t: Throwable) {

            Log.w("vishalerror", "" + t.message.toString())
            when (t) {
                is IOException -> {
                    _dropdownResponse.postValue(
                        Event(
                            Resource.Error("login" + t.message.toString(), null)
                        )
                    )
                }

                else -> {
                    _dropdownResponse.postValue(
                        Event(
                            Resource.Error(
                                getApplication<Kirtiman>().getString(
                                    R.string.conversion_error
                                ), null
                            )
                        )
                    )
                }
            }
        }
    }

    fun login(
        phoneNumber: String,
        deviceId: String,
        deviceModel: String,
        appVersion: String,
        fcmToken: String
    ) = viewModelScope.launch {

        _loginResponse.postValue(Event(Resource.Loading()))

        try {
            if (getApplication<Kirtiman>().hasInternetConnection()) {

                val response =
                    repository.login(phoneNumber, deviceId, deviceModel, appVersion, fcmToken)
                _loginResponse.postValue(handleLoginResponse(response))


                Log.w("vishalerror", "sucessif")


            } else {
                Log.w("vishalerror", "sucesselse")
                _loginResponse.postValue(
                    Event(
                        Resource.Error(
                            getApplication<Kirtiman>().getString(
                                R.string.no_internet_connection
                            ), null
                        )
                    )
                )
            }
        } catch (t: Throwable) {

            Log.w("vishalerror", "" + t.message.toString())
            when (t) {
                is IOException -> {
                    _settingResponse.postValue(
                        Event(
                            Resource.Error("login" + t.message.toString(), null)
                        )
                    )
                }

                else -> {
                    _settingResponse.postValue(
                        Event(
                            Resource.Error(
                                getApplication<Kirtiman>().getString(
                                    R.string.conversion_error
                                ), null
                            )
                        )
                    )
                }
            }
        }
    }


    fun home() = viewModelScope.launch {

        _homeResponse.postValue(Event(Resource.Loading()))

        try {
            if (getApplication<Kirtiman>().hasInternetConnection()) {

                val response = repository.home()
                if (response.code() == 200) {
                    _homeResponse.postValue(handleHomeResponse(response))
                } else {
                    _authResponse.postValue(
                        handleauthMessage(
                            response.errorBody()!!.charStream().readText()
                        )
                    )
                }



                Log.w("vishalerror", "sucessif")


            } else {
                Log.w("vishalerror", "sucesselse")
                _homeResponse.postValue(
                    Event(
                        Resource.Error(
                            getApplication<Kirtiman>().getString(
                                R.string.no_internet_connection
                            ), null
                        )
                    )
                )
            }
        } catch (t: Throwable) {

            Log.w("vishalerror", "" + t.message.toString())
            when (t) {
                is IOException -> {
                    _homeResponse.postValue(
                        Event(
                            Resource.Error("login" + t.message.toString(), null)
                        )
                    )
                }

                else -> {
                    _homeResponse.postValue(
                        Event(
                            Resource.Error(
                                getApplication<Kirtiman>().getString(
                                    R.string.conversion_error
                                ), null
                            )
                        )
                    )
                }
            }
        }
    }


    fun menu() = viewModelScope.launch {

        _menuResponse.postValue(Event(Resource.Loading()))

        try {
            if (getApplication<Kirtiman>().hasInternetConnection()) {

                val response = repository.menu()
                if (response.code() == 200) {
                    _menuResponse.postValue(handleMenuResponse(response))
                } else {
                    _authResponse.postValue(
                        handleauthMessage(
                            response.errorBody()!!.charStream().readText()
                        )
                    )
                }



                Log.w("vishalerror", "sucessif")


            } else {
                Log.w("vishalerror", "sucesselse")
                _menuResponse.postValue(
                    Event(
                        Resource.Error(
                            getApplication<Kirtiman>().getString(
                                R.string.no_internet_connection
                            ), null
                        )
                    )
                )
            }
        } catch (t: Throwable) {

            Log.w("vishalerror", "" + t.message.toString())
            when (t) {
                is IOException -> {
                    _menuResponse.postValue(
                        Event(
                            Resource.Error("login" + t.message.toString(), null)
                        )
                    )
                }

                else -> {
                    _menuResponse.postValue(
                        Event(
                            Resource.Error(
                                getApplication<Kirtiman>().getString(
                                    R.string.conversion_error
                                ), null
                            )
                        )
                    )
                }
            }
        }
    }

    fun profile() = viewModelScope.launch {

        _profileResponse.postValue(Event(Resource.Loading()))

        try {
            if (getApplication<Kirtiman>().hasInternetConnection()) {

                val response = repository.getProfile()
                if (response.code() == 200) {
                    _profileResponse.postValue(handleProfileResponse(response))
                } else {
                    _authResponse.postValue(
                        handleauthMessage(
                            response.errorBody()!!.charStream().readText()
                        )
                    )
                }



                Log.w("vishalerror", "sucessif")


            } else {
                Log.w("vishalerror", "sucesselse")
                _profileResponse.postValue(
                    Event(
                        Resource.Error(
                            getApplication<Kirtiman>().getString(
                                R.string.no_internet_connection
                            ), null
                        )
                    )
                )
            }
        } catch (t: Throwable) {

            Log.w("vishalerror", "" + t.message.toString())
            when (t) {
                is IOException -> {
                    _profileResponse.postValue(
                        Event(
                            Resource.Error("login" + t.message.toString(), null)
                        )
                    )
                }

                else -> {
                    _profileResponse.postValue(
                        Event(
                            Resource.Error(
                                getApplication<Kirtiman>().getString(
                                    R.string.conversion_error
                                ), null
                            )
                        )
                    )
                }
            }
        }
    }

    fun updateProfile(name: String, lastname: String, email: String, photo: String) =
        viewModelScope.launch {

            _commonResponse.postValue(Event(Resource.Loading()))

            try {
                if (getApplication<Kirtiman>().hasInternetConnection()) {

                    val response = repository.updateProfile(name, lastname, email, photo)
                    if (response.code() == 200) {
                        _commonResponse.postValue(handleLogoutResponse(response))
                    } else {
                        _authResponse.postValue(
                            handleauthMessage(
                                response.errorBody()!!.charStream().readText()
                            )
                        )
                    }



                    Log.w("vishalerror", "sucessif")


                } else {
                    Log.w("vishalerror", "sucesselse")
                    _commonResponse.postValue(
                        Event(
                            Resource.Error(
                                getApplication<Kirtiman>().getString(
                                    R.string.no_internet_connection
                                ), null
                            )
                        )
                    )
                }
            } catch (t: Throwable) {

                Log.w("vishalerror", "" + t.message.toString())
                when (t) {
                    is IOException -> {
                        _commonResponse.postValue(
                            Event(
                                Resource.Error("login" + t.message.toString(), null)
                            )
                        )
                    }

                    else -> {
                        _commonResponse.postValue(
                            Event(
                                Resource.Error(
                                    getApplication<Kirtiman>().getString(
                                        R.string.conversion_error
                                    ), null
                                )
                            )
                        )
                    }
                }
            }
        }


    fun logout() = viewModelScope.launch {

        _logoutResponse.postValue(Event(Resource.Loading()))

        try {
            if (getApplication<Kirtiman>().hasInternetConnection()) {

                val response = repository.logout()
                if (response.code() == 200) {
                    _logoutResponse.postValue(handleLogoutResponse(response))
                } else {
                    _authResponse.postValue(
                        handleauthMessage(
                            response.errorBody()!!.charStream().readText()
                        )
                    )
                }



                Log.w("vishalerror", "sucessif")


            } else {
                Log.w("vishalerror", "sucesselse")
                _logoutResponse.postValue(
                    Event(
                        Resource.Error(
                            getApplication<Kirtiman>().getString(
                                R.string.no_internet_connection
                            ), null
                        )
                    )
                )
            }
        } catch (t: Throwable) {

            Log.w("vishalerror", "" + t.message.toString())
            when (t) {
                is IOException -> {
                    _logoutResponse.postValue(
                        Event(
                            Resource.Error("login" + t.message.toString(), null)
                        )
                    )
                }

                else -> {
                    _logoutResponse.postValue(
                        Event(
                            Resource.Error(
                                getApplication<Kirtiman>().getString(
                                    R.string.conversion_error
                                ), null
                            )
                        )
                    )
                }
            }
        }
    }

    fun punchIn(punchRequest: PunchRequest, type: String) = viewModelScope.launch {

        _punchInResponse.postValue(Event(Resource.Loading()))

        try {
            if (getApplication<Kirtiman>().hasInternetConnection()) {

                val response = repository.punchIn(punchRequest, type)
                if (response.code() == 200) {
                    _punchInResponse.postValue(handlePunchInResponse(response))
                } else {
                    _authResponse.postValue(
                        handleauthMessage(
                            response.errorBody()!!.charStream().readText()
                        )
                    )
                }



                Log.w("vishalerror", "sucessif")


            } else {
                Log.w("vishalerror", "sucesselse")
                _punchInResponse.postValue(
                    Event(
                        Resource.Error(
                            getApplication<Kirtiman>().getString(
                                R.string.no_internet_connection
                            ), null
                        )
                    )
                )
            }
        } catch (t: Throwable) {

            Log.w("vishalerror", "" + t.message.toString())
            when (t) {
                is IOException -> {
                    _punchInResponse.postValue(
                        Event(
                            Resource.Error("login" + t.message.toString(), null)
                        )
                    )
                }

                else -> {
                    _punchInResponse.postValue(
                        Event(
                            Resource.Error(
                                getApplication<Kirtiman>().getString(
                                    R.string.conversion_error
                                ), null
                            )
                        )
                    )
                }
            }
        }
    }

    fun tracking(trackingRequest: PunchRequest, type: String) = viewModelScope.launch {

        _trackingResponse.postValue(Event(Resource.Loading()))

        try {
            if (getApplication<Kirtiman>().hasInternetConnection()) {

                val response = repository.tracking(trackingRequest, type)
                if (response.code() == 200) {
                    _trackingResponse.postValue(handlePunchInResponse(response))
                } else {
                    _authResponse.postValue(
                        handleauthMessage(
                            response.errorBody()!!.charStream().readText()
                        )
                    )
                }



                Log.w("vishalerror", "sucessif")


            } else {
                Log.w("vishalerror", "sucesselse")
                _trackingResponse.postValue(
                    Event(
                        Resource.Error(
                            getApplication<Kirtiman>().getString(
                                R.string.no_internet_connection
                            ), null
                        )
                    )
                )
            }
        } catch (t: Throwable) {

            Log.w("vishalerror", "" + t.message.toString())
            when (t) {
                is IOException -> {
                    _trackingResponse.postValue(
                        Event(
                            Resource.Error("login" + t.message.toString(), null)
                        )
                    )
                }

                else -> {
                    _trackingResponse.postValue(
                        Event(
                            Resource.Error(
                                getApplication<Kirtiman>().getString(
                                    R.string.conversion_error
                                ), null
                            )
                        )
                    )
                }
            }
        }
    }

    fun attandanceList(month: Int, year: Int) = viewModelScope.launch {

        _attendanceListResponse.postValue(Event(Resource.Loading()))

        try {
            if (getApplication<Kirtiman>().hasInternetConnection()) {

                val response = repository.getAttendance(month, year)
                if (response.code() == 200) {
                    _attendanceListResponse.postValue(handleAttendanceListResponse(response))
                } else {
                    _authResponse.postValue(
                        handleauthMessage(
                            response.errorBody()!!.charStream().readText()
                        )
                    )
                }



                Log.w("vishalerror", "sucessif")


            } else {
                Log.w("vishalerror", "sucesselse")
                _attendanceListResponse.postValue(
                    Event(
                        Resource.Error(
                            getApplication<Kirtiman>().getString(
                                R.string.no_internet_connection
                            ), null
                        )
                    )
                )
            }
        } catch (t: Throwable) {

            Log.w("vishalerror", "" + t.message.toString())
            when (t) {
                is IOException -> {
                    _attendanceListResponse.postValue(
                        Event(
                            Resource.Error("login" + t.message.toString(), null)
                        )
                    )
                }

                else -> {
                    _attendanceListResponse.postValue(
                        Event(
                            Resource.Error(
                                getApplication<Kirtiman>().getString(
                                    R.string.conversion_error
                                ), null
                            )
                        )
                    )
                }
            }
        }
    }

    fun attendanceDetail(id: String) = viewModelScope.launch {

        _attendanceDetailResponse.postValue(Event(Resource.Loading()))

        try {
            if (getApplication<Kirtiman>().hasInternetConnection()) {

                val response = repository.attendanceDetail(id)
                if (response.code() == 200) {
                    _attendanceDetailResponse.postValue(handleAttendnaceDetailResponse(response))
                } else {
                    _authResponse.postValue(
                        handleauthMessage(
                            response.errorBody()!!.charStream().readText()
                        )
                    )
                }



                Log.w("vishalerror", "sucessif")


            } else {
                Log.w("vishalerror", "sucesselse")
                _attendanceDetailResponse.postValue(
                    Event(
                        Resource.Error(
                            getApplication<Kirtiman>().getString(
                                R.string.no_internet_connection
                            ), null
                        )
                    )
                )
            }
        } catch (t: Throwable) {

            Log.w("vishalerror", "" + t.message.toString())
            when (t) {
                is IOException -> {
                    _attendanceDetailResponse.postValue(
                        Event(
                            Resource.Error("login" + t.message.toString(), null)
                        )
                    )
                }

                else -> {
                    _attendanceDetailResponse.postValue(
                        Event(
                            Resource.Error(
                                getApplication<Kirtiman>().getString(
                                    R.string.conversion_error
                                ), null
                            )
                        )
                    )
                }
            }
        }
    }


    fun getLeaveList(month: Int, year: Int) = viewModelScope.launch {

        _leaveListResponse.postValue(Event(Resource.Loading()))

        try {
            if (getApplication<Kirtiman>().hasInternetConnection()) {

                val response = repository.getLeaveList(month, year)
                if (response.code() == 200) {
                    _leaveListResponse.postValue(handleALeaveListResponse(response))
                } else {
                    _authResponse.postValue(
                        handleauthMessage(
                            response.errorBody()!!.charStream().readText()
                        )
                    )
                }

                Log.w("vishalerror", "sucessif")


            } else {
                Log.w("vishalerror", "sucesselse")
                _leaveListResponse.postValue(
                    Event(
                        Resource.Error(
                            getApplication<Kirtiman>().getString(
                                R.string.no_internet_connection
                            ), null
                        )
                    )
                )
            }
        } catch (t: Throwable) {

            Log.w("vishalerror", "" + t.message.toString())
            when (t) {
                is IOException -> {
                    _leaveListResponse.postValue(
                        Event(
                            Resource.Error("login" + t.message.toString(), null)
                        )
                    )
                }

                else -> {
                    _leaveListResponse.postValue(
                        Event(
                            Resource.Error(
                                getApplication<Kirtiman>().getString(
                                    R.string.conversion_error
                                ), null
                            )
                        )
                    )
                }
            }
        }
    }


    fun addLeave(leave_type_id: Int?, start_date: String, end_date: String, reason: String) =
        viewModelScope.launch {

            _commonResponse.postValue(Event(Resource.Loading()))

            try {
                if (getApplication<Kirtiman>().hasInternetConnection()) {

                    val response = repository.addLeave(leave_type_id, start_date, end_date, reason)
                    if (response.code() == 200) {
                        _commonResponse.postValue(handleLogoutResponse(response))
                    } else {
                        _authResponse.postValue(
                            handleauthMessage(
                                response.errorBody()!!.charStream().readText()
                            )
                        )
                    }



                    Log.w("vishalerror", "sucessif")


                } else {
                    Log.w("vishalerror", "sucesselse")
                    _commonResponse.postValue(
                        Event(
                            Resource.Error(
                                getApplication<Kirtiman>().getString(
                                    R.string.no_internet_connection
                                ), null
                            )
                        )
                    )
                }
            } catch (t: Throwable) {

                Log.w("vishalerror", "" + t.message.toString())
                when (t) {
                    is IOException -> {
                        _commonResponse.postValue(
                            Event(
                                Resource.Error("login" + t.message.toString(), null)
                            )
                        )
                    }

                    else -> {
                        _commonResponse.postValue(
                            Event(
                                Resource.Error(
                                    getApplication<Kirtiman>().getString(
                                        R.string.conversion_error
                                    ), null
                                )
                            )
                        )
                    }
                }
            }
        }

    fun expenseList(type: String, status: String = "") = viewModelScope.launch {

        _expenseListResponse.postValue(Event(Resource.Loading()))

        try {
            if (getApplication<Kirtiman>().hasInternetConnection()) {

                val response = repository.expenseList(type, status)
                if (response.code() == 200) {
                    _expenseListResponse.postValue(handleExpenseListResponse(response))
                } else {
                    _authResponse.postValue(
                        handleauthMessage(
                            response.errorBody()!!.charStream().readText()
                        )
                    )
                }



                Log.w("vishalerror", "sucessif")


            } else {
                Log.w("vishalerror", "sucesselse")
                _attendanceListResponse.postValue(
                    Event(
                        Resource.Error(
                            getApplication<Kirtiman>().getString(
                                R.string.no_internet_connection
                            ), null
                        )
                    )
                )
            }
        } catch (t: Throwable) {

            Log.w("vishalerror", "" + t.message.toString())
            when (t) {
                is IOException -> {
                    _expenseListResponse.postValue(
                        Event(
                            Resource.Error("login" + t.message.toString(), null)
                        )
                    )
                }

                else -> {
                    _expenseListResponse.postValue(
                        Event(
                            Resource.Error(
                                getApplication<Kirtiman>().getString(
                                    R.string.conversion_error
                                ), null
                            )
                        )
                    )
                }
            }
        }
    }

    fun expenseDetail(id: String) = viewModelScope.launch {

        _expenseDetailResponse.postValue(Event(Resource.Loading()))

        try {
            if (getApplication<Kirtiman>().hasInternetConnection()) {

                val response = repository.expenseDetail(id)
                if (response.code() == 200) {
                    _expenseDetailResponse.postValue(handleExpenseDetailResponse(response))
                } else {
                    _authResponse.postValue(
                        handleauthMessage(
                            response.errorBody()!!.charStream().readText()
                        )
                    )
                }



                Log.w("vishalerror", "sucessif")


            } else {
                Log.w("vishalerror", "sucesselse")
                _expenseDetailResponse.postValue(
                    Event(
                        Resource.Error(
                            getApplication<Kirtiman>().getString(
                                R.string.no_internet_connection
                            ), null
                        )
                    )
                )
            }
        } catch (t: Throwable) {

            Log.w("vishalerror", "" + t.message.toString())
            when (t) {
                is IOException -> {
                    _expenseDetailResponse.postValue(
                        Event(
                            Resource.Error("login" + t.message.toString(), null)
                        )
                    )
                }

                else -> {
                    _expenseDetailResponse.postValue(
                        Event(
                            Resource.Error(
                                getApplication<Kirtiman>().getString(
                                    R.string.conversion_error
                                ), null
                            )
                        )
                    )
                }
            }
        }
    }

    fun addExpense(
        city_id: Int,
        category_id: Int,
        requested_amount: String,
        expense_details: String,
        expense_type: String,
        strphotopath: String,
        date: String
    ) = viewModelScope.launch {

        _commonResponse.postValue(Event(Resource.Loading()))

        try {
            if (getApplication<Kirtiman>().hasInternetConnection()) {

                val response = repository.addExpense(
                    city_id,
                    category_id,
                    requested_amount,
                    expense_details,
                    expense_type,
                    strphotopath,
                    date
                )
                if (response.code() == 200) {
                    _commonResponse.postValue(handleLogoutResponse(response))
                } else {
                    _authResponse.postValue(
                        handleauthMessage(
                            response.errorBody()!!.charStream().readText()
                        )
                    )
                }



                Log.w("vishalerror", "sucessif")


            } else {
                Log.w("vishalerror", "sucesselse")
                _commonResponse.postValue(
                    Event(
                        Resource.Error(
                            getApplication<Kirtiman>().getString(
                                R.string.no_internet_connection
                            ), null
                        )
                    )
                )
            }
        } catch (t: Throwable) {

            Log.w("vishalerror", "" + t.message.toString())
            when (t) {
                is IOException -> {
                    _commonResponse.postValue(
                        Event(
                            Resource.Error("login" + t.message.toString(), null)
                        )
                    )
                }

                else -> {
                    _commonResponse.postValue(
                        Event(
                            Resource.Error(
                                getApplication<Kirtiman>().getString(
                                    R.string.conversion_error
                                ), null
                            )
                        )
                    )
                }
            }
        }
    }


    fun visitList() = viewModelScope.launch {

        _visitListResponse.postValue(Event(Resource.Loading()))

        try {
            if (getApplication<Kirtiman>().hasInternetConnection()) {

                val response = repository.visitList()
                if (response.code() == 200) {
                    _visitListResponse.postValue(handleVisitListResponse(response))
                } else {
                    _authResponse.postValue(
                        handleauthMessage(
                            response.errorBody()!!.charStream().readText()
                        )
                    )
                }



                Log.w("vishalerror", "sucessif")


            } else {
                Log.w("vishalerror", "sucesselse")
                _visitListResponse.postValue(
                    Event(
                        Resource.Error(
                            getApplication<Kirtiman>().getString(
                                R.string.no_internet_connection
                            ), null
                        )
                    )
                )
            }
        } catch (t: Throwable) {

            Log.w("vishalerror", "" + t.message.toString())
            when (t) {
                is IOException -> {
                    _visitListResponse.postValue(
                        Event(
                            Resource.Error("login" + t.message.toString(), null)
                        )
                    )
                }

                else -> {
                    _visitListResponse.postValue(
                        Event(
                            Resource.Error(
                                getApplication<Kirtiman>().getString(
                                    R.string.conversion_error
                                ), null
                            )
                        )
                    )
                }
            }
        }
    }

    fun visitDetail(id: String) = viewModelScope.launch {

        _visitDetailResponse.postValue(Event(Resource.Loading()))

        try {
            if (getApplication<Kirtiman>().hasInternetConnection()) {

                val response = repository.visitDetail(id)
                if (response.code() == 200) {
                    _visitDetailResponse.postValue(handleVisitDetailResponse(response))
                } else {
                    _authResponse.postValue(
                        handleauthMessage(
                            response.errorBody()!!.charStream().readText()
                        )
                    )
                }



                Log.w("vishalerror", "sucessif")


            } else {
                Log.w("vishalerror", "sucesselse")
                _visitDetailResponse.postValue(
                    Event(
                        Resource.Error(
                            getApplication<Kirtiman>().getString(
                                R.string.no_internet_connection
                            ), null
                        )
                    )
                )
            }
        } catch (t: Throwable) {

            Log.w("vishalerror", "" + t.message.toString())
            when (t) {
                is IOException -> {
                    _visitDetailResponse.postValue(
                        Event(
                            Resource.Error("login" + t.message.toString(), null)
                        )
                    )
                }

                else -> {
                    _visitDetailResponse.postValue(
                        Event(
                            Resource.Error(
                                getApplication<Kirtiman>().getString(
                                    R.string.conversion_error
                                ), null
                            )
                        )
                    )
                }
            }
        }
    }

    fun addVisit(addVisitRequest: AddVisitRequest) = viewModelScope.launch {

        _commonResponse.postValue(Event(Resource.Loading()))

        try {
            if (getApplication<Kirtiman>().hasInternetConnection()) {

                val response = repository.addVisit(addVisitRequest)
                if (response.code() == 200) {
                    _commonResponse.postValue(handleLogoutResponse(response))
                } else {
                    _authResponse.postValue(
                        handleauthMessage(
                            response.errorBody()!!.charStream().readText()
                        )
                    )
                }



                Log.w("vishalerror", "sucessif")


            } else {
                Log.w("vishalerror", "sucesselse")
                _commonResponse.postValue(
                    Event(
                        Resource.Error(
                            getApplication<Kirtiman>().getString(
                                R.string.no_internet_connection
                            ), null
                        )
                    )
                )
            }
        } catch (t: Throwable) {

            Log.w("vishalerror", "" + t.message.toString())
            when (t) {
                is IOException -> {
                    _commonResponse.postValue(
                        Event(
                            Resource.Error("login" + t.message.toString(), null)
                        )
                    )
                }

                else -> {
                    _commonResponse.postValue(
                        Event(
                            Resource.Error(
                                getApplication<Kirtiman>().getString(
                                    R.string.conversion_error
                                ), null
                            )
                        )
                    )
                }
            }
        }
    }


    fun paumentList() = viewModelScope.launch {

        _paymentListResponse.postValue(Event(Resource.Loading()))

        try {
            if (getApplication<Kirtiman>().hasInternetConnection()) {

                val response = repository.paymentList()
                if (response.code() == 200) {
                    _paymentListResponse.postValue(handlePaymentListResponse(response))
                } else {
                    _authResponse.postValue(
                        handleauthMessage(
                            response.errorBody()!!.charStream().readText()
                        )
                    )
                }



                Log.w("vishalerror", "sucessif")


            } else {
                Log.w("vishalerror", "sucesselse")
                _paymentListResponse.postValue(
                    Event(
                        Resource.Error(
                            getApplication<Kirtiman>().getString(
                                R.string.no_internet_connection
                            ), null
                        )
                    )
                )
            }
        } catch (t: Throwable) {

            Log.w("vishalerror", "" + t.message.toString())
            when (t) {
                is IOException -> {
                    _paymentListResponse.postValue(
                        Event(
                            Resource.Error("login" + t.message.toString(), null)
                        )
                    )
                }

                else -> {
                    _paymentListResponse.postValue(
                        Event(
                            Resource.Error(
                                getApplication<Kirtiman>().getString(
                                    R.string.conversion_error
                                ), null
                            )
                        )
                    )
                }
            }
        }
    }

    fun paymentDetail(id: String) = viewModelScope.launch {

        _paymentDetailResponse.postValue(Event(Resource.Loading()))

        try {
            if (getApplication<Kirtiman>().hasInternetConnection()) {

                val response = repository.paymentDetail(id)
                if (response.code() == 200) {
                    _paymentDetailResponse.postValue(handlePaymentDetailResponse(response))
                } else {
                    _authResponse.postValue(
                        handleauthMessage(
                            response.errorBody()!!.charStream().readText()
                        )
                    )
                }



                Log.w("vishalerror", "sucessif")


            } else {
                Log.w("vishalerror", "sucesselse")
                _paymentDetailResponse.postValue(
                    Event(
                        Resource.Error(
                            getApplication<Kirtiman>().getString(
                                R.string.no_internet_connection
                            ), null
                        )
                    )
                )
            }
        } catch (t: Throwable) {

            Log.w("vishalerror", "" + t.message.toString())
            when (t) {
                is IOException -> {
                    _paymentDetailResponse.postValue(
                        Event(
                            Resource.Error("login" + t.message.toString(), null)
                        )
                    )
                }

                else -> {
                    _paymentDetailResponse.postValue(
                        Event(
                            Resource.Error(
                                getApplication<Kirtiman>().getString(
                                    R.string.conversion_error
                                ), null
                            )
                        )
                    )
                }
            }
        }
    }

    fun addPayment(addPaymentRequest: AddPaymentRequest) = viewModelScope.launch {

        _commonResponse.postValue(Event(Resource.Loading()))

        try {
            if (getApplication<Kirtiman>().hasInternetConnection()) {

                val response = repository.addPayment(addPaymentRequest)
                if (response.code() == 200) {
                    _commonResponse.postValue(handleLogoutResponse(response))
                } else {
                    _authResponse.postValue(
                        handleauthMessage(
                            response.errorBody()!!.charStream().readText()
                        )
                    )
                }



                Log.w("vishalerror", "sucessif")


            } else {
                Log.w("vishalerror", "sucesselse")
                _commonResponse.postValue(
                    Event(
                        Resource.Error(
                            getApplication<Kirtiman>().getString(
                                R.string.no_internet_connection
                            ), null
                        )
                    )
                )
            }
        } catch (t: Throwable) {

            Log.w("vishalerror", "" + t.message.toString())
            when (t) {
                is IOException -> {
                    _commonResponse.postValue(
                        Event(
                            Resource.Error("login" + t.message.toString(), null)
                        )
                    )
                }

                else -> {
                    _commonResponse.postValue(
                        Event(
                            Resource.Error(
                                getApplication<Kirtiman>().getString(
                                    R.string.conversion_error
                                ), null
                            )
                        )
                    )
                }
            }
        }
    }

    fun partyList() = viewModelScope.launch {

        _partyListResponse.postValue(Event(Resource.Loading()))

        try {
            if (getApplication<Kirtiman>().hasInternetConnection()) {

                val response = repository.partyList()
                if (response.code() == 200) {
                    _partyListResponse.postValue(handlePartyListResponse(response))
                } else {
                    _authResponse.postValue(
                        handleauthMessage(
                            response.errorBody()!!.charStream().readText()
                        )
                    )
                }



                Log.w("vishalerror", "sucessif")


            } else {
                Log.w("vishalerror", "sucesselse")
                _partyListResponse.postValue(
                    Event(
                        Resource.Error(
                            getApplication<Kirtiman>().getString(
                                R.string.no_internet_connection
                            ), null
                        )
                    )
                )
            }
        } catch (t: Throwable) {

            Log.w("vishalerror", "" + t.message.toString())
            when (t) {
                is IOException -> {
                    _partyListResponse.postValue(
                        Event(
                            Resource.Error("login" + t.message.toString(), null)
                        )
                    )
                }

                else -> {
                    _partyListResponse.postValue(
                        Event(
                            Resource.Error(
                                getApplication<Kirtiman>().getString(
                                    R.string.conversion_error
                                ), null
                            )
                        )
                    )
                }
            }
        }
    }

    fun addParty(addPartyRequest: AddPartyRequest) = viewModelScope.launch {

        _commonResponse.postValue(Event(Resource.Loading()))

        try {
            if (getApplication<Kirtiman>().hasInternetConnection()) {

                val response = repository.addParty(addPartyRequest)
                if (response.code() == 200) {
                    _commonResponse.postValue(handleLogoutResponse(response))
                } else {
                    _authResponse.postValue(
                        handleauthMessage(
                            response.errorBody()!!.charStream().readText()
                        )
                    )
                }



                Log.w("vishalerror", "sucessif")


            } else {
                Log.w("vishalerror", "sucesselse")
                _commonResponse.postValue(
                    Event(
                        Resource.Error(
                            getApplication<Kirtiman>().getString(
                                R.string.no_internet_connection
                            ), null
                        )
                    )
                )
            }
        } catch (t: Throwable) {

            Log.w("vishalerror", "" + t.message.toString())
            when (t) {
                is IOException -> {
                    _commonResponse.postValue(
                        Event(
                            Resource.Error("login" + t.message.toString(), null)
                        )
                    )
                }

                else -> {
                    _commonResponse.postValue(
                        Event(
                            Resource.Error(
                                getApplication<Kirtiman>().getString(
                                    R.string.conversion_error
                                ), null
                            )
                        )
                    )
                }
            }
        }
    }

    fun partyDetail(id: String) = viewModelScope.launch {

        _partyDetailResponse.postValue(Event(Resource.Loading()))

        try {
            if (getApplication<Kirtiman>().hasInternetConnection()) {

                val response = repository.partyDetail(id)
                if (response.code() == 200) {
                    _partyDetailResponse.postValue(handlePartyDetailResponse(response))
                } else {
                    _authResponse.postValue(
                        handleauthMessage(
                            response.errorBody()!!.charStream().readText()
                        )
                    )
                }



                Log.w("vishalerror", "sucessif")


            } else {
                Log.w("vishalerror", "sucesselse")
                _partyDetailResponse.postValue(
                    Event(
                        Resource.Error(
                            getApplication<Kirtiman>().getString(
                                R.string.no_internet_connection
                            ), null
                        )
                    )
                )
            }
        } catch (t: Throwable) {

            Log.w("vishalerror", "" + t.message.toString())
            when (t) {
                is IOException -> {
                    _partyDetailResponse.postValue(
                        Event(
                            Resource.Error("login" + t.message.toString(), null)
                        )
                    )
                }

                else -> {
                    _partyDetailResponse.postValue(
                        Event(
                            Resource.Error(
                                getApplication<Kirtiman>().getString(
                                    R.string.conversion_error
                                ), null
                            )
                        )
                    )
                }
            }
        }
    }


    fun notificationList() = viewModelScope.launch {

        _notificationListResponse.postValue(Event(Resource.Loading()))

        try {
            if (getApplication<Kirtiman>().hasInternetConnection()) {

                val response = repository.notificationList()
                if (response.code() == 200) {
                    _notificationListResponse.postValue(handleNotificationListResponse(response))
                } else {
                    _authResponse.postValue(
                        handleauthMessage(
                            response.errorBody()!!.charStream().readText()
                        )
                    )
                }
                Log.w("vishalerror", "sucessif")


            } else {
                Log.w("vishalerror", "sucesselse")
                _notificationListResponse.postValue(
                    Event(
                        Resource.Error(
                            getApplication<Kirtiman>().getString(
                                R.string.no_internet_connection
                            ), null
                        )
                    )
                )
            }
        } catch (t: Throwable) {

            Log.w("vishalerror", "" + t.message.toString())
            when (t) {
                is IOException -> {
                    _notificationListResponse.postValue(
                        Event(
                            Resource.Error("login" + t.message.toString(), null)
                        )
                    )
                }

                else -> {
                    _notificationListResponse.postValue(
                        Event(
                            Resource.Error(
                                getApplication<Kirtiman>().getString(
                                    R.string.conversion_error
                                ), null
                            )
                        )
                    )
                }
            }
        }
    }


    fun clearNotification(notificationid: Int = 0) = viewModelScope.launch {

        _commonResponse.postValue(Event(Resource.Loading()))

        try {
            if (getApplication<Kirtiman>().hasInternetConnection()) {


                val response = repository.clearNotification(notificationid)
                if (response.code() == 200) {
                    _commonResponse.postValue(handleLogoutResponse(response))
                } else {
                    _authResponse.postValue(
                        handleauthMessage(
                            response.errorBody()!!.charStream().readText()
                        )
                    )
                }


                Log.w("vishalerror", "sucessif")


            } else {
                Log.w("vishalerror", "sucesselse")
                _commonResponse.postValue(
                    Event(
                        Resource.Error(
                            getApplication<Kirtiman>().getString(
                                R.string.no_internet_connection
                            ), null
                        )
                    )
                )
            }
        } catch (t: Throwable) {

            Log.w("vishalerror", "" + t.message.toString())
            when (t) {
                is IOException -> {
                    _commonResponse.postValue(
                        Event(
                            Resource.Error("login" + t.message.toString(), null)
                        )
                    )
                }

                else -> {
                    _commonResponse.postValue(
                        Event(
                            Resource.Error(
                                getApplication<Kirtiman>().getString(
                                    R.string.conversion_error
                                ), null
                            )
                        )
                    )
                }
            }
        }
    }


    private fun handleauthMessage(response: String): Event<Resource<String>> {
        val jsonObject = JSONObject(response)

        Log.e("vishaldata", "handleResponseelse$jsonObject")

        return Event(Resource.Error(jsonObject.getString("message")))

    }

    private fun handleSettingResponse(response: Response<SettingResponse>): Event<Resource<SettingResponse>> {
        if (response.isSuccessful) {
            Log.w("vishalerror", "handleCountriesResponseif")

            response.body()?.let { resultResponse ->
                return Event(Resource.Success(resultResponse))
            }
        } else {

            val jsonObject = JSONObject(response.errorBody()!!.string())

            Log.w("vishalerror", "handleResponseelse$jsonObject")



            return Event(Resource.Error(jsonObject.toString()))

        }

        return Event(Resource.Error(response.message()))
    }

    private fun handleDropDownResponse(response: Response<DropDownResponse>): Event<Resource<DropDownResponse>> {
        if (response.isSuccessful) {
            Log.w("vishalerror", "handleCountriesResponseif")

            response.body()?.let { resultResponse ->
                return Event(Resource.Success(resultResponse))
            }
        } else {

            val jsonObject = JSONObject(response.errorBody()!!.string())

            Log.w("vishalerror", "handleResponseelse$jsonObject")



            return Event(Resource.Error(jsonObject.toString()))

        }

        return Event(Resource.Error(response.message()))
    }

    private fun handleLoginResponse(response: Response<LoginResponse>): Event<Resource<LoginResponse>> {
        if (response.isSuccessful) {
            Log.w("vishalerror", "handleCountriesResponseif")

            response.body()?.let { resultResponse ->
                return Event(Resource.Success(resultResponse))
            }
        } else {

            val jsonObject = JSONObject(response.errorBody()!!.string())

            Log.w("vishalerror", "handleResponseelse$jsonObject")



            return Event(Resource.Error(jsonObject.toString()))

        }

        return Event(Resource.Error(response.message()))
    }


    private fun handleHomeResponse(response: Response<HomeResponse>): Event<Resource<HomeResponse>> {
        if (response.isSuccessful) {
            Log.w("vishalerror", "handleCountriesResponseif")

            response.body()?.let { resultResponse ->
                return Event(Resource.Success(resultResponse))
            }
        } else {

            val jsonObject = JSONObject(response.errorBody()!!.string())

            Log.w("vishalerror", "handleResponseelse$jsonObject")



            return Event(Resource.Error(jsonObject.toString()))

        }

        return Event(Resource.Error(response.message()))
    }


    private fun handleMenuResponse(response: Response<MenuResponse>): Event<Resource<MenuResponse>> {
        if (response.isSuccessful) {
            Log.w("vishalerror", "handleCountriesResponseif")

            response.body()?.let { resultResponse ->
                return Event(Resource.Success(resultResponse))
            }
        } else {

            val jsonObject = JSONObject(response.errorBody()!!.string())

            Log.w("vishalerror", "handleResponseelse$jsonObject")



            return Event(Resource.Error(jsonObject.toString()))

        }

        return Event(Resource.Error(response.message()))
    }

    private fun handleLogoutResponse(response: Response<CommonResponse>): Event<Resource<CommonResponse>> {
        if (response.isSuccessful) {
            Log.w("vishalerror", "handleCountriesResponseif")

            response.body()?.let { resultResponse ->
                return Event(Resource.Success(resultResponse))
            }
        } else {

            val jsonObject = JSONObject(response.errorBody()!!.string())

            Log.w("vishalerror", "handleResponseelse$jsonObject")



            return Event(Resource.Error(jsonObject.toString()))

        }

        return Event(Resource.Error(response.message()))
    }

    private fun handlePunchInResponse(response: Response<PunchResponse>): Event<Resource<PunchResponse>> {
        if (response.isSuccessful) {
            Log.w("vishalerror", "handleCountriesResponseif")

            response.body()?.let { resultResponse ->
                return Event(Resource.Success(resultResponse))
            }
        } else {

            val jsonObject = JSONObject(response.errorBody()!!.string())

            Log.w("vishalerror", "handleResponseelse$jsonObject")



            return Event(Resource.Error(jsonObject.toString()))

        }

        return Event(Resource.Error(response.message()))
    }

    private fun handleAttendanceListResponse(response: Response<AttendanceListResponse>): Event<Resource<AttendanceListResponse>> {
        if (response.isSuccessful) {
            Log.w("vishalerror", "handleCountriesResponseif")

            response.body()?.let { resultResponse ->
                return Event(Resource.Success(resultResponse))
            }
        } else {

            val jsonObject = JSONObject(response.errorBody()!!.string())

            Log.w("vishalerror", "handleResponseelse$jsonObject")



            return Event(Resource.Error(jsonObject.toString()))

        }

        return Event(Resource.Error(response.message()))
    }

    private fun handleAttendnaceDetailResponse(response: Response<AttendanceDetailResponse>): Event<Resource<AttendanceDetailResponse>> {
        if (response.isSuccessful) {
            Log.w("vishalerror", "handleCountriesResponseif")

            response.body()?.let { resultResponse ->
                return Event(Resource.Success(resultResponse))
            }
        } else {

            val jsonObject = JSONObject(response.errorBody()!!.string())

            Log.w("vishalerror", "handleResponseelse$jsonObject")



            return Event(Resource.Error(jsonObject.toString()))

        }

        return Event(Resource.Error(response.message()))
    }

    private fun handleALeaveListResponse(response: Response<LeaveListResponse>): Event<Resource<LeaveListResponse>> {
        if (response.isSuccessful) {
            Log.w("vishalerror", "handleCountriesResponseif")

            response.body()?.let { resultResponse ->
                return Event(Resource.Success(resultResponse))
            }
        } else {

            val jsonObject = JSONObject(response.errorBody()!!.string())

            Log.w("vishalerror", "handleResponseelse$jsonObject")



            return Event(Resource.Error(jsonObject.toString()))

        }

        return Event(Resource.Error(response.message()))
    }

    private fun handleExpenseListResponse(response: Response<ExpenseListResponse>): Event<Resource<ExpenseListResponse>> {
        if (response.isSuccessful) {
            Log.w("vishalerror", "handleCountriesResponseif")

            response.body()?.let { resultResponse ->
                return Event(Resource.Success(resultResponse))
            }
        } else {

            val jsonObject = JSONObject(response.errorBody()!!.string())

            Log.w("vishalerror", "handleResponseelse$jsonObject")



            return Event(Resource.Error(jsonObject.toString()))

        }

        return Event(Resource.Error(response.message()))
    }

    private fun handleExpenseDetailResponse(response: Response<ExpenseDetailResponse>): Event<Resource<ExpenseDetailResponse>> {
        if (response.isSuccessful) {
            Log.w("vishalerror", "handleCountriesResponseif")

            response.body()?.let { resultResponse ->
                return Event(Resource.Success(resultResponse))
            }
        } else {

            val jsonObject = JSONObject(response.errorBody()!!.string())

            Log.w("vishalerror", "handleResponseelse$jsonObject")



            return Event(Resource.Error(jsonObject.toString()))

        }

        return Event(Resource.Error(response.message()))
    }

    private fun handleVisitListResponse(response: Response<VisitListResponse>): Event<Resource<VisitListResponse>> {
        if (response.isSuccessful) {
            Log.w("vishalerror", "handleCountriesResponseif")

            response.body()?.let { resultResponse ->
                return Event(Resource.Success(resultResponse))
            }
        } else {

            val jsonObject = JSONObject(response.errorBody()!!.string())

            Log.w("vishalerror", "handleResponseelse$jsonObject")



            return Event(Resource.Error(jsonObject.toString()))

        }

        return Event(Resource.Error(response.message()))
    }

    private fun handleVisitDetailResponse(response: Response<VisitDetailResponse>): Event<Resource<VisitDetailResponse>> {
        if (response.isSuccessful) {
            Log.w("vishalerror", "handleCountriesResponseif")

            response.body()?.let { resultResponse ->
                return Event(Resource.Success(resultResponse))
            }
        } else {

            val jsonObject = JSONObject(response.errorBody()!!.string())

            Log.w("vishalerror", "handleResponseelse$jsonObject")



            return Event(Resource.Error(jsonObject.toString()))

        }

        return Event(Resource.Error(response.message()))
    }

    private fun handlePaymentListResponse(response: Response<PaymentListResponse>): Event<Resource<PaymentListResponse>> {
        if (response.isSuccessful) {
            Log.w("vishalerror", "handleCountriesResponseif")

            response.body()?.let { resultResponse ->
                return Event(Resource.Success(resultResponse))
            }
        } else {

            val jsonObject = JSONObject(response.errorBody()!!.string())

            Log.w("vishalerror", "handleResponseelse$jsonObject")



            return Event(Resource.Error(jsonObject.toString()))

        }

        return Event(Resource.Error(response.message()))
    }

    private fun handlePaymentDetailResponse(response: Response<PaymentDetailResponse>): Event<Resource<PaymentDetailResponse>> {
        if (response.isSuccessful) {
            Log.w("vishalerror", "handleCountriesResponseif")

            response.body()?.let { resultResponse ->
                return Event(Resource.Success(resultResponse))
            }
        } else {

            val jsonObject = JSONObject(response.errorBody()!!.string())

            Log.w("vishalerror", "handleResponseelse$jsonObject")



            return Event(Resource.Error(jsonObject.toString()))

        }

        return Event(Resource.Error(response.message()))
    }

    private fun handlePartyListResponse(response: Response<PartyListResponse>): Event<Resource<PartyListResponse>> {
        if (response.isSuccessful) {
            Log.w("vishalerror", "handleCountriesResponseif")

            response.body()?.let { resultResponse ->
                return Event(Resource.Success(resultResponse))
            }
        } else {

            val jsonObject = JSONObject(response.errorBody()!!.string())

            Log.w("vishalerror", "handleResponseelse$jsonObject")



            return Event(Resource.Error(jsonObject.toString()))

        }

        return Event(Resource.Error(response.message()))
    }

    private fun handlePartyDetailResponse(response: Response<PartyDetailResponse>): Event<Resource<PartyDetailResponse>> {
        if (response.isSuccessful) {
            Log.w("vishalerror", "handleCountriesResponseif")

            response.body()?.let { resultResponse ->
                return Event(Resource.Success(resultResponse))
            }
        } else {

            val jsonObject = JSONObject(response.errorBody()!!.string())

            Log.w("vishalerror", "handleResponseelse$jsonObject")



            return Event(Resource.Error(jsonObject.toString()))

        }

        return Event(Resource.Error(response.message()))
    }

    private fun handleNotificationListResponse(response: Response<NotificationListResponse>): Event<Resource<NotificationListResponse>> {
        if (response.isSuccessful) {
            Log.w("vishalerror", "handleCountriesResponseif")

            response.body()?.let { resultResponse ->
                return Event(Resource.Success(resultResponse))
            }
        } else {

            val jsonObject = JSONObject(response.errorBody()!!.string())

            Log.w("vishalerror", "handleResponseelse$jsonObject")



            return Event(Resource.Error(jsonObject.toString()))

        }

        return Event(Resource.Error(response.message()))
    }

    private fun handleProfileResponse(response: Response<ProfileResponse>): Event<Resource<ProfileResponse>> {
        if (response.isSuccessful) {
            Log.w("vishalerror", "handleCountriesResponseif")

            response.body()?.let { resultResponse ->
                return Event(Resource.Success(resultResponse))
            }
        } else {

            val jsonObject = JSONObject(response.errorBody()!!.string())

            Log.w("vishalerror", "handleResponseelse$jsonObject")



            return Event(Resource.Error(jsonObject.toString()))

        }

        return Event(Resource.Error(response.message()))
    }

    fun setAuthToken(token: String) {
        PreferenceHelper[AppConstants.PREF_KEY_AUTH_TOKEN] = token
    }

    fun setLoginStatus(isLoggedIn: Boolean) {
        PreferenceHelper[AppConstants.PREF_KEY_IS_LOGGED_IN] = isLoggedIn
    }

    fun setUserObject(user: User) {
        PreferenceHelper.saveObject(user, AppConstants.PREF_KEY_USER)
    }


    internal fun getTmpFileUri(): Uri {
        return FileProvider.getUriForFile(
            getApplication(), "com.easyfield.provider", createImageFile()
        )
    }

    var currentPhotoPath: String? = null

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val storageDir: File? = getApplication<Kirtiman>().cacheDir
        return File.createTempFile(
            "image_${System.currentTimeMillis()}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            currentPhotoPath = absolutePath
            log { "Current Photo Path ==> $currentPhotoPath" }
            createNewFile()
            deleteOnExit()
        }
    }

    fun compressFile(): MutableLiveData<String>? {
        var result: MutableLiveData<String>? = null
        viewModelScope.launch {
            val originalFile = File(currentPhotoPath!!)
            if (originalFile != null && originalFile.isFile && originalFile.exists()) {
                result = MutableLiveData<String>()
                log { "Original File Size (In KB): ${originalFile.length() / 1024}" }
                val compressedFile = originalFile.compress(getApplication())
                log { "Compressed File Size (In KB): ${compressedFile.length() / 1024}" }
                result!!.postValue(compressedFile.absolutePath)
            }
        }
        return result
    }
}