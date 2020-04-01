package xin.heng.service.vo;

import java.io.File;
import java.util.UUID;

public class HXFileHolder {
    private String boundary = UUID.randomUUID().toString();
    private String uploadName;
    private File file;

    public String getUploadName() {
        return uploadName;
    }

    public HXFileHolder setUploadName(String uploadName) {
        this.uploadName = uploadName;
        return this;
    }

    public File getFile() {
        return file;
    }

    public HXFileHolder setFile(File file) {
        this.file = file;
        return this;
    }

    public String getBoundary() {
        return boundary;
    }

    public HXFileHolder setBoundary(String boundary) {
        this.boundary = boundary;
        return this;
    }
}
