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
import java.util.List;

/**
 * 函数注册器
 * 
 * @Author xugang
 * @Date 2021/5/31 13:39
 */
public class ConvertPageNumberRegister implements IRegisterPlugin {

    private static final String Component_Code = "Serverfunc_ConvertPageNumber";
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
     * ConvertPageNumber函数
     *
     * @return ConvertPageNumber函数描述器
     */
    private IFunctionProfileVo getFunc() {
        IFunctionBuilder pluginBuilder = RegVds.getPlugin().getFunctiontPlugin();
        IFunctionProfileVo.IFunctionInputVo inputVo1 = pluginBuilder.newInput()
                .setDesc("记录开始数")
                .setType(VariableType.Integer)
                .setRequired(true)
                .build();
        IFunctionProfileVo.IFunctionInputVo inputVo2 = pluginBuilder.newInput()
                .setDesc("分页显示数")
                .setType(VariableType.Integer)
                .setRequired(true)
                .build();
        IFunctionProfileVo.IFunctionOutputVo outputVo = pluginBuilder.newOutput()
                .setDesc("页码")
                .setType(VariableType.Integer)
                .build();
        pluginBuilder.setAuthor(ServerFuncCommonUtils.Plugin_Author)
                .setCode(ServerFuncCommonUtils.ConvertPageNumber.Function_Code())
                .setDesc(ServerFuncCommonUtils.ConvertPageNumber.Function_Desc())
                .setName(ServerFuncCommonUtils.ConvertPageNumber.Function_Name())
                .setEntry(ConvertPageNumberFunc.class)
                .setOutput(outputVo)
                .addInputParam(inputVo1)
                .addInputParam(inputVo2);

        return pluginBuilder.build();
    }
}
