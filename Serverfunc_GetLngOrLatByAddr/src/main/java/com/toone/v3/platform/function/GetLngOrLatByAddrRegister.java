package com.toone.v3.platform.function;

import com.toone.v3.platform.function.common.ServerFuncCommonUtils;
import com.yindangu.v3.business.plugin.business.api.httpcommand.IHttpCommand;
import com.yindangu.v3.plugin.vds.reg.api.IRegisterPlugin;
import com.yindangu.v3.plugin.vds.reg.api.builder.IFunctionBuilder;
import com.yindangu.v3.plugin.vds.reg.api.builder.IHttpCommandBuilder;
import com.yindangu.v3.plugin.vds.reg.api.model.*;
import com.yindangu.v3.plugin.vds.reg.common.RegVds;

import java.util.ArrayList;
import java.util.List;

/**
 * 函数注册器
 * 
 * @Author xugang
 * @Date 2021/5/31 21:56
 */
public class GetLngOrLatByAddrRegister implements IRegisterPlugin {

    private static final String Component_Code = "Serverfunc_GetLngOrLatByAddr";
    private static final String Component_Version = "3.10.0";
    private static final String Command_Code = "GetLngOrLatByAddr";

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
        plugins.add(getCommand());

        return plugins;
    }

    /**
     * GetLngOrLatByAddr函数
     *
     * @return GetLngOrLatByAddr函数描述器
     */
    private IFunctionProfileVo getFunc() {
        IFunctionBuilder pluginBuilder = RegVds.getPlugin().getFunctiontPlugin();
        IFunctionProfileVo.IFunctionInputVo inputVo1 = pluginBuilder.newInput()
                .setDesc("地址")
                .setType(VariableType.Char)
                .setRequired(true)
                .build();
        IFunctionProfileVo.IFunctionInputVo inputVo2 = pluginBuilder.newInput()
                .setDesc("返回类型")
                .setType(VariableType.Char)
                .setRequired(true)
                .build();
        IFunctionProfileVo.IFunctionOutputVo outputVo = pluginBuilder.newOutput()
                .setDesc("返回值")
                .setType(VariableType.Number)
                .build();
        pluginBuilder.setAuthor(ServerFuncCommonUtils.Plugin_Author)
                .setCode(ServerFuncCommonUtils.GetLngOrLatByAddr.Function_Code())
                .setDesc(ServerFuncCommonUtils.GetLngOrLatByAddr.Function_Desc())
                .setName(ServerFuncCommonUtils.GetLngOrLatByAddr.Function_Name())
                .setEntry(GetLngOrLatByAddrFunc.class)
                .setOutput(outputVo)
                .addInputParam(inputVo1)
                .addInputParam(inputVo2);

        return pluginBuilder.build();
    }

    private IHttpCommandProfileVo getCommand() {
        IHttpCommandBuilder pluginBuilder = RegVds.getPlugin().getHttpCommandPlugin();
        pluginBuilder.setAuthor(ServerFuncCommonUtils.Plugin_Author)
                .setCode(Command_Code)
                .setDesc("")
                .setName("获取经纬度")
                .setEntry(GetLngOrLatByAddrCommand.class);

        return pluginBuilder.build();
    }
}
