package sidev.app.course.dicoding.moviecatalog1.util

import android.content.Context
import androidx.core.view.get
import androidx.viewpager2.widget.ViewPager2
import com.android.volley.NetworkResponse
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.HttpHeaderParser
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import org.jetbrains.anko.runOnUiThread
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import sidev.app.course.dicoding.moviecatalog1.R
import sidev.app.course.dicoding.moviecatalog1.data.model.ShowDetail
import sidev.app.course.dicoding.moviecatalog1.data.Result
import sidev.app.course.dicoding.moviecatalog1.data.Failure
import sidev.app.course.dicoding.moviecatalog1.data.Success
import sidev.lib.android.std.tool.util._NetworkUtil
import sidev.lib.structure.data.value.varOf
import java.io.IOException
import java.lang.IllegalStateException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

object Util {
    /**
     * Gson singleton for this project.
     */
    val gson: Gson by lazy { Gson() }


    suspend fun <T> Call<T>.get(): Result<T> = suspendCoroutine { continuation ->
        onResult {
            continuation.resume(it)
        }
    }


    /**
     * Convenient way to give callback to retrofit [Call].
     */
    private fun <T> Call<T>.onResult(c: (Result<T>) -> Unit) {
        enqueue(object : Callback<T> {
            /**
             * Invoked for a received HTTP response.
             *
             *
             * Note: An HTTP response may still indicate an application-level failure such as a 404 or 500.
             * Call [Response.isSuccessful] to determine if the response indicates success.
             */
            override fun onResponse(call: Call<T>, response: retrofit2.Response<T>) {
                c(
                    if(response.isSuccessful){
                        response.body()?.let { Success(it) }
                            ?: Failure(response.code(), IllegalStateException("Can't instantiate class 'T'"))
                    } else
                        Failure(response.code(), IOException(response.message()))
                )
            }

            /**
             * Invoked when a network exception occurred talking to the server or when an unexpected exception
             * occurred creating the request or processing the response.
             */
            override fun onFailure(call: Call<T>, t: Throwable) {
                c(Failure(1, t))
            }
        })
    }

    fun httpGet(
        c: Context?, // Nullable so it is easier to unit-test this.
        url: String,
        onError: ((code: Int, e: Exception) -> Unit)?= null,
        onResponse: (code: Int, content: String) -> Unit,
    ) : Job = GlobalScope.launch(Dispatchers.IO) {
        if(c != null){
            if(_NetworkUtil.isNetworkActive(c)){
                Volley.newRequestQueue(c).add(
                    createVolleyRequest(
                        Request.Method.GET, url, onError, onResponse
                    ))
            } else {
                c.runOnUiThread {
                    toast(c.getString(R.string.toast_check_connection))
                }
            }
        } else {
            val client = OkHttpClient()
            val call = okhttp3.Request.Builder()
                .url(url)
                .get()
                .build()
            try {
                val response = client.newCall(call).execute()
                onResponse(response.code, response.body?.string() ?: response.message)
            } catch (e: IOException) {
                // When error that client doesn't even get the response from server.
                onError?.invoke(-1, e)
            }
        }
    }

    private fun createVolleyRequest(
        method: Int,
        url: String,
        onError: ((code: Int, e: VolleyError) -> Unit)?= null,
        onResponse: (code: Int, content: String) -> Unit,
    ): StringRequest {
        val code= varOf(-1)
        return object: StringRequest(
            method,
            url,
            Response.Listener { onResponse(code.value, it) },
            Response.ErrorListener { onError?.invoke(it.networkResponse?.statusCode ?: -1, it) }
        ) {
            override fun parseNetworkResponse(response: NetworkResponse?): Response<String> {
                response?.also {
                    code.value= it.statusCode
                    if(it.statusCode.toString().startsWith("4")){
                        return Response.success(
                            response.headers?.get("Content-Length"),
                            HttpHeaderParser.parseCacheHeaders(response)
                        )
                    }
                }
                return super.parseNetworkResponse(response)
            }
        }
    }

    fun formatDate(dateString: String): String {
        val dates = dateString.split("-")
        val cal = Calendar.getInstance()
        cal.set(dates[0].toInt(), dates[1].toInt(), dates[2].toInt())
        val sdf = SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH)
        return sdf.format(cal.time)
    }

    fun Context.getDurationString(showDetail: ShowDetail): String? {
        val dur = showDetail.duration ?: return null
        val hour = dur / 60
        val minute = dur % 60
        return getString(R.string.duration, hour, minute)
    }

    fun BottomNavigationView.setupWithViewPager(vp: ViewPager2, idMapper: (id: Int) -> Int){
        var isInternallyChanged = true
        setOnNavigationItemSelectedListener {
            isInternallyChanged = false
            vp.currentItem = idMapper(it.itemId)
            isInternallyChanged = true
            true
        }
        vp.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                if(isInternallyChanged){ //So it won't cause an infinite loop.
                    this@setupWithViewPager.selectedItemId = menu[position].itemId
                }
            }
        })
    }

    fun JsonObject.getString(key: String): String = getAsJsonPrimitive(key).asString
    fun JsonObject.getIntOrNull(key: String): Int? = if(has(key)) getAsJsonPrimitive(key).asInt else null
    fun JsonObject.getDouble(key: String): Double = getAsJsonPrimitive(key).asDouble
}