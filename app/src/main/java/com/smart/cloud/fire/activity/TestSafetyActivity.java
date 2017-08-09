package com.smart.cloud.fire.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.smart.cloud.fire.global.ConstantValues;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import butterknife.Bind;
import butterknife.ButterKnife;
import fire.cloud.smart.com.smartcloudfire.R;

public class TestSafetyActivity extends Activity {

    @Bind(R.id.title_tv)
    TextView title_tv;
    @Bind(R.id.video_view)
    VideoView videoview;
    @Bind(R.id.webview)
    WebView webView;
    @Bind(R.id.safety_progressbar)
    ProgressBar safety_progressbar;
    @Bind(R.id.video_lin)
    RelativeLayout video_lin;
    @Bind(R.id.text_lin)
    LinearLayout text_lin;
    @Bind(R.id.progress_lin)
    RelativeLayout progress_lin;
    @Bind(R.id.progress_text)
    TextView progress_text;



    String firstDir;
    String secondDir;
    String thirdDir;
    int studyType;
    Thread downloadThread=null;
    OutputStream os=null;
    InputStream is=null;

    Context mContext;

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if(msg.what!=100){
                progress_text.setVisibility(View.VISIBLE);
                int a=msg.what;
                progress_text.setText(a+"%");
            }else{
                progress_lin.setVisibility(View.GONE);
                progress_text.setVisibility(View.GONE);
//                Toast.makeText(mContext,"已下载至"+Environment.getExternalStorageDirectory()+"/zdstDownload/",Toast.LENGTH_LONG).show();

                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setMessage("已下载至"+Environment.getExternalStorageDirectory()+"/zdstDownload/");

                builder.setTitle("在别的应用打开？");

                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent("android.intent.action.VIEW");
                        intent.addCategory("android.intent.category.DEFAULT");
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        Uri uri = Uri.fromFile(new File(Environment.getExternalStorageDirectory()+"/zdstDownload/"+thirdDir));
                        intent.setDataAndType(uri, "application/msword");
                        startActivity(intent);
                        finish();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                });
                builder.create().show();

            }
            super.handleMessage(msg);
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_safety);
        ButterKnife.bind(this);
        mContext=this;
        progress_lin.setVisibility(View.VISIBLE);

        firstDir=getIntent().getStringExtra("firstDir");
        secondDir=getIntent().getStringExtra("secondDir");
        thirdDir=getIntent().getStringExtra("ThirdDir");
        studyType=getIntent().getIntExtra("StudyType",1);
        title_tv.setText(thirdDir);

        String fileType=getExtensionName(thirdDir);
        if(fileType.equals("txt")||fileType.equals("jpg")){

            webView.getSettings().setDefaultTextEncodingName("gbk");
            // 开启支持视频
            webView.getSettings().setPluginState(WebSettings.PluginState.ON);
            webView.getSettings().setGeolocationEnabled(true);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setAllowFileAccessFromFileURLs(true);
//            webView.getSettings().setUseWideViewPort(true);
            webView.setWebViewClient(new WebViewClient(){
                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    safety_progressbar.setVisibility(View.GONE);
                }

                @Override
                public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                    super.onReceivedError(view, request, error);
                    safety_progressbar.setVisibility(View.GONE);
                }

                @Override
                public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
                    super.onReceivedHttpError(view, request, errorResponse);
                    safety_progressbar.setVisibility(View.GONE);
                }
            });
            webView.getSettings().setTextSize(WebSettings.TextSize.LARGER);
            webView.getSettings().setBuiltInZoomControls(true);
            if(studyType==1){
                webView.loadUrl(ConstantValues.SAFETY+firstDir+"/"+secondDir+"/"+thirdDir);
            }else{
                webView.loadUrl(ConstantValues.RULE+firstDir+"/"+thirdDir);
            }
        }else if (fileType.equals("mp4")){
            video_lin.setVisibility(View.VISIBLE);
//            videoview.setVideoURI(Uri.parse(getURLEncoderString("http://119.29.224.28:51091/safety/"+firstDir+"/"+secondDir+"/"+thirdDir)));
            String path= null;
            try {
                path = ConstantValues.SAFETY+firstDir+"/"+secondDir+"/"+ URLEncoder.encode(thirdDir,"utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            videoview.setVideoURI(Uri.parse(path));
            MediaController mediaController=new MediaController(this);
            mediaController.show();
            videoview.setMediaController(new MediaController(this));
            videoview.requestFocus();
            videoview.start();
            videoview.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    safety_progressbar.setVisibility(View.GONE);
                }
            });
        }else if(fileType.equals("pdf")||fileType.equals("doc")){
            String path= null;
                path = ConstantValues.RULE+firstDir+"/"+ thirdDir;
            final String finalPath = path;
            downloadThread=new Thread(new Runnable() {
                @Override
                public void run() {
                    download(finalPath);
                }
            });
            downloadThread.start();

        }


    }

    public static String getExtensionName(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot >-1) && (dot < (filename.length() - 1))) {
                return filename.substring(dot + 1);
            }
        }
        return filename;
    }


    public void download(String path){
        String urlPath=path;
        String filename=urlPath.substring(urlPath.lastIndexOf("/")+1);
        String dirName = Environment.getExternalStorageDirectory()+"/zdstDownload/";
        String newFilepath=dirName+filename;
        try {
            String changeFilename=URLEncoder.encode(filename,"utf-8");
            urlPath=urlPath.substring(0,urlPath.lastIndexOf("/")+1)+changeFilename;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 获得存储卡路径，构成 保存文件的目标路径

        File f = new File(dirName);
        if(!f.exists()){
            f.mkdir();
            }

        File file = new File(newFilepath);
        //如果目标文件已经存在，则删除。产生覆盖旧文件的效果
        if(file.exists()){
            file.delete();
        }else{
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            // 构造URL
            URL url = new URL(urlPath);
            // 打开连接
            URLConnection con = url.openConnection();
            //获得文件的长度
            int contentLength = con.getContentLength();
            System.out.println("长度 :"+contentLength);
            // 输入流
            is = con.getInputStream();
            // 1K的数据缓冲
            byte[] bs = new byte[1024];
            // 读取到的数据长度
            int len;
            // 输出的文件流
           os = new FileOutputStream(newFilepath);
            // 开始读取
            int loadNum=0;
            while ((len = is.read(bs)) != -1) {
                os.write(bs, 0, len);
                loadNum+=len;
                Message msg=new Message();
                float a=(float) loadNum/(float) contentLength*100;
                msg.what=(int)a;
                handler.sendMessage(msg);
                }

            safety_progressbar.setVisibility(View.GONE);
            // 完毕，关闭所有链接
            os.close();
            is.close();
            } catch (Exception e) {
            e.printStackTrace();
            }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler=null;
    }
}
