package study.applauncher;

import android.content.Context;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

import study.applauncher.R;

/**
 * Created by eka on 2017. 6. 25..
 */

public class ChatHeadsAdapter extends RecyclerView.Adapter<ChatHeadsAdapter.ViewHolder> {
    ArrayList<ResolveInfo> items = new ArrayList<>();
    Context context;

    public ChatHeadsAdapter(ArrayList<ResolveInfo> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.chathead, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Drawable drawable;
        drawable = items.get(position).loadIcon(context.getPackageManager());
        holder.icon.setImageDrawable(drawable);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick.onItemlick(v, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView icon;

        public ViewHolder(View itemView) {
            super(itemView);
            icon = (ImageView) itemView.findViewById(R.id.chathead_icon);
        }
    }

    private onItemClick onItemClick;

    public interface onItemClick {
        void onItemlick(View view, int position);
    }

    public void setOnItemClick(ChatHeadsAdapter.onItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }
}
