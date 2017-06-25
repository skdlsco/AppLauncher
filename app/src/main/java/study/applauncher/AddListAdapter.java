package study.applauncher;

import android.content.Context;
import android.content.pm.ResolveInfo;
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

public class AddListAdapter extends RecyclerView.Adapter<AddListAdapter.ViewHolder> {
    ArrayList<ResolveInfo> items = new ArrayList<>();
    Context context;

    public AddListAdapter(ArrayList<ResolveInfo> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.add_list_item, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.icon.setImageDrawable(items.get(position).loadIcon(context.getPackageManager()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick.onItemClick(v, position);
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
            icon = (ImageView) itemView.findViewById(R.id.add_list_item_icon);
        }
    }

    private onItemClick onItemClick;

    public interface onItemClick {
        void onItemClick(View view, int position);
    }

    public void setOnItemClick(AddListAdapter.onItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }
}
