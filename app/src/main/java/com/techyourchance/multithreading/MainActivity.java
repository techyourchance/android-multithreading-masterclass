package com.techyourchance.multithreading;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Choreographer;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.techyourchance.fragmenthelper.FragmentContainerWrapper;
import com.techyourchance.multithreading.common.ToolbarManipulator;
import com.techyourchance.multithreading.common.ScreensNavigator;
import com.techyourchance.multithreading.common.dependencyinjection.PresentationCompositionRoot;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class MainActivity extends AppCompatActivity implements
        FragmentContainerWrapper,
        ToolbarManipulator {

    private PresentationCompositionRoot mPresentationCompositionRoot;
    private ScreensNavigator mScreensNavigator;

    private ImageButton mBtnBack;
    private TextView mTxtScreenTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPresentationCompositionRoot = new PresentationCompositionRoot(
                this,
                ((MyApplication)getApplication()).getApplicationCompositionRoot()
        );

        mScreensNavigator = mPresentationCompositionRoot.getScreensNavigator();

        mBtnBack = findViewById(R.id.btn_back);
        mTxtScreenTitle = findViewById(R.id.txt_screen_title);

        mBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mScreensNavigator.navigateUp();
            }
        });

        if (savedInstanceState == null) {
            mScreensNavigator.toHomeScreen();
        }

        reduceChoreographerSkippedFramesWarningThreshold();
    }

    private void reduceChoreographerSkippedFramesWarningThreshold() {
        Field field = null;
        try {
            field = Choreographer.class.getDeclaredField("SKIPPED_FRAME_WARNING_LIMIT" );
            field.setAccessible(true);
            field.setInt(field, field.getModifiers() & ~Modifier.FINAL);
            field.set(null, 1);
        } catch (NoSuchFieldException|IllegalAccessException e) {
            // probably failed to change Choreographer's field, but it's not critical
        }
    }

    @Override
    public void onBackPressed() {
        mScreensNavigator.navigateBack();
    }

    @NonNull
    @Override
    public ViewGroup getFragmentContainer() {
        return findViewById(R.id.frame_content);
    }

    @Override
    public void setScreenTitle(String screenTitle) {
        mTxtScreenTitle.setText(screenTitle);
    }

    @Override
    public void showUpButton() {
        mBtnBack.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideUpButton() {
        mBtnBack.setVisibility(View.INVISIBLE);
    }
}
