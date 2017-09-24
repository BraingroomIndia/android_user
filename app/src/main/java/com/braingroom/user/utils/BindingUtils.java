package com.braingroom.user.utils;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.databinding.BindingConversion;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.braingroom.user.R;
import com.braingroom.user.UserApplication;
import com.braingroom.user.databinding.NavHeaderConnectBinding;
import com.braingroom.user.databinding.NavHeaderHomeBinding;
import com.braingroom.user.view.SpacingDecoration;
import com.braingroom.user.view.adapters.Connectable;
import com.braingroom.user.view.adapters.RecyclerViewAdapter;
import com.braingroom.user.view.adapters.ViewModelBinder;
import com.braingroom.user.view.adapters.ViewPagerAdapter;
import com.braingroom.user.view.adapters.ViewProvider;
import com.braingroom.user.viewmodel.ConnectHomeViewModel;
import com.braingroom.user.viewmodel.EmptyItemViewModel;
import com.braingroom.user.viewmodel.HomeViewModel;
import com.braingroom.user.viewmodel.IconTextShimmerItemViewModel;
import com.braingroom.user.viewmodel.RowShimmerItemViewModel;
import com.braingroom.user.viewmodel.TileShimmerItemViewModel;
import com.braingroom.user.viewmodel.ViewModel;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;

@SuppressWarnings("unused")
public class BindingUtils {

    private static final String TAG = BindingUtils.class.getSimpleName();

    public static final String DIVIDER_TYPE_LINE = "line_divider";
    public static final String DIVIDER_TYPE_SPACE = "space_divider";

    @Nullable
    private static ViewModelBinder defaultBinder = null;

    @Nullable
    public static ViewModelBinder getDefaultBinder() {
        return defaultBinder;
    }

    public static void setDefaultBinder(@NonNull ViewModelBinder viewModelBinder) {
        defaultBinder = viewModelBinder;
    }

    @BindingAdapter("adapter")
    public static void bindAdapter(@NonNull ViewPager viewPager, @Nullable PagerAdapter adapter) {
        PagerAdapter oldAdapter = viewPager.getAdapter();

        // Disconnect previous adapter if its Connectable
        if (oldAdapter != null && oldAdapter instanceof Connectable) {
            Disposable subscription = (Disposable) viewPager.getTag(R.integer.tag_subscription);
            if (subscription != null && !subscription.isDisposed()) {
                subscription.dispose();
            }
            viewPager.setTag(R.integer.tag_subscription, null);
        }

        // Store connection (Subscription) if new adapter is Connectable
        if (adapter != null && adapter instanceof Connectable) {
            viewPager.setTag(R.integer.tag_subscription, ((Connectable) adapter).connect());
        }
        viewPager.setAdapter(adapter);
    }

    @BindingAdapter("adapter")
    public static void bindAdapter(@NonNull RecyclerView recyclerView, @Nullable RecyclerView.Adapter adapter) {
        recyclerView.setAdapter(adapter);
    }

    @BindingAdapter({"items", "view_provider"})
    public static void bindAdapterWithDefaultBinder(@NonNull RecyclerView recyclerView, @Nullable Observable<List<ViewModel>> items, @Nullable ViewProvider viewProvider) {
        RecyclerViewAdapter adapter = null;
        if (items != null && viewProvider != null) {
            Preconditions.checkNotNull(defaultBinder, "Default Binder");
            adapter = new RecyclerViewAdapter(items, viewProvider, defaultBinder);
        }
        bindAdapter(recyclerView, adapter);
    }


    @BindingAdapter({"items", "view_provider"})
    public static void bindAdapterWithDefaultBinder(@NonNull ViewPager viewPager, @Nullable Observable<List<ViewModel>> items, @Nullable ViewProvider viewProvider) {
        ViewPagerAdapter adapter = null;
        if (items != null && viewProvider != null) {
            Preconditions.checkNotNull(defaultBinder, "Default Binder");
            adapter = new ViewPagerAdapter(items, viewProvider, defaultBinder);
        }
        bindAdapter(viewPager, adapter);
    }

    @BindingConversion
    @NonNull
    public static ViewProvider getViewProviderForStaticLayout(@LayoutRes final int layoutId) {
        return new ViewProvider() {
            @Override
            public int getView(ViewModel vm) {
                if (vm instanceof RowShimmerItemViewModel)
                    return R.layout.item_shimmer_row;
                if (vm instanceof TileShimmerItemViewModel)
                    return R.layout.item_shimmer_tile;
                if (vm instanceof EmptyItemViewModel)
                    return R.layout.item_empty_data;
                if (vm instanceof IconTextShimmerItemViewModel)
                    return R.layout.icon_text_shimmer;
                return layoutId;
            }
        };
    }

    @BindingConversion
    @Nullable
    public static <T extends ViewModel> Observable<List<ViewModel>> toGenericList(@Nullable Observable<List<T>> specificList) {
        return specificList == null ? null : specificList.map(new Function<List<T>, List<ViewModel>>() {
            @Override
            public List<ViewModel> apply(List<T> ts) throws Exception {
                return new ArrayList<ViewModel>(ts);
            }
        });
    }

    @BindingConversion
    @Nullable
    public static <T extends ViewModel> Observable<List<ViewModel>> toListObservable(@Nullable List<T> specificList) {
        return specificList == null ? null :
                Observable.just((List<ViewModel>) new ArrayList<ViewModel>(specificList));
    }

    // Extra Utilities
    @BindingAdapter(value = {"layout_vertical", "divider_decoration", "space_length", "paginate", "viewmodel"}, requireAll = false)
    public static void bindLinearLayoutManager(@NonNull RecyclerView recyclerView, boolean vertical, String decorationType, float spaceLength, boolean paginate, final ViewModel vm) {
        int orientation = vertical ? RecyclerView.VERTICAL : RecyclerView.HORIZONTAL;
        final LinearLayoutManager lm = new LinearLayoutManager(recyclerView.getContext(), orientation, false);
        lm.setAutoMeasureEnabled(true);

        if (DIVIDER_TYPE_LINE.equalsIgnoreCase(decorationType)) {
            recyclerView.addItemDecoration(getDividerDecoration(recyclerView.getContext(), decorationType, orientation));
        }
        if (DIVIDER_TYPE_SPACE.equalsIgnoreCase(decorationType)) {
            recyclerView.addItemDecoration(new SpacingDecoration((int) spaceLength, 1));
        }
        recyclerView.setLayoutManager(lm);

        if (paginate) {
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                }

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    int visibleItemCount = lm.getChildCount();
                    int totalItemCount = lm.getItemCount();
                    int firstVisibleItemPosition = lm.findFirstVisibleItemPosition();

                    if (dy > 0 && ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount)) {
                        vm.paginate();
                    }

                }
            });

        }
    }

    @BindingAdapter(value = {"layout_grid_vertical", "span_count", "divider_decoration", "space_length"}, requireAll = false)
    public static void bindGridLayoutManager(@NonNull RecyclerView recyclerView, boolean vertical, int spanCount, String decorationType, float spaceLength) {
        int orientation = vertical ? RecyclerView.VERTICAL : RecyclerView.HORIZONTAL;
        if (spanCount == 0) spanCount = 1;
        GridLayoutManager gm = new GridLayoutManager(recyclerView.getContext(), spanCount, orientation, false);
        gm.setAutoMeasureEnabled(true);
        if (DIVIDER_TYPE_LINE.equalsIgnoreCase(decorationType)) {
            recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(),
                    RecyclerView.VERTICAL));
            recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(),
                    RecyclerView.HORIZONTAL));
        }
        if (DIVIDER_TYPE_SPACE.equalsIgnoreCase(decorationType)) {
            recyclerView.addItemDecoration(new SpacingDecoration((int) spaceLength, 2));
        }
        recyclerView.setLayoutManager(gm);
    }


    @BindingAdapter(value = {"imageUrl", "placeHolder", "errorUrl", "scaleWidth", "scaleHeight"}, requireAll = false)
    public static void setImageUrl(ImageView imageView, String url, int placeHolder, int errorUrl, int scaleWidth, int scaleHeight) {
        Log.d(TAG, "setImageUrl: " + url);

        Log.d(TAG, "setImageUrl: " + url);
        if ("".equals(url)) url = null;
        Picasso picasso = Picasso.with(imageView.getContext());
        RequestCreator requestCreator;
        requestCreator = picasso.load(url);
        if (placeHolder != 0) {
            requestCreator = requestCreator.placeholder(placeHolder);
        }
        if (errorUrl != 0) {
            requestCreator.error(errorUrl);
        }
        if (scaleWidth == 0) scaleWidth = 250;
        if (scaleHeight == 0) scaleHeight = 250;
        requestCreator.centerCrop()
                .fit().into(imageView);
    }


    @BindingAdapter(value = {"font"})
    public static void setTypeface(TextView textView, String fontName) {
        UserApplication app = UserApplication.getInstance();
        if (app.getFont(fontName) == null)
            app.putFontCache(fontName, fontName);
        textView.setTypeface(app.getFont(fontName));
    }

    @BindingAdapter(value = {"videoUrl"})
    public static void setVideoSrc(final VideoView videoView, String url) {
        if (url != null && !url.isEmpty()) {
            MediaController mc = new MediaController(videoView.getContext());
            mc.setAnchorView(videoView);
            mc.setMediaPlayer(videoView);
            Uri uri = Uri.parse(url);
            videoView.setVideoURI(uri);
            videoView.start();
        }
        videoView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });


    }

    @BindingAdapter("imageResource")
    public static void setImageResource(ImageView imageView, int resource) {
        imageView.setImageResource(resource);
    }

//    @BindingAdapter(value = {"bind:selectedValue", "bind:selectedValueAttrChanged"}, requireAll = false)
//    public static void bindSpinnerData(AppCompatSpinner pAppCompatSpinner, String newSelectedValue, final InverseBindingListener newTextAttrChanged) {
//        pAppCompatSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                newTextAttrChanged.onChange();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//            }
//        });
//        if (newSelectedValue != null) {
//            int pos = ((ArrayAdapter<String>) pAppCompatSpinner.getAdapter()).getPosition(newSelectedValue);
//            pAppCompatSpinner.setSelection(pos, true);
//        }
//    }

//    @InverseBindingAdapter(attribute = "bind:selectedValue", event = "bind:selectedValueAttrChanged")
//    public static String captureSelectedValue(AppCompatSpinner pAppCompatSpinner) {
//        return (String) pAppCompatSpinner.getSelectedItem();
//    }

    @BindingAdapter("menu")
    public static void setMenu(NavigationView navigationView, int id) {
        navigationView.inflateMenu(id);
    }

//    @BindingAdapter("app:preset")
//    public static void setupShimmer(ShimmerFrameLayout shimmerContainer, String preset) {
//        shimmerContainer.startShimmerAnimation();
//    }


    @BindingAdapter("errorText")
    public static void setErrorMessage(TextInputLayout view, String errorMessage) {
        if (!TextUtils.isEmpty(errorMessage)) {
            view.setErrorEnabled(true);
            view.setError(errorMessage);
        } else view.setErrorEnabled(false);
    }


    @BindingAdapter({"model"})
    public static void loadHeader(NavigationView view, HomeViewModel model) {
        NavHeaderHomeBinding binding = NavHeaderHomeBinding.inflate(LayoutInflater.from(view.getContext()));
        binding.setVm(model);
        binding.executePendingBindings();
        view.addHeaderView(binding.getRoot());
    }

    @BindingAdapter({"model"})
    public static void loadHeader(NavigationView view, ConnectHomeViewModel model) {
        NavHeaderConnectBinding binding = NavHeaderConnectBinding.inflate(LayoutInflater.from(view.getContext()));
        binding.setVm(model);
        binding.executePendingBindings();
        view.addHeaderView(binding.getRoot());
    }

    public static DividerItemDecoration getDividerDecoration(Context context, String dividerType, int orientation) {
        if (DIVIDER_TYPE_LINE.equals(dividerType)) {
            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(context, orientation);
            dividerItemDecoration.setDrawable(ContextCompat.getDrawable(context, R.drawable.divider_gray));
            return dividerItemDecoration;
        }
        return null;
    }


}
