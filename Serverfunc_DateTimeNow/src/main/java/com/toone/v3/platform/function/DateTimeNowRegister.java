package com.toone.v3.platform.function;

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
    private final static String Group_Id = "com.toone.v3.platform";
    private final static String Plugin_Author = "同望科技";
    public static final String Plugin_Code = "DateTimeNow";
    private static final String Plugin_Name = "时间日期";
    private static final String Plugin_Desc = "返回服务器当前的日期和时间，格式为yyyy-MM-dd HH:mm:ss";
    private static final String Component_Version = "3.10.0";

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
        pluginBuilder.setAuthor(Plugin_Author)
                .setCode(Plugin_Code)
                .setDesc(Plugin_Desc)
                .setName(Plugin_Name)
                .setEntry(DateTimeNowFunc.class)
                .setExample("代码示例:DateTimeNow()返回值为\"2011-10-19 12:03:44\"。\n" +
                        "无参数；\n" +
                        "返回值为字符串类型。")
                .setOutput(outputVo);

        return pluginBuilder.build();
    }
}
