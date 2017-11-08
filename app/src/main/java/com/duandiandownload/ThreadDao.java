package com.duandiandownload;

import java.util.List;

/**
 * User: 张亚博
 * Date: 2017-11-08 11:28
 * Description：
 */
public interface ThreadDao {
    void addThreadInfo(ThreadInfo threadInfo);
    void deleteThreadInfo(String url,int thread_id);
    void updateThreadInfo(String url,int thread_id,int finished);
    List<ThreadInfo> getThreadInfo(String url);
    boolean findThreadInfo(String url,int thread_id);
}
