package com.aurine.cloudx.estate.util;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.NativeLong;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.NativeLongByReference;

/** Linx C库调用，目前集成了宇视人脸预识别库功能，这里做java层jna封装
 * @author chensl
 *
 */
interface IFaceDetectBase extends Library {
    final String libName = "facedetect";

    static IFaceDetectBase Instance = (IFaceDetectBase)Native.synchronizedLibrary(
            (IFaceDetectBase)Native.loadLibrary(libName, IFaceDetectBase.class)
    );

    /**
     * 指定jpg文件预识别人脸，输出照片、人脸框x,y,w,h
     * C/C++原型： int STDCALL FaceDetect(char * jpgfile, unsigned char *jpgBufOut, unsigned long *pSizeJpgBufOut, int *px, int *py, int *pw, int *ph) EXPORT_SO;
     *
     * @param jpgfile		jpg文件路径
     * @param jpgBufOut		out输出参数，输出识别成功后jpg缓冲区，需要在调用前分配好空间，空间大小由GetOutJpgSize指定
     * @param pSizejpgBufOut	out输出参数，输出jpgBufOut指向的大小
     * @param px				out输出参数，当识别到人脸时，指定人脸区域的左上角x坐标值
     * @param py				out输出参数，当识别到人脸时，指定人脸区域的左上角y坐标值
     * @param pw				out输出参数，当识别到人脸时，指定人脸区域的宽度
     * @param ph				out输出参数，当识别到人脸时，指定人脸区域的高度
     *
     * @return 成功识别到人脸返回0，否则返回错误代码，错误代码定义如下：
     *		5		其他未知状态
     *		4		人脸尺寸不合格
     *		3		黑白照片（不合规）
     *		2		检测到多张人脸
     *		1		检测不到人脸
     *		0		成功（检测正常人脸）
     *		-1		指定输入的jpg文件不存在或无法打开
     *		-2		无法确定输入的jpg文件大小
     *		-3		输入的jpg文件大小为0
     *		-4		分配jpg空间错误
     *		-5		读取输入的jpg文件错误
     *		-6		无法初始化jpg编解码
     *		-7		无法读取输入的jpg文件头信息或非jpg文件
     *		-8		输入的jpg文件格式无法支持
     */
    int FaceDetect(String jpgfile, byte[] jpgBufOut, NativeLongByReference pSizejpgBufOut,
                   IntByReference px, IntByReference py, IntByReference pw, IntByReference ph);

    /** 功能与 FaceDetect类同，前两参数即原FaceDetect第一参数文件名的二进制流及流大小
     * C/C++原型： int STDCALL FaceStreamDetect(unsigned char *jpegBuf, unsigned long jpegSize, unsigned char *jpgBufOut, unsigned long *pSizeJpgBufOut, int *px, int *py, int *pw, int *ph) EXPORT_SO;
     * @param jpgStream
     * @param sizeStream
     * @param jpgBufOut
     * @param pSizejpgBufOut
     * @param px
     * @param py
     * @param pw
     * @param ph
     * @return
     */
    int FaceStreamDetect(byte[] jpgStream, NativeLong sizeStream, byte[] jpgBufOut, NativeLongByReference pSizejpgBufOut,
                         IntByReference px, IntByReference py, IntByReference pw, IntByReference ph);
    /**
     * 获取识别到人脸照片，输出的jpg文件字节数（内部用）
     * C/C++原型：unsigned long STDCALL GetOutJpgSize() EXPORT_SO;
     * @return
     */
    NativeLong GetOutJpgSize();

//	static IFaceDetect load(){
//		IFaceDetect obj = null;
//		try{
//			obj = Native.load("facedetect", IFaceDetect.class);
//		}catch (Exception ex){
//			obj = null;
//			System.out.println("**************");
//		}
//		return obj;
//	}
}
