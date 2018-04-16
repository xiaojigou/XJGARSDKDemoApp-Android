# XJGARSDKDemoApp-Android
一、准备工作<br>
- -------------------
1.	安装Android Studio；<br>
本说明使用的Andrid Studio 2.3.3，操作系统为Windows 7 64位旗舰版。<br>

二、生成工程文件<br>
- --------------------
1.	将“XJGARSDKDemoApp-Android-master.zip”解压，解压后的文件夹中有名为“XJGArSdkDemoApp”的文件夹。（注意：解压的路径全文中不得有中文）。<br>
2.	使用Android Studio打开“XJGArSdkDemoApp”的文件夹所对应的项目。<br>
3.	若用户除了“XJGARSDKDemoApp-Android-master.zip”文件之外，还获得了更新的动态库文件（文件类型为aar）或者更新的License字段，那么执行以下3.1至3.3步骤。<br>
* 将最新的动态库文件（文件类型为aar）放入文件夹“XJGArSdkDemoApp”文件夹路径\XJGArSdkDemoApp\app\libs中<br>
* 将项目内的HomeActivity.java文件中的licenseText的内容换成最新的License字段<br>
* 将“XJGArSdkDemoApp”文件夹路径\XJGArSdkDemoApp\app\build.gradle中字段“compile(name:'xjgarsdklibrary-release-9.1.0-2018-02-07', ext:'aar')”的对应文件名更新为最新的aar文件的文件名<br>

三、运行工程<br>
----------------------
运行项目的方式有两种，一是在安卓手机上测试，二是在计算机的安卓模拟器上测试。因为本APP需要调用手机摄像头，而安卓模拟器无法调用摄像头，因此不推荐第二种测试方式（安卓模拟器）。<br>
1	将安卓手机（本文所用手机为华为荣耀V10手机）的开发模式打开。具体步骤如下：<br>
1.1	在手机标准界面找到“设置”<br>
1.2	打开设置，下拉选项，找到“关于手机”<br>
1.3	在关于手机选项里，找到“版本号”，连点7次<br>
1.4	之后会提示您，您已处于开发者模式，我们会在设置选项里，看到开发者模式这一项。<br>
1.5	在“设置”-》“开发人员选项”中，打开“USB调试”开关。个别手机还需要开启““仅充电”模式下允许ADB调试”开关<br>
1.6	将安卓手机通过USB线连接到电脑。<br>
1.7	在安卓手机的提示界面选择“仅充电”选项<br>
1.8	使用Android Studio打开项目，点击运行按钮<br>
1.9	选择安卓手机作为调试平台<br>
