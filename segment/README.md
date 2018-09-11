# NLP工具项目
###
## 1.分词
使用HanLP开源工具分词，HanLP官网：https://github.com/hankcs/HanLP

### 使用教程： ###
1. 配置HanLP数据;

    1.1 下载HanLP数据（分词字典、模型），下载地址：[data-for-1.6.6.zip](http://hanlp.linrunsoft.com/release/data-for-1.6.6.zip)
    
    1.2 解压后将相应的地址配置到本工程`src/main/resources/hanlp.properties`文件中：

        假设解压路径为`D:\a`，那么这个路径下应该具有如下目录结构：
   
        ```bash
        .
        └── data
            ├── dictionary
            ├── model
            ├── README.url
            └── version.txt
        ```
    
        将`src/main/resources/hanlp.properties`中的`root`配置为`D:\a`即可。

    1.3 将本工程的`src/main/resources/custom_dict`目录下的`black_dict.txt`和`huya.txt`复制到`D:\a\data\dictionary\custom`目录下。

    
2. 准备需要分词的文本，格式如下：
    ```csv
    一行一句话。
    这是第二句话。
    这是需要分词的第三句话
    ```
    
3. 运行
```shell
mvn exec:java -Dexec.mainClass="segment.HanlpProcess" -Dexec.classpathScope=runtime -Dexec.args="/home/liangyuxin/dev/data/danmu_dataset_legal legal"
```