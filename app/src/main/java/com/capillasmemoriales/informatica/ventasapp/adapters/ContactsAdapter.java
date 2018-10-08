package com.capillasmemoriales.informatica.ventasapp.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.capillasmemoriales.informatica.ventasapp.activities.ContactDetail;
import com.capillasmemoriales.informatica.ventasapp.models.Contact;
import com.capillasmemoriales.informatica.ventasapp.R;

import java.util.List;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ViewHolder> {

    private final Context mContext;
    private List<Contact> mData;

    public ContactsAdapter(Context mContext, List<Contact> list) {
        this.mContext = mContext;
        this.mData = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int pos) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.contacts_list, viewGroup, false);
        //View view;
        //LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        //view = layoutInflater.inflate(R.layout.contacts_list, viewGroup, false);
        final ViewHolder vh = new ViewHolder(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent details = new Intent(mContext.getApplicationContext(), ContactDetail.class);
                details.putExtra("Id", mData.get(vh.getAdapterPosition()).getId());
                details.putExtra("fName", mData.get(vh.getAdapterPosition()).getfName());
                details.putExtra("lName", mData.get(vh.getAdapterPosition()).getlName());
                details.putExtra("Phone", mData.get(vh.getAdapterPosition()).getPhone());
                details.putExtra("Mail", mData.get(vh.getAdapterPosition()).getMail());
                mContext.startActivity(details);
            }
        });

        view.setOnLongClickListener(new View.OnLongClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public boolean onLongClick(View view) {
                String phoneNumber = mData.get(vh.getAdapterPosition()).getPhone();
                Intent dial = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
                //dial.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(dial);
                return false;
            }
        });
        return vh;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int pos) {
        viewHolder.txtcId.setText("ID: "+mData.get(pos).getId());
        viewHolder.txtfName.setText(""+mData.get(pos).getfName());
        viewHolder.txtlName.setText(" "+mData.get(pos).getlName());
        viewHolder.txtcPhone.setText(""+mData.get(pos).getPhone());
        viewHolder.txtcMail.setText(""+mData.get(pos).getMail());
        if(pos % 2 == 0) {
            viewHolder.itemView.setBackgroundResource(R.color.gray_alt);
        } else {
            viewHolder.itemView.setBackgroundResource(R.color.gray);
        }
    }

    @Override
    public int getItemCount() {
        if (mData != null)
            return mData.size();
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtcId, txtfName, txtlName, txtcPhone, txtcMail;

        public ViewHolder(View itemView) {
            super(itemView);
            txtcId = itemView.findViewById(R.id.txtcId);
            txtfName = itemView.findViewById(R.id.txtfName);
            txtlName = itemView.findViewById(R.id.txtlName);
            txtcPhone = itemView.findViewById(R.id.txtcPhone);
            txtcMail = itemView.findViewById(R.id.txtcMail);
        }
    }
}
