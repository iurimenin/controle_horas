package io.github.iurimenin.horastrabalhadas.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import android.widget.Toast
import io.github.iurimenin.horastrabalhadas.DayLog
import io.github.iurimenin.horastrabalhadas.R


/**
 * Created by Iuri Menin on 27/12/2017.
 */
class DayLogWidgetProvider : AppWidgetProvider() {

    private val addDAyLogAction = "addDayLog"

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {

        // Perform this loop procedure for each App Widget that belongs to this provider
        for (appWidgetId in appWidgetIds) {

            // Create an Intent to launch ExampleActivity
            val intent = Intent(context, javaClass)
            intent.action = addDAyLogAction
            val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)

            // Get the layout for the App Widget and attach an on-click listener
            // to the button
            val views = RemoteViews(context.packageName, R.layout.daylog_widget)
            views.setOnClickPendingIntent(R.id.image_button_daylog_widget, pendingIntent)

            // Tell the AppWidgetManager to perform an update on the current app widget
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        intent?.let{

            if (it.action == addDAyLogAction){
                context?.let {
                    DayLog.logNow(context)
                    Toast.makeText(context,
                            context.getString(R.string.log_sucess),
                            Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}