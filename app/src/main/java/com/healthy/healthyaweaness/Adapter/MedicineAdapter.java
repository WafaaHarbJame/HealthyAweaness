package com.healthy.healthyaweaness.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.healthy.healthyaweaness.Activity.AddMedicineActivity;
import com.healthy.healthyaweaness.Activity.AddToDoActivity;
import com.healthy.healthyaweaness.DB.AnalyticsApplication;
import com.healthy.healthyaweaness.Model.Medicine;
import com.healthy.healthyaweaness.Model.SharedPManger;
import com.healthy.healthyaweaness.Model.ToDoItem;
import com.healthy.healthyaweaness.R;

import java.util.List;

public class MedicineAdapter extends RecyclerView.Adapter<MedicineAdapter.MyHolder> {

    MyHolder holder;

    public static List<Medicine> medicines;
    public static Context context;
    private LayoutInflater inflater;
    public static final int REQUEST_ID_TODO_ITEM = 100;
    public static final String TODOITEM = "com.healthy.healthyaweaness.Activity.MainActivity";
    public static AnalyticsApplication app;
    String Phone_with_plus;
    SharedPManger sharedPManger;
    private DatabaseReference mFirebaseDatabase;

    public MedicineAdapter(Context context, List<Medicine> medicines){
        this.medicines = medicines;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        app = (AnalyticsApplication)((Activity) context).getApplication();
        sharedPManger=new SharedPManger(context);
        Phone_with_plus=sharedPManger.getDataString("KEY_PHONE");
        mFirebaseDatabase = FirebaseDatabase.getInstance().getReference("Medicine");;


    }

    public interface OnClickMedicine{
        void onClick();
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_medicine,parent,false);
        MyHolder holder = new MyHolder(view);
        context = parent.getContext();
        app = (AnalyticsApplication)((Activity) context).getApplication();


        return holder;
    }

    @Override
    public void onBindViewHolder(final MyHolder holder, final int position) {
        this.holder = holder;
        app = (AnalyticsApplication)((Activity) context).getApplication();

        if (!(medicines.isEmpty())) {
            holder.Medicine_Name.setText(medicines.get(position).getMedicine_Name());
            holder.Medicine_Description.setText(medicines.get(position).getMedicine_Description());

        }



    }


    @Override
    public int getItemCount() {
        return medicines.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {
        TextView Medicine_Name;
        TextView Medicine_Description;
        LinearLayout cardView;
        ImageView delete_Medicine;



        public MyHolder(View itemView) {
            super(itemView);
            app = (AnalyticsApplication)((Activity) context).getApplication();

            Medicine_Name = itemView.findViewById(R.id.Medicine_Name);
            Medicine_Description = itemView.findViewById(R.id.Medicine_Description);
            cardView=itemView.findViewById(R.id.listItemLinearLayout);
            delete_Medicine=itemView.findViewById(R.id.delete_Medicine);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position=getAdapterPosition();
                    app.send(this, "Action", "FAB pressed");
                    Intent intent=new Intent(context, AddMedicineActivity.class);
                    intent.putExtra("medicine_Name",medicines.get(position).getMedicine_Name());
                    intent.putExtra("Medicine_Description",medicines.get(position).getMedicine_Description());
                    intent.putExtra("ID",medicines.get(position).getId());
                    intent.putExtra("Update",true);
                    context.startActivity(intent);

//                    app.send(context, "Action", "FAB pressed");
//                    Intent newTodo = new Intent(context, AddToDoActivity.class);
//                    newTodo.putExtra("medicine_Name",medicines.get(position).getMedicine_Name());
//                    newTodo.putExtra("Medicine_Description",medicines.get(position).getMedicine_Description());
//                    newTodo.putExtra("ID",medicines.get(position).getId());
//                    newTodo.putExtra("Update",true);
//                    newTodo.putExtra("adding",true);
//                    ToDoItem item = new ToDoItem("","", true, null);
//                    int color = ColorGenerator.MATERIAL.getRandomColor();
//                    item.setTodoColor(color);
//                    newTodo.putExtra(TODOITEM, item);
//                       context.startActivity(newTodo);
//                    ((Activity)(context)).startActivityForResult(newTodo, REQUEST_ID_TODO_ITEM);


                }
            });



            delete_Medicine.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final int position=getAdapterPosition();
                    mFirebaseDatabase.child(Phone_with_plus).child(medicines.get(position).getId()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(context, "تم الحذف بنجاح ", Toast.LENGTH_SHORT).show();
                                notifyDataSetChanged();
//                                medicines.remove(position);
                                notifyDataSetChanged();
                                notifyItemRemoved(position);

                            }

                        }
                    });

                }
            });

        }


        }

     public void onClick(){
         int position=1;
//                    app.send(this, "Action", "FAB pressed");
//                    int position=getAdapterPosition();
//                    Intent intent=new Intent(context, AddMedicineActivity.class);
//                    intent.putExtra("medicine_Name",medicines.get(position).getMedicine_Name());
//                    intent.putExtra("Medicine_Description",medicines.get(position).getMedicine_Description());
//                    intent.putExtra("ID",medicines.get(position).getId());
//                    intent.putExtra("Update",true);
//                    context.startActivity(intent);

         app.send(context, "Action", "FAB pressed");
         Intent newTodo = new Intent(context, AddToDoActivity.class);
         newTodo.putExtra("medicine_Name",medicines.get(position).getMedicine_Name());
         newTodo.putExtra("Medicine_Description",medicines.get(position).getMedicine_Description());
         newTodo.putExtra("ID",medicines.get(position).getId());
         newTodo.putExtra("Update",true);
         newTodo.putExtra("adding",true);
         ToDoItem item = new ToDoItem("","","", true, null);
         int color = ColorGenerator.MATERIAL.getRandomColor();
         item.setTodoColor(color);
         newTodo.putExtra(TODOITEM, item);
         context.startActivity(newTodo);
         ((Activity)(context)).startActivityForResult(newTodo, REQUEST_ID_TODO_ITEM);
     }



    }




