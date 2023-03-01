package com.fictivestudios.docsvisor.apiManager.client

import com.fictivestudios.docsvisor.apiManager.requestBody.AgoraRequestBody
import com.fictivestudios.docsvisor.apiManager.response.*
import com.fictivestudios.docsvisor.apiManager.response.doctorthird.DoctorThridPartApiModel
import com.fictivestudios.docsvisor.apiManager.response.symptonchecker.*
import com.fictivestudios.docsvisor.helper.*
import com.fictivestudios.docsvisor.model.RejectCallRequestBody
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*
import java.util.ArrayList

interface ApiInterface {

/*
    @Headers("Content-Type:application/json")
*/
/*
    @POST(SIGNUP_URL)
    fun signup(@Body info: RequestBody): retrofit2.Call<SignupResponse>
*/

    @Multipart
    @POST(SIGNUP_URL)
    fun signup(@PartMap hashMap: HashMap<String, RequestBody>, @Part image:MultipartBody.Part?, @Part certifications: ArrayList<MultipartBody.Part>?): retrofit2.Call<SignupResponse>


    @POST(SOCIAL_LOGIN_URL)
    fun socialLogin(@Body info: RequestBody): retrofit2.Call<LoginResponse>

    @POST(LOGIN_URL)
    fun login(@Body info: RequestBody): retrofit2.Call<LoginResponse>

    @POST(VERIFY_OTP_URL)
    fun verifyOTP(@Body info: RequestBody): retrofit2.Call<VerifyOTPResponse>

    @POST(RESEND_OTP_URL)
    fun resendOTP(@Body info: RequestBody): retrofit2.Call<ResendOTPResponse>

    @POST(FORGET_PASSWORD_URL)
    fun forgetPassword(@Body info: RequestBody): retrofit2.Call<ForgetPasswordResponse>

    @POST(CHANGE_PASSWORD_URL)
    fun changePassword(@Body info: RequestBody): retrofit2.Call<ChangePassword>

    //TODO() remove otp
    @POST(RESET_PASSWORD_URL)
    fun resetPassword(@Body info: RequestBody): retrofit2.Call<ChangePassword>

    @Multipart
    @POST(UPDATE_PROFILE_URL)
    fun updateProfile(@PartMap hashMap: HashMap<String,RequestBody>,@Part image:MultipartBody.Part?): retrofit2.Call<UpdateResponse>

    @Multipart
    @POST(UPDATE_PROFILE_URL)
    fun updateDoctorProfile(@PartMap hashMap: HashMap<String,RequestBody>,@Part image:MultipartBody.Part?,@Part certifications: ArrayList<MultipartBody.Part>?): retrofit2.Call<UpdateResponse>

    @POST(DELETE_CERTIFICATE)
    fun deleteCertificate(@Body info: RequestBody): retrofit2.Call<CommonResponse>

    //TODO() need to create response
    @POST(CONTENT_URL)
    fun content(@Query("type") type:String): retrofit2.Call<UpdateResponse>

    @POST(LOGOUT_URL)
    fun logout(): retrofit2.Call<CommonResponse>

    @GET(DELETE_ACCOUNT_URL)
    fun deleteAccount(): retrofit2.Call<CommonResponse>




    /**************  SCHEDULE  ****************/
    @POST(ADD_SCHEDULE_URL)
    fun addSchedule(@Body info: RequestBody): retrofit2.Call<AddScheduleResponse>

    @GET(GET_SCHEDULE_URL)
    fun getSchedulePatient(@Query("doctor_id") doctor_id:String,@Query("date") date:String): retrofit2.Call<ScheduleUserResponse>

    @GET(GET_SCHEDULE_DOCTOR_URL)
    fun getScheduleDoctor(): retrofit2.Call<GetSchedule>

    @POST(ADD_OFF_DAYS_URL)
    fun addOffDays(@Body info: RequestBody): retrofit2.Call<CommonResponse>
    @GET(GET_OFF_DAYS_URL)
    fun getOffDays(): retrofit2.Call<OffDaysRes>

    @POST(DELETE_OFF_DAYS_URL)
    fun deleteOffDays(@Body info: RequestBody): retrofit2.Call<CommonResponse>
    /**************  USERS  ****************/

    @GET(DOCTORS_URL)
    fun doctor(): retrofit2.Call<DoctorResponse>

    @GET(GET_WATCH_DATA)
    fun getWatchData(): retrofit2.Call<AppResponseModel<Any>>


    /**************  APPOINTMENT  ****************/

    @POST(BOOK_APPOINTMENT_URL)
    fun bookAppointment(@Body info: RequestBody): retrofit2.Call<BookAppointmentResponse>

    @GET(GET_APPOINTMENT_URL)
    fun getAppointment(): retrofit2.Call<ViewAppointmentResponse>

    @GET(GET_APPOINTMENT_URL)
    fun getPendingAppointment(@Query("type") type:String): retrofit2.Call<PendingAppointment>

    @POST(ACCEPT_APPOINTMENT_URL)
    fun acceptAppointment(@Body info: RequestBody): retrofit2.Call<CommonResponse>
    /**************  ALARMS  ****************/
    @POST(ADD_ALARM_URL)
    fun addAlarm(@Body info: RequestBody): retrofit2.Call<AlarmResponse>

    @GET(GET_ALARM_URL)
    fun getAlarm(): retrofit2.Call<GetAlarmResponse>

    @POST(DELETE_ALARM_URL)
    fun deleteAlarm(@Body info: RequestBody): retrofit2.Call<CommonResponse>
    /**************  PATIENTS  ****************/
    @GET(GET_PATIENT_URL)
    fun getPatient(): retrofit2.Call<GetPatientResponse>

    @GET(GET_PATIENT_PROFILE_URL)
    fun getPatientProfile(@Query("patient_id") patient_id: String,@Query("category") category: String): retrofit2.Call<GetPatientProfileResponse>

    /**************  FITNESS  ****************/
    @GET(GET_FITNESS_URL)
    fun getFitness(@Query("type") type:String): retrofit2.Call<FitnessResponse>

    @POST(UPDATE_FITNESS_URL)
    fun updateFitness(@Body info: RequestBody): retrofit2.Call<CommonResponse>

    @POST(CREATE_TEST_URL)
    fun addTest(@Body info: RequestBody): retrofit2.Call<CommonResponse>

    @GET(GET_GRAPH_DATA_URL)
    fun getGraphData(@Query("type") type:String,@Query("category") category:String): retrofit2.Call<GraphResponse>

    @GET(GET_GRAPH_INDEX_URL)
    fun getGraphIndex(@Query("type") type:String,@Query("category") category:String): retrofit2.Call<GraphResponse>

    @GET(GET_TEST_HISTORY_URL)
    fun getTestHistory(@Query("category") category:String): retrofit2.Call<TestHistoryResponse>




    @GET(CHAT_LIST_URL)
    fun getChatList(): retrofit2.Call<ChatListResponse>

    @Multipart
    @POST(CHAT_MEDIA_URL)
    fun chatMedia(@Part image:MultipartBody.Part?): retrofit2.Call<ChatMediaResponse>



    @GET(GET_NOTIFICATION_URL)
    fun getNotificationList(): retrofit2.Call<GetNotificationResponse>

    @POST(DELETE_NOTIFICATION_URL)
    fun deleteNotification(@Body info: RequestBody): retrofit2.Call<CommonResponse>


    @POST(WATCH_CHECK_BOUND)
    fun checkBound(@Body info: RequestBody): retrofit2.Call<WatchBindResponse>


    @POST(CALIBRATE_URL)
    fun calibrate(@Body info: RequestBody): retrofit2.Call<CalibrationResponse>

    //video audio call

    @POST(VIDEO_CALL_URL)
    fun videoCall(@Body agoraRequestBody: AgoraRequestBody): retrofit2.Call<VideoCallResponse>

    @POST(REJECT_CALL_URL)
    fun rejectCall(@Body rejectCallRequestBody: RejectCallRequestBody): retrofit2.Call<CommonResponse>


    /**************  DOCTOR THIRDPARTY  ****************/


    @GET(DOCTOR_THRID_PARTY)
    fun doctorThirdAPI(): retrofit2.Call<DoctorThridPartApiModel>


    /**************  SYMPTON CHECKER  ****************/

    @GET(BODY_PART_URL)
    fun bodyPart(@Query("language") language: String , @Header("X-RapidAPI-Key") value:String): retrofit2.Call<BodyPartResponse>

    @GET(SUB_BODY_PART_URL)
    fun subBodyPart(@Path("location") loc :String,@Query("location") location: String,@Query("language") language: String , @Header("X-RapidAPI-Key") value:String): retrofit2.Call<BodyPartResponse>

    @GET(SYMPTOMS_URL)
    fun sypmtons(@Query("language") language: String , @Header("X-RapidAPI-Key") value:String): retrofit2.Call<BodyPartResponse>

    @GET(SUB_SYMPTOMS_URL)
    fun subSympton(@Path("locationid") locid :String,@Path("selectorstatus") gender_selector :String,@Query("selectorstatus") gendertype: String,@Query("locationid") locationid: String,@Query("language") language: String , @Header("X-RapidAPI-Key") value:String): retrofit2.Call<SubSymptonResponse>

    @GET(HEALTH_ISSUES_URL)
    fun healthissues1(@Query("language") language: String , @Header("X-RapidAPI-Key") value:String): retrofit2.Call<BodyPartResponse>


    @GET(HEALTH_ISSUES2_URL)
    fun healthissues2(@Path("issue_id") issuseid :String,@Query("language") language: String , @Header("X-RapidAPI-Key") value:String): retrofit2.Call<IssuesInfoResponse>

    @GET(DIAGONSIS_URL)
    fun diagonsis(@Query("language") language: String, @Query("gender") gender: String, @Query("year_of_birth") YOB: String, @Query("symptoms") symptomList:String , @Header("X-RapidAPI-Key") value:String): retrofit2.Call<DiagonisResponse>

    @GET(PROPOSED_URL)
    fun proposed(@Query("language") language: String,@Query("gender") gender: String,@Query("year_of_birth") YOB: String,@Query("symptoms") symptomList: String, @Header("X-RapidAPI-Key") value:String): retrofit2.Call<BodyPartResponse>

    @GET(SPECIFICATION_DIAGONIS_URL)
    fun diagonisBasedOnSpecification(@Query("language") language: String,@Query("gender") gender: String,@Query("year_of_birth") YOB: String,@Query("symptoms") symptomList: String, @Header("X-RapidAPI-Key") value:String): retrofit2.Call<SpecificationBasedOnDiagonisisResponse>

    @GET(SPECIFICATION_URL)
    fun speciification(@Query("language") language: String , @Header("X-RapidAPI-Key") value:String): retrofit2.Call<BodyPartResponse>


}