package study.applauncher;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import study.applauncher.R;

/**
 * Created by eka on 2017. 6. 20..
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    ArrayList<ResolveInfo> items = new ArrayList<>();
    Context context;

    public MyAdapter(ArrayList<ResolveInfo> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        ResolveInfo item = items.get(position);

        String _name = "";
        Drawable drawable = null;

        try {
            drawable = context.getPackageManager().getApplicationIcon(item.activityInfo.packageName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        _name = item.activityInfo.loadLabel(context.getPackageManager()).toString();

        holder.icon.setImageDrawable(drawable);
        holder.name.setText(_name);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClick.ItemClick(v, position);
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                itemLongClick.LongClick(v, position);
                return true;
            }
        });
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView icon;

        public ViewHolder(View itemView) {
            super(itemView);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            itemView.setLayoutParams(params);
            name = (TextView) itemView.findViewById(R.id.item_name);
            icon = (ImageView) itemView.findViewById(R.id.item_icon);
        }
    }

    private ItemClick itemClick;
    private ItemLongClick itemLongClick;

    public interface ItemClick {
        void ItemClick(View view, int position);
    }

    public interface ItemLongClick {
        void LongClick(View view, int position);
    }

    public void setItemClick(ItemClick itemClick) {
        this.itemClick = itemClick;
    }

    public void setItemLongClick(ItemLongClick itemLongClick) {
        this.itemLongClick = itemLongClick;
    }
}
