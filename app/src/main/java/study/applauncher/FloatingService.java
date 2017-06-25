package study.applauncher;

import android.app.Service;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;

import java.util.ArrayList;

import study.applauncher.R;

/**
 * Created by eka on 2017. 6. 20..
 */

public class FloatingService extends Service {
    LayoutInflater layoutInflater;
    WindowManager windowManager;
    ArrayList<ResolveInfo> items = new ArrayList<>();
    ChatHeadsAdapter adapter;
    RecyclerView recyclerView;
    View view;
    ImageButton btn_close;
    ImageButton btn_head;
    Boolean isVisible = false;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("FloatingService", "StartService2");
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        layoutInflater = LayoutInflater.from(this);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("FloatingService", "onStartCommand");
        view = layoutInflater.inflate(R.layout.chatheads, null);
        btn_close = (ImageButton) view.findViewById(R.id.chatheads_close);
        btn_head = (ImageButton) view.findViewById(R.id.chatheads_head);
        recyclerView = (RecyclerView) view.findViewById(R.id.chatheads_RecyclerView);
        if (intent.getParcelableArrayListExtra("app_list") != null) {
            items = intent.getParcelableArrayListExtra("app_list");
        }

        adapter = new ChatHeadsAdapter(items, this);
        adapter.setOnItemClick(new ChatHeadsAdapter.onItemClick() {
            @Override
            public void onItemlick(View view, int position) {
                changedListViewVisibility();
                Intent intent = getPackageManager().getLaunchIntentForPackage(items.get(position).activityInfo.packageName);
                startActivity(intent);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);

        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_TOAST,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.TOP | Gravity.LEFT;
        windowManager.addView(view, params);

        btn_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changedListViewVisibility();
            }
        });
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("FloatingService", "clicked close button");
                stopSelf();
            }
        });
        btn_head.setOnTouchListener(new View.OnTouchListener() {
            int params_x, params_y;
            float start_x, start_y;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        params_x = params.x;
                        params_y = params.y;
                        start_x = event.getRawX();
                        start_y = event.getRawY();
                    case MotionEvent.ACTION_MOVE:
                        params.x = (int) (params_x + event.getRawX() - start_x);
                        params.y = (int) (params_y + event.getRawY() - start_y);
                        windowManager.updateViewLayout(view, params);
                }
                return false;
            }
        });
        return START_NOT_STICKY;
    }

    public void changedListViewVisibility() {
        if (isVisible) {
            recyclerView.setVisibility(View.GONE);
            isVisible = false;
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            isVisible = true;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        windowManager.removeView(view);
    }
}
