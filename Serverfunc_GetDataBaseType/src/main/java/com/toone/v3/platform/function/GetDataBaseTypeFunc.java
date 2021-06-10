package com.toone.v3.platform.function;

import com.toone.v3.platform.function.common.ServerFuncCommonUtils;
import com.toone.v3.platform.function.common.exception.ServerFuncException;
import com.yindangu.v3.business.VDS;
import com.yindangu.v3.business.plugin.business.api.func.IFuncContext;
import com.yindangu.v3.business.plugin.business.api.func.IFuncOutputVo;
import com.yindangu.v3.business.plugin.business.api.func.IFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 获取服务连接的数据库类型<br>
 * <br>
 * 代码示例:GetDataBaseType()，返回执行系统服务连接的数据库类型（返回值枚举：Mssql2000、Mssql、DB2、Oracle、Mysql、H2、Unknow），
 * 其中SqlServer 2000以上版本都返回Mssql，若读取不到数据库类型，则返回Unknow。<br>
 * 无参数；<br>
 * 返回值类型：字符串类型。<br>
 *
 * @Author xugang
 * @Date 2021/5/31 21:47
 */
public class GetDataBaseTypeFunc implements IFunction {

    // 函数编码
    private final String funcCode = ServerFuncCommonUtils.GetDataBaseType.Function_Code();
    private final static Logger log = LoggerFactory.getLogger(GetDataBaseTypeFunc.class);

    @Override
    public IFuncOutputVo evaluate(IFuncContext context) {
        IFuncOutputVo outputVo = context.newOutputVo();
        Object param = null;
        try {
            ServerFuncCommonUtils service = VDS.getIntance().getService(ServerFuncCommonUtils.class, ServerFuncCommonUtils.OutServer_Code);

        } catch (ServerFuncException e) {
            outputVo.setSuccess(false);
            outputVo.setMessage(e.getMessage());
        } catch (Exception e) {
            outputVo.setSuccess(false);
            outputVo.setMessage("函数【" + funcCode + "】计算有误，参值1：" + param + ", " + e.getMessage());
            log.error("函数【" + funcCode + "】计算失败", e);
        }
        return outputVo;
    }
}
