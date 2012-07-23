package org.healthchecktool.reporter;

/**
 * Created with IntelliJ IDEA.
 * User: azvolskiy
 * Date: 7/23/12
 * Time: 2:09 PM
 * To change this template use File | Settings | File Templates.
 */

import java.io.IOException;
import jxl.write.WriteException;

import org.healthchecktool.util.excel.WriteExcel;

public class reporter {
    private WriteExcel excel;
    private String content;
    private String fielPath;

    public reporter(String filePath,String content) {
        this.content = content;
        this.fielPath = filePath;
    }

    public void createExcel() throws WriteException, IOException {
        excel = new WriteExcel(this.fielPath);
        excel.setContent(content);
        excel.write();
    }
}
