package com.jcsoft.emsystem.bean;

import java.io.Serializable;

/**
 * 图片对象
 */
public class ImageItem implements Serializable {
    private static final long serialVersionUID = -7188270558443739436L;
    public String imageId;
    public String picName;
    public String thumbnailPath;
    public String sourcePath;
    public boolean isSelected = false;
    public String size;
    public int fileType;

    public ImageItem() {
    }

    public ImageItem(String imageId, String picName) {
        this.imageId = imageId;
        this.picName = picName;
    }
}
