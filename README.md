# Custom-Media-Controller
Android 自定义 Media Controller（非PopupWindow，非Dialog），使用原生MediaController逻辑，反射PhoneWindow实现。
## 用法
将PhoneWindowMediaController.java 和 media_controller.xml 复制到自己的项目，在用到MediaController的地方new出来即可。
```java
PhoneWindowMediaController mMediaController = new PhoneWindowMediaController(getActivity());
mMediaController.setAnchorView(需要附着的根布局);
mMediaController.setMediaPlayer(MediaController.MediaPlayerControl接口);
```
## 原理
参考系统原生的MediaController，复制其所有代码，将所有报错的
```java
com.android.internal.R.drawable.*
com.android.internal.R.string.*
com.android.internal.R.id.*
com.android.internal.R.layout.*
```
资源替换成自己想要的图片/字符串/布局资源
这样剩下报错的地方就只有
```java
mAccessibilityManager = AccessibilityManager.getInstance(context);
```
和
```java
mWindow = new PhoneWindow(mContext);
```
两处代码。
### 解决第一处
   AccessibilityManager对象可以通过
```java
(AccessibilityManager) context.getSystemService(Context.ACCESSIBILITY_SERVICE);
```
   获取到
### 解决第二处
这里稍微麻烦些，因为PhoneWindow是hide的
```java
/**
 * Android-specific Window.
 * <p>
 * todo: need to pull the generic functionality out into a base class
 * in android.widget.
 *
 * @hide
 */
public class PhoneWindow extends Window implements MenuBuilder.Callback {
    ·····
}
```
    需要用到反射，但也是很简单的。如下：
    
```java
private void createPhoneWindow(Context mContext) {
    Class<?> phoneWindowClazz = null;
    try {
    //这里注意，在sdk中看到的PhoneWindow是在com.android.internal.policy包下面，但实际不是，而是在
    //com.android.internal.policy.impl包下面，需要看源码。
    
        //1.拿到PhoneWindow类的对象
        phoneWindowClazz = Class.forName("com.android.internal.policy.impl.PhoneWindow");
        //2.拿到PhoneWindow的构造器的对象
        final Constructor<?> constructor = phoneWindowClazz.getDeclaredConstructor(Context.class);
        //3.用构造器创建出Window
        mWindow = (Window) constructor.newInstance(mContext);
    } catch (ClassNotFoundException e) {
        e.printStackTrace();
    } catch (NoSuchMethodException e) {
        e.printStackTrace();
    } catch (IllegalAccessException e) {
        e.printStackTrace();
    } catch (InstantiationException e) {
        e.printStackTrace();
    } catch (InvocationTargetException e) {
        e.printStackTrace();
    }
}
```
    这样顺利的拿到了Window对象，当然获取window的方式有很多种。
##注意
    关注最新版本的Android源代码，看PhoneWindow的构造器或包名是否发生变化。不过一般不会变。
    