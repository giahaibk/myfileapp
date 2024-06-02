package com.example.myfileapp.Fragments;

import android.Manifest;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfileapp.FileAdaptor;
import com.example.myfileapp.R;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;

import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class InternalFragment extends Fragment {
    private RecyclerView recyclerView;
    private FileAdaptor fileAdaptor;
    private List<File> fileList;
    private ImageView img_black;
    private TextView tv_pathHolder;
    File storage;
    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_internal, container, false);

        tv_pathHolder = view.findViewById(R.id.tv_pathHolder);
        img_black =view.findViewById(R.id.img_black);

        String internalStorage = System.getenv("EXTERNAL_STORAGE");
        storage = new File(internalStorage);
        tv_pathHolder.setText(storage.getAbsolutePath());

        runtimePermission();

        return view;
    }

    private void runtimePermission() {
        Dexter.withContext(getContext()).withPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.MANAGE_EXTERNAL_STORAGE,
                Manifest.permission.READ_MEDIA_AUDIO,
                Manifest.permission.READ_MEDIA_VIDEO,
                Manifest.permission.READ_MEDIA_IMAGES
        ).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                displayFiles();
            }
            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();
            }
        }).check();
    }
    public ArrayList<File> findFiles(File file) {
        ArrayList<File> arrayList = new ArrayList<>();
        File[] files = file.listFiles();

        for (File singleFile : files) {
            if (singleFile.isDirectory() && !singleFile.isHidden()) {
                arrayList.add(singleFile);
            }
        }
        for (File singleFile : files) {
            if (singleFile.getName().toLowerCase().endsWith(".jpg")
                    || singleFile.getName().toLowerCase().endsWith(".png")
                    || singleFile.getName().toLowerCase().endsWith(".jpeg")
                    || singleFile.getName().toLowerCase().endsWith(".gif")
                    || singleFile.getName().toLowerCase().endsWith(".mp4")
                    || singleFile.getName().toLowerCase().endsWith(".3gp")
                    || singleFile.getName().toLowerCase().endsWith(".mkv")
                    || singleFile.getName().toLowerCase().endsWith(".avi")
                    || singleFile.getName().toLowerCase().endsWith(".mov")
                    || singleFile.getName().toLowerCase().endsWith(".mp3")
                    || singleFile.getName().toLowerCase().endsWith(".wav")
                    || singleFile.getName().toLowerCase().endsWith(".apk")
                    || singleFile.getName().toLowerCase().endsWith(".zip")
                    || singleFile.getName().toLowerCase().endsWith(".rar")
                    || singleFile.getName().toLowerCase().endsWith(".7z")
                    || singleFile.getName().toLowerCase().endsWith(".pdf")
                    || singleFile.getName().toLowerCase().endsWith(".docx"))
            {
                arrayList.add(singleFile);
            }
        }
        return arrayList;
    }

    private void displayFiles() {
        recyclerView = view.findViewById(R.id.recycle_internal);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        fileList = new ArrayList<>();
        fileList.addAll(findFiles(storage));
        fileAdaptor = new FileAdaptor(getContext(), fileList);
        recyclerView.setAdapter(fileAdaptor);
    }

}
