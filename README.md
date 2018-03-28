# storage
[![](https://jitpack.io/v/krealseu/storage.svg)](https://jitpack.io/#krealseu/storage)  
自己的Android统一路径   /UUID/path
  
##  相关类
```Kotlin
data class Volume(val path: String, val isRemovable: Boolean, val state: String, val uuid: String, val isGrant: Boolean)
```
  
##  方法说明
  code | 说明
  --- | ---
constructor(context: Context) | 构造函数，需要context支持
reload() | 重新加载设备的存储信息
getVolumes():List\<Volume\> | 获得存储设备列表
getAvailableVolumes():List\<Volume\> | 获得已经mounted的存储设备列表
getDocumentFile(path: String, canWrite: Boolean):DocumentFile? | 根据path获取DocumentFile，canWrite指定是否需要写权限default false
