package com.example.android.bakingapp.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.example.android.bakingapp.R;

public class WidgetUpdateService extends IntentService {

    public static final String EXTRA_UPDATE = "com.example.android.bakingapp.action.extra.update";

    public WidgetUpdateService(){
        super("WidgetUpdateService");
    }

    public static void updateWidget(Context context){
        Intent intent = new Intent(context, WidgetUpdateService.class);
        intent.setAction(EXTRA_UPDATE);
        context.startService(intent);

    }
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent!=null){
            String action = intent.getAction();
            if (EXTRA_UPDATE.equals(action)){
                handleUpdate();
            }
        }
    }

    private void handleUpdate(){
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, RecipeWidget.class));
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_lv);
    }
}
