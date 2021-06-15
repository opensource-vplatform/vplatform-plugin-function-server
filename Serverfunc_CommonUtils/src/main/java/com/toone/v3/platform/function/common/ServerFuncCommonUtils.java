package com.toone.v3.platform.function.common;

import com.toone.v3.platform.function.common.exception.ServerFuncException;
import com.yindangu.v3.business.plugin.business.api.func.IFuncContext;
import com.yindangu.v3.business.plugin.business.api.service.IOutService;

import java.text.SimpleDateFormat;
import java.util.regex.Pattern;

/**
 * 服务端函数公共方法及常量定义
 *
 * @Author xugang
 * @Date 2021/5/28 13:43
 */
public interface ServerFuncCommonUtils extends IOutService {
    /**
     * 插件作者
     */
    public final static String Plugin_Author = "同望科技";
    /**
     * 组织标识
     */
    public final static String Group_Id = "com.toone.v3.platform";

    public final static String OutServer_Code = "com_toone_v3_platform_Serverfunc_CommonUtils_serverFuncCommonUtils";

    public final static FunctionDesc OutService = new FunctionDesc("serverFuncCommonUtils", "服务端函数工具类", "服务端函数工具类");

    /**
     * 转换绝对值
     */
    public final static FunctionDesc ABS = new FunctionDesc("ABS", "转换绝对值", "返回一个数的绝对值。");
    /**
     * 反余弦
     */
    public final static FunctionDesc Acos = new FunctionDesc("Acos", "反余弦", "反余弦，返回余弦值为指定值的角度。");
    /**
     * 给数字添加千分位
     */
    public final static FunctionDesc AddThousandBits = new FunctionDesc("AddThousandBits", "给数字添加千分位", "给数字添加千分位。");
    /**
     * Ascii转Unicode
     */
    public final static FunctionDesc AsciiToUnicode = new FunctionDesc("AsciiToUnicode", "Ascii转Unicode", "将指定Ascii字符串转换成,返回Unicode字符串。");
    /**
     * 反正弦
     */
    public final static FunctionDesc Asin = new FunctionDesc("Asin", "反正弦", "反正弦，返回正弦值为指定值的角度。");
    /**
     * 反正切
     */
    public final static FunctionDesc Atan = new FunctionDesc("Atan", "反正切", "反正切，返回正切值为指定值的角度。");
    /**
     * 计算实体某个字段的平均值
     */
    public final static FunctionDesc AvgColumnFunc = new FunctionDesc("AvgColumnFunc", "计算实体某个字段的平均值", "计算实体某个字段的平均值。");
    /**
     * 小数的最小整数值
     */
    public final static FunctionDesc Ceiling = new FunctionDesc("Ceiling", "小数的最小整数值", "小数的最小整数值(返回大于或等于指定小数的最小整数值)。");
    /**
     * 将人民币金额转成中文大写
     */
    public final static FunctionDesc ChangeMoneyToChinese = new FunctionDesc("ChangeMoneyToChinese", "将人民币金额转成中文大写", "将人民币金额转成中文大写。");
    /**
     * 校验验证码
     */
    public final static FunctionDesc CheckCertCodeFun = new FunctionDesc("CheckCertCodeFun", "校验验证码", "校验输入的验证码是否正确。");
    /**
     * 中文检查
     */
    public final static FunctionDesc CheckChinese = new FunctionDesc("CheckChinese", "中文检查", "检查指定字符串是否包含中文字符;");
    /**
     * 中文转Unicode
     */
    public final static FunctionDesc ChineseToUnicode = new FunctionDesc("ChineseToUnicode", "中文转Unicode", "将指定字符串转换成Unicode,返回Unicode字符串。");
    /**
     * 比较函数
     */
    public final static FunctionDesc Compare = new FunctionDesc("Compare", "比较函数", "比较两个字符串是否一致,返回比较结果，相等则返回True。");
    /**
     * 字符串合并
     */
    public final static FunctionDesc ConcatStr = new FunctionDesc("ConcatStr", "字符串合并", "按参数顺序合并字符串。可以任意多个参数。");
    /**
     * 验证包含
     */
    public final static FunctionDesc Contains = new FunctionDesc("Contains", "验证包含", "检查指定的字符串中是否包含另一指定的字符串，包含则返回True。");
    /**
     * 格式转换，将指定值转换成指定格式返回
     */
    public final static FunctionDesc ConvertFunc = new FunctionDesc("ConvertFunc", "格式转换，将指定值转换成指定格式返回", "格式转换，将指定值转换成指定格式返回。");
    /**
     * 转换页码
     */
    public final static FunctionDesc ConvertPageNumber = new FunctionDesc("ConvertPageNumber", "转换页码", "转换页码，将记录开始数转换成页码。");
    /**
     * 将字符串的值添加到实体中指定的字段
     */
    public final static FunctionDesc ConvertVarToEntityColumn = new FunctionDesc("ConvertVarToEntityColumn", "将字符串的值添加到实体中指定的字段", "将字符串的值添加到实体中指定的字段。");
    /**
     * 根据文件标识id复制文件
     */
    public final static FunctionDesc CopyFileByFileId = new FunctionDesc("CopyFileByFileId", "根据文件标识id复制文件", "根据文件标识id复制文件;");
    /**
     * 余弦
     */
    public final static FunctionDesc Cos = new FunctionDesc("Cos", "余弦", "余弦,返回值为指定角度的余弦值。");
    /**
     * 双曲余弦值
     */
    public final static FunctionDesc Cosh = new FunctionDesc("Cosh", "双曲余弦值", "双曲余弦,返回值为指定角度的双曲余弦值。");
    /**
     * 将指定日期加上一定的时间间隔后的时间字符串
     */
    public final static FunctionDesc DateAddFunc = new FunctionDesc("DateAddFunc", "将指定日期加上一定的时间间隔后的时间字符串", "将时间加上一定的时间间隔，返回计算后的时间字符串。");
    /**
     * 时间单位的相互转换
     */
    public final static FunctionDesc DateConvert = new FunctionDesc("DateConvert", "时间单位的相互转换", "将一时间的单位转换成另一种单位后的时间数。");
    /**
     * 将两个日期相减后的时间段
     */
    public final static FunctionDesc Datediff = new FunctionDesc("Datediff", "将两个日期相减后的时间段", "返回两个日期之间的时间间隔；");
    /**
     * 将时间减去一定的时间间隔，返回计算后的时间字符串
     */
    public final static FunctionDesc DateSub = new FunctionDesc("DateSub", "将时间减去一定的时间间隔，返回计算后的时间字符串", "将时间减去一定的时间间隔，返回计算后的时间字符串。");
    /**
     * 时间日期
     */
    public final static FunctionDesc DateTimeNow = new FunctionDesc("DateTimeNow", "时间日期", "返回服务器当前的日期和时间，格式为yyyy-MM-dd HH:mm:ss");
    /**
     * 根据日期时间转成时间戳
     */
    public final static FunctionDesc DateTimeToUnixtimestamp = new FunctionDesc("DateTimeToUnixtimestamp", "根据日期时间转成时间戳", "根据日期时间转成时间戳。");
    /**
     * 根据指定格式，将时间格式化为字符串返回。
     */
    public final static FunctionDesc DateToString = new FunctionDesc("DateToString", "根据指定格式，将时间格式化为字符串返回。", "根据指定格式，将时间格式化为字符串返回。");
    /**
     * 对字符串解码
     */
    public final static FunctionDesc DecodeBASE64 = new FunctionDesc("DecodeBASE64", "对字符串解码", "对字符串基于64位解码，返回值为字符串。");
    /**
     * 对字符串作为URI组件进行解码
     */
    public final static FunctionDesc DecodeURIComponent = new FunctionDesc("DecodeURIComponent", "对字符串作为URI组件进行解码", "将字符串作为URI组件进行解码。");
    /**
     * 对字符串进行解密
     */
    public final static FunctionDesc DecryptFunc = new FunctionDesc("DecryptFunc", "对字符串进行解密", "对字符串进行解密。");
    /**
     * 整数商
     */
    public final static FunctionDesc Divrem = new FunctionDesc("Divrem", "整数商", "两数相除后的整数商，返回值为整数。");
    /**
     * 下载网络文件到本地文件系统
     */
    public final static FunctionDesc DowdloadFileToFileSystem = new FunctionDesc("DowdloadFileToFileSystem", "下载网络文件到本地文件系统", "下载网络文件到本地文件系统。");
    /**
     * 从ftp下载文件
     */
    public final static FunctionDesc DownloadFromFtp = new FunctionDesc("DownloadFromFtp", "从ftp下载文件", "从ftp下载文件到文件服务中， 返回文件id");
    /**
     * e
     */
    public final static FunctionDesc E = new FunctionDesc("E", "e", "返回自然对数的底数。");
    /**
     * 对字符串编码
     */
    public final static FunctionDesc EncodeBASE64 = new FunctionDesc("EncodeBASE64", "对字符串编码", "对字符串基于64位编码，返回值为字符串。");
    /**
     * 对字符串作为URI组件进行编码
     */
    public final static FunctionDesc EncodeURIComponent = new FunctionDesc("EncodeURIComponent", "对字符串作为URI组件进行编码", "将字符串作为URI组件进行编码。");
    /**
     * 对字符串进行加密
     */
    public final static FunctionDesc EncryptFunc = new FunctionDesc("EncryptFunc", "对字符串进行加密", "对字符串进行加密。");
    /**
     * 对字符串进行加密
     */
    public final static FunctionDesc EncryptionFunc = new FunctionDesc("EncryptionFunc", "对字符串进行加密", "按照加密策略，对字符串进行加密。");
    /**
     * 是否以指定串结尾
     */
    public final static FunctionDesc EndsWith = new FunctionDesc("EndsWith", "是否以指定串结尾", "检查指定的字符串是否以另一指定的字符串结尾，是则返回True。");
    /**
     * 执行表达式
     */
    public final static FunctionDesc EvalExpression = new FunctionDesc("EvalExpression", "执行表达式", "校验执行表达式的函数");
    /**
     * e的指定次幂
     */
    public final static FunctionDesc Exp = new FunctionDesc("Exp", "e的指定次幂", "返回e的指定次幂");
    /**
     * 小数的最大整数值
     */
    public final static FunctionDesc Floor = new FunctionDesc("Floor", "小数的最大整数值", "小数的最大整数值(返回小于或等于指定小数的最大整数值)。");
    /**
     * 拼接格式
     */
    public final static FunctionDesc Format = new FunctionDesc("Format", "拼接格式", "在格式串内拼接指定字符串,返回结果串。");
    /**
     * 取连续的流水号
     */
    public final static FunctionDesc GenerateSequenceNumber = new FunctionDesc("GenerateSequenceNumber", "取连续的流水号", "取连续的流水号。");
    /**
     * 取流水号
     */
    public final static FunctionDesc GenerateSequenceNumberQuick = new FunctionDesc("GenerateSequenceNumberQuick", "取流水号", "取流水号，最优化处理。有断号。");
    /**
     * 生成uuid
     */
    public final static FunctionDesc GenerateUUID = new FunctionDesc("GenerateUUID", "生成uuid", "生成一个uuid。");
    /**
     * 根据身份证号码获取年龄
     */
    public final static FunctionDesc GetAgeByIdCard = new FunctionDesc("GetAgeByIdCard", "根据身份证号码获取年龄", "根据身份证号码获取年龄。");
    /**
     * 根据树节点id查出它所有的父节点集合组装成约定的字符串
     */
    public final static FunctionDesc GetAllParentIds = new FunctionDesc("GetAllParentIds", "根据树节点id查出它所有的父节点集合组装成约定的字符串", "根据树节点id查出它所有的父节点集合组装成约定的字符串。");
    /**
     * 按照条件获取对应的实体的字段信息
     */
    public final static FunctionDesc GetConditionColumnValue = new FunctionDesc("GetConditionColumnValue", "按照条件获取对应的实体的字段信息", "1、根据条件查询指定数据源中某个字段的值 2、如果该条件下返回多条记录，则取第一条记录的值。");
    /**
     * 获取服务连接的数据库类型
     */
    public final static FunctionDesc GetDataBaseType = new FunctionDesc("GetDataBaseType", "获取服务连接的数据库类型", "获取服务连接的数据库类型。");
    /**
     * 获取日期的某一部分
     */
    public final static FunctionDesc GetDateSection = new FunctionDesc("GetDateSection", "获取日期的某一部分", "获取日期的某一部分返回。");
    /**
     * 获取轨迹里程
     */
    public final static FunctionDesc GetDistance = new FunctionDesc("GetDistance", "获取轨迹里程", "根据经纬度数据顺序集合，自动计算轨迹里程。");
    /**
     * 根据条件从实体获取记录数
     */
    public final static FunctionDesc GetEntityRowCountFunc = new FunctionDesc("GetEntityRowCountFunc", "根据条件从实体获取记录数", "根据条件从实体获取记录数。");
    /**
     * 获取文件信息
     */
    public final static FunctionDesc GetFileInfo = new FunctionDesc("GetFileInfo", "获取文件信息", "根据文件id获取文件信息。");
    /**
     * 根据文件Id获取文件url
     */
    public final static FunctionDesc GetFileUrlByFileId = new FunctionDesc("GetFileUrlByFileId", "根据文件Id获取文件url", "根据文件Id获取文件url");
    /**
     * 获取实体变量首行记录字段值
     */
    public final static FunctionDesc GetFirstRowColumnValue = new FunctionDesc("GetFirstRowColumnValue", "获取实体变量首行记录字段值", "获取实体变量首行记录字段值。");
    /**
     * 获取图片的URL路径
     */
    public final static FunctionDesc GetImageUrl = new FunctionDesc("GetImageUrl", "获取图片的URL路径", "根据图片的文件id获取图片的URL路径。");
    /**
     * 获取客户端请求的IP地址
     */
    public final static FunctionDesc GetIPAddressFunc = new FunctionDesc("GetIPAddressFunc", "获取客户端请求的IP地址", "返回当前客户端的IP地址(注意返回的是服务器端获取到的请求IP地址)。");
    /**
     * 获取字符串的长度
     */
    public final static FunctionDesc GetLength = new FunctionDesc("GetLength", "获取字符串的长度", "返回字符串的长度。");
    /**
     * 根据地址获取经度或纬度
     */
    public final static FunctionDesc GetLngOrLatByAddr = new FunctionDesc("GetLngOrLatByAddr", "根据地址获取经度或纬度", "根据地址获取经度或纬度。");
    /**
     * 根据经度纬度获取地理位置
     */
    public final static FunctionDesc GetLoactionPlace = new FunctionDesc("GetLoactionPlace", "根据经度纬度获取地理位置", "根据经度纬度获取地理位置");
    /**
     * 获取对象属性值
     */
    public final static FunctionDesc GetProperty = new FunctionDesc("GetProperty", "获取对象属性值", "获取对象属性值。");
    /**
     * 流水号函数
     */
    public final static FunctionDesc GetSerialNumberFunc = new FunctionDesc("GetSerialNumberFunc", "流水号函数", "根据前缀，取出表里的最大流水号加1后，补齐位数返回字符串。");
    /**
     * 获取系统变量、系统常量函数
     */
    public final static FunctionDesc GetSystemVariable = new FunctionDesc("GetSystemVariable", "获取系统变量、系统常量函数", "获取构件变量或者构件常量，如无该构件变量时，就取构件常量。");
    /**
     * 根据表名字段名以及过滤条件获取数据
     */
    public final static FunctionDesc GetTableData = new FunctionDesc("GetTableData", "根据表名字段名以及过滤条件获取数据", "根据表名字段名以及过滤条件获取数据,返回获取的结果。");
    /**
     * 获取地图坐标的缩放等级
     */
    public final static FunctionDesc GetZoomLevel = new FunctionDesc("GetZoomLevel", "获取地图坐标的缩放等级", "根据经纬度数据集合，自动计算缩放等级。");
    /**
     * 检查指定的表是否有数据
     */
    public final static FunctionDesc HasRecord = new FunctionDesc("HasRecord", "检查指定的表是否有数据", "计算数据库表中数据量。");
    /**
     * 查找指定字符串的位置
     */
    public final static FunctionDesc IndexOf = new FunctionDesc("IndexOf", "查找指定字符串的位置", "检查指定字符串在被检查串中的指定位置后的第一个匹配项的位置（0基准）,返回查找结果,没有找到返回-1。");
    /**
     * 插入指定字符串
     */
    public final static FunctionDesc Insert = new FunctionDesc("Insert", "插入指定字符串", "在指定字符串的指定位置插入另外一指定的模式串,返回插入指定串后的结果字符串。");
    /**
     * 检查是否为空值
     */
    public final static FunctionDesc IsEmpty = new FunctionDesc("IsEmpty", "检查是否为空值", "检查输入的参数是否为空串，为空返回True。");
    /**
     * 空值处理
     */
    public final static FunctionDesc IsNull = new FunctionDesc("IsNull", "空值处理", "检查输入的参数是否为空值,不为空返回原值，为空返回默认值。");
    /**
     * 空值空字符串处理
     */
    public final static FunctionDesc IsNullOrEmptyFunc = new FunctionDesc("IsNullOrEmptyFunc", "空值空字符串处理", "判断参数是否为空或0长度字符串,返回 true 或者 false。");
    /**
     * 检查字符串是否仅为空白字符组成
     */
    public final static FunctionDesc IsWhiteOrSpace = new FunctionDesc("IsWhiteOrSpace", "检查字符串是否仅为空白字符组成", "检查字符串是否为空串或者全部是空格，如果是返回True。");
    /**
     * 倒序查找指定
     */
    public final static FunctionDesc LastIndexOf = new FunctionDesc("LastIndexOf", "倒序查找指定", "从头到指定索引位置（0基准）之间，找到最后一个匹配串，并返回其索引位置；没有找到返回-1。");
    /**
     * 将实体某个字段拼接成一个字符串返回
     */
    public final static FunctionDesc ListToStringFunc = new FunctionDesc("ListToStringFunc", "将实体某个字段拼接成一个字符串返回", "将实体某个字段拼接成一个字符串返回。");
    /**
     * 求10的底
     */
    public final static FunctionDesc Log10 = new FunctionDesc("Log10", "求10的底", "返回指定数字以 10 为底的对数。");
    /**
     * 打日志信息到控制台上
     */
    public final static FunctionDesc LogFunc = new FunctionDesc("LogFunc", "打日志信息到控制台上", "打日志信息到控制台上。");
    /**
     * 求两数最大值
     */
    public final static FunctionDesc Max = new FunctionDesc("Max", "求两数最大值", "返回两个数字中较大的一个。");
    /**
     * 求指定实体的某个字段的最大值
     */
    public final static FunctionDesc MaxColumn = new FunctionDesc("MaxColumn", "求指定实体的某个字段的最大值", "查询指定实体的字段的最大值并返回。");
    /**
     * MD5加密
     */
    public final static FunctionDesc MD5Encrypt = new FunctionDesc("MD5Encrypt", "MD5加密", "对字符串进行加密，加密算法为MD5+BASE64算法。");
    /**
     * utf-8字符集的MD5加密
     */
    public final static FunctionDesc MD5EncryptByUTF8 = new FunctionDesc("MD5EncryptByUTF8", "utf-8字符集的MD5加密", "utf-8字符集的MD5加密");
    /**
     * 求两数最小值
     */
    public final static FunctionDesc Min = new FunctionDesc("Min", "求两数最小值", "返回两个数字中较小的一个。");
    /**
     * 获取实体的某个字段的最小值
     */
    public final static FunctionDesc MinColumn = new FunctionDesc("MinColumn", "获取实体的某个字段的最小值", "查询指定实体的字段的最小值并返回。");
    /**
     * 产生空值
     */
    public final static FunctionDesc Null = new FunctionDesc("Null", "产生空值", "产生空值。可在加载规则查询条件中使用，令查询条件失效。");
    /**
     * 编码字符串累加
     */
    public final static FunctionDesc NumberCodeAdd = new FunctionDesc("NumberCodeAdd", "编码字符串累加", "对数值型的字符串进行数值加减操作，并依照原格式返回字符串。");
    /**
     * 左侧填充
     */
    public final static FunctionDesc PadLeft = new FunctionDesc("PadLeft", "左侧填充", "左填充字符串，使其达到指定长度。返回填充后的字符串。");
    /**
     * 右侧填充
     */
    public final static FunctionDesc PadRight = new FunctionDesc("PadRight", "右侧填充", "检查指定字符串长度是否达到指定长度,未达到则用指定字符在末尾填充,返回填充好的字符串。");
    /**
     * 圆周率
     */
    public final static FunctionDesc Pai = new FunctionDesc("Pai", "圆周率", "返回圆周率的值。");
    /**
     * 指定数字的幂
     */
    public final static FunctionDesc Pow = new FunctionDesc("Pow", "指定数字的幂", "返回指定数字的指定次幂。");
    /**
     * 随机数
     */
    public final static FunctionDesc Random = new FunctionDesc("Random", "随机数", "返回指定区间的随机数。");
    /**
     * 作废流水号
     */
    public final static FunctionDesc RecyclingSequenceNumber = new FunctionDesc("RecyclingSequenceNumber", "作废流水号", "作废流水号");
    /**
     * 余数
     */
    public final static FunctionDesc Remainder = new FunctionDesc("Remainder", "余数", "返回两数相除之后的余数。");
    /**
     * 移除字符
     */
    public final static FunctionDesc Remove = new FunctionDesc("Remove", "移除字符", "移除指定索引（0基准）位置、指定长度的字符,返回移除后的字符串。");
    /**
     * 字符串替换
     */
    public final static FunctionDesc ReplaceByIndex = new FunctionDesc("ReplaceByIndex", "字符串替换", "按位置替换字符串。替换位于指定位置范围的字符串。索引超出指定范围的不变,beginIndex < endIndex并且为有效范围才替换。");
    /**
     * 替代字符串内的内容
     */
    public final static FunctionDesc ReplaceFunc = new FunctionDesc("ReplaceFunc", "替代字符串内的内容", "用指定的字符串替换原始字符串中的子串,返回替换后的结果。");
    /**
     * 求浮点数的四舍五入值
     */
    public final static FunctionDesc Round = new FunctionDesc("Round", "求浮点数的四舍五入值", "将浮点数保留指定小数位进行四舍五入。");
    /**
     * 加法
     */
    public final static FunctionDesc ServerAdd = new FunctionDesc("ServerAdd", "加法", "1. 对数值做加法运算\r\n2. 最后一位参数为小数保留位数 \r\n3. 结果按照保留小数位数进行四舍五入");
    /**
     * 除法
     */
    public final static FunctionDesc ServerDivide = new FunctionDesc("ServerDivide", "除法", "1. 对数值做除法运算\r\n2. 如果除数为0，则返回NaN\r\n3. 最后一位参数为小数保留位数\r\n4. 结果按照保留小数位数进行四舍五入");
    /**
     * 乘法
     */
    public final static FunctionDesc ServerMultiply = new FunctionDesc("ServerMultiply", "乘法", "1. 对数值做乘法运算\r\n2. 最后一位参数为小数保留位数\r\n3. 结果按照保留小数位数进行四舍五入");
    /**
     * 减法
     */
    public final static FunctionDesc ServerSubtract = new FunctionDesc("ServerSubtract", "减法", "1. 对数值做减法运算\r\n2. 最后一位参数为小数保留位数\r\n3. 结果按照保留小数位数进行四舍五入");
    /**
     * 短日期
     */
    public final static FunctionDesc ShortDateNow = new FunctionDesc("ShortDateNow", "短日期", "返回服务器当前时间的短时间格式，格式为yyyy-MM-dd。");
    /**
     * 短时间
     */
    public final static FunctionDesc ShortTimeNow = new FunctionDesc("ShortTimeNow", "短时间", "返回服务器当前时间的短时间格式，格式为HH:mm。");
    /**
     * 求正负值
     */
    public final static FunctionDesc Sign = new FunctionDesc("Sign", "求正负值", "数字的符号的值。");
    /**
     * 正弦
     */
    public final static FunctionDesc Sin = new FunctionDesc("Sin", "正弦", "正弦,返回值为指定角度的正弦值。");
    /**
     * 双曲正弦值
     */
    public final static FunctionDesc Sinh = new FunctionDesc("Sinh", "双曲正弦值", "双曲正弦,返回值为指定角度的双曲正弦值。");
    /**
     * 数字的平方根
     */
    public final static FunctionDesc Sqrt = new FunctionDesc("Sqrt", "数字的平方根", "返回指定数字的平方根。");
    /**
     * 标准MD5加密
     */
    public final static FunctionDesc StandardMD5Encrypt = new FunctionDesc("StandardMD5Encrypt", "标准MD5加密", "对字符串进行标准MD5加密。");
    /**
     * 是否以指定串开头
     */
    public final static FunctionDesc StartsWith = new FunctionDesc("StartsWith", "是否以指定串开头", "检查指定的字符串是否以另一指定的模式串开头,返回检查结果。");
    /**
     * 子链提取
     */
    public final static FunctionDesc Substring = new FunctionDesc("Substring", "子链提取", "从字符串指定索引（0基准）位置开始提取指定长度的该字符串的子串,返回提取出的子串。");
    /**
     * 正切
     */
    public final static FunctionDesc Tan = new FunctionDesc("Tan", "正切", "正切,返回值为指定角度的正切值。");
    /**
     * 双曲正切值
     */
    public final static FunctionDesc Tanh = new FunctionDesc("Tanh", "双曲正切值", "双曲正切,返回值为指定角度的双曲正切值。");
    /**
     * 小写转换
     */
    public final static FunctionDesc ToLower = new FunctionDesc("ToLower", "小写转换", "将指定字符串转换成小写形式(非英文忽略),返回小写字符串。");
    /**
     * 计算实体某个字段的总和
     */
    public final static FunctionDesc TotalColumnFunc = new FunctionDesc("TotalColumnFunc", "计算实体某个字段的总和", "计算实体某个字段的总和。");
    /**
     * 大写转换
     */
    public final static FunctionDesc ToUpper = new FunctionDesc("ToUpper", "大写转换", "将指定字符串转换成大写形式(非英文忽略),返回大写字符串。");
    /**
     * 树数据根据权重向根节点汇总
     */
    public final static FunctionDesc TreeDataUpwardCollect = new FunctionDesc("TreeDataUpwardCollect", "树数据根据权重向根节点汇总", "树数据根据权重向根节点汇总。");
    /**
     * 后导空白字符移除
     */
    public final static FunctionDesc TrimEnd = new FunctionDesc("TrimEnd", "后导空白字符移除", "移除字符串末尾的空格，返回移除后的字符串。");
    /**
     * 前导空白字符移除
     */
    public final static FunctionDesc TrimStart = new FunctionDesc("TrimStart", "前导空白字符移除", "移除字符串开头的空格,返回移除后的字符串。 ");
    /**
     * 求浮点数的整数部分
     */
    public final static FunctionDesc Truncate = new FunctionDesc("Truncate", "求浮点数的整数部分", "取指定数值的整数部分。");
    /**
     * Unicode转Ascii
     */
    public final static FunctionDesc UnicodeToAscii = new FunctionDesc("UnicodeToAscii", "Unicode转Ascii", "将指定Unicode字符串转换成,返回Ascii字符串。");
    /**
     * Unicode转中文
     */
    public final static FunctionDesc UnicodeToChinese = new FunctionDesc("UnicodeToChinese", "Unicode转中文", "将指定Unicode字符串转换成中文,返回中文字符串。");
    /**
     * 根据时间戳转成日期时间
     */
    public final static FunctionDesc UnixtimestampToDateTime = new FunctionDesc("UnixtimestampToDateTime", "根据时间戳转成日期时间", "根据时间戳转成日期时间。");
    /**
     * 系统文件上传到ftp
     */
    public final static FunctionDesc UploadToFtp = new FunctionDesc("UploadToFtp", "系统文件上传到ftp", "系统文件上传到ftp。");
    /**
     * 三元运算函数
     */
    public final static FunctionDesc V3If = new FunctionDesc("V3If", "三元运算函数", "先计算条件表达式的结果，再根据结果返回参数值；结果为true,则返回第1个参数，否则返回第2个参数。");
    /**
     * 将变量实体转换为二维数组json
     */
    public final static FunctionDesc VConvertEntityToJsonFunc = new FunctionDesc("VConvertEntityToJsonFunc", "将变量实体转换为二维数组json", "将变量实体转换为二维数组json。");
    /**
     * 将变量实体转换为XML字符串
     */
    public final static FunctionDesc VConvertEntityToXMLFunc = new FunctionDesc("VConvertEntityToXMLFunc", "将变量实体转换为XML字符串", "将变量实体转换为XML字符串。");
    /**
     * 二维数组json还原为实体
     */
    public final static FunctionDesc VRestoreJsonToEntityFunc = new FunctionDesc("VRestoreJsonToEntityFunc", "二维数组json还原为实体", "二维数组json还原为实体。");
    /**
     * 将XML数据还原为实体数据
     */
    public final static FunctionDesc VRestoreXMLToEntityFunc = new FunctionDesc("VRestoreXMLToEntityFunc", "将XML数据还原为实体数据", "将XML数据还原为实体数据。");

    /**
     * 函数描述信息类
     */
    public static class FunctionDesc {
        private final String Function_Code;
        private final String Function_Name;
        private final String Function_Desc;

        public FunctionDesc(String Function_Code, String Function_Name, String Function_Desc) {
            this.Function_Code = Function_Code;
            this.Function_Name = Function_Name;
            this.Function_Desc = Function_Desc;
        }

        public String Function_Code() {
            return this.Function_Code;
        }

        public String Function_Name() {
            return this.Function_Name;
        }

        public String Function_Desc() {
            return this.Function_Desc;
        }
    }

    /**
     * 检查参数个数
     *
     * @param funcName 函数名
     * @param context  函数上下文
     * @param size     预期的参数个数
     * @throws ServerFuncException 参数个数不符合要求时，抛出异常
     */
    default void checkParamSize(String funcName, IFuncContext context, int size) throws ServerFuncException {
        int inputSize = context.getInputSize();
        if (inputSize != size) {
            throw new ServerFuncException("函数【" + funcName + "】要求参数个数：" + size + ", 实际参数个数：" + inputSize);
        }
    }

    /**
     * 检查参数是否为null
     *
     * @param funcName 函数名
     * @param objs     参数列表
     * @throws ServerFuncException 当参数中存在null时，抛出异常
     */
    default void checkParamNull(String funcName, Object... objs) throws ServerFuncException {
        if (objs != null && objs.length > 0) {
            for (int i = 0; i < objs.length; i++) {
                if (objs[i] == null) {
                    throw new ServerFuncException("函数【" + funcName + "】的第" + (i + 1) + "个参数不能为空");
                }
            }
        }
    }

    /**
     * 检查参数是否为null或者空字符串
     *
     * @param funcName 函数名
     * @param objs     参数列表
     * @throws ServerFuncException 当参数中存在null或者空字符串时，抛出异常
     */
    default void checkParamBlank(String funcName, Object... objs) throws ServerFuncException {
        if (objs != null && objs.length > 0) {
            for (int i = 0; i < objs.length; i++) {
                if (objs[i] == null || objs[i].toString().trim().equals("")) {
                    throw new ServerFuncException("函数【" + funcName + "】的第" + (i + 1) + "个参数不能为空");
                }
            }
        }
    }

    /**
     * 检查参数是否为数字类型
     *
     * @param funcName 函数名
     * @param objs     参数列表
     * @throws ServerFuncException 当参数中存在null时，抛出异常
     */
    default void checkParamNumeric(String funcName, Object... objs) throws ServerFuncException {
        if (objs != null && objs.length > 0) {
            for (int i = 0; i < objs.length; i++) {
                if (objs[i] == null || !isNumeric(objs[i].toString())) {
                    throw new ServerFuncException("函数【" + funcName + "】的第" + (i + 1) + "个参数必须是数字类型");
                }
            }
        }
    }

    /**
     * 检查参数是否为数字类型
     *
     * @param funcName 函数名
     * @param obj      参数
     * @param index    参数序号
     * @throws ServerFuncException 当参数中存在null时，抛出异常
     */
    default void checkParamNumeric(String funcName, Object obj, int index) throws ServerFuncException {
        if (obj == null || !isNumeric(obj.toString())) {
            throw new ServerFuncException("函数【" + funcName + "】的第" + index + "个参数必须是数字类型");
        }
    }

    /**
     * 判断字符串是否为非零正整数
     *
     * @param funcName 函数名
     * @param obj      参数
     * @param index    参数顺序
     * @throws ServerFuncException 如果参数不满足非零正整数，抛出异常
     */
    default void checkParamInteger(String funcName, Object obj, int index) throws ServerFuncException {
        boolean isMatch = false;
        if (obj != null) {
            Pattern pattern = Pattern.compile("^[1-9]\\d*$");
            isMatch = pattern.matcher(obj.toString().trim()).matches();
        }
        if (!isMatch) {
            throw new ServerFuncException("函数【" + funcName + "】的第" + index + "个参数必须是非零正整数");
        }
    }

    /**
     * 是否数字
     *
     * @param str 字符串
     * @return 数字返回true，否则返回false
     */
    default boolean isNumeric(String str) {
        String regex = "^(-?[1-9]\\d*\\.?\\d*)|(-?0\\.\\d*[1-9])|(-?[0])|(-?[0]\\.\\d*)$";
        if (!str.matches(regex)) {
            return false;
        }
        return true;
    }

    /**
     * 获取日期格式
     *
     * @param funcName 函数名
     * @param obj      参数
     * @param index    参数序号
     * @return
     * @throws ServerFuncException
     */
    default SimpleDateFormat getSimpleDateFormat(String funcName, Object obj, int index) throws ServerFuncException {
        if (obj == null) {
            throw new ServerFuncException("函数【" + funcName + "】的第" + index + "个参数必须满足格式：yyyy-MM-dd或者yyyy-MM-dd HH:mm:ss，当前值：" + obj);
        } else {
            String str = obj.toString().trim();
            if (str.length() > 10) {
                return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            } else {
                return new SimpleDateFormat("yyyy-MM-dd");
            }
        }
    }

    /**
     * 弧度转角度
     *
     * @param arg 弧度
     * @return 角度
     */
    default double radianToAngle(double arg) {
        return 180 * arg / Math.PI;
    }

    /**
     * 角度转弧度
     *
     * @param arg 角度
     * @return 弧度
     */
    default double angleToRadian(double arg) {
        return arg * Math.PI / 180;
    }
}
