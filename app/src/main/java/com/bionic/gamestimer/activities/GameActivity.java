package com.bionic.gamestimer.activities;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bionic.gamestimer.R;
import com.bionic.gamestimer.base.BaseActivity;



import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import com.musenkishi.wally.anim.interpolator.EaseInOutBezierInterpolator;
import com.musenkishi.wally.base.WallyApplication;
import com.musenkishi.wally.dataprovider.DataProvider;
import com.musenkishi.wally.dataprovider.FileManager;
import com.musenkishi.wally.dataprovider.models.DataProviderError;
import com.musenkishi.wally.dataprovider.models.SaveImageRequest;
import com.musenkishi.wally.models.Author;
import com.musenkishi.wally.models.Image;
import com.musenkishi.wally.models.ImagePage;
import com.musenkishi.wally.models.Size;
import com.musenkishi.wally.observers.FileChangeReceiver;
import com.musenkishi.wally.util.Blur;
import com.musenkishi.wally.util.PaletteRequest;
import com.musenkishi.wally.views.ObservableScrollView;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

import static com.musenkishi.wally.views.ObservableScrollView.ScrollViewListener;


/**
 * Created by Leha on 22.01.2015.
 */
public class GameActivity extends BaseActivity implements Handler.Callback {

    private Handler uiHandler;
    private Handler backgroundHandler;

    private ObservableScrollView scrollView;
    private PhotoView photoView;
    private PhotoViewAttacher photoViewAttacher;
    private ImageButton buttonFullscreen;
    private ProgressBar loader;
    private Uri pageUri;
    private ShareActionProvider shareActionProvider;
    private TextView textViewUploader;
    private TextView textViewUploadDate;
    private TextView textViewSource;
    private TextView textViewResolution;
    private TextView textViewCategory;
    private TextView textViewRating;
    private Button buttonSetAs;
    private Button buttonSave;
    private ImagePage imagePage;
    private ViewGroup imageHolder;
    private ViewGroup photoLayoutHolder;

    private int currentHandlerCode;
    private Palette palette;
    private ViewGroup toolbar;
    private ViewGroup specsLayout;
    private Size imageSize;

    private boolean isInFullscreen = false;
    private View detailsViewGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_details);

        setToolbar((Toolbar) findViewById(R.id.toolbar));

        if (getToolbar() != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setStatusBarColor(Color.TRANSPARENT);
                getWindow().getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                getToolbar().setPadding(0, getStatusBarHeight(), 0, 0);
            }
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
        }

        final Intent intent = getIntent();
        String action = intent.getAction();


    }


    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    private void setupPaddings(final Size size, boolean animate) {

        int animationDuration = animate ? 300 : 0;

        photoView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        photoViewAttacher.setScaleType(ImageView.ScaleType.CENTER_CROP);

        final int sidePadding = getResources().getDimensionPixelSize(R.dimen.activity_details_scrollview_side_padding);
        int fabPadding = getResources().getDimensionPixelSize(R.dimen.fab_padding_positive);

        int minimumAllowedHeight = fabPadding;

        if (size.getHeight() < minimumAllowedHeight) {
            size.setHeight(size.getHeight());
            ValueAnimator valueAnimator = ValueAnimator.ofInt(photoLayoutHolder.getPaddingTop());
            valueAnimator.setInterpolator(new EaseInOutBezierInterpolator());
            valueAnimator.setDuration(animationDuration);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    photoLayoutHolder.setPadding(
                            0,
                            (Integer) valueAnimator.getAnimatedValue(),
                            0,
                            0
                    );
                }
            });
            valueAnimator.start();
        } else {
            photoLayoutHolder.setPadding(
                    0,
                    0,
                    0,
                    0
            );
        }

        scrollView.setPadding(
                0,
                0,
                0,
                -fabPadding
        );
        specsLayout.setPadding(0, 0, 0, fabPadding);

        ValueAnimator valueAnimator = ValueAnimator.ofInt(detailsViewGroup.getPaddingTop(), size.getHeight());
        valueAnimator.setInterpolator(new EaseInOutBezierInterpolator());
        valueAnimator.setDuration(animationDuration);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                detailsViewGroup.setPadding(sidePadding,
                        (Integer) valueAnimator.getAnimatedValue(),
                        sidePadding,
                        detailsViewGroup.getPaddingBottom());
            }
        });
        valueAnimator.start();
    }

    private void setupViews() {
        scrollView = (ObservableScrollView) findViewById(R.id.image_details_scrollview);
        imageHolder = (ViewGroup) findViewById(R.id.image_details_imageview_holder);
        photoView = (PhotoView) findViewById(R.id.image_details_imageview);
        buttonFullscreen = (ImageButton) findViewById(R.id.image_details_button_fullscreen);
        loader = (ProgressBar) findViewById(R.id.image_details_loader);
        textViewUploader = (TextView) findViewById(R.id.image_details_uploader);
        textViewUploadDate = (TextView) findViewById(R.id.image_details_upload_date);
        textViewSource = (TextView) findViewById(R.id.image_details_source);
        textViewResolution = (TextView) findViewById(R.id.image_details_resolution);
        textViewCategory = (TextView) findViewById(R.id.image_details_category);
        textViewRating = (TextView) findViewById(R.id.image_details_rating);
        buttonSetAs = (Button) findViewById(R.id.toolbar_set_as);
        buttonSave = (Button) findViewById(R.id.toolbar_save);
        toolbar = (ViewGroup) findViewById(R.id.image_details_toolbar);
        photoLayoutHolder = (ViewGroup) findViewById(R.id.image_details_photo_layout_holder);
        specsLayout = (ViewGroup) findViewById(R.id.image_details_specs);
        detailsViewGroup = findViewById(R.id.image_details_layout);

        specsLayout.setAlpha(0.0f);

        int sidePadding = getResources().getDimensionPixelSize(R.dimen.activity_details_scrollview_side_padding);
        int fabPadding = getResources().getDimensionPixelSize(R.dimen.fab_padding_positive);
        scrollView.setPadding(0, 0, 0, -fabPadding);
        specsLayout.setPadding(0, 0, 0, fabPadding);
        photoLayoutHolder.setPadding(0, 0, 0, 0);
        detailsViewGroup.setPadding(
                sidePadding,
                detailsViewGroup.getPaddingTop(),
                sidePadding,
                detailsViewGroup.getPaddingBottom()
        );

        photoViewAttacher = new PhotoViewAttacher(photoView);
        photoViewAttacher.setZoomable(false);
        photoViewAttacher.setScaleType(ImageView.ScaleType.FIT_XY);
    }
}
