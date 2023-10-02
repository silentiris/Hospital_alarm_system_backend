package com.sipc.hospitalalarmsystem.util;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.http.HttpMethodName;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectRequest;
import com.sipc.hospitalalarmsystem.config.OssConfig;
import jakarta.annotation.Resource;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameRecorder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.*;
import static org.bytedeco.javacpp.avcodec.AV_CODEC_ID_H264;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Data
@Slf4j
@Component
public class RecordUtil extends Thread {
    private String streamURL;
    private final ThreadLocal<Deque<Frame>> frameThreadLocal = ThreadLocal.withInitial(LinkedList::new);
    @Autowired
    private OssConfig ossConfig ;
    public RecordUtil(String streamURL) {
        this.streamURL = streamURL;
    }

    public FFmpegFrameRecorder getRecorder(OutputStream os) {
        FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(os, 540, 360, 1);
        recorder.setVideoCodec(AV_CODEC_ID_H264);
        recorder.setFormat("flv");
        recorder.setFrameRate(25);
        return recorder;
    }

    public String getClipLink()  {
        log.info("开始生成片段...");
        InputStream is;
        try {
            is = new ByteArrayInputStream(getMovie(new ArrayList<>(frameThreadLocal.get())).toByteArray());
        } catch (FrameRecorder.Exception e) {
            throw new RuntimeException(e);
        }
        ObjectMetadata objectMetadata = new ObjectMetadata();
        try {
            objectMetadata.setContentLength(is.available());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String randomId = String.valueOf(UUID.randomUUID());
        PutObjectRequest putObjectRequest = new PutObjectRequest(
                "hospital-alarm-1318141347",
                randomId + ".flv",
                is,
                objectMetadata);
        COSClient cosClient = ossConfig.cosClient();
        cosClient.putObject(putObjectRequest);

        return cosClient.generatePresignedUrl(
                "hospital-alarm-1318141347",
                randomId + ".flv",
                new Date(System.currentTimeMillis() + (long) 365 * 24 * 60 * 60 * 1000),
                HttpMethodName.GET).toString();
    }


    @Override
    public void run() {
        System.out.println(streamURL);
        int clipSize = 250;
        try (FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(streamURL)) {
            grabber.setFrameRate(25);
            grabber.start();
            Frame frame = grabber.grabImage();
            while (frame == null) {
                frame = grabber.grabImage();
                log.info(streamURL+"等待视频流...");
            }
            while ((frame != null)) {
                frame = grabber.grabImage();
                frameThreadLocal.get().offer(frame.clone());
                if (frameThreadLocal.get().size() > clipSize) {
                    frameThreadLocal.get().poll();
                }
            }
            System.out.println("录制完成");
            grabber.stop();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ByteArrayOutputStream getMovie(List<Frame> frames) throws FrameRecorder.Exception {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        FFmpegFrameRecorder cacheRecorder = getRecorder(os);
        cacheRecorder.start();
        for (Frame frame : frames) {
            cacheRecorder.record(frame);
        }
        cacheRecorder.stop();
        return os;
    }
}
