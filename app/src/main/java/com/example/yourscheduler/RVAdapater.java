package com.example.yourscheduler;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.ColorSpace;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Locale;
import java.util.zip.Inflater;

public class RVAdapater extends RecyclerView.Adapter<RVAdapater.ViewHolder> implements Filterable {


    ArrayList<ModelConstructors> arrModel;
    ArrayList<ModelConstructors> backup;
    Context context;
    private String title, note;
    private EditText edtTitle, edtNote;
    Button upSave;
    DatabaseManager db;



    public RVAdapater(ArrayList<ModelConstructors> arrModel, Context context) {
        this.arrModel = arrModel;
        this.context = context;
        backup= new ArrayList<>(arrModel);
    }


    @NonNull
    @Override
    public RVAdapater.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_row, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull RVAdapater.ViewHolder holder,  int position) {
        ModelConstructors model = arrModel.get(position);
        holder.Tvtitle.setText(model.getTitle());
        holder.Tvnote.setText(model.getNote());

        holder.imgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(context, holder.imgBtn);
                popupMenu.inflate(R.menu.popup_menu);

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {

                        switch (menuItem.getItemId()) {
                            case R.id.menuEdit:
                                Dialog dialog = new Dialog(context);
                                dialog.setContentView(R.layout.dialog_update);

                                edtTitle = dialog.findViewById(R.id.updateTitle);
                                edtNote = dialog.findViewById(R.id.updateNote);
                                edtTitle.setText(arrModel.get(holder.getAdapterPosition()).getTitle());
                                edtNote.setText(arrModel.get(holder.getAdapterPosition()).getNote());

                                edtNote.addTextChangedListener(updatingNote);

                                title = model.getTitle();
                                note = model.getNote();

                                upSave = dialog.findViewById(R.id.UpsaveBtn);


                                upSave.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        model.setTitle(edtTitle.getText().toString());
                                        model.setNote(edtNote.getText().toString());


                                        String res = db.updateData(title, edtTitle.getText().toString(), edtNote.getText().toString());
                                        Toast.makeText(context, res, Toast.LENGTH_SHORT).show();


                                        notifyItemChanged(holder.getAdapterPosition());

                                        dialog.dismiss();
                                    }
                                });
                                dialog.show();
                                break;

                            case R.id.menuDelete:
                                String res = db.deleteData(model.getTitle());
                                Toast.makeText(context, res, Toast.LENGTH_SHORT).show();
                                arrModel.remove(holder.getAdapterPosition());
                                notifyItemRemoved(holder.getAdapterPosition());


                        }

                        return false;
                    }
                });
                popupMenu.show();
            }

        });

    }


    @Override
    public int getItemCount() {
        return arrModel.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView Tvtitle, Tvnote;
        ImageView imgBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Tvtitle = itemView.findViewById(R.id.Ttitle);
            Tvnote = itemView.findViewById(R.id.Tnote);
            imgBtn = itemView.findViewById(R.id.imgBtn);

            db = new DatabaseManager(context);
        }
    }


    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
                @SuppressLint("SuspiciousIndentation")
                @Override
        protected FilterResults performFiltering(CharSequence charSequence) {

            ArrayList<ModelConstructors> filtereddata = new ArrayList<>();
            if (charSequence.toString().isEmpty())
                filtereddata.addAll(backup);
            else {
                for (ModelConstructors obj : backup) {
                    if (obj.getTitle().toString().toLowerCase().contains(charSequence.toString().toLowerCase()))
                    filtereddata.add(obj);
                }
            }
            FilterResults result = new FilterResults();
            result.values = filtereddata;
            return result;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

            arrModel.clear();
            arrModel.addAll((ArrayList<ModelConstructors>) filterResults.values);
            notifyDataSetChanged();

        }
    };
    private TextWatcher updatingNote = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            String upTitle = edtTitle.getText().toString();
            String upNote = edtNote.getText().toString();
            upSave.setEnabled(!upTitle.isEmpty() && !upNote.isEmpty());

        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };
}


