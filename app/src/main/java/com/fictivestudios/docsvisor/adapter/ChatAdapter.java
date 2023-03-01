package com.fictivestudios.docsvisor.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fictivestudios.docsvisor.constants.Constants;
import com.fictivestudios.docsvisor.databinding.ItemMessageBinding;
import com.fictivestudios.docsvisor.databinding.ItemSendMsgBinding;
import com.fictivestudios.docsvisor.helper.PreferenceUtils;
import com.fictivestudios.docsvisor.model.ChatRequest;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ChatAdapter{} /*extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<ChatRequest> list;
    private Context mContext;
    private int VIEW_TYPE_MY_MSG = 0;
    private int VIEW_TYPE_OTHER_MSG = 1;
    private String userId;


    public ChatAdapter(Context context, List<ChatRequest> ModelList,String userID) {
        this.list = ModelList;
        mContext = context;
        this.userId=userID;
    }

    public void updateList(ChatRequest data) {
        list.add(data);
        notifyItemChanged(list.size() - 1);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case 0:
                LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
                ItemMessageBinding itemBinding = ItemMessageBinding.inflate(layoutInflater, parent, false);
                return new MyMessageViewHolder(itemBinding);
            case 1:
                layoutInflater = LayoutInflater.from(parent.getContext());
                ItemSendMsgBinding accessItemBinding = ItemSendMsgBinding.inflate(layoutInflater, parent, false);
                return new SendMessageViewHolder(accessItemBinding);

        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ChatRequest modal = list.get(position);
        switch (holder.getItemViewType()) {
            case 0:
                MyMessageViewHolder viewHolder = (MyMessageViewHolder) holder;
                viewHolder.bind(modal);
                break;
            case 1:
                SendMessageViewHolder holderview = (SendMessageViewHolder) holder;
                holderview.bind(modal,holderview.itemView);

                break;

        }
    }

    @Override
    public int getItemViewType(int position) {

        if (userId == list.get(position).getSender_id() ) {
            return VIEW_TYPE_MY_MSG;
        } else {
            return VIEW_TYPE_OTHER_MSG;
        }
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    class MyMessageViewHolder extends RecyclerView.ViewHolder {

        private ItemMessageBinding binding;

        public MyMessageViewHolder(ItemMessageBinding binding) {
            super(binding.getRoot());

            this.binding = binding;

        }

        public void bind(ChatRequest data) {



            binding.executePendingBindings();
        }

    }

    class SendMessageViewHolder extends RecyclerView.ViewHolder {

        private ItemSendMsgBinding binding;
        private SimpleDateFormat dateFormatprev;
        private Date d;

        public SendMessageViewHolder(ItemSendMsgBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }

        public void bind(ChatRequest data, View itemView) {
            binding.executePendingBindings();

         //   binding.tvMessage.setText(data.);
            binding.tvMessage.setText(data.getReciever_id());
        }



    }


}

*/