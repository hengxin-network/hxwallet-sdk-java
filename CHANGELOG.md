### 2020.8.21
* 更新了decodeAddress方法的逻辑为校验国密主网版本的Address

### 2020.8.17
* 新增getNewworkSnapshots接口及相关model

### 2020.8.13
* 算法切换至国密
* postTransaction增加了 async 参数
* 原sdk放至在v1分支


### 2020.5.27
* postUser接口返回数据中增加了private_key字段.


### 2020.4.29
* snapshot 结构中增加了tx_hash字段.
* 增加了新的API getSnapshotByTxHash 通过tx_hash查询snapshot记录



### 2020.4.28
* snapshot中增加senders，receivers，priv_data字段
* postTransaction对应增加了senders_required,receivers_required和priv_data.



### 2020.4.3
* 增加了文件下载getFile
* 修复jwt生成校验逻辑里的一个bug
* 增加了根据id查询snapshot的api



### 2020.4.2
* 增加了文件权限更新
* 字段修改符合最新API文档
* 增加了CHANGELOG.md（即本文件）



### 2020.4.1

* 增加了SM4秘钥生成
* `postTransaction`增加了文件上传能力
* HXUtils 的 `verifyJwt` 改为返回HXJwtVerifyResult取代抛出异常
* README更新



### 2020.3.31

* 增加了SM2加密、解密
* 增加了SM4加密、解密
* Sample 项目更新



### 2020.3.26

首次上传至公开仓库,包含以下内容

* SM2 签名、验证
* SM3 摘要
* jwt 生成、校验
* `getInfo`,`getSnapshots`,`postTransaction`接口
* Sample 项目

