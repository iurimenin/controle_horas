package io.github.iurimenin.horastrabalhadas

import android.app.Notification
import android.app.NotificationManager
import android.content.Context
import android.media.RingtoneManager
import android.support.v4.app.NotificationCompat

/**
 * Created by Iuri Menin on 18/12/2017.
 */
class NotificationUtils {

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
//                    .setStyle(NotificationCompat
//                            .BigTextStyle()
//                            .bigText(context
//                                    .getString(R.string.notification_content_leave_time,
//                                            dayLog.estimatedLeaveTime)
//                            )
//                    )

            val mNotificationManager =
                    context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            mNotificationManager.notify(1, builder.build())
        }

        fun notifyWorkedTime(dayLog: DayLog, context: Context) {

            val builder = NotificationCompat.Builder(context, channelId)
                    .setSmallIcon(R.drawable.ic_thumb_up_white_24dp)
                    .setContentTitle(context
                            .getString(R.string.notification_title_worked_time,
                                    dayLog.workedTime))
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
//                    .setStyle(NotificationCompat
//                            .BigTextStyle()
//                            .bigText(context
//                                    .getString(R.string.notification_content_worked_time,
//                                            dayLog.workedTime)
//                            )
//                    )

            val mNotificationManager =
                    context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            mNotificationManager.notify(1, builder.build())
        }

    }
}