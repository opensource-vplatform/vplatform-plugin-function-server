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
 * @Date 2021/5/31 22:00
 */
public class GetZoomLevelRegister implements IRegisterPlugin {

    private static final String Component_Code = "Serverfunc_GetZoomLevel";
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
     * GetZoomLevel函数
     *
     * @return GetZoomLevel函数描述器
     */
    private IFunctionProfileVo getFunc() {
        IFunctionBuilder pluginBuilder = RegVds.getPlugin().getFunctiontPlugin();
        IFunctionProfileVo.IFunctionInputVo inputVo = pluginBuilder.newInput()
                .setDesc("正弦值")
                .setType(VariableType.Number)
                .setRequired(true)
                .build();
        IFunctionProfileVo.IFunctionOutputVo outputVo = pluginBuilder.newOutput()
                .setDesc("角度")
                .setType(VariableType.Number)
                .build();
        pluginBuilder.setAuthor(ServerFuncCommonUtils.Plugin_Author)
                .setCode(ServerFuncCommonUtils.GetZoomLevel.Function_Code())
                .setDesc(ServerFuncCommonUtils.GetZoomLevel.Function_Desc())
                .setName(ServerFuncCommonUtils.GetZoomLevel.Function_Name())
                .setEntry(GetZoomLevelFunc.class)
                .setOutput(outputVo)
                .addInputParam(inputVo);

        return pluginBuilder.build();
    }
}
