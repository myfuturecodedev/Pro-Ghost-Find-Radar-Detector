package com.futurecode.ghostfinderradardetector.utils
import com.futurecode.ghostfinderradardetector.activity.MyApplication
import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityManager
import android.app.DatePickerDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.net.Uri
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowManager
import android.widget.NumberPicker
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import com.futurecode.ghostfinderradardetector.R
import com.futurecode.ghostfinderradardetector.ads.AdInterface
import com.futurecode.ghostfinderradardetector.ads.interstitial_ad.FullScreenAdsHelper
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONArray
import java.lang.reflect.Field
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.Random

object Utils {
    fun Float.dpToPx(activity: Activity): Float =
        this * activity.resources.displayMetrics.density

    fun NumberPicker.removeDividers() {
        try {
            val fields: Array<Field> = NumberPicker::class.java.declaredFields
            for (field in fields) {
                if (field.name == "mSelectionDivider") {
                    field.isAccessible = true
                    field.set(this, null)
                    break
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @SuppressLint("SoonBlockedPrivateApi")
    fun NumberPicker.applySelectedColor(hexColor: String) {
        // Color selected number green, others gray
        try {
            val selectorWheelPaintField =
                NumberPicker::class.java.getDeclaredField("mSelectorWheelPaint")
            selectorWheelPaintField.isAccessible = true
            (selectorWheelPaintField.get(this) as android.graphics.Paint).apply {
                color = Color.parseColor(hexColor)
                textSize = 52f * resources.displayMetrics.scaledDensity
                typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            }
            invalidate()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // Use a custom formatter to style non-selected items gray
        this.setFormatter { value -> value.toString() }
    }

    fun getWidth(context: Context): Int {
        val displayMetrics = DisplayMetrics()
        val windowmanager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        windowmanager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.widthPixels
    }

    fun formatTimestampToHours(timestamp: Long): String {
        val diffMinutes = (System.currentTimeMillis() - timestamp) / (1000 * 60)

        return when {
            diffMinutes < 60 -> "just now"
            else -> {
                val hours = diffMinutes / 60
                if (hours == 1L) "1 hr" else "$hours hr"
            }
        }
    }

    fun isMyServiceRunning(context: Context, serviceClass: Class<*>): Boolean {
        val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) return true
        }
        return false
    }

    fun showDatePickerDialog(
        context: Context,
        yearr: Int,
        initialYear: Int?,
        initialMonth: Int?,
        initialDay: Int?,
        selectedDate: (String) -> Unit
    ) {
        val calendar = Calendar.getInstance()

        // If initial date values are provided, set the calendar to that date.
        if (initialYear != null && initialMonth != null && initialDay != null) {
            // Subtract 1 from initialMonth because months are 0-based in DatePickerDialog and Calendar
            calendar.set(initialYear, initialMonth - 1, initialDay)
        } else {
            // Otherwise, allow selection up to `yearr` years in the past
            calendar.add(Calendar.YEAR, -yearr)
        }

        val selectedYear = calendar.get(Calendar.YEAR)
        val selectedMonth = calendar.get(Calendar.MONTH)
        val selectedDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        // Set the maxDate to allow the user to select dates from `yearr` years ago
        val maxCalendar = Calendar.getInstance()
        maxCalendar.add(Calendar.YEAR, -yearr)
        val maxDate = maxCalendar.timeInMillis

        // Create the DatePickerDialog
        val datePickerDialog = DatePickerDialog(
            context,
            { _, pickedYear, pickedMonth, pickedDayOfMonth ->
                val selectedCalendar = Calendar.getInstance().apply {
                    set(pickedYear, pickedMonth, pickedDayOfMonth)
                }

                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val formattedDate = dateFormat.format(selectedCalendar.time)

                selectedDate(formattedDate) // Callback function with the selected date
            },
            selectedYear,
            selectedMonth,
            selectedDayOfMonth
        )

        // Set the max date to limit the selection to `yearr` years ago
        datePickerDialog.datePicker.maxDate = maxDate

        // If a date was previously selected, set that date on the dialog
        if (initialYear != null && initialMonth != null && initialDay != null) {
            // Adjust the month by subtracting 1 as DatePickerDialog expects 0-based month
            datePickerDialog.updateDate(initialYear, initialMonth - 1, initialDay)
        }

        // Show the date picker dialog
        datePickerDialog.show()
    }



    fun openCustomChrome(activity: Activity, url: String) {
        try {
            val customIntent = CustomTabsIntent.Builder()

            customIntent.setToolbarColor(ContextCompat.getColor(activity, R.color.black))

            // we are calling below method after
            // setting our toolbar color.
            val customTabsIntent = customIntent.build()
            val packageName = "com.android.chrome"
            customTabsIntent.intent.setPackage(packageName)
            customTabsIntent.launchUrl(activity, Uri.parse(url))
        } catch (e: Exception) {
            openBrowser(activity, url)
            e.printStackTrace()
        }

    }

    fun openBrowser(activity: Activity, url: String) {
        val link = url.ifBlank {
            "https://www.google.com"
        }

        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link)).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            setPackage("com.android.chrome")
        }

        try {
            activity.startActivity(intent)
        } catch (ex: ActivityNotFoundException) {
            // Chrome not available, try default browser
            val fallbackIntent = Intent(Intent.ACTION_VIEW, Uri.parse(link)).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                setPackage(null)
            }

            // ✅ Only try if there's an app to handle it
            if (fallbackIntent.resolveActivity(activity.packageManager) != null) {
                activity.startActivity(fallbackIntent)
            } else {
                Toast.makeText(activity, "No browser found to open the link", Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

//    fun jsonToStringList(jsonString: String?): List<String> {
//        return if (jsonString != null) {
//            val gson = Gson()
//            val type = object : TypeToken<List<String>>() {}.type
//            gson.fromJson(jsonString, type)
//        } else {
//            emptyList()
//        }
//    }


    fun jsonToStringList(json: String?): List<String> {
        if (json.isNullOrEmpty()) return emptyList()
        val list = mutableListOf<String>()
        val array = JSONArray(json)
        for (i in 0 until array.length()) {
            list.add(array.getString(i))
        }
        return list
    }

    fun getRandomUrls(activity: Context): String {
        val list = jsonToStringList(MyApplication.app.prefManager.customUrls)
        return if (list.isNotEmpty()) {
            val random = Random()
            val randomString = list[random.nextInt(list.size)]

            randomString
        } else {
            "https://www.google.com/"
        }

    }
//
//    fun View.setAdClickListener(
//        activity: Activity,
//        isShowEveryTime: Boolean = false,
//        onFinished: () -> Unit
//    ) {
//        setOnClickListener {
//            ProgressBarUtils.showProgressDialog(activity)
//            FullScreenAdsHelper(activity).showInterstitialAds(isShowEveryTime, object : AdInterface {
//                override fun finished() {
//                    ProgressBarUtils.hideProgressDialog()
//                    onFinished()
//                }
//            })
//        }
//    }


    fun View.setAdClickListener(
        activity: Activity,
        adsHelper: FullScreenAdsHelper, // Pass the helper here
        isShowEveryTime: Boolean = false,
        onFinished: () -> Unit
    ) {
        setOnClickListener {
            ProgressBarUtils.showProgressDialog(activity)
            // Use the passed helper instead of creating a new one
            adsHelper.showInterstitialAds(isShowEveryTime, object : AdInterface {
                override fun finished() {
                    ProgressBarUtils.hideProgressDialog()
                    onFinished()
                }
            })
        }
    }

}