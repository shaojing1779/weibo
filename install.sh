#/bin/bash
#date 2015-03-18

# Host Name
sudo sed -i '1chadoop' /etc/hostname
sudo sed -i '$a\127.0.0.1	hadoop' /etc/hosts

# Decompressed.
sudo rm -rf /usr/local/hadoop*
sudo rm -rf /usr/local/jdk*
sudo rm /usr/local/java*
sudo tar -zxvf jdk-* -C /usr/local
sudo tar -zxvf hadoop-* -C /usr/local
cd /usr/local
sudo mv hadoop* hadoop
sudo ln -s jdk* java
THIS_USER=`who |head -n1|cut -d " " -f1`
THIS_UID=`id $THIS_USER|cut -d "=" -f2|cut -d "(" -f1`
THIS_GID=`id $THIS_USER|cut -d " " -f1,2|cut -d "=" -f3|cut -d "(" -f1`
sudo chown -R $THIS_UID:$THIS_GID hadoop
sudo chown -R root:root jdk*
cd ~

# Shell Enviroment.

sudo sed -i '/export JAVA_HOME/d' /etc/profile
sudo sed -i '/export JRE_HOME/d' /etc/profile
sudo sed -i '/export CLASSPATH/d' /etc/profile
sudo sed -i '/export PATH=$PATH:$JAVA_HOME/d' /etc/profile

sudo sed -i '/export PATH/a export JAVA_HOME=/usr/local/java/' /etc/profile
sudo sed -i '/export JAVA_HOME/a export JRE_HOME=/usr/local/java/jre' /etc/profile
sudo sed -i '/export JRE_HOME/a export CLASSPATH=$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar:$JAVA_HOME/jre/lib:.:$JAVA_HOME/lib' /etc/profile
sudo sed -i '/export CLASSPATH/a export PATH=$PATH:$JAVA_HOME/bin:$JAVA_HOME/jre/bin:/usr/local/eclipse:/usr/local/hadoop/bin' /etc/profile

# SSH server non password login.
sudo chmod 755 ~/.ssh
sudo rm ~/.ssh/*
ssh-keygen -t rsa  -f ~/.ssh/id_rsa -P ""
#sudo sed -i '/#AuthorizedKeysFile/a AuthorizedKeysFile	%h/.ssh/authorized_keys' /etc/ssh/sshd_config
cat ~/.ssh/id_rsa.pub > ~/.ssh/authorized_keys
sudo chmod 700 ~/.ssh
sudo chmod 600 ~/.ssh/*

# Hadoop configure files
# /usr/local/hadoop/conf/hadoop-env.sh
# /usr/local/hadoop/conf/core-site.xml
# /usr/local/hadoop/conf/hdfs-site.xml
# /usr/local/hadoop/conf/mapred-site.xml
# /usr/local/hadoop/conf/masters
# /usr/local/hadoop/conf/slaves

sed -i '/export JAVA_HOME/a export JAVA_HOME=/usr/local/java/' /usr/local/hadoop/conf/hadoop-env.sh
# sed -i '$a\text' file.txt

sed -i '/configuration>/d' /usr/local/hadoop/conf/core-site.xml
sed -i '$a\<configuration>' /usr/local/hadoop/conf/core-site.xml
sed -i '$a\<property>' /usr/local/hadoop/conf/core-site.xml
sed -i '$a\<name>fs.default.name</name>' /usr/local/hadoop/conf/core-site.xml
sed -i '$a\<value>hdfs://hadoop:9000</value>' /usr/local/hadoop/conf/core-site.xml
sed -i '$a\</property>' /usr/local/hadoop/conf/core-site.xml
sed -i '$a\<property>' /usr/local/hadoop/conf/core-site.xml
sed -i '$a\<name>hadoop.tmp.dir</name>' /usr/local/hadoop/conf/core-site.xml
sed -i '$a\<value>/usr/local/hadoop/tmp</value>' /usr/local/hadoop/conf/core-site.xml
sed -i '$a\</property>' /usr/local/hadoop/conf/core-site.xml
sed -i '$a\</configuration>' /usr/local/hadoop/conf/core-site.xml

#<configuration>
#<property>
#<name>fs.default.name</name>
#<value>hdfs://hadoop:9000</value>
#</property>
#<property>
#<name>hadoop.tmp.dir</name>
#<value>/usr/local/hadoop/tmp</value>
#</property>
#</configuration>

sed -i '/configuration>/d' /usr/local/hadoop/conf/hdfs-site.xml
sed -i '$a\<configuration>' /usr/local/hadoop/conf/hdfs-site.xml
sed -i '$a\<property>' /usr/local/hadoop/conf/hdfs-site.xml
sed -i '$a\<name>dfs.data.dir</name>' /usr/local/hadoop/conf/hdfs-site.xml
sed -i '$a\<value>/usr/local/hadoop/data</value>' /usr/local/hadoop/conf/hdfs-site.xml
sed -i '$a\</property>' /usr/local/hadoop/conf/hdfs-site.xml
sed -i '$a\<property>' /usr/local/hadoop/conf/hdfs-site.xml
sed -i '$a\<name>dfs.replication</name>' /usr/local/hadoop/conf/hdfs-site.xml
sed -i '$a\<value>1</value>' /usr/local/hadoop/conf/hdfs-site.xml
sed -i '$a\</property>' /usr/local/hadoop/conf/hdfs-site.xml
sed -i '$a\</configuration>' /usr/local/hadoop/conf/hdfs-site.xml

#<configuration>
#<property>
#<name>dfs.data.dir</name>
#<value>/usr/local/hadoop/data</value>
#</property>
#<property>
#<name>dfs.replication</name>
#<value>1</value>
#</property>
#</configuration>

sed -i '/configuration>/d' /usr/local/hadoop/conf/mapred-site.xml
sed -i '$a\<configuration>' /usr/local/hadoop/conf/mapred-site.xml
sed -i '$a\<property>' /usr/local/hadoop/conf/mapred-site.xml
sed -i '$a\<name>mapred.job.tracker</name>' /usr/local/hadoop/conf/mapred-site.xml
sed -i '$a\<value>hadoop:9001</value>' /usr/local/hadoop/conf/mapred-site.xml
sed -i '$a\</property>' /usr/local/hadoop/conf/mapred-site.xml
sed -i '$a\</configuration>' /usr/local/hadoop/conf/mapred-site.xml

#<configuration>
#<property>
#<name>mapred.job.tracker</name>
#<value>hadoop:9001</value>
#</property>
#</configuration>

# Format & Reboot computer

hadoop namenode -format
sudo reboot
