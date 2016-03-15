package com.estasvegano.android.estasvegano.view.util;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;

import com.estasvegano.android.estasvegano.R;

import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.util.Date;

import timber.log.Timber;

public class ImagePickerHelper {

    private final int maxSize;
    private final int cameraRequestCode;
    private final int galleryRequestCode;
    private Uri imageUri;

    public ImagePickerHelper(int maxSize, final int cameraRequestCode, final int galleryRequestCode) {
        this.maxSize = maxSize;
        this.cameraRequestCode = cameraRequestCode;
        this.galleryRequestCode = galleryRequestCode;
    }

    @NonNull
    private static Bitmap rotateImageIfNeeded(@NonNull Bitmap bitmap, @NonNull String path) {
        ExifInterface ei;
        try {
            ei = new ExifInterface(path);
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    bitmap = rotateImage(bitmap, 90);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    bitmap = rotateImage(bitmap, 180);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    bitmap = rotateImage(bitmap, 270);
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    @NonNull
    private static Bitmap rotateImage(@NonNull Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        try {
            return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                    matrix, true);
        } catch (OutOfMemoryError err) {
            err.printStackTrace();
        }
        return source;
    }

    public void showImagePickerDialog(@NonNull final Context context, @NonNull final Fragment fragment) {
        new AlertDialog.Builder(context).
                setTitle(R.string.choose_image_dialog_title).
                setMessage(R.string.choose_image_dialog_message).
                setPositiveButton(
                        R.string.choose_image_dialog_camera,
                        (dialog, which) -> {
                            final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, getImageUri());
                            fragment.startActivityForResult(intent, cameraRequestCode);
                        }
                )
                .setNegativeButton(
                        R.string.choose_image_dialog_gallery,
                        (dialog, which) -> {
                            Intent intent = new Intent();
                            intent.setType("image/*");
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            fragment.startActivityForResult(Intent.createChooser(intent, ""), galleryRequestCode);
                        }
                )
                .create().show();
    }

    @Nullable
    public Bitmap handleActivityResult(@NonNull Context context, int requestCode, @Nullable Intent data) {
        if (requestCode == galleryRequestCode) {
            if (data != null) {
                if (Build.VERSION.SDK_INT < 19) {
                    String[] projection = {MediaStore.MediaColumns.DATA};
                    Cursor cursor = context.getContentResolver().query(data.getData(),
                            projection, null, null, null);
                    if (cursor != null) {
                        cursor.moveToFirst();
                        int idx = cursor.getColumnIndex(projection[0]);
                        String fileSrc = cursor.getString(idx);
                        cursor.close();
                        return decodeFile(fileSrc);
                    }
                } else {
                    ParcelFileDescriptor parcelFileDescriptor;
                    try {
                        parcelFileDescriptor = context.getContentResolver().openFileDescriptor(data.getData(), "r");
                        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
                        Bitmap image = decodeFileDescriptor(fileDescriptor);
                        parcelFileDescriptor.close();
                        return image;
                    } catch (IOException e) {
                        Timber.e(e, "Get image from gallery failed");
                    }
                }
            }
        }
        if (requestCode == cameraRequestCode) {
            return decodeFile(getImageUri().getPath());
        }

        return null;
    }

    @NonNull
    private Uri getImageUri() {
        if (imageUri != null) return imageUri;

        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),
                "image" + new Date().getTime() + ".png");
        imageUri = Uri.fromFile(file);
        return imageUri;
    }

    @Nullable
    private Bitmap decodeFile(@NonNull String path) {
        try {
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, o);

            int scale = 1;
            while (o.outWidth / scale / 2 >= maxSize || o.outHeight / scale / 2 >= maxSize) {
                scale *= 2;
            }

            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            Bitmap original = BitmapFactory.decodeFile(path, o2);
            return rotateImageIfNeeded(original, path);
        } catch (Exception e) {
            Timber.e(e, "Decode image from gallery failed");
        }
        return null;
    }

    @Nullable
    private Bitmap decodeFileDescriptor(@NonNull FileDescriptor descriptor) {
        try {
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeFileDescriptor(descriptor, null, o);

            int scale = 1;
            while (o.outWidth / scale / 2 >= maxSize || o.outHeight / scale / 2 >= maxSize) {
                scale *= 2;
            }

            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            Bitmap original = BitmapFactory.decodeFileDescriptor(descriptor, null, o2);
            return original;
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }
}