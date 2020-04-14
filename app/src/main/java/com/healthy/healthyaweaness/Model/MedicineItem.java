package com.healthy.healthyaweaness.Model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

public class MedicineItem implements Serializable{
    private String mToDoText;
    private String mALETER_ID;
    private String Medcibe_desc;

    private boolean mHasReminder;
//    private Date mLastEdited;
    private int mTodoColor;
    private Date mToDoDate;
    private UUID mTodoIdentifier;
    private static final String TODOTEXT = "todotext";
    private static final String TODOREMINDER = "todoreminder";
//    private static final String TODOLASTEDITED = "todolastedited";
    private static final String TODOCOLOR = "todocolor";
    private static final String TODODATE = "tododate";
    private static final String TODOIDENTIFIER = "todoidentifier";
    private static final String ALETER_ID = "todoidentifier";
    private static final String MEDCICNE_DES = "todoidentifier";



    public MedicineItem(String ALETER_ID, String todoBody, String MEDCICNE_DES, boolean hasReminder, Date toDoDate){
        mToDoText = todoBody;
        mHasReminder = hasReminder;
        mToDoDate = toDoDate;
        mTodoColor = 1677725;
        mTodoIdentifier = UUID.randomUUID();
        mALETER_ID =ALETER_ID;
        Medcibe_desc=MEDCICNE_DES;

    }

    public MedicineItem(JSONObject jsonObject) throws JSONException{
        mToDoText = jsonObject.getString(TODOTEXT);
        mHasReminder = jsonObject.getBoolean(TODOREMINDER);
        mTodoColor = jsonObject.getInt(TODOCOLOR);
        mTodoIdentifier = UUID.fromString(jsonObject.getString(TODOIDENTIFIER));

//        if(jsonObject.has(TODOLASTEDITED)){
//            mLastEdited = new Date(jsonObject.getLong(TODOLASTEDITED));
//        }
        if(jsonObject.has(TODODATE)){
            mToDoDate = new Date(jsonObject.getLong(TODODATE));
        }
    }

    public JSONObject toJSON() throws JSONException{
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(TODOTEXT, mToDoText);
        jsonObject.put(ALETER_ID,mALETER_ID);
        jsonObject.put(MEDCICNE_DES,Medcibe_desc);
        jsonObject.put(TODOREMINDER, mHasReminder);
//        jsonObject.put(TODOLASTEDITED, mLastEdited.getTime());
        if(mToDoDate!=null){
            jsonObject.put(TODODATE, mToDoDate.getTime());
        }
        jsonObject.put(TODOCOLOR, mTodoColor);
        jsonObject.put(TODOIDENTIFIER, mTodoIdentifier.toString());

        return jsonObject;
    }




    public MedicineItem(){
        this(ALETER_ID,"Clean my room","", true, new Date());
    }

    public String getToDoText() {
        return mToDoText;
    }

    public String getmALETER_ID() {
        return mALETER_ID;
    }

    public void setmALETER_ID(String mALETER_ID) {
        this.mALETER_ID = mALETER_ID;
    }

    public void setToDoText(String mToDoText) {
        this.mToDoText = mToDoText;
    }

    public boolean hasReminder() {
        return mHasReminder;
    }

    public void setHasReminder(boolean mHasReminder) {
        this.mHasReminder = mHasReminder;
    }

    public Date getToDoDate() {
        return mToDoDate;
    }

    public int getTodoColor() {
        return mTodoColor;
    }

    public void setTodoColor(int mTodoColor) {
        this.mTodoColor = mTodoColor;
    }

    public void setToDoDate(Date mToDoDate) {
        this.mToDoDate = mToDoDate;
    }

    public String getMedcibe_desc() {
        return Medcibe_desc;
    }

    public void setMedcibe_desc(String medcibe_desc) {
        Medcibe_desc = medcibe_desc;
    }

    public UUID getIdentifier(){
        return mTodoIdentifier;
    }
}

