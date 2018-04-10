package com.example.kebo.project_search;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

/**
 * Created by kebo on 2017/12/31.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.mViewHolder>{

    private Context mContext;
    private List<Map<String,String>> mdata;
    private OnItemClickListener mOnItemClickListener;

    public void DeleteDataFromList(int position){
        mdata.remove(position);
    }

    public interface OnItemClickListener{
        void OnClick(View v, int position);
        void OnLongClick(View v,int position);
    }
    public void setItemClickListener(OnItemClickListener listener){
        mOnItemClickListener=listener;
    }

    public RecyclerAdapter(Context  context,List<Map<String,String>> datas)
    {
        this.mContext=context;
        this.mdata=datas;
    }

    @Override
    public mViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v= LayoutInflater.from(mContext).inflate(R.layout.recycler_item,parent,false);
        mViewHolder holder=new mViewHolder(v,mOnItemClickListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(final mViewHolder holder, final int position) {
        holder.SongName.setText(mdata.get(position).get("SongName"));
        holder.Singer.setText(mdata.get(position).get("Singer"));
        holder.Album.setText(mdata.get(position).get("Album"));
    }

    @Override
    public int getItemCount(){ return mdata.size();}

    public class mViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView SongName;
        TextView Singer;
        TextView Album;
        OnItemClickListener mListener;

        public mViewHolder(View v,OnItemClickListener tmp) {
            super(v);
            SongName =(TextView)v.findViewById(R.id.SongName);
            Singer =(TextView)v.findViewById(R.id.Singer);
            Album =(TextView)v.findViewById(R.id.Album);
            this.mListener=tmp;
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(mListener!=null){
                mListener.OnClick(v,getPosition());
            }
        }


    }
}

