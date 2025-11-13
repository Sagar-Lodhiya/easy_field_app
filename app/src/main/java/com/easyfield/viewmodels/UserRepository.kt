package com.easyfield.viewmodels


import com.easyfield.attendance.response.attendancedetail.AttendanceDetailResponse
import com.easyfield.attendance.response.attendancelist.AttendanceListResponse
import com.easyfield.attendance.response.leavelist.LeaveListResponse
import com.easyfield.base.response.commonresponse.CommonResponse
import com.easyfield.base.response.dropdownresponse.DropDownResponse
import com.easyfield.dashboard.response.HomeResponse
import com.easyfield.expense.response.expensedetail.ExpenseDetailResponse
import com.easyfield.expense.response.expenselist.ExpenseListResponse
import com.easyfield.login.response.loginresponse.LoginResponse
import com.easyfield.menu.response.MenuResponse
import com.easyfield.network.RetrofitClient
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
import com.easyfield.visit.request.AddVisitRequest
import com.easyfield.visit.response.visitdetail.VisitDetailResponse
import com.easyfield.visit.response.visitlistresponse.VisitListResponse

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import java.io.File

class UserRepository() {


    suspend fun settings(): Response<SettingResponse> {
        return RetrofitClient.instance.getSetting()
    }

    suspend fun dropdown(): Response<DropDownResponse> {
        return RetrofitClient.instance.getDropDown()
    }

    suspend fun login(
        phoneNumber: String,
        deviceId: String,
        deviceModel: String, appVersion: String, fcmToken: String,
    ): Response<LoginResponse> {
        val requestMap = hashMapOf<String, String>()
        requestMap["username"] = phoneNumber
        requestMap["device_id"] = deviceId
//        requestMap["device_id"]="123456"

        requestMap["device_type"] = "Android"
        requestMap["device_model"] = deviceModel
        requestMap["app_version"] = appVersion
        requestMap["device_token"] = fcmToken
        return RetrofitClient.instance.login(requestMap)
    }

    suspend fun home(): Response<HomeResponse> {
        return RetrofitClient.instance.getHome()
    }

    suspend fun menu(): Response<MenuResponse> {
        return RetrofitClient.instance.getMenu()
    }


    suspend fun logout(): Response<CommonResponse> {
        return RetrofitClient.instance.logout()
    }

    suspend fun punchIn(punchOutRequest: PunchRequest, type: String): Response<PunchResponse> {


        var multiPartBody: MultipartBody.Part? = null
        punchOutRequest.meterReadingPhoto?.let {
            val file = File(it)
            val requestBody = file.asRequestBody(AppConstants.MEDIA_TYPE_IMAGE.toMediaTypeOrNull())
            multiPartBody = MultipartBody.Part.createFormData(
                "image", file.name, requestBody
            )
        }


        if (type.equals("1")) {
            if (punchOutRequest.punch_in_type == "O") {
                return RetrofitClient.instance.punchIn(
                    null,
                    null,
                    null,
                    null,
                    null,
                    null, null, null,
                    punchOutRequest.punch_in_type?.toRequestBody()
                )

            } else {
                return RetrofitClient.instance.punchIn(
                    punchOutRequest.lat,
                    punchOutRequest.lng,
                    punchOutRequest.place?.toRequestBody(),
                    punchOutRequest.vehicleType?.toRequestBody(),
                    punchOutRequest.meterReading,
                    punchOutRequest.battery!!.toRequestBody(),
                    punchOutRequest.mobile_network!!.toRequestBody(),
                    multiPartBody,
                    punchOutRequest.punch_in_type?.toRequestBody()
                )
            }
        } else {

            if (punchOutRequest.punch_in_type == "S") {
                return RetrofitClient.instance.punchOut(
                    punchOutRequest.lat,
                    punchOutRequest.lng,
                    punchOutRequest.place?.toRequestBody(),
                    punchOutRequest.vehicleType?.toRequestBody(),
                    punchOutRequest.meterReading,
                    punchOutRequest.battery!!.toRequestBody(),
                    punchOutRequest.mobile_network!!.toRequestBody(),
                    punchOutRequest.tour_details!!.toRequestBody(),
                    punchOutRequest.isNightStay,
                    multiPartBody,
                    punchOutRequest.punch_in_type?.toRequestBody()
                )
            } else {
                return RetrofitClient.instance.punchOut(
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    punchOutRequest.punch_in_type?.toRequestBody()
                )
            }
        }
    }

    suspend fun tracking(trackingRequest: PunchRequest, type: String): Response<PunchResponse> {


        var multiPartBody: MultipartBody.Part? = null
        trackingRequest.meterReadingPhoto?.let {
            val file = File(it)
            val requestBody = file.asRequestBody(AppConstants.MEDIA_TYPE_IMAGE.toMediaTypeOrNull())
            multiPartBody = MultipartBody.Part.createFormData(
                "image", file.name, requestBody
            )
        }


        if (type.equals("1")) {
            return RetrofitClient.instance.startTracking(
                trackingRequest.lat,
                trackingRequest.lng,
                trackingRequest.place?.toRequestBody(),
                trackingRequest.vehicleType?.toRequestBody(),
                trackingRequest.meterReading,
                trackingRequest.battery!!.toRequestBody(),
                trackingRequest.mobile_network!!.toRequestBody(),
                multiPartBody,
                trackingRequest.punch_in_type?.toRequestBody()
            )
        } else {
            return RetrofitClient.instance.stopTracking(
                trackingRequest.lat,
                trackingRequest.lng,
                trackingRequest.place?.toRequestBody(),
                trackingRequest.vehicleType?.toRequestBody(),
                trackingRequest.meterReading,
                trackingRequest.battery!!.toRequestBody(),
                trackingRequest.mobile_network!!.toRequestBody(),
                trackingRequest.tour_details!!.toRequestBody(),
                trackingRequest.isNightStay,
                multiPartBody,
            )
        }
    }

    suspend fun getAttendance(month: Int, year: Int): Response<AttendanceListResponse> {
        return RetrofitClient.instance.getAttedance(month, year)
    }

    suspend fun attendanceDetail(id: String): Response<AttendanceDetailResponse> {
        return RetrofitClient.instance.attendanceDetail(id)
    }


    suspend fun getLeaveList(month: Int, year: Int): Response<LeaveListResponse> {
        return RetrofitClient.instance.leaveList(month, year)
    }

    suspend fun addLeave(
        leave_type_id: Int?,
        start_date: String,
        end_date: String,
        reason: String
    ): Response<CommonResponse> {
        return RetrofitClient.instance.addLeave(
            leave_type_id,
            start_date.toRequestBody(),
            end_date.toRequestBody(),
            reason.toRequestBody()
        )
    }


    suspend fun getProfile(): Response<ProfileResponse> {
        return RetrofitClient.instance.getProfile()
    }

    suspend fun updateProfile(
        name: String,
        lastname: String,
        email: String,
        photo: String
    ): Response<CommonResponse> {


        var multiPartBody: MultipartBody.Part? = null

        if (photo.isEmpty()) {
            return RetrofitClient.instance.updateProfileNoPhoto(
                name.toRequestBody(),
                lastname.toRequestBody(),
                email.toRequestBody()
            )
        } else {
            photo.let {
                val file = File(it)
                val requestBody =
                    file.asRequestBody(AppConstants.MEDIA_TYPE_IMAGE.toMediaTypeOrNull())
                multiPartBody = MultipartBody.Part.createFormData(
                    "profile", file.name, requestBody
                )
            }

            return RetrofitClient.instance.updateProfile(
                name.toRequestBody(),
                lastname.toRequestBody(),
                email.toRequestBody(),
                multiPartBody
            )
        }


    }

    suspend fun expenseList(type: String, status: String = ""): Response<ExpenseListResponse> {

        if (status.isNotEmpty()) {
            return RetrofitClient.instance.expenseList(type, status)
        } else {
            return RetrofitClient.instance.expenseList(type)
        }
    }

    suspend fun expenseDetail(id: String): Response<ExpenseDetailResponse> {
        return RetrofitClient.instance.expenseDetail(id)
    }

    suspend fun addExpense(
        city_id: Int, category_id: Int, requested_amount: String, expense_details: String,
        expense_type: String,
        strphotopath: String, date: String
    ): Response<CommonResponse> {

        var multiPartBody: MultipartBody.Part? = null
        strphotopath.let {
            val file = File(it)
            val requestBody = file.asRequestBody(AppConstants.MEDIA_TYPE_IMAGE.toMediaTypeOrNull())
            multiPartBody = MultipartBody.Part.createFormData(
                "expense_photo", file.name, requestBody
            )
        }
        return RetrofitClient.instance.addExpense(
            city_id,
            category_id,
            requested_amount.toRequestBody(),
            expense_details.toRequestBody(),
            expense_type.toRequestBody(),
            multiPartBody,
            date.toRequestBody()
        )


    }

    suspend fun visitList(): Response<VisitListResponse> {
        return RetrofitClient.instance.visitList()
    }


    suspend fun visitDetail(id: String): Response<VisitDetailResponse> {
        return RetrofitClient.instance.visitDetail(id)
    }


    suspend fun addVisit(addVisitRequest: AddVisitRequest): Response<CommonResponse> {


        return RetrofitClient.instance.addVisit(
            addVisitRequest.created_at!!.toRequestBody(),
            addVisitRequest.time!!.toRequestBody(),
            addVisitRequest.place!!.toRequestBody(),
            addVisitRequest.partyId!!.toRequestBody(),
            addVisitRequest.duration!!.toRequestBody(),
            addVisitRequest.discussionPoint!!.toRequestBody(),
            addVisitRequest.remark!!.toRequestBody(),
            addVisitRequest.lat,
            addVisitRequest.long
        )


    }

    suspend fun paymentList(): Response<PaymentListResponse> {
        return RetrofitClient.instance.paymentList()
    }

    suspend fun paymentDetail(id: String): Response<PaymentDetailResponse> {
        return RetrofitClient.instance.paymentDetail(id)
    }


    suspend fun addPayment(addPaymentRequest: AddPaymentRequest): Response<CommonResponse> {

        var multiPartBody: MultipartBody.Part? = null
        addPaymentRequest.file.let {
            val file = File(it)
            val requestBody = file.asRequestBody(AppConstants.MEDIA_TYPE_IMAGE.toMediaTypeOrNull())
            multiPartBody = MultipartBody.Part.createFormData(
                "payments_photo", file.name, requestBody
            )
        }
        return RetrofitClient.instance.addPayment(
            addPaymentRequest.created_at!!.toRequestBody(),
            addPaymentRequest.partyId,
            addPaymentRequest.amount,
            addPaymentRequest.amountType!!.toRequestBody(),
            addPaymentRequest.amount_details!!.toRequestBody(),
            addPaymentRequest.collectionOf!!.toRequestBody(),
            addPaymentRequest.paymentDetails!!.toRequestBody(),
            addPaymentRequest.extra!!.toRequestBody(),
            multiPartBody
        )


    }


    suspend fun partyList(): Response<PartyListResponse> {
        return RetrofitClient.instance.partyList()
    }

    suspend fun partyDetail(id: String): Response<PartyDetailResponse> {
        return RetrofitClient.instance.partyDetail(id)
    }


    suspend fun addParty(addPartyRequest: AddPartyRequest): Response<CommonResponse> {


        return RetrofitClient.instance.addParty(
            addPartyRequest.firm_name!!.toRequestBody(),
            addPartyRequest.dealer_name!!.toRequestBody(),
            addPartyRequest.party_category_id!!,
            addPartyRequest.dealer_phone!!.toRequestBody(),
            addPartyRequest.dealer_city!!.toRequestBody(),
            addPartyRequest.dealer_address!!.toRequestBody(),
            addPartyRequest.gst_no!!.toRequestBody(),
            addPartyRequest.aadhar_no!!.toRequestBody()
        )
    }

    suspend fun notificationList(): Response<NotificationListResponse> {
        return RetrofitClient.instance.notificationList()
    }

    suspend fun clearNotification(notificationid: Int = 0): Response<CommonResponse> {

        if (notificationid == 0) {
            return RetrofitClient.instance.clearNotification()
        } else {
            return RetrofitClient.instance.clearNotification(notificationid)
        }
    }

}