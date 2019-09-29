# TextSearch
windows记事本文本搜索器  
![1](https://github.com/dddder4/TextSearch/blob/master/examples/1.png)  
![2](https://github.com/dddder4/TextSearch/blob/master/examples/2.png)  
![3](https://github.com/dddder4/TextSearch/blob/master/examples/3.png)  
![4](https://github.com/dddder4/TextSearch/blob/master/examples/4.png)  
![5](https://github.com/dddder4/TextSearch/blob/master/examples/5.png)  
## 功能介绍
### 在单个文件可以查找特定字符串
利用LineNumbeRreader的readLine读取文件，再用String.contain查找。采用LineNumberReader是因为有getLineNumber这个函数可以直接获取行数不用多开一个变量来统计。  
过程中发现输出的时间远大于查找的时间，所以找到时先append，等查找完再一次性输出。
### 直接搜索目录，目录下有子文件夹还能递归查找
用file.listfile查找出目录下的所有文件，如果是文件直接查找内容，如果是文件夹递归查找。
### 目录和文件名支持*？通配符。
利用正则表达式匹配文件。首先利用’\’来切割字符串，存入字符串数组中，如果下标相邻的两个字符串没有通配符就拼接起来，让程序可以最大限度地直接定位而不用一层一层搜索；然后将通配符换成正则表达式，在相应的目录下搜索到符合的文件名就直接获取该文件或文件夹进行递归查找。例如路径C:\U*s\Admin\Desktop\x?x.txt是允许的。  
在过程中发现java文件在E盘下路径只输入E:会变成相对路径，要使路径名变成E:\才是绝对路径。因此在初处理时会在切割后加上’\’。  
在输入”新建?本文档 (2).txt”时发现“新建(.)本文档 (2).txt”无法匹配“新建文本文档 (2).txt”，原因是正则中的括号在作怪，应该改成”新建(.)本文档 \\(2\\).txt”
### 进度条
多开一个线程显示进度。利用’\r’让命令行一直在同一行输出，过程中发现由于’\r’执行太频繁导致输出闪烁看不清，因此加入Thread.sleep延长刷新时间。
### 支持多种语言以及多种编码。
检测编码利用bom头
### 统计时间
利用函数读取查找开始时间与查找结束时间，作差得
