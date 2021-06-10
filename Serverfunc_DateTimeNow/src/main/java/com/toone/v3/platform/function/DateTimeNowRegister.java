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
 * @Date 2021/5/31 21:02
 */
public class DateTimeNowRegister implements IRegisterPlugin {

    private static final String Component_Code = "Serverfunc_DateTimeNow";
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
     * DateTimeNow函数
     *
     * @return DateTimeNow函数描述器
     */
    private IFunctionProfileVo getFunc() {
        IFunctionBuilder pluginBuilder = RegVds.getPlugin().getFunctiontPlugin();
        IFunctionProfileVo.IFunctionOutputVo outputVo = pluginBuilder.newOutput()
                .setDesc("当前时间")
                .setType(VariableType.Char)
                .build();
        pluginBuilder.setAuthor(ServerFuncCommonUtils.Plugin_Author)
                .setCode(ServerFuncCommonUtils.DateTimeNow.Function_Code())
                .setDesc(ServerFuncCommonUtils.DateTimeNow.Function_Desc())
                .setName(ServerFuncCommonUtils.DateTimeNow.Function_Name())
                .setEntry(DateTimeNowFunc.class)
                .setOutput(outputVo);

        return pluginBuilder.build();
    }
}
