package io.github.iurimenin.horastrabalhadas

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import android.support.v4.app.NotificationCompat
import android.util.Log
import br.com.softfocus.dateutils.DateUtils
import com.google.firebase.analytics.FirebaseAnalytics
import java.util.*




/**
 * Created by Iuri Menin on 18/12/2017.
 */
class NotificationPublisher : BroadcastReceiver() {

    companion object {

        private val channelId = "notification_logs"
        private val notificationID = 1
        private val importance = NotificationManager.IMPORTANCE_HIGH

        fun notifyAfternoonLeaveTime(dayLog: DayLog, context: Context) {

            val builder = NotificationCompat.Builder(context, channelId)
                    .setSmallIcon(R.drawable.ic_timelapse_white_24dp)
                    .setContentTitle(context
                            .getString(R.string.notification_title_leave_time,
                                    dayLog.estimatedLeaveTime))
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))

            val notificationManager =
                    context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val name = context.getString(R.string.channel_name)
                val channel = NotificationChannel(channelId, name, importance)
                notificationManager.createNotificationChannel(channel)
            }

            notificationManager.notify(notificationID, builder.build())

            scheduleLeaveNotification(context, dayLog.estimatedLeaveTime)
        }

        private fun scheduleLeaveNotification(context: Context, estimatedLeaveTime: String) {

            val future = Calendar.getInstance()
            future.setLeaveTime(estimatedLeaveTime)

            val intentAlarm = Intent(context, NotificationPublisher::class.java)

            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

            alarmManager.set(AlarmManager.RTC_WAKEUP,
                    future.timeInMillis,
                    PendingIntent.getBroadcast(context,
                            1,
                            intentAlarm,
                            PendingIntent.FLAG_UPDATE_CURRENT))

            val params = Bundle()
            params.putString("estimated_leave_time", estimatedLeaveTime)
            params.putString("date", DateUtils.format(Date(), DateUtils.DayMonthYearFormat))
            FirebaseAnalytics.getInstance(context).logEvent("schedule_leave_notification", params)
        }

        fun notifyWorkedTime(dayLog: DayLog, context: Context) {

            val builder = NotificationCompat.Builder(context, channelId)
                    .setSmallIcon(R.drawable.ic_thumb_up_white_24dp)
                    .setContentTitle(context
                            .getString(R.string.notification_title_worked_time,
                                    dayLog.workedTime))
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))

            val notificationManager =
                    context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val name = context.getString(R.string.channel_name)
                val channel = NotificationChannel(channelId, name, importance)
                notificationManager.createNotificationChannel(channel)
            }

            notificationManager.notify(notificationID, builder.build())
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {

        Log.d("NotificationPublisher", "onReceive")
        context?.let {

            val params = Bundle()
            params.putString("date", DateUtils.format(Date(), DateUtils.DayMonthYearFormat))
            FirebaseAnalytics.getInstance(it).logEvent("onReceive_scheduled_notification", params)

            val builder = NotificationCompat.Builder(context, channelId)
                    .setSmallIcon(R.drawable.ic_add_alarm_white_24dp)
                    .setContentTitle(context.getString(R.string.notification_time_leave))
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))

            val notificationManager =
                    context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val name = context.getString(R.string.channel_name)
                val channel = NotificationChannel(channelId, name, importance)
                notificationManager.createNotificationChannel(channel)
            }

            notificationManager.notify(notificationID, builder.build())
        }
    }
}
