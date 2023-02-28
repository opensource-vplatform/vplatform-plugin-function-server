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
 * @Date 2021/6/17 9:42
 */
public class MaxColumnRegister implements IRegisterPlugin {

    private static final String Component_Code = "Serverfunc_MaxColumn";
    private final static String Group_Id = "com.toone.v3.platform";
    private final static String Plugin_Author = "同望科技";
    public static final String Plugin_Code = "MaxColumn";
    private static final String Plugin_Name = "求指定实体的某个字段的最大值";
    private static final String Plugin_Desc = "查询指定实体的字段的最大值并返回。";
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
     * MaxColumn函数
     *
     * @return MaxColumn函数描述器
     */
    private IFunctionProfileVo getFunc() {
        IFunctionBuilder pluginBuilder = RegVds.getPlugin().getFunctiontPlugin();
        IFunctionProfileVo.IFunctionInputVo inputVo1 = pluginBuilder.newInput()
                .setDesc("实体名称")
                .setType(VariableType.Range)
                .setTypeRange(Arrays.asList(VariableType.Char, VariableType.Entity))
                .setRequired(true)
                .build();
        IFunctionProfileVo.IFunctionInputVo inputVo2 = pluginBuilder.newInput()
                .setDesc("实体字段")
                .setType(VariableType.Char)
                .setRequired(true)
                .build();
        
        IFunctionProfileVo.IFunctionInputVo filters = pluginBuilder.newInput()
                .setDesc("过滤条件")
                .setType(VariableType.Char)
                .setRequired(false)
                .build();
        
        IFunctionProfileVo.IFunctionInputVo pars = pluginBuilder.newInput()
                .setDesc("条件参数(json)")
                .setType(VariableType.Char)
                .setRequired(false)
                .build();
        
        IFunctionProfileVo.IFunctionOutputVo outputVo = pluginBuilder.newOutput()
                .setDesc("最大值")
                .setType(VariableType.Range)
                .setTypeRange(Arrays.asList(VariableType.Integer, VariableType.Number))
                .build();
        pluginBuilder.setAuthor(Plugin_Author)
                .setCode(Plugin_Code)
                .setDesc(Plugin_Desc)
                .setName(Plugin_Name)
                .setEntry(MaxColumnFunc.class)
                .setExample("代码示例:MaxColumn(\"EntityName\",\"ColumnName\") 返回值为实体\"EntityName\"的\"ColumnName\"字段的最大值。\n" +
                        "参数1--实体名（字符串类型，需要加入前缀)；例如：BR_IN_PARENT:方法输入实体，BR_OUT_PARENT：方法输出实体，BR_VAR_PARENT：方法变量实体；\n" +
                        "参数2--字段名称(字符串类型)；\n" +
                        "参数3--过滤条件，格式:fd1=:a and fd2=:b\n" +
                        "参数4--条件参数map或者base64的json: eyJtMSI6IuaVsOWtly/lj5jph4/lkI0iLCJtMiI6Ilwi5a2X56ym5LiyXCIifQ==\n" +
                        "返回值类型：数字类型。")
                .setOutput(outputVo)
                .addInputParam(inputVo1)
                .addInputParam(inputVo2)
                .addInputParam(filters)
		        .addInputParam(pars)
		        ;

        return pluginBuilder.build();
    }
}
