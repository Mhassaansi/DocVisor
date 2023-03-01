package com.fictivestudios.docsvisor.helper

import com.fictivestudios.docsvisor.apiManager.response.DoctorData
import com.fictivestudios.docsvisor.apiManager.response.FitnessData
import com.fictivestudios.docsvisor.apiManager.response.PatientProfileData


//const val BASE_URL = "https://server.appsstaging.com/3048/"

const val BASE_URL = "https://glucovital.com/webservices/live/api/"

//const val AGORA_BASE_URL = "https://server.appsstaging.com:3090/"

const val AGORA_BASE_URL = "https://client1.appsstaging.com:3000"

const val SOCKET_BASE_URL = "https://client1.appsstaging.com:3001"

const val WATCH_BASE_URL = "https://server.appsstaging.com/2522/docsvisor/api/"

const val SYMPTON_CHECKER_BASE_URL = "https://priaid-symptom-checker-v1.p.rapidapi.com/"

const val X_RapidAPI_Key = "02229dc003msh0c443063d9830bdp11a6e9jsne4e4ab5936a6"

const val DOCTOR_THRID_PARTY_BASE = "https://data.cms.gov/provider-data/api/1/datastore/query/mj5m-pzi6/0/"

const val DOCTOR_THRID_PARTY = "?offset=0&count=true&results=true&schema=true&keys=true&format=json&rowIds=false"

//*** URLS ***
const val LOGIN_URL = "auth/login"
const val SOCIAL_LOGIN_URL = "auth/social"

const val VERIFY_OTP_URL = "verify-otp"
const val RESEND_OTP_URL = "auth/signup/resend-otp"
const val SIGNUP_URL = "auth/signup"
const val FORGET_PASSWORD_URL = "auth/password/forgot"
const val CHANGE_PASSWORD_URL = "auth/password/change"
const val RESET_PASSWORD_URL = "auth/password/reset"

const val GET_WATCH_DATA = "seer-health"




const val UPDATE_PROFILE_URL ="profile"
const val DELETE_CERTIFICATE ="delete/certificate"
const val CONTENT_URL ="content"
const val ADD_SCHEDULE_URL ="schedule"
const val GET_SCHEDULE_URL ="user/schedule"
const val GET_SCHEDULE_DOCTOR_URL ="doctor/schedule"
const val ADD_OFF_DAYS_URL ="offdays/create"
const val GET_OFF_DAYS_URL ="offdays/index"
const val DELETE_OFF_DAYS_URL ="offdays/delete"
const val DELETE_ACCOUNT_URL = "delete-account"
const val LOGOUT_URL ="auth/logout"
const val DOCTORS_URL ="doctors"
const val IS_SOCIAL_LOGIN="isSocialLogin"
/*const val PATIENTS_URL ="doctors"*/
const val WATCH_DATA_URL ="seer-health"
const val BOOK_APPOINTMENT_URL ="appointment/create"
const val GET_APPOINTMENT_URL ="appointment/index"
const val ACCEPT_APPOINTMENT_URL ="appointment/accept"
const val ADD_ALARM_URL ="alaram/create"
const val GET_ALARM_URL ="alaram/index"

const val DELETE_ALARM_URL ="alaram/delete"
const val GET_PATIENT_URL ="patients"
const val GET_PATIENT_PROFILE_URL ="patient/profile"
const val GET_FITNESS_URL ="fitness/index"
const val UPDATE_FITNESS_URL ="fitness/create"
const val GET_GRAPH_DATA_URL ="test/graph"


const val GET_GRAPH_INDEX_URL ="test/index"

const val GET_TEST_HISTORY_URL ="test/history"

const val CREATE_TEST_URL ="test/create"

const val WATCH_CHECK_BOUND ="binding"

const val CALIBRATE_URL ="calibration"

const val CHAT_LIST_URL ="chat"
const val CHAT_MEDIA_URL ="chat/media"

const val GET_NOTIFICATION_URL ="notification/index"
const val DELETE_NOTIFICATION_URL ="notification/delete"

const val VIDEO_CALL_URL ="rtctoken"

const val REJECT_CALL_URL ="reject-call"
//****************************//


const val OTP_REF_CODE ="otpReferenceCode"
const val OTP ="otpCode"
const val EMAIL ="email"
const val USER_OBJECT ="userObject"
var VERIFY_TYPE_ACCOUNT ="ACCOUNT_VERIFICATION"
var TYPE_ACCOUNT_VERIFICATION="ACCOUNT_VERIFICATION"
var TYPE_PASSWORD_RESET="PASSWORD_RESET"
var FITNESS_TYPE_FITNESS="fitness"
var FITNESS_TYPE_DIET="diet"
var FITNESS_TYPE_MEDICS="medics"
var FITNESS_TYPE="diet"


const val SOCIAL_TOKEN = "socialToken"
const val SOCIAL_NAME = "socialName"

var TEST_TYPE_WEEK="week"
var TEST_TYPE_MONTH="month"

var USER_TYPE_PATIENT="patient"
var USER_TYPE_DOCTOR="doctor"

var MESSAGE_TYPE_DOCUMENT="document"
var MESSAGE_TYPE_MEDIA="media"
var MESSAGE_TYPE_TEXT="text"
var FILE_TYPE_PHOTO="image"
var FILE_TYPE_DOC="pdf"

var CALL_TYPE_VIDEO="video"
var CALL_TYPE_AUDIO="audio"

const val AGE="age"
const val ACCESS_TOKEN ="accessToken"
const val FCM ="fcm"
const val REJECT_CALL ="rejectCall"


const val VIEW_TYPE_MESSAGE_RECEIVED =145
const val VIEW_TYPE_MESSAGE_SENT =169




//**************SYMPTOMS CHECKERS******************//


const val BODY_PART_URL ="body/locations?"
const val SUB_BODY_PART_URL ="body/locations/{location}?"
const val SYMPTOMS_URL ="symptoms?"
const val SUB_SYMPTOMS_URL ="symptoms/{locationid}/{selectorstatus}?"
const val HEALTH_ISSUES_URL ="issues?"
const val HEALTH_ISSUES2_URL ="issues/{issue_id}/info?"
const val DIAGONSIS_URL ="diagnosis?"
const val PROPOSED_URL ="symptoms/proposed?"
const val SPECIFICATION_DIAGONIS_URL ="diagnosis/specialisations?"
const val SPECIFICATION_URL ="specialisations?"


//////////////**************OBJECTS*********************//////
var doctorData: DoctorData? = null
var patientFitnessdata: PatientProfileData?= null
var arrayFitnessList:ArrayList<FitnessData>?= ArrayList()
var dayNote:String? = null

