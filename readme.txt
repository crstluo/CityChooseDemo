城市选择demo
1.适配6.0,增加权限请求
2.使用百度地图定位
3.城市选择

功能实现:
    1.权限相关:
    百度地图所需运行时权限:
        <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
        <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
        <uses-permission android:name="android.permission.READ_PHONE_STATE" />
        <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
    数据库操作文件操作所需权限
        <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />

    2.定位功能:
    这里使用百度地图定位sdk,需要详细了解的可以访问百度开发者网址:
    http://lbsyun.baidu.com/index.php?title=android-locsdk

    3.城市选择功能:
    实现步骤:
    a.assets里面拷贝现有城市数据库文件到项目数据库文件夹中并从中获取所有城市数据,保存到集合中
    b.使用适配器将其显示到listView里面,这里通过ViewType区分特殊item,比如定位,热门以及最近访问
    c.使用自定义View作为右侧字母索引栏,将索引栏与listView关联.
    这里listView中的item位置与字母存放在一个alphaIndexer这个HashMap中,
    其key值即为字母或特殊字符(比如定位,热门等),其value即为该字母所在listView的position位置
    当字母索引栏字母被触摸时,则会返回当前字母,通过该字母即可获得该字母第一个item的位置,
    然后使用listView.setSelection(position)的方法设置listView的位置

    关于右侧字母索引栏,
    可以看我上传到github的一个小demo:https://github.com/crstluo/SideIndexBarDemo

 note:
 1:demo的一些UI细节尚未处理,看得不爽还请谅解
 2:关于listView中的已访问城市,由于设计只留4个item,
 为了方便,目前暂时使用SharedPreferences实现,就不将数据插入数据库了
 3:demo没有添加搜索城市的功能,有需要的可以自行添加.
 实现步骤也简单,通过监听输入框变化,获取输入框的字符,
 然后通过模糊查询城市数据库获取到城市列表,最后显示在新的listView中.
 查询语句如下:
 Cursor cursor = db.rawQuery("select * from city where name like \"%" +
 keyword+ "%\" or pinyin like \"%" + keyword + "%\"", null);
 keyword即为从输入框获取的字符

 4.右侧字母索引栏的英文字母与拼音并非一一对应,比如i,U,V这些字母不能做声母,
 所以没有以这三个字母作为开头的城市名称,
 所以alphaIndexer这个Map里面也就没有其键值对,这里需要判断一下非空,避免出现空指针.


这里感谢csdn网友奋斗之路提供的基础数据库文件及参考代码资料
尊重一下原创:http://blog.csdn.net/dmk877/article/details/49757731

ps:由于上传csdn的资源最少都要2个csdn的积分,为了方便后面的人,还是上传到GitHub吧
资源路径: