package com.healthy.healthyaweaness.Fragment;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeProgressDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeSuccessDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.interfaces.Closure;
import com.healthy.healthyaweaness.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class BaseFragment extends Fragment {
    AwesomeProgressDialog awesomeProgressDialog;


    public BaseFragment() {
        // Required empty public constructor
    }




    public void Sucessdialag(final Activity activity, String tittle, String message){

        new AwesomeSuccessDialog(activity)
                .setTitle(tittle)
                .setMessage(message)
                .setColoredCircle(R.color.colorPrimaryDark)
                .setDialogIconAndColor(R.drawable.ic_dialog_info, R.color.white)
                .setCancelable(true)
                .setPositiveButtonText(getString(R.string.dialog_yes_button))
                .setPositiveButtonbackgroundColor(R.color.colorPrimaryDark)
                .setPositiveButtonTextColor(R.color.white)
                // .setNegativeButtonText(getString(R.string.dialog_no_button))
                .setNegativeButtonbackgroundColor(R.color.colorPrimaryDark)
                .setNegativeButtonTextColor(R.color.white)
                .setNegativeButtonClick(new Closure() {
                    @Override
                    public void exec() {
                        //click
                    }
                })
                .show();
    }

    public void showProgreesDilaog(Activity activity,String tittle,String message){

        awesomeProgressDialog = new AwesomeProgressDialog(activity);
        awesomeProgressDialog.setTitle(tittle).setMessage(message).
                setColoredCircle(R.color.colorPrimaryDark).setDialogIconAndColor(R.drawable.ic_dialog_info,
                R.color.white).setCancelable(true).show();
    }

    public void hideProgreesDilaog(Activity activity,String tittle,String message){

        if(awesomeProgressDialog!=null){
            awesomeProgressDialog.hide();

        }
    }


    public void showSucessdialag(final Activity activity, String tittle, String message){

        new AwesomeSuccessDialog(activity)
                .setTitle(tittle)
                .setMessage(message)
                .setColoredCircle(R.color.colorPrimaryDark)
                .setDialogIconAndColor(R.drawable.ic_dialog_info, R.color.white)
                .setCancelable(true)
                .setPositiveButtonText(getString(R.string.yes))
                .setPositiveButtonbackgroundColor(R.color.colorPrimaryDark)
                .setPositiveButtonTextColor(R.color.white)
                // .setNegativeButtonText(getString(R.string.dialog_no_button))
               // .setNegativeButtonbackgroundColor(R.color.darkpink)
             //   .setNegativeButtonTextColor(R.color.white)

                .setNegativeButtonClick(new Closure() {
                    @Override
                    public void exec() {
                        //click
                    }
                })
                .show();
    }
    public void Toast(String msg) {

        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    protected Activity getActiviy() {
        return getActivity();

    }

    public void Toast(int resId) {

        Toast.makeText(getActivity(), getString(resId), Toast.LENGTH_SHORT).show();
    }
    protected boolean CheckInternet() {
        boolean connected = false;

        ConnectivityManager conMgr = (ConnectivityManager) getActiviy().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            connected = true;

        } else {
            connected = false;

        }

        return connected;

    }


}
