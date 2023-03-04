# 旅法师营地第三方应用体验demo

**预览图片**

[![pStssJS.jpg](https://s1.ax1x.com/2023/01/25/pStssJS.jpg)](https://imgse.com/i/pStssJS)
[![pStsgMj.jpg](https://s1.ax1x.com/2023/01/25/pStsgMj.jpg)](https://imgse.com/i/pStsgMj)
[![pStsRLn.jpg](https://s1.ax1x.com/2023/01/25/pStsRLn.jpg)](https://imgse.com/i/pStsRLn)
[![pSts4oV.jpg](https://s1.ax1x.com/2023/01/25/pSts4oV.jpg)](https://imgse.com/i/pSts4oV)

[![20230125-140635.gif](https://i.postimg.cc/q7CMbPwY/20230125-140635.gif)](https://postimg.cc/xkn2qFWy) <--图床有大小限制，点进去能看更大的

## [APP下载体验](https://github.com/graveyard233/YDJavaDemo/releases/tag/v1.0.0)
仅申请网络权限，UI丑我认了，尽量在WiFi环境下使用，不缺流量的话无所谓

以下是罗里吧嗦的开发感言

- [旅法师营地第三方应用体验demo](#旅法师营地第三方应用体验demo)
  - [APP下载体验](#app下载体验)
  - [**背景**](#背景)
  - [**目标**](#目标)
  - [**需求调研**](#需求调研)
  - [**分析概要**](#分析概要)
  - [**概要设计**](#概要设计)
    - [**可行性分析**](#可行性分析)
    - [**技术实现**](#技术实现)
    - [**解析思路**](#解析思路)
  - [**主要功能**](#主要功能)
  - [**致谢**](#致谢)
## **背景**
旅法师营地应用使用带有大量广告，有相当多不需要的功能，或者设计不合理之处，所以需要一个新的应用，如同NGA开源版，低配版也行。
## **目标**
需要一个能够流畅运行，能够让用户纯粹看营地炉石帖子的安卓端APP。

这里的纯粹的意思是用户不能登录，发表回复，评论等。只能看贴，不能点赞或点踩。

纯看新闻流和社区帖子，不做其他任何事情。开发者自己也是，沉默地使用营地应用。
## **需求调研**
- 大部分都是广告恶心人，广告分类：开屏广告和套牌广场的广告。
- 社区的帖子筛选放在左上角，恶心右手使用用户。
- UI不符合Android的Material风格。
- 应用臃肿，有着大量的sdk和获取用户个人信息，搞个商城都不用还弄进来。
- 需要单手操作，最好能让用户一只手就能搞定主要浏览功能，最好部分控件放在底部。
## **分析概要**
- 痛点大部分在广告上，用户讨厌广告，但是你想用营地的服务，如卡组构筑，心得交流等功能就得登录才能用。而营地必须要广告才能生存下去。
- 发现现在大部分用户都不进行评论，纯粹是用营地客户端来进行浏览官方推流帖子，看日报抄卡组，看热评笑话和立省百分百。
- 部分用户依赖日报卡组，以及单卡查询。或者去套牌广场搜卡组。
## **概要设计**
### **可行性分析**

营地的web和webView都采用了Vue的框架，大大增加了解析难度，评论数据都是异步加载。最衰的是，评论数据，用户数据和营地的搜索，都是POST请求而且需要**签名字段**。不知道怎么生成签名字段，所以做不了这些。评论做不了，点赞和点踩也做不了，当然这些也是需要登录才能，而且需要很多东西。

[https://iyingdi.com/tz/tag/17](https://iyingdi.com/tz/tag/17)

通过get请求去拿营地的web界面的html，然后解析获取到的数据转成json或直接转成实体类。这里可以拿到官方置顶的新闻流和常规帖子，如日报之类的。数据加载到list上面形成主界面推流list或社区的信息流。

[https://iyingdi.com/tz/tag/17?page=post&order=hot](https://iyingdi.com/tz/tag/17?page=post&order=hot) -|- 
[https://iyingdi.com/tz/tag/17?page=post&order=created](https://iyingdi.com/tz/tag/17?page=post&order=created)

社区帖子采用get请求，拿到html代码后进行解析。

点击帖子，因为a标签里面带有目标URL，所以，直接获取那里的html代码，解析发帖用户等数据。轮询注入js获取渲染好的源码，获取HTML代码后解析，生成评论数据。

日报或攻略帖子中的卡组，会带个data-id，那里有卡组的id，去套牌广场搜就好了
### **技术实现**

单卡查询和套牌广场，都是post接口，但是不需要签名就可使用，所以只需要填写好表单就行。返回的Msg数据转成实体类即可。拿到数据后直接展示就行。（然而这个查询在这里没有做，但已经预研了，具体可以看另一个测试APP的[RetrofitFragment](https://github.com/graveyard233/AndroidToolsLearn/blob/master/app/src/main/java/com/lyd/tooltest/UI/Fragment/RetrofitFragment.java)，因为想用kotlin实现）

主要语言使用Java，采用MVVM架构，尽量不使用本地存储。在必要部分使用Jetpack组件，给未来升级维护做铺垫。毕竟这只是一个体验demo，Kotlin版本的应用会维护到营地倒闭。

核心思路：界面导航 -> Scene；HTML解析 -> Jsoup；网络请求 -> 由于没做接口请求，所以暂时采用okhttp原始方式，其实也可以retrofit直接转成string也行，当时还不知道怎么做；不需要申请任何存储读写权限也能往相册写入图片 -> ContentResolver(29<=sdk<=32,尽可能保护用户隐私，到安卓13要申请相册读写权限)

### **解析思路**
官方推流列表没什么好说的，采用解析移动端网页的方式获取推流列表（不解析web端是因为会有置顶帖子影响），获取的时候会发现div嵌套的非常工整，数据非常好获取，但没有做在官方流中的社区帖子解析。banner的置顶推流采用解析web端html里面某一个script标签内部的内容（曾经是第五个，目前变动到第六个，所以需要动态获取index，判断它的长度即可），这里面大部分都类似于json数据。里面有个top_content的字段，这字段括住的全部都是官方置顶的帖子，且没有广告内容，对其进行裁剪即可。

重头戏是社区帖子的解析，一共分为4个类型：1.常规帖子 带title和预览文本，还有可能带有图片，预览时最多三张，超过会显示还有多少张，所有的图片URL在script中；2.卡组帖子 和常规帖子属性基本一致，还多出一个卡组信息，在web端上不会附带这个帖子的图片URL，需要去script去拿数据，同时卡组数据也在那里；3.文章类帖子 写成了文章，带文章的题图，title和预览文本；4.投票帖 带基础title，预览文本和一个投票比例； 依靠每个div内部的区别不同来区分不同类型，比如带deck-item的div的就是卡组帖子，第一个子标签是a标签的是文章帖子等。其中获取卡组信息是需要到script中解析卡组json，注意，在移除反斜杠的时候不能移除那些三个连着的反斜杠，这些是用于保护内部字段的。

以上解析细节都在[MessageRepository](https://github.com/graveyard233/YDJavaDemo/blob/master/app/src/main/java/com/lyd/yingdijava/Repository/MessageRepository.java)中，注释都已经标注好。

评论数据的解析是个比较头疼的难点，评论的接口是post接口，需要签名，只能另寻他路：使用webView，让webView把数据渲染好，同时还可以注入js移除webView上所有营地广告，并且在webView代理中的shouldOverrideUrlLoading方法中拦截所有营地scheme跳转。然后调用注入的js获取webView的所有html代码来进行解析。评论数据是异步加载的，调用js的时机是在监听到有一个开头是谷歌的post请求的时候（因为用的是Chrome作为浏览器，这个请求大概是分析网页的）可以进行轮询调用。所有细节都在[NewsWebFragment](https://github.com/graveyard233/YDJavaDemo/blob/master/app/src/main/java/com/lyd/yingdijava/UI/Fragment/NewsWebFragment.java)和[CommentsNodeBuilder](https://github.com/graveyard233/YDJavaDemo/blob/master/app/src/main/java/com/lyd/yingdijava/Entity/Comment/CommentsNodeBuilder.java)中，注释都已经标注好。


## **主要功能**

- [x]  官方信息流帖子浏览，只能显示当前web界面的数据，大约17个item（即拖动到最底下能加载更多的数据，这个做不出来）
- [x]  社区帖子浏览，按热度和时间分类。
- [ ]  炉石的单卡检索查询
- [ ]  炉石的套牌广场检索查询卡组

Java版本算是个试水预研吧，待学完协程等kotlin特性后，没有实现的功能交给**kotlin**版本，，没有接口做起来有点吃力，优缺点写在了下载地址那了。

## **致谢**
开发Java版demo的时候，开发者不过是进入Android开发行业半年都不到的菜鸟，互联网开发经验更是没有，真的是要啥啥不会，都是现学现用。这个营地JavaDemo可以说是开发者第一个正儿八经的"互联网"应用，和那些用上各种技术的应用真比不了，代码写的抽象也认了，功能真做不出来只能说当时水平不够，能整出这个Demo要感谢各种开源应用的启发。

首先感谢**yh**，作为web端+后端大佬给予各种相当实在的建议；

感谢营地的lol群里的各个伙伴，愿意倾听开发者肆意吹水；感谢营地web开发者，web端按照开发规范来走，给数据解析做出了巨大便利；
感谢wanandroid的各种博客的帮助，感谢掘金的各种分享博客的技术支持；

感谢帮助开发的各种库：

导航使用字节的scene，简单易用，容易上手，感谢scene的navigation帮我保存fragment，jetpack的navigation直接销毁真的太难用了；

感谢html解析的Jsoup；感谢网络请求的squareup的okhttp3，没做接口请求真是遗憾；

感谢BaseRecyclerViewAdapterHelper，比较简单的实现了点击事件和多类型列表实现；

感谢youth5201314的banner；
感谢glide加载图片；

感谢drakeet的drawer全屏拖拽侧边栏支持，虽然在Android12有点bug；
感谢chrisbanes的PhotoView做画廊图片查看

感谢NGA开源版，图片下载的思路是抄他的；

感谢EhViewer-Overhauled，未来的kotlin版本也要多多指教了
