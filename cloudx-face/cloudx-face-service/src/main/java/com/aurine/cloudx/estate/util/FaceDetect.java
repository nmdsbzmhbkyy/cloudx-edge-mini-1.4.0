package com.aurine.cloudx.estate.util;

import com.sun.jna.Native;
import com.sun.jna.NativeLong;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.NativeLongByReference;

public class FaceDetect {
    //private IFaceDetect detector;
    public FaceDetect(){
        //detector = IFaceDetect.Instance;
        Native.setProtected(false);
    }

    /** 指定jpg文件预识别人脸，输出照片、人脸框x,y,w,h
     * @param jpgfile jpg文件路径
     * @return 返回识别结果
     */
    public DetectResult detect(String jpgfile){
        DetectResult info = new DetectResult();
        NativeLongByReference sizeOut = new NativeLongByReference();
        IntByReference px = new IntByReference(), py = new IntByReference(),
                pw = new IntByReference(), ph = new IntByReference();
        px.setValue(0); py.setValue(0); pw.setValue(0); ph.setValue(0);
        sizeOut.setValue(new NativeLong(info.getJpgBuf().length));
        int res = IFaceDetect.FaceDetect(jpgfile, info.getJpgBuf(), sizeOut, px, py, pw, ph);
        info.setResult(res);
        info.setX(px.getValue());
        info.setY(py.getValue());
        info.setW(pw.getValue());
        info.setH(ph.getValue());
        info.setSizeBuf(sizeOut.getValue().intValue());

        return info;
    }
    public DetectResult detectStream(byte[]jpgBuffer){
        long sizeBuffer = jpgBuffer.length;
        DetectResult info = new DetectResult();
        NativeLongByReference sizeOut = new NativeLongByReference();
        IntByReference px = new IntByReference(), py = new IntByReference(),
                pw = new IntByReference(), ph = new IntByReference();
        NativeLong sizeVal = new NativeLong(sizeBuffer);
        sizeOut.setValue(new NativeLong(info.getJpgBuf().length));
        int res = IFaceDetect.FaceStreamDetect(jpgBuffer, sizeVal, info.getJpgBuf(), sizeOut, px, py, pw, ph);
        info.setResult(res);
        info.setX(px.getValue());
        info.setY(py.getValue());
        info.setW(pw.getValue());
        info.setH(ph.getValue());
        info.setSizeBuf(sizeOut.getValue().intValue());
        return info;
    }
}
