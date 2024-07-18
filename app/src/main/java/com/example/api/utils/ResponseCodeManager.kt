package com.example.api.utils

import org.json.JSONObject
import retrofit2.Response

object ResponseCodeManager {
    fun checkRetrofitApiResponse(response: Response<*>): String {
        when (response.code()) {
            401 -> {
                return Constant.STS_401
            }
            403 -> {
                return Constant.STS_403
            }
            404 -> {
                return Constant.STS_404
            }
            408 -> {
                return Constant.STS_408
            }
            422 -> {
                return try {
                    val errorString = response.errorBody()?.byteStream()?.bufferedReader()
                        .use { it?.readText() } // defaults to UTF-8
                    val json = JSONObject(errorString)
                    json["message"].toString()
                } catch (e: Exception) {
                    "Invalid Credentials"
                }
            }
            500 -> {
                return Constant.STS_500
            }
            502 -> {
                return Constant.STS_502
            }
            503 -> {
                return Constant.STS_503
            }
            504 -> {
                return Constant.STS_504
            }
            else -> {
                return Constant.STS_DEFAULT
            }
        }
    }

    fun checkNormalApiResponse(statusCode:Int,responseString: String): String {
        when (statusCode) {
            401 -> {
                return Constant.STS_401
            }
            403 -> {
                return Constant.STS_403
            }
            404 -> {
                return Constant.STS_404
            }
            408 -> {
                return Constant.STS_408
            }
            422 -> {
                return  try {
                    val json = JSONObject(responseString)
                    json["message"].toString()
                } catch (e: Exception) {
                    "Invalid Credentials"
                }
            }
            500 -> {
                return Constant.STS_500
            }
            502 -> {
                return Constant.STS_502
            }
            503 -> {
                return Constant.STS_503
            }
            504 -> {
                return Constant.STS_504
            }
            else -> {
                return Constant.STS_DEFAULT
            }
        }

    }

}
