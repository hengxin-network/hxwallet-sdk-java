# 恒信Wallet SDK (Java版本)

恒信Java SDK提供了基本的密码学接口封装以及业务接口的封装，
实例项目在[sample](sample)目录下，SDK项目代码结构如下:
> Sample是一个独立的Project, 默认IDEA可能会将其导入到SDK主工程中
> 需要在File>Project Structure>Project Settings>Modules里面将其移除
> 查看Sample工程我们建议作为单独的Project打开

```
/HXUtils                    提供json解析
/IHXJsonParser              json解析抽象接口
/HXWallet                   钱包，提供秘钥签名，摘要等方法
--/crypto                   密码学相关类
----IHXKeyFactory           秘钥解析接口
----IHXSM2Engine            SM2加密模块接口
----IHXSM2Signer            SM2签名算法接口
----IHXSM3Digest            SM3摘要算法接口
----iHXSM4Engine            SM4加密算法接口
----HXDefaultxxxxxxx        上述接口封装的默认实现
--/service                  业务相关类
----/vo                     业务vo
----/dto                    业务dto
----HXConstants             业务常量，目前主要是error code
----HXService               恒信service服务，提供业务接口
----HXDefaultHttpClient     网络访问模块抽象接口
```

### 接入方法

1. 引入jar包依赖 位置:[hxwallet-1.0.jar](sample/src/main/lib/hxwallet.jar)
2. 使用钱包，需要注入必要的模块
- 需要注入的有三个模块，分别是IHXSM2Engine,IHXSM2Signer和IHXSM3Digest
- 其中IHXSM2Signer和IHXSM3Digest提供了基于BC库的默认注入实现，由于BC库SM2加解密的实现逻辑未使用Java Security标准API，SDK中不提供IHXSM2Engine的默认实现
- HXWallet是一个轻量的对象，一般使用单例即可，也可以根据需求new HXWallet()自行管理;
- 实例注入代码:
```java
    // sample使用的是BouncyCastle提供sm2，sm3等算法的密码学开源库
    // 根据需要更换为对应的实现，initDefaultInjects即为注入默认实现
    Security.addProvider(new BouncyCastleProvider());
    HXWallet.getInstance().initDefaultInjects();
    HXWallet.getInstance().injectSM2Engine(customSM2Engine);

    // 自定义注入如下
    HXWallet.getInstance().injectSM2Signer(customSM2Signer);
    HXWallet.getInstance().injectSM3(customSM3Digest);
    HXWallet.getInstance().injectSM2Engine(customSM2Engine);
```
3. 如果需要调用业务接口，需要创建业务服务实例，并注入业务服务所需要的json解析，钱包和http模块
- sample里json解析是对alibaba的fastjson的封装
- HXService构造方法如果不传入参数,则wallet使用HXWallet.getInstance()实例,httpclient使用new HXDefaultHttpClient(),是对HTTPURLConnection的简单封装
实例:

```java
    HXUtils.injectJsonParser(FastJsonParser.getInstance());
    HXService service = new HXService();
    service.injectWallet(wallet);
    service.injectHttpClient(customClient);
```

以上就初始化完毕了，可以开始使用wallet和service了

### API调用
>在调用API前需要先进行初始化

API分为两部分，钱包API和业务服务API

#### 钱包API

1. 设置PrivateKey和PublicKey
这里提供了两个方式，一个是将读取的私钥字符串作为String设置，另一个则是通过Java API中获取到的java.security.PrivateKey/PublicKey 私钥/公钥导入
Signer和Engine允许分开单独设置PrivateKey
***重要: 设置Key前记得先注入对应的实例，否则会报错***

API:
```java
    // 同时设置Signer和Engine
    public void setSM2PrivateKey(PrivateKey privateKey) throws InvalidKeyException
    public void setSM2PrivateKey(String encodedKey) throws InvalidKeySpecException, InvalidKeyException
    // 只设置Signer
    public void setSM2SignerPrivateKey(PrivateKey privateKey) throws InvalidKeyException
    public void setSM2SignerPrivateKey(String privateKey) throws InvalidKeySpecException, InvalidKeyException
    public void setSM2SignerPublicKey(PublicKey publicKey) throws InvalidKeyException 
    public void setSM2SignerPublicKey(String publicKey) throws InvalidKeySpecException, InvalidKeyException
    // 只设置Engine
    public void setSM2EnginePrivateKey(PrivateKey privateKey) 
    public void setSM2EnginePrivateKey(String encodedKey) throws InvalidKeySpecException 
```
例:
```java
    HXWallet.getInstance().setSM2PrivateKey(privateKey);
    HXWallet.getInstance().setSM2SignerPublicKey(publicKey);
```

2. 生成SM3摘要

API:
```java
    public byte[] digestBySM3(byte[] message)
```

例: 对"data"进行sm3 hash.
```java
    byte[] sm3hash = HXWallet.getInstance().digestBySM3("data".getBytes());
```

3. 生成SM2签名和验证签名
- 生成SM2签名前，需要先给SM2Signer设置PrivateKey，否则会报错
- 验证签名前，则需要先给SM2Signer设置PublicKey，否则会报错

API:
```java
    public byte[] signBySM2(byte[] rawData) throws SignatureException 
    public boolean verifyBySM2(byte[] rawData, byte[] signature) throws SignatureException
```

例: 对"data"生成签名
```java
    byte[] rawData = "the data will be sign.".getBytes();
    byte[] signData = HXWallet.getInstance().signBySM2(rawData);
    boolean verify  = HXWallet.getInstance().verifyBySM2(rawData, signData);
```

4. 使用SM2加密和解密数据
- 使用SM2Engine加密前需要设置PrivateKey，
- 解密用的PublicKey直接通过调用传入

API: 
```java
    public byte[] encryptBySM2(byte[] rawData,PublicKey publicKey);
    public byte[] decryptBySM2(byte[] encryptData);
```

```java
    byte[] rawData = "the data will be sm2 encrypt.".getBytes();
    byte[] encryptData = HXWallet.getInstance().encryptBySM2(rawData, publicKey);
    // encryptData即为加密后的数据
    byte[] decryptData =  HXWallet.getInstance().decryptBySM2(encryptData);
    // decryptData即为解密后的数据
```

5. 使用SM4加密和解密数据
- 需要先注入SM4Engine实例,
- 通过updateSM4Cipher方法更新engine中的秘钥key

API:
```java
    public SecretKey generateSM4Key(); 
    public void updateSM4Cipher(byte[] key) throws InvalidKeyException 
    public byte[] encryptBySM4(byte[] rawData) throws BadPaddingException, IllegalBlockSizeException 
    public byte[] decryptBySM4(byte[] encryptData) throws BadPaddingException, IllegalBlockSizeException 
```

```java
    SecretKey sm4Key = HXWallet.getInstance().generateSM4Key();
    HXWallet.getInstance().updateSM4Cipher(sm4Key.getEncoded());
    byte[] rawData = "the data will be sm4 encrypt.".getBytes();
    byte[] encryptData = HXWallet.getInstance().encryptBySM4(rawData);
    // encryptData即为加密后的数据
    byte[] decryptData = HXWallet.getInstance().decryptBySM4(rawData);
    // decryptData即为解密后的数据
```


#### 业务API

业务服务API调用前需要初始化完毕，并且提供BaseUrl，以及业务服务内的Wallet需要传入PrivateKey

HXUtils里提供了一些辅助API方法，JWT相关API也在HXUtils里

##### JWT相关
恒信网络的访问授权机制通过JWT实现，其中用到的签名算法为SM2，摘要算法为SM3，生成JWT前

创建jwt需要完成对一个HXWallet对象的初始化设置，来获取秘钥和调用各种算法。

恒信JWT中提供了对接口的Path，Query，Body进行签名的机制，来确保整个请求不会被篡改

1. 生成JWT

需要创建一个生成jwt的物料对象 HXJwtBuildMaterial

包含字段有

- address即为业务方自己的address
- expiredTime为token过期时间,单位是秒
- jti是用来防止「重放攻击」的唯一标识，一般用uuid即可
- method，url，body为请求附带的信息
- method: "GET"或"POST"
- url为URL标准中的Path + Query
  - 例如:"/info","/snapshots?limit=20&offset=1"
- body为post请求要传输的body
- requestSignature 标识是否要对请求进行签名生成payload sig，默认为true，绝大多数情况不用对其进行修改

HXUtils中提供了两个方法，生成JWT，一个返回HXJwt对象，一个返回JWT字符串

HXJwt对象中保管了原始字符串raw和解析序列化后的header,payload,signature三部分

我们推荐使用HXJwt对象进行操作

API: 
```java
    public static HXJwt buildJwt(HXWallet wallet, HXJwtBuildMaterial jwtMaterial) throws SignatureException 
    public static String buildJwtString(HXWallet wallet, HXJwtBuildMaterial jwtMaterial) throws SignatureException 
```

```java
    // 之前已经设置好了wallet对象
    HXJwtBuildMaterial buildMaterial = new HXJwtBuildMaterial()// set方法支持链式调用结构
        .setRequestMethod("GET")
        .setBody(null)
        .setAddress(TestUtil.userAddress)
        .setExpiredTime(7200L)
        .setUrl(HXUtils.buildUrlPathWithQueries("/info", null));
    HXJwt jwt = HXUtils.buildJwt(wallet, buildMaterial);// 返回 HXJwt
    String jwtString = HXUtils.buildJwtString(wallet, buildMaterial); // 只返回jwt String
```

2. 校验JWT

校验jwt方法会对jwt String进行各种校验，如果校验成功会返回一个HXJwt对象，如果校验过程中出错则会抛出异常

需要创建一个生成jwt的物料对象 HXJwtVerifyMaterial

包含字段有

- rawJwtString为要验证的Jwt
- method，url，body为对请求签名时的信息
- method: "GET"或"POST"
- url为URL标准中的Path + Query
  - 例如:"/info", "/snapshots?limit=20&offset=1"
- body为post请求要传输的body
- verifySig 标识是否要对请求签名进行验证，默认为true，绝大多数情况是不用对其进行修改的

```java
    HXJwtVerifyMaterial verifyMaterial = new HXJwtVerifyMaterial()
        .setRequestMethod("GET")
        .setBody(null)
        .setRawJwtString(jwtToken.getRaw())
        .setUrl(HXUtils.buildUrlPathWithQueries("/info", null));
    HXJwt jwt = HXUtils.verifyJwt(wallet, verifyMaterial);
```

3. 生成Body的byte[]的工具类方法
jwt中，会对请求的method url body进行签名，工具类提供了两个生成body的byte[]的方法

- convertJsonData方法会生成常规post请求的body数据，对应Content-Type为application/json
- convertMultiPartFormData方法用于有文件需要上传的情形，对应Content-Type为multipart/form-data;boundary={HXFileHolder.getBoundary()}

```java
    public static byte[] convertJsonData(Map<String,Object> body)
    public static byte[] convertMultiPartFormData(Map<String, String> body, HXFileHolder fileHolder) throws IOException 
```

##### 网络接口

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
参数以Query的格式带在url里
参数默认值:limit=20, from=1, order为HXSnapshotsRequest.ORDER_ASC,
- limit:一次返回多少条数据 最大不能超过500
- from :
- order:查询排序 为字符串枚举 "ASC"或"DESC" HXSnapshotsRequest类提供了常量ORDER_ASC和ORDER_DESC
- asset:为可选参数,如果未传,则按顺序返回该address能查询到所有asset数据

```java
    public HXResponse<HXSnapshotsBody> getSnapshots(String address, HXSnapshotRequest requestMap) throws SignatureException 
```

```java
    HXResponse<HXSnapshotsBody> response = service.getSnapshots(userAddress, new HXSnapshotRequest())
```

3. postTransaction 提交一条记录上链

TransactionRequest的所有参数均为必填，HXFileHolder为选填，需上传文件附件时
参数以multipart/form-data的形式上传,包含asset, opponent_addresses, trace_id, memo

- trace_id: 防重放攻击使用,一般使用UUID
- memo: 链上记录数据，为TransactionMemo
- opponent_addresses: 是一个包含了相关address的列表

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
   
    File file;// file为要上传的文件
    HXResponse<HXResponseBody<HXTransaction>> HXResponse = api.postTransactions(userAddress, requestMap, file);       

```

