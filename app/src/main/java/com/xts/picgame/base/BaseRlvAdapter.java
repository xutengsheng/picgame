package com.xts.picgame.base;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;


import com.xts.picgame.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseRlvAdapter<T> extends RecyclerView.Adapter {

    public ArrayList<T> mList;
    public Context mContext ;
    public OnItemClickListener mListener;
    public static final int TYPE_HEADER = 0;
    public static final int TYPE_NORMAL = 1;
    private View mHeaderView;


    public BaseRlvAdapter(Context context, ArrayList<T> list){
        mList = list;
        mContext = context;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mHeaderView != null && viewType == TYPE_HEADER){
            return new VH(mHeaderView);
        }
        View inflate = LayoutInflater.from(mContext).inflate(getLayout(), parent, false);
        final VH vh = new VH(inflate);
        inflate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null){
                    mListener.onItemClick(v,vh.getLayoutPosition());
                }
            }
        });

        return vh;
    }

    protected abstract int getLayout();

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_HEADER){
            return;
        }

        int newPosition = position;
        if (mHeaderView != null){
            newPosition -=1;
        }

        VH vh = (VH) holder;
        T t = mList.get(newPosition);
        bindData(vh,t);
    }

    public void update(List<T> list){
        mList.clear();
        mList.addAll(list);
        notifyDataSetChanged();
    }

    public void addData(List<T> list){
        mList.addAll(list);
        notifyDataSetChanged();
    }


    protected abstract void bindData(VH vh, T t);

    @Override
    public int getItemCount() {
        return mHeaderView == null ? mList.size() : mList.size()+1;
    }

    public class VH extends RecyclerView.ViewHolder{

        SparseArray<View> mViews;
        public VH(@NonNull View itemView) {
            super(itemView);
            mViews = new SparseArray<>();
        }

        public View getViewById(int id){
            View view = mViews.get(id);
            if (view == null){
                view = itemView.findViewById(id);
                mViews.append(id,view);
            }

            return view;
        }
    }

    public interface OnItemClickListener{
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    /**
     * 调用setAdapter()时触发
     * @param recyclerView
     */
    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        LogUtils.print("onAttachedToRecyclerView");
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager){
            final GridLayoutManager layoutManager = (GridLayoutManager) manager;
            layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    //每个位置得item占用的格数,如果有头,头部应该占据 网格布局的列数
                    return getItemViewType(position) == TYPE_HEADER ? layoutManager.getSpanCount() : 1;
                }
            });
        }

    }

    /**
     * 适配器移除的时候调用
     * @param recyclerView
     */
    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        LogUtils.print("onDetachedFromRecyclerView");
    }

    /**
     * 当item移入屏幕的时候调用
     * @param holder
     */
    @Override
    public void onViewAttachedToWindow(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        LogUtils.print("onViewAttachedToWindow:"+holder.getLayoutPosition());
        ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
        if (layoutParams != null && layoutParams instanceof StaggeredGridLayoutManager.LayoutParams){
            StaggeredGridLayoutManager.LayoutParams lp = (StaggeredGridLayoutManager.LayoutParams) layoutParams;
            //设置占满全部空间
            lp.setFullSpan(getItemViewType(holder.getLayoutPosition())==TYPE_HEADER);
        }

    }

    /**
     * 当item移除屏幕的时候调用
     * @param holder
     */
    @Override
    public void onViewDetachedFromWindow(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        LogUtils.print("onViewDetachedFromWindow:"+holder.getLayoutPosition());
    }

    public void addHeaderView(View headerView){
        mHeaderView = headerView;
        notifyDataSetChanged();
    }

    public View getHeaderView(){
        return mHeaderView;
    }

    @Override
    public int getItemViewType(int position) {
        if (mHeaderView == null){
            return TYPE_NORMAL;
        }
        if (mHeaderView != null && position == 0){
            return TYPE_HEADER;
        }
        return TYPE_NORMAL;
    }
}
