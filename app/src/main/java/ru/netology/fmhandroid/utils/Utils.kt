package ru.netology.fmhandroid.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import retrofit2.Response
import ru.netology.fmhandroid.exceptions.ApiException
import ru.netology.fmhandroid.exceptions.ServerException
import ru.netology.fmhandroid.exceptions.UnknownException
import java.io.IOException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

object Utils {

    fun convertDate(dateTime: String): String {

        val localDateTime = LocalDateTime.parse(dateTime.toString())
        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern(
            "dd.MM.yyy",
            Locale.getDefault()
        )
        return formatter.format(localDateTime)
    }

    fun convertTime(dateTime: Long): String {

        val localDateTime = LocalDateTime.parse(dateTime.toString())
        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern(
            "HH-mm",
            Locale.getDefault()
        )
        return formatter.format(localDateTime)
    }

    suspend fun <T, R> makeRequest(
        request: suspend () -> Response<T>,
        onSuccess: suspend (body: T) -> R
    ): R {
        try {
            val response = request()
            if (!response.isSuccessful) {
                throw ApiException(response.code(), response.message())
            }
            val body =
                response.body() ?: throw ApiException(response.code(), response.message())
            return onSuccess(body)
        } catch (e: IOException) {
            throw ServerException
        } catch (e: Exception) {
            e.printStackTrace()
            throw UnknownException
        }
    }

    fun shortUserNameGenerator(lastName: String, firstName: String, middleName: String): String {
        return "$lastName ${firstName.first().uppercase()}. ${middleName.first().uppercase()}."
    }

    fun fullUserNameGenerator(lastName: String, firstName: String, middleName: String): String {
        return "$lastName $firstName $middleName"
    }

    fun hideKeyboard(view: View) {
        val imm =
            view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}