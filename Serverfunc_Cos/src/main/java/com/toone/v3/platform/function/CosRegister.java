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
 * @Date 2021/5/31 13:40
 */
public class CosRegister implements IRegisterPlugin {

    private static final String Component_Code = "Serverfunc_Cos";
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
     * Cos函数
     *
     * @return Cos函数描述器
     */
    private IFunctionProfileVo getFunc() {
        IFunctionBuilder pluginBuilder = RegVds.getPlugin().getFunctiontPlugin();
        IFunctionProfileVo.IFunctionInputVo inputVo = pluginBuilder.newInput()
                .setDesc("角度")
                .setType(VariableType.Range)
                .setTypeRange(Arrays.asList(VariableType.Number, VariableType.Integer))
                .setRequired(true)
                .build();
        IFunctionProfileVo.IFunctionOutputVo outputVo = pluginBuilder.newOutput()
                .setDesc("余弦值")
                .setType(VariableType.Range)
                .setTypeRange(Arrays.asList(VariableType.Number, VariableType.Integer))
                .build();
        pluginBuilder.setAuthor(ServerFuncCommonUtils.Plugin_Author)
                .setCode(ServerFuncCommonUtils.Cos.Function_Code())
                .setDesc(ServerFuncCommonUtils.Cos.Function_Desc())
                .setName(ServerFuncCommonUtils.Cos.Function_Name())
                .setEntry(CosFunc.class)
                .setOutput(outputVo)
                .addInputParam(inputVo);

        return pluginBuilder.build();
    }
}
