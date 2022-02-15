package com.toone.v3.platform.function;

import com.sun.org.apache.xpath.internal.operations.Variable;
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
 * @Date 2021/6/17 9:43
 */
public class NullRegister implements IRegisterPlugin {

    private static final String Component_Code = "Serverfunc_Null";
    private final static String Group_Id = "com.toone.v3.platform";
    private final static String Plugin_Author = "同望科技";
    public static final String Plugin_Code = "Null";
    private static final String Plugin_Name = "产生空值";
    private static final String Plugin_Desc = "产生空值。可在加载规则查询条件中使用，令查询条件失效。";
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
     * Null函数
     *
     * @return Null函数描述器
     */
    private IFunctionProfileVo getFunc() {
        IFunctionBuilder pluginBuilder = RegVds.getPlugin().getFunctiontPlugin();
        IFunctionProfileVo.IFunctionOutputVo outputVo = pluginBuilder.newOutput()
                .setDesc("返回值")
                .setType(VariableType.Range)
                .setTypeRange(Arrays.asList(VariableType.Char, VariableType.Boolean, VariableType.Date, VariableType.Entity, VariableType.Integer, VariableType.LongDate, VariableType.Number, VariableType.Text))
                .build();
        pluginBuilder.setAuthor(Plugin_Author)
                .setCode(Plugin_Code)
                .setDesc(Plugin_Desc)
                .setName(Plugin_Name)
                .setEntry(NullFunc.class)
                .setExample("代码示例:Null()，返回Null。\n" +
                        "返回值为Null。")
                .setOutput(outputVo);

        return pluginBuilder.build();
    }
}
