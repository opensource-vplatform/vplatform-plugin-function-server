package com.toone.v3.platform.function;

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
 * @Date 2021/5/31 21:04
 */
public class DateToStringRegister implements IRegisterPlugin {

    private static final String Component_Code = "Serverfunc_DateToString";
    private final static String Group_Id = "com.toone.v3.platform";
    private final static String Plugin_Author = "同望科技";
    public static final String Plugin_Code = "DateToString";
    private static final String Plugin_Name = "根据指定格式，将时间格式化为字符串返回。";
    private static final String Plugin_Desc = "根据指定格式，将时间格式化为字符串返回。";
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
     * DateToString函数
     *
     * @return DateToString函数描述器
     */
    private IFunctionProfileVo getFunc() {
        IFunctionBuilder pluginBuilder = RegVds.getPlugin().getFunctiontPlugin();
        IFunctionProfileVo.IFunctionInputVo inputVo1 = pluginBuilder.newInput()
                .setDesc("格式串")
                .setType(VariableType.Char)
                .setRequired(true)
                .build();
        IFunctionProfileVo.IFunctionInputVo inputVo2 = pluginBuilder.newInput()
                .setDesc("时间")
                .setType(VariableType.Range)
                .setTypeRange(Arrays.asList(VariableType.Char, VariableType.Date, VariableType.LongDate))
                .setRequired(true)
                .build();
        IFunctionProfileVo.IFunctionOutputVo outputVo = pluginBuilder.newOutput()
                .setDesc("转换后的时间")
                .setType(VariableType.Char)
                .build();
        pluginBuilder.setAuthor(Plugin_Author)
                .setCode(Plugin_Code)
                .setDesc(Plugin_Desc)
                .setName(Plugin_Name)
                .setEntry(DateToStringFunc.class)
                .setExample("代码示例:DateToString(\"yyyy-MM-dd HH:mm:ss\",DateTimeNow())，返回值为\"2012-04-19 12:03:44\"。 \n" +
                        "参数1--格式串(字符串类型)；\n" +
                        "参数2--时间(时间类型或满足时间格式的字符串类型)；\n" +
                        "返回值为字符串类型。")
                .setOutput(outputVo)
                .addInputParam(inputVo1)
                .addInputParam(inputVo2);

        return pluginBuilder.build();
    }
}
