# 恒信Wallet SDK (Java版本)

恒信Java SDK提供了基本的密码学接口封装以及业务接口的封装，
实例项目在[sample](sample)目录下，SDK项目代码结构如下:

```
/HXUtils                    提供json解析
/IHXJsonParser              json解析抽象接口
/HXWallet                   钱包，提供秘钥签名，摘要等方法
--/crypto                   密码学相关类
----IHXKeyFactory           秘钥接口封装
----IHXSM2Signer            SM2签名算法接口封装
----IHXSM3Digest            SM3摘要算法接口封装
----HXDefaultxxxxxxx        上述接口封装的默认实现
--/service                  业务相关类
----/vo                     业务vo
----/dto                    业务dto
----HXConstants             业务常量，目前主要是error code
----HXService               恒信service服务，提供业务接口
----HXDefaultHttpClient     网络访问模块抽象接口
```

### 接入方法

1. 引入jar包依赖 位置:[hxwallet-1.0.jar](outputs/hxwallet-1.0.jar)
2. 使用钱包，需要注入必要的模块
- 需要注入的有两个模块，分别是IHXSM2Signer和IHXSM3Digest
- HXWallet是一个轻量的对象，一般使用单例即可，也可以new HXWallet();
- 实例注入代码:
```java
    // sample使用的是BouncyCastle提供sm2，sm3等算法的密码学开源库
    // 根据需要更换为对应的实现，initDefaultInjects即为注入默认实现
    Security.addProvider(new BouncyCastleProvider());
    HXWallet.getInstance().initDefaultInjects();

    // 自定义注入如下
    HXWallet.getInstance().injectSM2(customSM2Signer);
    HXWallet.getInstance().injectSM3(customSM3Digest);
```
3. 如果需要调用业务接口，需要创建业务服务实例，并注入业务服务所需要的json解析，钱包和http模块
```java
    // sample里json解析是对alibaba的fastjson的封装
    HXUtils.injectJsonParser(FastJsonParser.getInstance());
    // HXService构造方法如果不传入参数
    // 则wallet使用HXWallet.getInstance()实例
    // httpclient使用new HXDefaultHttpClient(),是对HTTPURLConnection的简单封装
    HXService service = new HXService();
    service.injectWallet(wallet);
    service.injectHttpClient(customClient);
```

以上就初始化完毕了，可以开始使用wallet和service了

### API调用
>在调用API前需要先进行初始化

API分为两部分，钱包API和业务服务API

#### 钱包API

1. 设置PrivateKey
这里提供了两个方法，一个是将读取的私钥字符串作为String设置，另一个则是通过Java API中获取到的java.security.PrivateKey 私钥导入
```java
    public void setPrivateKey(PrivateKey privateKey) throws InvalidKeyException
    public void setPrivateKey(String encodedKey) throws InvalidKeySpecException, InvalidKeyException

    // 例
    HXWallet.getInstance().setPrivateKey(privateKey);
```

2. 生成SM3摘要

Wallet API:
```java
    public byte[] digestBySM3(byte[] message)
```

例: 对"data"进行sm3 hash.
```java
    byte[] sm3hash = HXWallet.getInstance().digestBySM3("data".getBytes());
```

3. 生成SM2签名
> 生成SM2签名前，需要先设置PrivateKey，否则将报错

Wallet API:
```java
    public byte[] signBySM2(byte[] rawData) throws SignatureException 
```

例: 对"data"生成签名
```java
    byte[] signByte = sm2.sign("data".getBytes());
```

#### 业务API

业务服务API调用前需要初始化完毕，并且提供BaseUrl，以及业务服务内的Wallet需要传入PrivateKey

0. 设置BaseUrl 
设置BaseUrl可以通过IHXHttpClient的`setBaseUrl`方法设置，也可以通过HXService提供的桥接方法设置，最终都会调用client的`setBaseUrl`方法
```java
    // 三个参数分别为schema, host, port
    // 例 http , "106.14.59.4", 8930
    service.setBaseUrl(new BaseUrl("http",host,port))
```

1. getInfo 根据address获取UserInfo信息

```java
    public HXResponse<HXUserInfoBody> getInfo(String address) throws SignatureException
```

```java
    HXResponse<HXUserInfoBody> response = service.getInfo(userAddress);
```

2. getSnapshots 查询snapshots记录
根据address和提交的参数查询snapshots的记录
默认limit=20, from=1, order为HXSnapshotsRequest.ORDER_ASC,
limit 最大不能超过500
order为字符串枚举 "ASC"或"DESC" HXSnapshotsRequest类提供了常量ORDER_ASC和ORDER_DESC
asset为可选参数

```java
    public HXResponse<HXSnapshotsBody> getSnapshots(String address, HXSnapshotRequest requestMap) throws SignatureException 
```

```java
    HXResponse<HXSnapshotsBody> response = service.getSnapshots(userAddress, new HXSnapshotRequest())
```

3. postTransaction 提交一条记录上链

TransactionRequest的所有参数均为必填

包含asset, opponent_addresses, trace_id, memo

trace_id一般使用UUID

memo为TransactionMemo

opponent_addresses 是一个包含了相关address的列表

```java
    HXTransactionMemo memo = new HXTransactionMemo()
                    .setT("test-type")  // t : type
                    .setD("test-data")  // d : data
                    .setH(Hex.toHexString(HXWallet.getInstance().digestBySM3("test-data".getBytes()))); // h : hash
    
    HXTransactionRequest requestMap = new HXTransactionRequest()
                    .setAsset("6b4d1e14ea651021fa5720b9b6e540fcc048760733bc1b0c8756eb84af40f0fa")
                    .setMemo(memo)
                    .setOpponent_addresses(Collections.singletonList(opponentAddress))
                    .setTrace_id(UUID.randomUUID().toString());
    HXResponse<HXResponseBody<HXTransaction>> HXResponse = api.postTransactions(userAddress, requestMap);
```

