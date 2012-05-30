package com.alberovalley.primerWidget;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class PrimerWidgetProvider extends AppWidgetProvider {

	DateFormat df = new SimpleDateFormat("hh:mm:ss");
	/**
	 * Custom Intent name that is used by the 'AlarmManager' to tell us to update the
	clock once per second.
	 */
	public static String CLOCK_WIDGET_UPDATE = "com.alberovalley.primerwidget.widget.PRIMERWIDGET_WIDGET_UPDATE";
	@Override    public void onReceive(Context context, Intent intent) {
	    super.onReceive(context, intent);
	    if (CLOCK_WIDGET_UPDATE.equals(intent.getAction())) {
	        // Obtén el widget manager y el id para este widget provider, 
	        // tras ello llama al método de actualización del reloj compartido
	        ComponentName thisAppWidget = new ComponentName(context.getPackageName(), getClass().getName());
	        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
	        int ids[] = appWidgetManager.getAppWidgetIds(thisAppWidget);
	        for (int appWidgetID: ids) {
	            updateAppWidget(context, appWidgetManager, appWidgetID);
	        }
	    }
	}
	
	public void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetID){
	      // Crea un Intent para lanzar PrimerWidgetActivity
	      Intent intent = new Intent(context, PrimerWidgetActivity.class);
	      PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
	      // Obtiene el layout para el App Widget y le asigna un on-click listener al botón
	      RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget1);
	      views.setOnClickPendingIntent(R.id.button, pendingIntent);
	      // Para actualizar el textview
	      views.setTextViewText(R.id.widget1label, df.format(new Date()));
	      // Le dice al AppWidgetManager que realice una actualización al widget actual
	      appWidgetManager.updateAppWidget(appWidgetID, views);
	    
	}
	
	  public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		  final int N = appWidgetIds.length;
//		    Log.i("ExampleWidget",  "Updating widgets " + Arrays.asList(appWidgetIds));
		    // Este bucle se ejecuta para cada App Widget perteneciente a este provider
		    for (int i = 0; i < N; i++) {
		    	int appWidgetId = appWidgetIds[i];
		    	updateAppWidget(context, appWidgetManager, appWidgetId);
		    }
	  }

	  private PendingIntent createClockTickIntent(Context context) {
		    Intent intent = new Intent(CLOCK_WIDGET_UPDATE);
		    PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		    return pendingIntent;
		}
		@Override
		public void onEnabled(Context context) {
		        super.onEnabled(context);
		        //Log.d(LOG_TAG, "Widget Provider enabled.  Starting timer to update widget every second");
		        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
		        Calendar calendar = Calendar.getInstance();
		        calendar.setTimeInMillis(System.currentTimeMillis());
		        calendar.add(Calendar.SECOND, 1);
		        alarmManager.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), 1000
		, createClockTickIntent(context));
		}
		@Override
		public void onDisabled(Context context) {
		        super.onDisabled(context);
		       // Log.d(LOG_TAG, "Widget Provider disabled. Turning off timer");
		        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
		        alarmManager.cancel(createClockTickIntent(context));
		}
	
}
