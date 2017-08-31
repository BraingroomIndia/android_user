package com.braingroom.user.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.braingroom.user.R;
import com.braingroom.user.viewmodel.ViewModel;
import com.braingroom.user.viewmodel.fragment.ConnectPostViewModel;

import lombok.Getter;


/**
 * Created by agrahari on 07/04/17.
 */

public class ConnectPostFragment extends BaseFragment {
    @Getter
    ConnectPostViewModel viewModel;

    private String TAG = getClass().getCanonicalName();

    private static final String POST_TYPE = "POST_TYPE";

    public static ConnectPostFragment newInstance() {
        return new ConnectPostFragment();
    }

    public static BaseFragment newInstanceByPostType(String postType) {
        ConnectPostFragment mFragment = new ConnectPostFragment();
        Bundle args = new Bundle();
        args.putString(POST_TYPE, postType);
        mFragment.setArguments(args);
        return mFragment;
    }

    public interface UiHelper {
        void next();
    }

    @NonNull
    @Override
    protected ViewModel createViewModel() {
        Bundle args = getArguments();
        String postType = null;
        if (args != null) {
            postType = args.getString(POST_TYPE);
        }
        viewModel = new ConnectPostViewModel(activity.getMessageHelper(), activity.getNavigator(), activity.getHelperFactory(), postType, new UiHelper() {
            @Override
            public void next() {
                getActivity().onBackPressed();
            }
        });
        return viewModel;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_connect_post;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: " + requestCode);
        vm.handleActivityResult(requestCode, resultCode, data);
    }




//    /**
//     * TODO: Need to start image chooser activity from fragment instead of activity to receive onActivityResult call
//     * XXX: This block is written to bypass Navigator inside fragment
//     */
//    private Navigator mNavigator = new Navigator() {
//        @Override
//        public void navigateActivity(Class<?> destination, @Nullable Bundle bundle) {
//            activity.getNavigator().navigateActivity(destination,bundle);
//        }
//
//        @Override
//        public void navigateActivityForResult(Class<?> destination, @Nullable Bundle bundle, int reqCode) {
//            activity.getNavigator().navigateActivityForResult(destination,bundle,reqCode);
//        }
//
//        @Override
//        public void navigateActivity(Intent intent) {
//            activity.getNavigator().navigateActivity(intent);
//        }
//
//        @Override
//        public void finishActivity() {
//            activity.getNavigator().finishActivity();
//        }
//
//        @Override
//        public void launchImageChooserActivity(final int reqCode) {
//
//            RxPermissions rxPermissions = new RxPermissions(getActivity());
//            rxPermissions.request(Manifest.permission.READ_EXTERNAL_STORAGE).subscribe(new Consumer<Boolean>() {
//                @Override
//                public void accept(@io.reactivex.annotations.NonNull Boolean granted) throws Exception {
//                    if (granted) {
//                        Intent intent = new Intent();
//                        intent.setType("image/*");
//                        intent.setAction(Intent.ACTION_GET_CONTENT);
//                        startActivityForResult(Intent.createChooser(intent, "Select Picture"), reqCode);
//                    }
//                }
//            });
//
//
//        }
//
//        @Override
//        public void launchVideoChooserActivity(final int reqCode) {
//            RxPermissions rxPermissions = new RxPermissions(getActivity());
//            rxPermissions.request(Manifest.permission.READ_EXTERNAL_STORAGE).subscribe(new Consumer<Boolean>() {
//                @Override
//                public void accept(@io.reactivex.annotations.NonNull Boolean granted) throws Exception {
//                    if (granted) {
//                        Intent intent = new Intent();
//                        intent.setType("video/*");
//                        intent.setAction(Intent.ACTION_GET_CONTENT);
//                        startActivityForResult(Intent.createChooser(intent, "Select Video"), reqCode);
//                    }
//                }
//            });
//        }
//
//        @Override
//        public void launchPlaceSearchIntent(int reqCode) {
//            activity.getNavigator().launchPlaceSearchIntent(reqCode);
//        }
//
//        @Override
//        public void finishActivity(Intent resultData) {
//            activity.getNavigator().finishActivity(resultData);
//        }
//
//        @Override
//        public void openStandaloneYoutube(String videoId) {
//            activity.getNavigator().openStandaloneYoutube(videoId);
//        }
//    };

}
