package com.duandiandownload;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private TextView tv;

    private boolean isDownload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar= (ProgressBar) this.findViewById(R.id.progressbar);
        progressBar.setMax(100);
        tv = (TextView) this.findViewById(R.id.progresstv);

        //开始下载
        findViewById(R.id.start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!isDownload){
                    FileInfo fileInfo  =new FileInfo(0,"http://b.hiphotos.baidu.com/image/pic/item/0823dd54564e925838c205c89982d158ccbf4e26.jpg","110.jpg",0,0);
                    Intent intent = new Intent(MainActivity.this,DownloadService.class);
                    intent.setAction(DownloadService.ACTION_START);
                    intent.putExtra("fileInfo",fileInfo);
                    startService(intent);
                    isDownload=true;
                }
            }
        });
        //停止下载
        findViewById(R.id.pause).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  downloadFileUtil.setIsDownload(false);
                isDownload=false;
                FileInfo fileInfo  =new FileInfo(0,"http://wap.apk.anzhi.com/data3/apk/201710/31/c81fa79c5a6e992b5e915707da17d4b9_69682800.apk","112.jpg",0,0);
                Intent intent = new Intent(MainActivity.this,DownloadService.class);
                intent.setAction(DownloadService.ACTION_STOP);
                intent.putExtra("fileInfo",fileInfo);
                startService(intent);
            }
        });

        init();

    }
    //初始化
    private void init() {

        //当打开下载界面显示历史下载进度
        ThreadDaoImpl dao  =new ThreadDaoImpl(this);
        List<ThreadInfo> threadInfos = dao.getThreadInfo("http://b.hiphotos.baidu.com/image/pic/item/0823dd54564e925838c205c89982d158ccbf4e26.jpg");
        if(threadInfos.size()>0){
            ThreadInfo threadInfo = threadInfos.get(0);
            int value=threadInfo.getFinished()*100/threadInfo.getEnd();
            progressBar.setProgress(value);
            tv.setText(value+"%");
        }
        //注册广播
        IntentFilter intent = new IntentFilter();
        intent.addAction(DownloadService.UPDATE);
        registerReceiver(reciver,intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(reciver);
    }

    BroadcastReceiver reciver  =new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int finished = intent.getIntExtra("finished",-1);
            if(finished!=-1){
                if ( finished<=100) {
                    progressBar.setProgress( finished);
                    tv.setText( finished+"%");
                    if( finished==100){
                        isDownload=false;
                        Toast.makeText(MainActivity.this, "下载完成", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    };

}
