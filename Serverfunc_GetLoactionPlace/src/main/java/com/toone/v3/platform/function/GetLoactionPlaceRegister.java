package com.toone.v3.platform.function;

import com.yindangu.v3.plugin.vds.reg.api.IRegisterPlugin;
import com.yindangu.v3.plugin.vds.reg.api.builder.IFunctionBuilder;
import com.yindangu.v3.plugin.vds.reg.api.builder.IHttpCommandBuilder;
import com.yindangu.v3.plugin.vds.reg.api.model.*;
import com.yindangu.v3.plugin.vds.reg.common.RegVds;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 函数注册器
 * 
 * @Author xugang
 * @Date 2021/5/31 21:56
 */
public class GetLoactionPlaceRegister implements IRegisterPlugin {

    private static final String Component_Code = "Serverfunc_GetLoactionPlace";
    private final static String Group_Id = "com.toone.v3.platform";
    private final static String Plugin_Author = "同望科技";
    public static final String Plugin_Code = "GetLoactionPlace";
    private static final String Plugin_Name = "根据经度纬度获取地理位置";
    private static final String Plugin_Desc = "根据经度纬度获取地理位置";
    private static final String Component_Version = "3.10.0";
    private static final String Command_Code = "GetLocationPlace";

    @Override
    public IComponentProfileVo getComponentProfile() {
        return RegVds.getPlugin()
                .getComponentProfile()
                .setGroupId(Group_Id)
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
     * GetLoactionPlace函数
     *
     * @return GetLoactionPlace函数描述器
     */
    private IFunctionProfileVo getFunc() {
        IFunctionBuilder pluginBuilder = RegVds.getPlugin().getFunctiontPlugin();
        IFunctionProfileVo.IFunctionInputVo inputVo1 = pluginBuilder.newInput()
                .setDesc("纬度")
                .setType(VariableType.Range)
                .setTypeRange(Arrays.asList(VariableType.Number, VariableType.Char))
                .setRequired(true)
                .build();
        IFunctionProfileVo.IFunctionInputVo inputVo2 = pluginBuilder.newInput()
                .setDesc("经度")
                .setType(VariableType.Range)
                .setTypeRange(Arrays.asList(VariableType.Number, VariableType.Char))
                .setRequired(true)
                .build();
        IFunctionProfileVo.IFunctionInputVo inputVo3 = pluginBuilder.newInput()
                .setDesc("格式")
                .setType(VariableType.Char)
                .setRequired(false)
                .build();
        IFunctionProfileVo.IFunctionOutputVo outputVo = pluginBuilder.newOutput()
                .setDesc("城市")
                .setType(VariableType.Char)
                .build();
        pluginBuilder.setAuthor(Plugin_Author)
                .setCode(Plugin_Code)
                .setDesc(Plugin_Desc)
                .setName(Plugin_Name)
                .setEntry(GetLoactionPlaceFunc.class)
                .setExample("代码示例:GetLoactionPlace(22.280539000,113.5719410)，返回值：珠海。\n" +
                        "参数1--纬度（小数类型）；\n" +
                        "参数2--经度（小数类型）；\n" +
                        "返回值类型：字符串类型。")
                .setOutput(outputVo)
                .addInputParam(inputVo1)
                .addInputParam(inputVo2)
                .addInputParam(inputVo3);

        return pluginBuilder.build();
    }

    private IHttpCommandProfileVo getCommand() {
        IHttpCommandBuilder pluginBuilder = RegVds.getPlugin().getHttpCommandPlugin();
        pluginBuilder.setAuthor(Plugin_Author)
                .setCode(Command_Code)
                .setDesc("")
                .setName("获取城市名称")
                .setEntry(GetLocationPlaceCommand.class);

        return pluginBuilder.build();
    }
}
