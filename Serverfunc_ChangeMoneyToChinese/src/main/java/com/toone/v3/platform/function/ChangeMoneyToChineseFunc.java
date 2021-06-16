package com.toone.v3.platform.function;

import com.toone.v3.platform.function.common.ServerFuncCommonUtils;
import com.toone.v3.platform.function.common.exception.ServerFuncException;
import com.yindangu.v3.business.VDS;
import com.yindangu.v3.business.plugin.business.api.func.IFuncContext;
import com.yindangu.v3.business.plugin.business.api.func.IFuncOutputVo;
import com.yindangu.v3.business.plugin.business.api.func.IFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

/**
 * 将人民币金额转成中文大写<br>
 * <br>
 * 代码示例:ChangeMoneyToChinese(998.1314)<br>
 * 返回值："玖佰玖拾捌元壹角叁分壹厘肆毫"<br>
 * <br>
 * 参数数量:1<br>
 * 参数1--要转换的金额(数字类型)<br>
 * 返回值为字符串类型<br>
 *
 * @Author xugang
 * @Date 2021/5/31 10:26
 */
public class ChangeMoneyToChineseFunc implements IFunction {

    private final static Logger log = LoggerFactory.getLogger(ChangeMoneyToChineseFunc.class);

    @Override
    public IFuncOutputVo evaluate(IFuncContext context) {
        IFuncOutputVo outputVo = context.newOutputVo();
        Object param = null;
        try {
            ServerFuncCommonUtils service = VDS.getIntance().getService(ServerFuncCommonUtils.class, ServerFuncCommonUtils.OutServer_Code);

            service.checkParamSize(ServerFuncCommonUtils.ChangeMoneyToChinese.Function_Code(), context, 1);
            param = context.getInput(0);
            service.checkParamNull(ServerFuncCommonUtils.ChangeMoneyToChinese.Function_Code(), param);

            Double number;
            BigDecimal result = new BigDecimal(param.toString());
            try {
                number = Double.parseDouble(result.toPlainString());
            } catch(Exception e) {
                throw new ServerFuncException("函数【" + ServerFuncCommonUtils.ChangeMoneyToChinese.Function_Code() + "】的第1个参数必须是数字类型，参数1：" + param);
            }

            double maxNum = 999999999999999.9999;
            if (number < 0) {
                throw new ServerFuncException("函数【" + ServerFuncCommonUtils.ChangeMoneyToChinese.Function_Code() + "】暂不支持负数，参数1：" + param);
            }
            if (number >= maxNum) {
                throw new ServerFuncException("函数【" + ServerFuncCommonUtils.ChangeMoneyToChinese.Function_Code() + "】参数1：" + param + "，超过能处理的最大值：" + maxNum);
            }

            String[] cnNums = {"零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖"};
            String[] cnIntRadice = {"", "拾", "佰", "仟"};
            String[] cnIntUnits = {"", "万", "亿", "兆"};
            String[] cnDecUnits = {"角", "分", "厘", "毫"};
            String cnInteger = "整";
            String cnIntLast = "元";
            String IntegerNum;
            String DecimalNum;
            String ChineseStr = "";
            String[] parts;

            String money = result.toPlainString();
            if (money.indexOf(".") == -1) {
                IntegerNum = money;
                DecimalNum = "";
            } else {
                parts = money.split("\\.");
                IntegerNum = parts[0];
                if (parts[1].length() > 4) {
                    DecimalNum = parts[1].substring(0, 4);
                } else {
                    DecimalNum = parts[1];
                }
            }

            if ((IntegerNum != null || !IntegerNum.equals("")) && !IntegerNum.equals("0")) { //获取整型部分转换
                int zeroCount = 0;
                int IntLen = IntegerNum.length();
                for (int i = 0; i < IntLen; i++) {
                    String n = IntegerNum.substring(i, i + 1);
                    int p = IntLen - i - 1;
                    int q = p / 4;
                    int m = p % 4;
                    if (n.equals("0")) {
                        zeroCount++;
                    } else {
                        if (zeroCount > 0) {
                            ChineseStr += cnNums[0];
                        }
                        zeroCount = 0; //归零
                        ChineseStr += cnNums[Integer.parseInt(n)] + cnIntRadice[m];
                    }
                    if (m == 0 && zeroCount < 4) {
                        ChineseStr += cnIntUnits[q];
                    }
                }
                ChineseStr += cnIntLast;
                //整型部分处理完毕
            }

            if (DecimalNum != "") { //小数部分
                int decLen = DecimalNum.length();
                for (int i = 0; i < decLen; i++) {
                    String n = DecimalNum.substring(i, i + 1);
                    if (!n.equals("0")) {
                        ChineseStr += cnNums[Integer.parseInt(n)] + cnDecUnits[i];
                    }
                }
            }
            if (ChineseStr == "") {
                ChineseStr += cnNums[0] + cnIntLast + cnInteger;
            } else if (DecimalNum == "") {
                ChineseStr += cnInteger;
            }

            outputVo.setSuccess(true);
            outputVo.put(ChineseStr);
        } catch (ServerFuncException e) {
            outputVo.setSuccess(false);
            outputVo.setMessage(e.getMessage());
        } catch (Exception e) {
            outputVo.setSuccess(false);
            outputVo.setMessage("函数【" + ServerFuncCommonUtils.ChangeMoneyToChinese.Function_Code() + "】计算有误，参数1：" + param + "，" + e.getMessage());
            log.error("函数【" + ServerFuncCommonUtils.ChangeMoneyToChinese.Function_Code() + "】计算失败，参数1：" + param, e);
        }
        return outputVo;
    }
}
