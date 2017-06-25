package study.applauncher;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

import study.applauncher.R;

public class MainActivity extends AppCompatActivity {
    PackageManager packageManager;
    ArrayList<ResolveInfo> items = new ArrayList<>();
    ArrayList<ResolveInfo> chathead_list = new ArrayList<>();
    RecyclerView recyclerView;
    RecyclerView addlistview;
    AddListAdapter addListAdapter;
    MyAdapter myAdapter;
    Button button;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = (Button) findViewById(R.id.btn);
        recyclerView = (RecyclerView) findViewById(R.id.listview);
        addlistview = (RecyclerView) findViewById(R.id.add_list);
        packageManager = getPackageManager();
        intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        items = (ArrayList<ResolveInfo>) packageManager.queryIntentActivities(intent, 0);

        myAdapter = new MyAdapter(items, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        myAdapter.setItemClick(new MyAdapter.ItemClick() {
            @Override
            public void ItemClick(View view, int position) {
                for (ResolveInfo info : chathead_list) {
                    if (info == items.get(position)) {
                        return;
                    }
                }
                chathead_list.add(items.get(position));
                addListAdapter.notifyItemInserted(chathead_list.size() - 1);
            }
        });
        myAdapter.setItemLongClick(new MyAdapter.ItemLongClick() {
            @Override
            public void LongClick(View view, int position) {
                Intent intent = getPackageManager().getLaunchIntentForPackage(items.get(position).activityInfo.packageName);
                startActivity(intent);
            }
        });

        recyclerView.setAdapter(myAdapter);

        addListAdapter = new AddListAdapter(chathead_list, this);
        addlistview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        addlistview.setItemAnimator(new DefaultItemAnimator());
        addListAdapter.setOnItemClick(new AddListAdapter.onItemClick() {
            @Override
            public void onItemClick(View view, int position) {
                chathead_list.remove(position);
                addListAdapter.notifyDataSetChanged();
            }
        });
        addlistview.setAdapter(addListAdapter);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("MainActivity", "StartService");
                intent = new Intent(MainActivity.this, FloatingService.class);
                intent.putParcelableArrayListExtra("app_list", chathead_list);
                stopService(intent);
                startService(intent);
                chathead_list.clear();
                addListAdapter.notifyDataSetChanged();
            }
        });
    }
}
