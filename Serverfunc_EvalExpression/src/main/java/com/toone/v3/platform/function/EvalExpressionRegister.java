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
 * @Date 2021/5/31 21:19
 */
public class EvalExpressionRegister implements IRegisterPlugin {

    private static final String Component_Code = "Serverfunc_EvalExpression";
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
     * EvalExpression函数
     *
     * @return EvalExpression函数描述器
     */
    private IFunctionProfileVo getFunc() {
        IFunctionBuilder pluginBuilder = RegVds.getPlugin().getFunctiontPlugin();
        IFunctionProfileVo.IFunctionInputVo inputVo = pluginBuilder.newInput()
                .setDesc("字符串表达式")
                .setType(VariableType.Char)
                .setRequired(true)
                .build();
        IFunctionProfileVo.IFunctionOutputVo outputVo = pluginBuilder.newOutput()
                .setDesc("结果")
                .setType(VariableType.Range)
                .setTypeRange(Arrays.asList(VariableType.Boolean, VariableType.Char, VariableType.Date, VariableType.Number, VariableType.Entity, VariableType.Integer, VariableType.LongDate, VariableType.Text))
                .build();
        pluginBuilder.setAuthor(ServerFuncCommonUtils.Plugin_Author)
                .setCode(ServerFuncCommonUtils.EvalExpression.Function_Code())
                .setDesc(ServerFuncCommonUtils.EvalExpression.Function_Desc())
                .setName(ServerFuncCommonUtils.EvalExpression.Function_Name())
                .setEntry(EvalExpressionFunc.class)
                .setOutput(outputVo)
                .addInputParam(inputVo);

        return pluginBuilder.build();
    }
}
