# 关于斗图Demo
 本着学习的心态现已把app 源码开源放到Github。时间匆忙可能遗留存在一些bug。
图片内容均来源于互联网搜索。APP不作任何商业用途，仅供学习使用 欢迎大家交流学习！

 * 新增悬浮球功能，可以运行在QQ后台发图了！
 * 新增图片收藏，保存至本地sqlite
 * 新增GIF动图添加文字功能（暂不支持背景透明的GIF，会黑边- -!）
   
 
 
 >项目是16年花2个多月 业余时间做出来的。构架上还没用现在主流的：MVP+Rxjava2+Retrofit2+Dagger2 . [(有兴趣可看看这个我写的例子)](https://github.com/yezihengok/MVP-Rxjava2-Retrofit2-Dagger2)

  
### V1.4.8 2019年3月更新：

   > 快2年更改过这个项目了，最近闲来无事，打算找几个表情包，发现图片接口几乎全挂...查询了原因；用的接口https证书错误导致glide无法加载图片，
      遗憾的是 拥有表情包资源最丰富的模块 “主题表情包” 接口彻底挂了,服务器也打不开. 好在其他模块经过小小整顿一切都正常显示了。
    
* **本次更新 ：1、让Glide 加载HTTPS图片过滤证书问题 2、app兼容至Android 9.0**

* 兼容7.0以上 使用 Uri.fromFile报错问题 
* 兼容8.0以上 使用悬浮窗类型变更 、启动service 方式变更(Android 8.0 不允许其创建后台服务的情况下使用 startService() 函数，需要startForegroundService)
* 兼容9.0以上 http报错问题（在Android P系统的设备上，使用的是非加密的明文流量的http网络请求，则会导致该应用无法进行网络请求，application标签增加以下属性：android:networkSecurityConfig）     
 
 
      
##   **通过本DEMO可以学习到以下内容：**

* Drawerlayout和NavigationView实现侧边栏，以及相关用法
* 新控件CoordinatorLayout、FloatingActionButton的用法，以及仿QQ控件标题动画效果；
* Gilde操作使用：加载圆形图、利用Gilde下载图片。
* popWindow+属性动画实现的通知效果
* QQ 微信SDK 分享使用、直接调用微信QQ分享界面
* SwipeRefreshLayout和RecyclerView结合实现下拉刷新，TabLayout CardView 等等 Material Design风格使用例子...
* 其它DEMO中运用到的框架
   感谢：xrecyclerview、glide、okhttputils opengit等开源作者

   Tips：关于微信分享SDK机制，是我微信开发者平台注册我自己的机器打包的签名才能分享成功的，你自己运行项目的签名和我机器打包的签名肯定是不一样的。

  至于要看效果 app我已上架至应用市场 (恒妹子斗图)
* 应用宝下载地址：http://sj.qq.com/myapp/detail.htm?apkName=com.yzi.doutu
* 百度应用市场下载地址：http://shouji.baidu.com/s?wd=%E6%81%92%E5%A6%B9%E5%AD%90%E6%96%97%E5%9B%BE&data_type=app&f=header_software%40input%40btn_search&from=as

####效果动图：
![Doutu-master](https://github.com/yezihengok/Doutu-master/blob/master/screenshots/test.gif)
####部分截图：
![Doutu-master](https://github.com/yezihengok/Doutu-master/blob/master/screenshots/device-1.png)
![Doutu-master](https://github.com/yezihengok/Doutu-master/blob/master/screenshots/device-2.png)
![Doutu-master](https://github.com/yezihengok/Doutu-master/blob/master/screenshots/device-3.png)
![Doutu-master](https://github.com/yezihengok/Doutu-master/blob/master/screenshots/device-4.png)
