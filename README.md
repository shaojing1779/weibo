weibo------------------计算关注数量或者被关注数量（代码需要微小改动）<br/>
Follow-----------------计算相互关注数，输入为原始数据-输出为未统计的用户--相互关注数<br/>
FollowE----------------统计相互关注数输入为Follow的输出，输出为统计后的用户--相互关注数<br/>

######Hadoop_install
支持版本：Hadoop-1.2.1 Java1.6以上 Debian7.8 Fedora20

使用方式：

1、下载此代码(install.sh);

2、下载sun jdk（建议使用1.7)

    http://www.oracle.com/technetwork/java/javase/downloads/jdk7-downloads-1880260.html

3、下载Hadoop1.2.1二进制包;

4、将三者放在当前用户目录下(~/);

5、给install.sh加执行权限

    chmod +x install.sh

6、执行shell程序开始安装,安装完毕计算机自动重启

    ./install.sh

7、系统重新启动后运行命令jps可以看到以下六个进程（其中Jps表示执行jps命令本身这个java进程）表示安装成功

    3270 Jps

    2920 JobTracker

    2634 NameNode

    3028 TaskTracker

    2736 DataNode

    2840 SecondaryNameNode
