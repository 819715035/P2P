package cn.cndoppler.p2p.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.cndoppler.p2p.R;
import cn.cndoppler.p2p.common.ActivityManager;
import cn.cndoppler.p2p.common.BaseActivity;
import cn.cndoppler.p2p.ui.CommonTitleView;
import cn.cndoppler.p2p.util.BitmapUtils;
import cn.cndoppler.p2p.util.ToastUtils;

public class UserInfoActivity extends BaseActivity{

    @BindView(R.id.titleView)
    CommonTitleView titleView;
    @BindView(R.id.iv_user_icon)
    ImageView ivUserIcon;
    @BindView(R.id.tv_user_change)
    TextView tvUserChange;
    @BindView(R.id.btn_user_logout)
    Button btnUserLogout;
    private static final int PICTURE = 100;
    private static final int CAMERA = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        ButterKnife.bind(this);
        ActivityManager.getInstance().add(this);
        setListener();
    }

    private void setListener() {
        titleView.setLeftListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityManager.getInstance().removeCurrent();
            }
        });

        btnUserLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //"退出登录"button的回调方法
                //1.将保存在sp中的数据清除
                SharedPreferences sp = UserInfoActivity.this.getSharedPreferences("user_info", Context.MODE_PRIVATE);
                sp.edit().clear().commit();//清除数据操作必须提交；提交以后，文件仍存在，只是文件中的数据被清除了
                //2.将本地保存的图片的file删除
                File filesDir;
                if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){//判断sd卡是否挂载
                    //路径1：storage/sdcard/Android/data/包名/files
                    filesDir = UserInfoActivity.this.getExternalFilesDir("");

                }else{//手机内部存储
                    //路径：data/data/包名/files
                    filesDir = UserInfoActivity.this.getFilesDir();

                }
                File file = new File(filesDir,"icon.png");
                if(file.exists()){
                    file.delete();//删除存储中的文件
                }
                //3.销毁所有的activity
                UserInfoActivity.this.removeAll();
                //4.重新进入首页面
                UserInfoActivity.this.openActivity(MainActivity.class);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 0){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                startImage();
            }else{
                ToastUtils.showToastShort(this,"请先授权，不然无法调用图库");
            }
        }
        return;
    }


    @OnClick({R.id.tv_user_change})
    public void onChangeIcon(View view){
        String[] items = new String[]{"图库","相机"};
        new AlertDialog.Builder(this)
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0 :
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                                {
                                    if (ContextCompat.checkSelfPermission(UserInfoActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                                        //没有授权
                                        ActivityCompat.requestPermissions(UserInfoActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},0);
                                    }else {
                                        startImage();
                                    }
                                }

                                break;
                            case 1://相机
//                                    UIUtils.toast("相机",false);
                                Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(camera, CAMERA);
                                break;
                        }
                    }
                })
                .setTitle("选择来源")
                .setCancelable(false)
                .create().show();
    }

    private void startImage() {
        //图库
        //UIUtils.toast("图库",false);
        //启动其他应用的activity:使用隐式意图
        Intent picture = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(picture, PICTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICTURE && resultCode == RESULT_OK && data!=null){
            //图库
            Uri selectedImage = data.getData();
            //android各个不同的系统版本,对于获取外部存储上的资源，返回的Uri对象都可能各不一样,
            // 所以要保证无论是哪个系统版本都能正确获取到图片资源的话就需要针对各种情况进行一个处理了
            //这里返回的uri情况就有点多了
            //在4.4.2之前返回的uri是:content://media/external/images/media/3951或者file://....
            // 在4.4.2返回的是content://com.android.providers.media.documents/document/image

            String pathResult = getPath(selectedImage);
            //存储--->内存
            Bitmap decodeFile = BitmapFactory.decodeFile(pathResult);
            Bitmap zoomBitmap = BitmapUtils.zoom(decodeFile, ivUserIcon.getWidth(),ivUserIcon.getHeight());
            //bitmap圆形裁剪
            Bitmap circleImage = BitmapUtils.circleBitmap(zoomBitmap);

            //加载显示
            ivUserIcon.setImageBitmap(circleImage);
            //上传到服务器（省略）

            //保存到本地
            saveImage(circleImage);
        }else if (requestCode == CAMERA && resultCode == RESULT_OK && data!=null){
            //相机
            //获取intent中的图片对象
            Bundle extras = data.getExtras();
            Bitmap bitmap = (Bitmap) extras.get("data");
            //对获取到的bitmap进行压缩、圆形处理
            bitmap = BitmapUtils.zoom(bitmap,ivUserIcon.getWidth(),ivUserIcon.getHeight());
            bitmap = BitmapUtils.circleBitmap(bitmap);

            //加载显示
            ivUserIcon.setImageBitmap(bitmap);
            //上传到服务器（省略）

            //保存到本地
            saveImage(bitmap);
        }
    }

    //将Bitmap保存到本地的操作

    /**
     * 数据的存储。（5种）
     * Bimap:内存层面的图片对象。
     *
     * 存储--->内存：
     *      BitmapFactory.decodeFile(String filePath);
     *      BitmapFactory.decodeStream(InputStream is);
     * 内存--->存储：
     *      bitmap.compress(Bitmap.CompressFormat.PNG,100,OutputStream os);
     */
    private void saveImage(Bitmap bitmap) {
        File filesDir;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            //判断sd卡是否挂载
            //路径1：storage/sdcard/Android/data/包名/files
            filesDir = getExternalFilesDir("");
        }else{
            //手机内部存储
            //路径：data/data/包名/files
            filesDir = getFilesDir();
        }
        File file = null;
        FileOutputStream os = null;
        try {
            file = new File(filesDir,"icon.png");
            os = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG,100,os);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }finally {
            if (os!=null){
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 根据系统相册选择的文件获取路径
     *
     * @param uri
     */
    @SuppressLint("NewApi")
    private String getPath(Uri uri) {
        int sdkVersion = Build.VERSION.SDK_INT;
        //高于4.4.2的版本
        if (sdkVersion >= 19) {
            Log.e("TAG", "uri auth: " + uri.getAuthority());
            if (isExternalStorageDocument(uri)) {
                String docId = DocumentsContract.getDocumentId(uri);
                String[] split = docId.split(":");
                String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(id));
                return getDataColumn(this, contentUri, null, null);
            } else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};
                return getDataColumn(this, contentUri, selection, selectionArgs);
            } else if (isMedia(uri)) {
                String[] proj = {MediaStore.Images.Media.DATA};
                Cursor actualimagecursor = this.managedQuery(uri, proj, null, null, null);
                int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                actualimagecursor.moveToFirst();
                return actualimagecursor.getString(actual_image_column_index);
            }


        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();
            return getDataColumn(this, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    /**
     * uri路径查询字段
     *
     * @param context
     * @param uri
     * @param selection
     * @param selectionArgs
     * @return
     */
    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    private boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static boolean isMedia(Uri uri) {
        return "media".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }
}
