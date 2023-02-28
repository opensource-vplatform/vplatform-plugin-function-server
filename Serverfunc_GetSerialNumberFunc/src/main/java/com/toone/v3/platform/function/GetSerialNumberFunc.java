package com.toone.v3.platform.function;

import com.toone.v3.platform.function.common.exception.ServerFuncException;
import com.yindangu.v3.business.VDS;
import com.yindangu.v3.business.plugin.business.api.func.IFuncContext;
import com.yindangu.v3.business.plugin.business.api.func.IFuncOutputVo;
import com.yindangu.v3.business.plugin.business.api.func.IFunction;
import com.yindangu.v3.business.sequence.api.ISerialNumber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 流水号函数<br>
 * <br>
 * 代码示例:GetSerialNumberFunc("TableName","ColumnName","20151103--","11","0","0","3","true","true") 返回值为00000000001。<br>
 * 参数1--表名(字符串类型)；<br>
 * 参数2--字段名(字符串类型)；<br>
 * 参数3--前缀字符串(字符串类型，只用于查询流水号时使用，不包含在返回结果中)；<br>
 * 参数4--流水号长度(字符串类型)<br>
 * 参数5--补位符(字符串类型，并且长度必须为1)；<br>
 * 参数6--查询语句的like值(字符串类型)<br>
 * 参数7--截取流水号的起始位置，下标从0开始(字符串类型)<br>
 * 参数8--是否从左边截取(字符串类型)<br>
 * 参数9--是否重用流水号(字符串类型)<br>
 * 返回值为字符串类型。<br>
 *
 * @Author xugang
 * @Date 2021/5/31 21:58
 */
public class GetSerialNumberFunc implements IFunction {

    // 函数编码
    private final static String funcCode = GetSerialNumberRegister.Plugin_Code;
    private final static Logger log = LoggerFactory.getLogger(GetSerialNumberFunc.class);

    @Override
    public IFuncOutputVo evaluate(IFuncContext context) {
        IFuncOutputVo outputVo = context.newOutputVo();
        Object param = null;
        long startTime = System.currentTimeMillis();
        try {
            //ServerFuncCommonUtils service = VDS.getIntance().getService(ServerFuncCommonUtils.class, ServerFuncCommonUtils.OutServer_Code);

            String tableName = (String) context.getInput(0);
            String tableColumn = (String) context.getInput(1);
            String prefix = (String) context.getInput(2);
            int length = Integer.parseInt((String) context.getInput(3));
            String coverLetter = ((String) context.getInput(4)).trim();
            String likeValStr = ((String) context.getInput(5)).trim();
            String subLength = ((String) context.getInput(6)).trim();
            boolean isLeftSubFlag = Boolean.parseBoolean(((String) context.getInput(7)).trim());
            boolean isReuseSerialNumber =Boolean.parseBoolean(((String) context.getInput(8)).trim());

            // 旧的流水号接口封装在新接口里面，新接口的种子随便传一个就行了，实际没有使用
            ISerialNumber serialNumber = VDS.getIntance().getSerialNumber("aaa");
            String number = serialNumber.generateSerialNumber(tableName, tableColumn, prefix, length, coverLetter, likeValStr, subLength, isLeftSubFlag, isReuseSerialNumber);

            outputVo.put(number);
            outputVo.setSuccess(true);
        } catch (ServerFuncException e) {
            outputVo.setSuccess(false);
            outputVo.setMessage(e.getMessage());
            log.error("函数[" + funcCode + "]计算失败", e);
        } catch (Exception e) {
            outputVo.setSuccess(false);
            outputVo.setMessage("函数【" + funcCode + "】计算有误，" + e.getMessage());
            log.error("函数【" + funcCode + "】计算有误", e);
        }
        finally {

			long time = System.currentTimeMillis() - startTime;
			log.info("GetSerialNumberFunc,耗时:{}",time);
        }
        return outputVo;
    }

}
