package com.example.wizardapp;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.widget.ViewFlipper;

public class MainActivity extends FragmentActivity {
    /**
     * The number of pages (wizard steps) to show in this demo.
     */
    private static final int NUM_PAGES = 5;
    private ViewFlipper viewFlipper;

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private WizardFragmentInterface wizardFragmentInterface = new WizardFragmentInterface() {
        @Override
        public void goNext() {
            viewFlipper.setInAnimation(getApplicationContext(), R.anim.slide_in_right);
            viewFlipper.setOutAnimation(getApplicationContext(), R.anim.slide_out_left);
            int actual = viewFlipper.getDisplayedChild();
            int childCount = viewFlipper.getChildCount() - 1;
            if(actual < childCount){
                viewFlipper.showNext();
            }
        }

        @Override
        public void goBack() {
            viewFlipper.setInAnimation(getApplicationContext(), android.R.anim.slide_in_left);
            viewFlipper.setOutAnimation(getApplicationContext(), android.R.anim.slide_out_right);
            viewFlipper.showPrevious();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NewUserFragment newUserFragment = new NewUserFragment(wizardFragmentInterface);
        NewBookFragment newBookFragment = new NewBookFragment(wizardFragmentInterface);
        NewLibFragment newLibFragment = new NewLibFragment(wizardFragmentInterface);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.card_0, newUserFragment);
        transaction.add(R.id.card_1, newBookFragment);
        transaction.add(R.id.card_2, newLibFragment);
        transaction.commit();

        viewFlipper = findViewById(R.id.container);

    }

    @Override
    public void onBackPressed() {
        viewFlipper.setInAnimation(getApplicationContext(), android.R.anim.slide_in_left);
        viewFlipper.setOutAnimation(getApplicationContext(), android.R.anim.slide_out_right);
        if(viewFlipper.getDisplayedChild() > 0) {
            viewFlipper.showPrevious();
        }
        else {
            super.onBackPressed();
        }
    }
}
