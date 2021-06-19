package com.toone.v3.platform.function;

import com.toone.v3.platform.function.common.ServerFuncCommonUtils;
import com.yindangu.v3.plugin.vds.reg.api.IRegisterPlugin;
import com.yindangu.v3.plugin.vds.reg.api.builder.IFunctionBuilder;
import com.yindangu.v3.plugin.vds.reg.api.model.IComponentProfileVo;
import com.yindangu.v3.plugin.vds.reg.api.model.IFunctionProfileVo;
import com.yindangu.v3.plugin.vds.reg.api.model.IPluginProfileVo;
import com.yindangu.v3.plugin.vds.reg.api.model.VariableType;
import com.yindangu.v3.plugin.vds.reg.common.RegVds;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 函数注册器
 * 
 * @Author xugang
 * @Date 2021/5/31 13:37
 */
public class ConvertRegister implements IRegisterPlugin {

    private static final String Component_Code = "Serverfunc_ConvertFunc";
    private static final String Component_Version = "3.10.0";

    @Override
    public IComponentProfileVo getComponentProfile() {
        return RegVds.getPlugin()
                .getComponentProfile()
                .setGroupId(ServerFuncCommonUtils.Group_Id)
                .setCode(Component_Code)
                .setVersion(Component_Version)
                .build();
    }

    @Override
    public List<IPluginProfileVo> getPluginProfile() {
        List<IPluginProfileVo> plugins = new ArrayList<>();
        plugins.add(getFunc());

        return plugins;
    }

    /**
     * Convert函数
     *
     * @return Convert函数描述器
     */
    private IFunctionProfileVo getFunc() {
        IFunctionBuilder pluginBuilder = RegVds.getPlugin().getFunctiontPlugin();
        IFunctionProfileVo.IFunctionInputVo inputVo1 = pluginBuilder.newInput()
                .setDesc("转换对象")
                .setType(VariableType.Range)
                .setTypeRange(Arrays.asList(VariableType.Boolean, VariableType.Integer, VariableType.Number, VariableType.Char))
                .setRequired(true)
                .build();
        IFunctionProfileVo.IFunctionInputVo inputVo2 = pluginBuilder.newInput()
                .setDesc("转换类型")
                .setType(VariableType.Integer)
                .setRequired(true)
                .build();
        IFunctionProfileVo.IFunctionOutputVo outputVo = pluginBuilder.newOutput()
                .setDesc("返回值")
                .setType(VariableType.Range)
                .setTypeRange(Arrays.asList(VariableType.Boolean, VariableType.Integer, VariableType.Number, VariableType.Char))
                .build();
        pluginBuilder.setAuthor(ServerFuncCommonUtils.Plugin_Author)
                .setCode(ServerFuncCommonUtils.ConvertFunc.Function_Code())
                .setDesc(ServerFuncCommonUtils.ConvertFunc.Function_Desc())
                .setName(ServerFuncCommonUtils.ConvertFunc.Function_Name())
                .setEntry(ConvertFunc.class)
                .setExample("代码示例:ConvertFunc(3,2) 返回值为\"3\"。\n" +
                        "参数1--传入的数据(各种类型)；\n" +
                        "参数2--转换的类型，1-整数,2-字符串,3-布尔值,4-小数； \n" +
                        "返回值为不确定的类型，根据参数2的格式而定。\n" +
                        "注：整数的支持范围是：[-2147483648，2147483647]，小数位数最大支持6位。")
                .setOutput(outputVo)
                .addInputParam(inputVo1)
                .addInputParam(inputVo2);

        return pluginBuilder.build();
    }
}
