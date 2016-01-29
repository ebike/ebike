package com.jcsoft.emsystem.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 相册对象
 */
public class ImageBucket implements Serializable {
    public int count = 0;
    public String bucketName;
    public List<ImageItem> imageList;
    public boolean selected = false;
}
