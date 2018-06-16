#工程配置<br>
==================
一、准备工作<br>
--------------------
1.	安装Android Studio；<br>
本说明使用的Andrid Studio 2.3.3，操作系统为Windows 7 64位旗舰版。<br>
![image](https://github.com/TeacherLuo/XJGARSDKDemoApp-Android/raw/master/ImageCache/1.png)<br>
![image](https://github.com/TeacherLuo/XJGARSDKDemoApp-Android/raw/master/ImageCache/2.png)<br>
二、生成工程文件<br>
---------------------
1.	将“XJGARSDKDemoApp-Android-master.zip”解压，解压后的文件夹中有名为“XJGArSdkDemoApp”的文件夹。（注意：解压的路径全文中不得有中文）。<br>
2.	使用Android Studio打开“XJGArSdkDemoApp”的文件夹所对应的项目。<br>
3.	若用户除了“XJGARSDKDemoApp-Android-master.zip”文件之外，还获得了更新的动态库文件（文件类型为aar）或者更新的License字段，那么执行以下3.1至3.3步骤。<br>
* 将最新的动态库文件（文件类型为aar）放入文件夹“XJGArSdkDemoApp”文件夹路径\XJGArSdkDemoApp\app\libs中<br>
* 将项目内的HomeActivity.java文件中的licenseText的内容换成最新的License字段<br>
![image](https://github.com/TeacherLuo/XJGARSDKDemoApp-Android/raw/master/ImageCache/3.png)<br>
* 将“XJGArSdkDemoApp”文件夹路径\XJGArSdkDemoApp\app\build.gradle中字段“compile(name:'xjgarsdklibrary-release-9.1.0-2018-02-07', ext:'aar')”的对应文件名更新为最新的aar文件的文件名<br>
![image](https://github.com/TeacherLuo/XJGARSDKDemoApp-Android/raw/master/ImageCache/4.png)<br>

三、运行工程<br>
----------------------
运行项目的方式有两种，一是在安卓手机上测试，二是在计算机的安卓模拟器上测试。因为本APP需要调用手机摄像头，而安卓模拟器无法调用摄像头，因此不推荐第二种测试方式（安卓模拟器）。<br>
1	将安卓手机（本文所用手机为华为荣耀V10手机）的开发模式打开。具体步骤如下：<br>
1.1	在手机标准界面找到“设置”<br>
1.2	打开设置，下拉选项，找到“关于手机”<br>
1.3	在关于手机选项里，找到“版本号”，连点7次<br>
![image](https://github.com/TeacherLuo/XJGARSDKDemoApp-Android/raw/master/ImageCache/5.png)<br>
1.4	之后会提示您，您已处于开发者模式，我们会在设置选项里，看到开发者模式这一项。<br>
1.5	在“设置”-》“开发人员选项”中，打开“USB调试”开关。个别手机还需要开启““仅充电”模式下允许ADB调试”开关<br>
![image](https://github.com/TeacherLuo/XJGARSDKDemoApp-Android/raw/master/ImageCache/6.png)<br>
1.6	将安卓手机通过USB线连接到电脑。<br>
1.7	在安卓手机的提示界面选择“仅充电”选项<br>
1.8	使用Android Studio打开项目，点击运行按钮<br>
![image](https://github.com/TeacherLuo/XJGARSDKDemoApp-Android/raw/master/ImageCache/7.png)<br>
1.9	选择安卓手机作为调试平台<br>
![image](https://github.com/TeacherLuo/XJGARSDKDemoApp-Android/raw/master/ImageCache/8.png)<br>


#免费License申请、贴纸制作工具地址
====================
http://www.xiaojigou.cn 首页-》开发包-》说明文档-》XJGARSDK说明文档



#API接口：<br>
====================
注意：SDK中各个函数需要在单一的线程中调用。<br>
1.	初始化<br>
>####初始化方法：<br>
	private static native boolean XJGARSDKInitialization(String licenseText, String userName, String companyName);<br>
* context参数为获取SD卡里存放SDK模型（model）的目<br>
* licenseText参数为license key的字符串值<br>
* userName参数为该licence key对应的用户名<br>
* companyName参数为该license key对应的公司名<br>
注：license相关的参数均需要提前申请<br>

>####销毁方法：
public static native boolean XJGARSDKCleanUP();<br>
2.	使用人脸整形<br>
>####大眼：<br>
public static native boolean XJGARSDKSetBigEyeParam (int eyeParam);<br>
* eyeParam参数为0-100，数值越大眼睛越大<br>

>####瘦脸：<br>
public static native boolean XJGARSDKSetThinChinParam (int chinParam); <br>
* chinParam参数为0-100，数值越大脸部下吧越瘦<br>

>####红润：<br>
public static native boolean XJGARSDKSetRedFaceParam(int redFaceParam)； <br>
* redFaceParam参数为0-100，数值越大脸部皮肤越红润<br>

>####美白：<br>
public static native boolean XJGARSDKSetWhiteSkinParam(int whiteSkinParam); <br>
* whiteSkinParam参数为0-100，数值越大脸部皮肤越白<br>

>####磨皮：<br>
public static native boolean XJGARSDKSetSkinSmoothParam(int skinSmoothParam); <br>
* skinSmoothParam参数为0-100， 数值越大越皮肤越光滑<br>

3.	使用人脸滤镜<br>
SDK启动时默认不使用滤镜<br>

>####切换滤镜：<br>
public static native boolean XJGARSDKChangeFilter(String filterTypeName);<br>
* filterTypeName参数为滤镜名字，目前可选的滤镜有6种，分别是冰冷,健康,祖母绿,怀旧, 蜡笔, 常青，填入“无”不使用滤镜;<br>
* 在某些中文输入有问题的状况下可以使用英文参数输入，6种滤镜分别为："filter_cool", "filter_Healthy","filter_emerald","filter_nostalgia","filter_crayon", "filter_evergreen"。填入"filter_none",不使用滤镜。<br>


4.	使用人脸道具<br>
>####显示贴纸：<br>
public static native boolean XJGARSDKSetShowStickerPapers(boolean bShowStickPaper);<br>
* bShowStickPaper参数 为true时，显示贴纸<br>

>####是否显示LandMark<br>
public static native boolean XJGARSDKSetShowLandMarks(boolean bShowLandMarks); <br>

>####切换贴纸：<br>
public static native boolean XJGARSDKChangeStickpaper(String stickPaperName);<br>
* stickPaperName参数为贴纸名称，目前可选的贴纸见StickerPapers子文件夹，每个文件夹的名称均是贴纸名称<br>

5.	图片视频流处理
>####初始化OpenGL环境：如果用户没有opengl环境，则创建一个虚拟的opengl环境<br>
public static native boolean XJGARSDKInitOpenglEnvironment(int width,	int height);<br>
* 	width参数为输入图片宽度<br>
* 	height参数为输入图片高度<br>

>####销毁OpenGL环境<br>
public static native boolean XJGARSDKDestroyOpenglEnvironment();<br>

>####释放OpenGL占用的所有资源<br>
public static native boolean XJGARSDKReleaseAllOpenglResources();<br>

>####设置贴纸根路径<br>
public static native boolean XJGARSDKSetRootDirectory( String rootDirectory);<br>
* 参数rootDirctory为路径名称<br>

>####设置模式<br>
public static native boolean XJGARSDKSetOptimizationMode(int mode);<br>
* mode参数为0代表视频，1代表图片<br>

>####对图片进行美颜处理<br>
	public static native void XJGARSDKRenderImage(byte[] rgbInputImageData , int width, int height);<br>
* 参数rgbInputImageData为3通道的RGB图像<br>
* 参数width为图像宽度<br>
* 参数height为图像高度<br>
* 返回：即经过美颜，滤镜，道具处理后的图像<br>

>####对Bitmap图像进行美颜<br>
public static Bitmap XJGARSDKRenderImage(Bitmap bitmap,boolean bFlipYAxis)<br>
* 参数bitmap为输入图像<br>
* 返回：美颜后的bitmap图片<br>

>####对Bitmap图像进行美颜并设定是否水平翻转<br>
public static Bitmap XJGARSDKRenderImage(Bitmap bitmap,boolean bFlipYAxis)<br>
* 参数bitmap为输入图像<br>
* 参数bFlipYAxis为是否进行水平翻转<br>
* 返回：美颜后的bitmap图片，当bFlipYAxis为true时为水平翻转后的美颜图片<br>

>####在Opengl屏幕缓存中存储最后渲染的结果<br>
public static native void XJGARSDKRenderImage(byte[] rgbInputImageData , int width, int height);<br>
* 参数rgbInputImageData为3通道的RGB图像<br>
* 参数width为图像宽度<br>
* 参数height为图像高度<br>

>####获取opengl texture的地址<br>
public static native void XJGARSDKRenderImageToGLTex(byte[] rgbInputImageData, int width, int height, int[]  outputTexId);<br>
* 参数rgbInputImageData为3通道的RGB图像<br>
*	参数width为图像宽度<br>
*	参数height为图像高度<br>
*	返回：opengl texture的地址<br>

>####获取opengl的texture<br>
public static native int  XJGARSDKRenderGLTexToGLTex( int inputTexId,  int width, int height );<br>
* 参数rgbInputImageData为3通道的RGB图像<br>
* 参数width为图像宽度<br>
* 参数height为图像高度<br>
* 返回：opengl的texture<br>

>####将Opengl texture放到opengl屏幕缓存中<br>
public static native void  XJGARSDKRenderGLTexture( int inputTexId,  int width,  int height );<br>
•	参数inputTexId为opengl的texuture地址<br>
•	参数width为texture的宽度<br>
•	参数height为texture的高度<br>
6.	其他<br>
>####显示各种算法的执行效率（以LOG的方式在android studio 控制台中打印显示）<br>
public static native boolean XJGARSDKSetShowPerformanceStatic(boolean bPerformanceStatic);<br>

>####通过android应用初始化SDK<br>
public static boolean XJGARSDKInitialization(Context context,String licenseText,String userName,String companyName);<br>
* context参数为获取SD卡里存放SDK模型（model）的目录<br>
* licenseText参数为license key的字符串值<br>
* userName参数为该licence key对应的用户名<br>
* ompanyName参数为该license key对应的公司名<br>
注：license相关的参数均需要提前申请<br>

>####复制贴纸到应用目录<br>
public static boolean XJGARSDKCopyStickerPaperes( Context context);<br>
* context参数为android应用程序上下文，函数会获取APP的私有存储空间目录并将模型贴纸等复制到目的<br>

>####获取应用内部路径<br>
public static String getPrivateResDataDir(Context context);<br>
* context参数为android应用程序上下文，函数会获取APP的私有存储空间目录<br>

>####从assets目录中复制整个文件夹内容<br>
public static void copyFilesFromAssets(Context context, String oldPath, String newPath);<br>
* 参数context为使用android应用程序上下文对象<br>
* 参数oldPath为源文件路径，如/original/path<br>
* 参数newPath为复制后的路径，如/target/path<br>

>####水平翻转bitmap图片<br>
public static Bitmap FlipBitmapYAxis(Bitmap a, int width, int height);<br>
* 参数a为原始bitmap图片<br>
* 参数width为图片宽度<br>
* 参数height为图片高度<br>

>####获取Bitmap图片的grb值<br>
public static byte[] rgbValuesFromBitmap(Bitmap bitmap, boolean bFlipYAxis);<br>
* 参数bitmap为输入图片<br>
* 参数bFlipYAxis为是否对图像进行水平翻转<br>
* 返回：（翻转后的）Bitmap图片的rgb值<br>

>####通过本地资源文件ID加载本地资源图片<br>
public static Bitmap getResourceBitmap(Context context, int ResID);<br>
* 参数context为android应用程序上下文对象<br>
* 参数ResID为本地资源图片的ID<br>
* 返回：加载到的Bitmap图片<br>

>####通过本地资源文件名称加载本地资源图片<br>
public static Bitmap getResourceBitmap(Context context, String ResName);<br>
* 参数context为android应用程序上下文对象<br>
* 参数ResName为本地资源文件的名称<br>
* 返回：加载到的Bitmap图片<br>

>####通过文件URL加载本地资源图片<br>
public static Bitmap getLoacalBitmap(String url);<br>
* 参数url为本地资源文件的url<br>
* 返回：加载到的Bitmap图片<br>

>####通过http url加载服务器端资源图片<br>
public static Bitmap getHttpBitmap(String url);<br>
* 参数url服务器端的资源图片的http url<br>
* 返回：加载到的Bitmap图片<br>


