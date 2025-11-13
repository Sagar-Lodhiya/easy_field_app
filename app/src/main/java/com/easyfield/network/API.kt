package com.easyfield.network

import com.easyfield.attendance.response.attendancedetail.AttendanceDetailResponse
import com.easyfield.attendance.response.attendancelist.AttendanceListResponse
import com.easyfield.attendance.response.leavelist.LeaveListResponse
import com.easyfield.base.response.commonresponse.CommonResponse
import com.easyfield.base.response.dropdownresponse.DropDownResponse
import com.easyfield.dashboard.response.HomeResponse
import com.easyfield.expense.response.expensedetail.ExpenseDetailResponse
import com.easyfield.expense.response.expenselist.ExpenseListResponse
import com.easyfield.location.models.BulkLocationRequest
import com.easyfield.login.response.loginresponse.LoginResponse
import com.easyfield.menu.response.MenuResponse
import com.easyfield.notification.response.NotificationListResponse
import com.easyfield.party.response.PartyListResponse
import com.easyfield.party.response.partydetailresponse.PartyDetailResponse
import com.easyfield.payment.response.PaymentListResponse
import com.easyfield.payment.response.paymentDetailresponse.PaymentDetailResponse
import com.easyfield.profile.response.ProfileResponse
import com.easyfield.punchin.response.PunchResponse
import com.easyfield.splash.models.settingResponse.SettingResponse
import com.easyfield.visit.response.visitdetail.VisitDetailResponse
import com.easyfield.visit.response.visitlistresponse.VisitListResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query


interface API {


    @GET("setting")
    suspend fun getSetting():Response<SettingResponse>

    @GET("dropdown")
    suspend fun getDropDown():Response<DropDownResponse>

    @FormUrlEncoded
    @POST("login")
    suspend fun login(@FieldMap queryMap: Map<String, String>? = mapOf()): Response<LoginResponse>

    @GET("profile")
    suspend fun getProfile():Response<ProfileResponse>

    @Multipart
    @POST("profile")
    suspend fun updateProfile(

        @Part("name") name: RequestBody?,
        @Part("last_name") last_name: RequestBody?,
        @Part("email") email: RequestBody?,
        @Part image: MultipartBody.Part?
    ): Response<CommonResponse>

    @Multipart
    @POST("profile")
    suspend fun updateProfileNoPhoto(
        @Part("name") name: RequestBody?,
        @Part("last_name") last_name: RequestBody?,
        @Part("email") email: RequestBody?,
    ): Response<CommonResponse>



    @GET("home")
    suspend fun getHome():Response<HomeResponse>

    @GET("menu")
    suspend fun getMenu():Response<MenuResponse>

    @POST("logout")
    suspend fun logout():Response<CommonResponse>

    @Multipart
    @POST("punch-in")
    suspend fun punchIn(
        @Part("latitude") lat: Double?,
        @Part("longitude") lng: Double?,
        @Part("place") place: RequestBody?,
        @Part("vehicle_type") vehicleType: RequestBody?,
        @Part("meter_reading_in_km") meterReading: Double?,
        @Part("battery") battery: RequestBody?,
        @Part("mobile_network") mobile_network: RequestBody?,
        @Part image: MultipartBody.Part?,
        @Part("punch_in_type") punch_in_type: RequestBody?,
    ): Response<PunchResponse>

    @Multipart
    @POST("punch-out")
    suspend fun punchOut(
        @Part("latitude") lat: Double?,
        @Part("longitude") lng: Double?,
        @Part("place") place: RequestBody?,
        @Part("vehicle_type") vehicleType: RequestBody?,
        @Part("meter_reading_in_km") meterReading: Double?,
        @Part("battery") battery: RequestBody?,
        @Part("mobile_network") mobile_network: RequestBody?,
        @Part("tour_details") tour_details: RequestBody?,
        @Part("is_night_stay") isNightStay: Boolean?,
        @Part image: MultipartBody.Part?,
        @Part("punch_in_type") punch_in_type: RequestBody?,
    ): Response<PunchResponse>

    @Multipart
    @POST("start-tracking")
    suspend fun startTracking(
        @Part("latitude") lat: Double?,
        @Part("longitude") lng: Double?,
        @Part("place") place: RequestBody?,
        @Part("vehicle_type") vehicleType: RequestBody?,
        @Part("meter_reading_in_km") meterReading: Double?,
        @Part("battery") battery: RequestBody?,
        @Part("mobile_network") mobile_network: RequestBody?,
        @Part image: MultipartBody.Part?,
        @Part("punch_in_type") punch_in_type: RequestBody?,
    ): Response<PunchResponse>

    @Multipart
    @POST("stop-tracking")
    suspend fun stopTracking(
        @Part("latitude") lat: Double?,
        @Part("longitude") lng: Double?,
        @Part("place") place: RequestBody?,
        @Part("vehicle_type") vehicleType: RequestBody?,
        @Part("meter_reading_in_km") meterReading: Double?,
        @Part("battery") battery: RequestBody?,
        @Part("mobile_network") mobile_network: RequestBody?,
        @Part("tour_details") tour_details: RequestBody?,
        @Part("is_night_stay") isNightStay: Boolean?,
        @Part image: MultipartBody.Part?
    ): Response<PunchResponse>

    @Multipart
    @POST("log")
    suspend fun sendLocation(@Part("latitude") lat: Double?,
                             @Part("longitude") lng: Double?,
                             @Part("punch_in_id") place: RequestBody?,
                             @Part("is_location_enabled") isLocationEnabled: Int?,
                             @Part("battery") battery: Int,
                             @Part("mobile_network") mobile_network: RequestBody?): Response<CommonResponse>

    @POST("bulk-location")
    suspend fun bulkPostLocation(@Body list: BulkLocationRequest): Response<CommonResponse>


    @GET("attendance")
    suspend fun getAttedance(@Query ("month") month:Int,
                             @Query ("year") year:Int,):Response<AttendanceListResponse>

    @GET("attendance")
    suspend fun attendanceDetail(@Query ("id") id:String):Response<AttendanceDetailResponse>



    @GET("leave")
    suspend fun leaveList(@Query ("LeavesSearch[month]") month:Int,
                             @Query ("LeavesSearch[year]") year:Int,):Response<LeaveListResponse>


    @Multipart
    @POST("leave")
    suspend fun addLeave(@Part("leave_type_id") leave_type_id: Int?,
                             @Part("start_date") start_date: RequestBody?,
                             @Part("end_date") end_date: RequestBody?,
                             @Part("reason") reason: RequestBody?): Response<CommonResponse>

    @GET("expense")
    suspend fun expenseList(@Query ("ExpensesSearch[expense_type]") expense:String):Response<ExpenseListResponse>

    @GET("expense")
    suspend fun expenseList(@Query ("ExpensesSearch[expense_type]") expense:String,@Query ("ExpensesSearch[status_id]") status:String):Response<ExpenseListResponse>

    @Multipart
    @POST("expense")
    suspend fun addExpense(
        @Part("city_id") city_id: Int?,
        @Part("category_id") category_id: Int?,
        @Part("requested_amount") requested_amount: RequestBody?,
        @Part("expense_details") expense_details: RequestBody?,
        @Part("expense_type") expense_type: RequestBody?,
        @Part image: MultipartBody.Part?,
        @Part("expense_date") expense_date: RequestBody?,
    ): Response<CommonResponse>

    @GET("expense")
    suspend fun expenseDetail(@Query ("id") id:String):Response<ExpenseDetailResponse>


    @GET("visit")
    suspend fun visitList():Response<VisitListResponse>


    @GET("visit")
    suspend fun visitDetail(@Query ("id") id:String):Response<VisitDetailResponse>




    @Multipart
    @POST("visit")
    suspend fun addVisit(
        @Part("created_at") created_at: RequestBody?,
        @Part("time") time: RequestBody?,
        @Part("place") place: RequestBody?,
        @Part("party_id") party_id: RequestBody?,


        @Part("duration") duration: RequestBody?,

        @Part("discussion_point") discuss_point: RequestBody?,
        @Part("remark") remark: RequestBody?,
        @Part("latitude") latitude: Double?,
        @Part("longitude") longitude: Double?,
    ): Response<CommonResponse>



    @GET("payment")
    suspend fun paymentList():Response<PaymentListResponse>

    @GET("payment")
    suspend fun paymentDetail(@Query ("id") id:String):Response<PaymentDetailResponse>


    @Multipart
    @POST("payment")
    suspend fun addPayment(
        @Part("created_at") created_at: RequestBody?,
        @Part("party_id") party_id: Int?,
        @Part("amount") amount: Double?,
        @Part("amount_type") amount_type: RequestBody?,
        @Part("amount_details") amount_details: RequestBody?,
        @Part("collection_of") collection_of: RequestBody?,


        @Part("payment_details") payment_details: RequestBody?,
        @Part("extra") extra: RequestBody?,
        @Part image: MultipartBody.Part?
    ): Response<CommonResponse>

    @GET("parties")
    suspend fun partyList():Response<PartyListResponse>


    @GET("parties")
    suspend fun partyDetail(@Query ("id") id:String):Response<PartyDetailResponse>



    @Multipart
    @POST("parties")
    suspend fun addParty(
        @Part("firm_name") firm_name: RequestBody?,
        @Part("dealer_name") dealer_name: RequestBody?,
        @Part("party_category_id") party_category_id: Int,
        @Part("dealer_phone") dealer_phone: RequestBody?,


        @Part("city_or_town") city_or_town: RequestBody?,

        @Part("address") address: RequestBody?,
        @Part("gst_number") gst_number: RequestBody?,  @Part("dealer_aadhar") dealer_aadhar: RequestBody?,

    ): Response<CommonResponse>

    @GET("notification")
    suspend fun notificationList():Response<NotificationListResponse>

    @DELETE("notification")
    suspend fun clearNotification():Response<CommonResponse>

    @Multipart
    @POST("notification")
    suspend fun clearNotification(@Part("id") notiicationId: Int ):Response<CommonResponse>
}