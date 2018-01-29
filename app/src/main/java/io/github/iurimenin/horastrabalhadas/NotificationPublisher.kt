package io.github.iurimenin.horastrabalhadas

import android.app.AlarmManager
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.support.v4.app.NotificationCompat
import com.crashlytics.android.Crashlytics
import java.util.*


/**
 * Created by Iuri Menin on 18/12/2017.
 */
class NotificationPublisher : BroadcastReceiver() {

    companion object {

        private val channelId = "notification_logs"

        fun notifyAfternoonLeaveTime(dayLog: DayLog, context: Context) {

            val builder = NotificationCompat.Builder(context, channelId)
                    .setSmallIcon(R.drawable.ic_timelapse_white_24dp)
                    .setContentTitle(context
                            .getString(R.string.notification_title_leave_time,
                                    dayLog.estimatedLeaveTime))
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))

            val mNotificationManager =
                    context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            mNotificationManager.notify(1, builder.build())

            scheduleLeaveNotification(context, dayLog.estimatedLeaveTime)
        }

        private fun scheduleLeaveNotification(context: Context, estimatedLeaveTime: String) {

            val notificationIntent = Intent(context, NotificationPublisher::class.java)
            val pendingIntent = PendingIntent.getBroadcast(context, 0,
                    notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT)

            val future = Calendar.getInstance()
            future.setLeaveTime(estimatedLeaveTime)

            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, future.timeInMillis, pendingIntent)

            Crashlytics.log("scheduled Leave Notification to $estimatedLeaveTime")
        }

        fun notifyWorkedTime(dayLog: DayLog, context: Context) {

            val builder = NotificationCompat.Builder(context, channelId)
                    .setSmallIcon(R.drawable.ic_thumb_up_white_24dp)
                    .setContentTitle(context
                            .getString(R.string.notification_title_worked_time,
                                    dayLog.workedTime))
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))

            val mNotificationManager =
                    context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            mNotificationManager.notify(1, builder.build())
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {

        Crashlytics.log("onReceive NotificationPublisher")
        context?.let {

            Crashlytics.log("onReceive NotificationPublisher context not null")

            val builder = NotificationCompat.Builder(context, channelId)
                    .setSmallIcon(R.drawable.ic_add_alarm_white_24dp)
                    .setContentTitle(context.getString(R.string.notification_time_leave))
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))

            val mNotificationManager =
                    context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            mNotificationManager.notify(1, builder.build())
        }
    }
}
